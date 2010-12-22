require 'rubygems'
require 'json'

# Usage: Create a new client object. Then call make_move with the gamestate. Then call take_action with the tilestate
module AIPlayer
	class Client
		attr_accessor :board, :player, :others, :base_prices, :costs

		def initialize(debug = false);
			if debug
				data = ''
				File.open("sample-gamestate", "r").each_line{|line| data += line }
				puts make_move!(JSON.parse(data)).inspect
				data = ''
				File.open("sample-turnstate", "r").each_line{|line| data += line }
				puts take_action!(JSON.parse(data)).inspect
			end
		end

		def all_player_locations(include_self = true); include_self ? other_player_locations << player_location : other_player_locations end
		def get_tile(location); @board.get_tile(location) end
		def player_tile; get_tile(@player.location) end
		def other_player_locations; @others.map(&:location) end

		def make_move!(gamestate)
			@base_prices =	{ "V" => gamestate["prices"]["V"]	\
							, "C" => gamestate["prices"]["C"]	\
							, "S" => gamestate["prices"]["S"]	}
			@costs =		{ "V" => gamestate["costs"]["V"]	\
							, "C" => gamestate["costs"]["C"]	\
							, "S" => gamestate["costs"]["S"]	}
			@board = AIPlayer::Board.new(gamestate["game_board"], self)
			@player = AIPlayer::Player.new(gamestate["players"]["you"], :self)
			@others = gamestate["players"]["others"].map{|o| AIPlayer::Player.new(o)}
			if !gamestate["game_over"] && gamestate["players"]["you"]["turn"]
				choose_move.location
			elsif !gamestate["game_over"]
				{:error => "Not my turn!"}
			else
				{:error => "Game Over"}
			end
		end

		def choose_move
			if @player.inventory_low?
				move_towards_store
			else
				unoccupied_neighbors = get_neighbors(:unoccupied)
				occupied_neighbors = get_neighbors(:occupied)
				move = make_dick_move(occupied_neighbors) unless occupied_neighbors.empty?
				move ||= move_towards_frontier 
			end
		end

		def move_towards_store
			dist = nil
			store = nil
			@board.stores.each do |s|
				d = s.distance_to(player_location)
				if dist.nil? || d < dist
					dist = d
					store = s
				end
			end
			n_dist = nil
			choice = nil
			player_location.neighbors.each do |n|
				nd = n.distance_to(store)
				if n_dist.nil? || nd < n_dist
					n_dist = nd
					choice = n
				end
			end
			choice
		end

		def get_neighbors(group = :all)
			if group == :unoccupied
				player_tile.neighbors_with_no_other_players
			elsif group == :occupied
				player_tile.neighbors_with_other_players
			elsif group == :all
				player_tile.neighbors
			end
		end

		def make_dick_move(options)
			move = nil
			options.each do |o|
				move = o if (o.zombies > 0 && o.customers.size == 0) || (o.zombies == 0 && o.customers.size > 0)
			end
			move
		end

		def move_towards_frontier
			dist = nil
			tile = nil
			frontier = @board.tiles.collect do |t|
				t.not_visible_neighbors
			end.flatten.compact.uniq
			frontier.each do |n| # Likely breaks if there is no frontier, but that requires every tile to have been explored
				d = player_tile.distance_to(tile)
				if dist.nil? || d < dist
					dist = d
					tile = n
				end
			end
			n_dist = nil
			choice = nil
			player_tile.neighbors.each do |n|
				nd = n.distance_to(tile)
				if n_dist.nil? || nd < n_dist
					n_dist = nd
					choice = n
				end
			end
			choice
		end

		def take_action!(tilestate)
			prev = get_tile(@player.prev_location)
			curr = Tile.new(tilestate, @board)
			if curr.zombies > 0
				kill_zombies
			elsif curr.customers.size > 0
				sell_ice_cream(curr, @player.inventory)
			elsif curr.store
				buy_inventory(@player.money)
			elsif prev && (prev.zombies + prev.customers.size) > 0 && (curr.zombies + curr.customers.size) == 0
				run_away
			else
				do_nothing
			end
		end

		def kill_zombies
			{:action => :kill}
		end

		def sell_ice_cream(curr, inv)
			sale = choose_best_sale(curr, inv)
			{ :action =>	:sell				\
			, :customer =>	sale.customer.id	\
			, :flavor =>	sale.flavor			\
			, :number =>	sale.number			}
		end

		def run_away
			{:action => :run}
		end

		def buy_inventory(money)
			purchase = purchase_from_store(money)
			{ :action => :buy				\
			, :flavor => purchase.flavor	\
			, :number => purchase.number	}
		end

		def do_nothing
			kill_zombies
		end

		def choose_best_sale(tile, inv)
			options = tile.customers.collect{|c| c.find_sale(inv)}
			options.max{|i,j| i.score <=> j.score}
		end

		def purchase_from_store(money)
			Purchase.new(money, costs)
		end
	end

	class Board
		attr_accessor :client, :dimensions, :tiles, :stores

		def initialize(hashdata, client)
			@client = client
			@stores = []
			@dimensions =	{ :x => hashdata["size"].first	\
							, :y => hashdata["size"].last	}
			@tiles = hashdata["known"].collect{|t| Tile.new(t, self)}
		end


		def tiles_adjacent_to(tile)
			x, y = tile.x, tile.y
			adjacencies = [[+1,0], [-1,0], [0,+1], [0,-1]]
			adjacencies.map{|(dx, dy)| get_tile(:x => x + dx, :y => y + dy)}.compact
		end

		def get_tile(loc)
			if loc[:x] && loc[:y] && loc[:x] > 0 && loc[:x] < @dimensions[:x] && loc[:y] > 0 && loc[:y] < @dimensions[:y] # If in bounds
				@tiles.select{|t| t.x == loc[:x] && t.y == loc[:y]}.first || Tile.new({"x" => loc[:x], "y" => loc[:y]}, @board, false)
			else
				nil
			end
		end

		def add_store(store); @stores << store end
		def visible_tiles_adjacent_to(tile); tiles_adjacent_to(tile).select{|t| t.visible} end
		def not_visible_tiles_adjacent_to(tile); tiles_adjacent_to(tile).select{|t| !t.visible} end
		def other_player_locations; @client.other_player_locations end
		def base_prices; @client.base_prices end
	end

	class Tile
		attr_accessor :board, :x, :y, :zombies, :customers, :visible, :store

		def initialize(hashdata, board, visible = true)
			@board = board
			@visible = visible
			@x = hashdata["x"]
			@y = hashdata["y"]
			@zombies = hashdata["zombies"] || 0
			@customers = if hashdata["customers"]
				hashdata["customers"].collect{|c| Customer.new(c, self)}
			else
				[]
			end
			if hashdata["store"]
				@board.add_store(self)
				@store = true
			end
		end

		def location; {:x => @x, :y => @y} end
		def neighbors; @board.tiles_adjacent_to(self) end
		def visible_neighbors; @board.visible_tiles_adjacent_to(self) end
		def not_visible_neighbors; @board.not_visible_tiles_adjacent_to(self) end
		def base_prices; @board.base_prices end

		def distance_to(other)
			if other.is_a? AIPlayer::Tile
				(self.x - other.x).abs + (self.y - other.y).abs
			elsif other
				(self.x-other[:x]).abs + (self.y-other[:y]).abs
			elsif other.nil?
				1.0/0
			else
				{:error => "Not a valid tile, location hash, or nil object"}
			end
		end

		def neighbors_with_other_players
			other_player_locations = @board.other_player_locations
			visible_neighbors.select{|n| other_player_locations.include? n.location }
		end

		def neighbors_with_no_other_players
			other_player_locations = @board.other_player_locations
			visible_neighbors.reject{|n| other_player_locations.include? n.location }
		end
	end

	class Customer
		attr_accessor :tile, :id, :favorite

		def initialize(hashdata, tile)
			@tile = tile
			@id = hashdata["id"]
			@favorite = { :flavor => hashdata["favorite_type"]	\
						, :price => hashdata["favorite_price"]	\
						, :number => hashdata["favorite_number"]}
		end

		def base_prices; @tile.base_prices end

		def find_sale(inv)
			Sale.new(@favorite, base_prices, inv, self)
		end
	end

	class Player
		attr_accessor :who, :location, :prev_location, :score, :money, :inventory, :kills, :sales, :turns_remaining, :can_act, :can_move

		def initialize(hashdata, who = :other)
			@who = who
			@inventory = (@who == :self) ?	{ :v => hashdata["vanilla"]		\
											, :c => hashdata["chocolate"]	\
											, :s => hashdata["strawberry"]	} : {}
			@prev_location =	{:x => hashdata["prev_x"],	:y => hashdata["prev_y"]}
			@location = 		{:x => hashdata["x"],		:y => hashdata["y"]}
			@score =	hashdata["score"]
			@money =	hashdata["money"]
			@kills =	hashdata["kills"]
			@sales =	hashdata["sales"]
			@can_act =	hashdata["can_act"]
			@can_move =	hashdata["can_move"]
			@turns_remaining = hashdata["turns_remaining"]
		end

		def inventory_low?
			v, c, s = @inventory[:v], @inventory[:c], @inventory[:s]
			[v,c,s].max < 4 || v+c+s < 10
		end
	end

	class Sale
		attr_reader :flavor, :number, :price, :customer

		def initialize(fav, bases, inv, customer)
			@customer = customer
			bases = bases.clone
			bases.delete "C" if inv[:c] < 1
			bases.delete "S" if inv[:s] < 1
			bases.delete "V" if inv[:v] < 1

			options = {}
			bases.each{|k,v| options[k] = {:price => v, :number => 1}}

			hunger = fav[:number]
			if hunger >= 1
				ingredients = fav[:flavor].split ","
				unspent = [inv[:c],	inv[:s],	inv[:v]]
				flv_idx = ["C",		"S",		"V"]
				amount = 0
				price = 0
				until hunger < 1 || ingredients.empty? || unspent.inject{|s,v| s += v} == 0
					flavor = ingredients.pop.upcase
					i = case flavor
					when "C" ; 0
					when "S" ; 1
					when "V" ; 2
					end
					break if unspent[flv_idx.index(flavor)] < 1
					unspent[flv_idx.index(flavor)] -= 1
					if ingredients.empty?
						price += fav[:price]
						ingredients = fav[:flavor].split ","
						hunger -= 1
						amount += 1
					end
				end
				options.merge!({fav[:flavor] => {:number => amount, :price => price}}) if amount > 0
			end
			best = {:flavor => "", :price => 0, :number => 0}
			options.each do |k,v|
				best = {:flavor => k, :price => v[:price], :number => v[:number]} if v[:price] >= best[:price]
			end
			@flavor = best[:flavor]
			@number = best[:number]
			@price = best[:price]
			best
		end
	end

	class Purchase
		attr_reader :flavor, :number, :unit_cost

		def initialize(money, costs)
			@flavor = ["C", "S", "V"][rand(3)]
			@unit_cost = costs[@flavor]
			@number = (money/@unit_cost).floor
		end

		def total_cost; @number*@cost end
	end
end

###

AIPlayer::Client.new(true)