require 'rubygems'
require 'json'
require 'net/http'

module AIPlayer
	class Client
		attr_accessor :board, :player, :others, :base_prices, :costs
		def initialize()
		end

		def make_move(gamestate)
			if !gamestate[:game_over] && gamestate[:your_turn]
				@base_prices = {	"V" => gamestate[:base_prices]["V"],
									"C" => gamestate[:base_prices]["C"],
									"S" => gamestate[:base_prices]["S"] }
				@costs = {	"V" => gamestate[:costs]["V"],
							"C" => gamestate[:costs]["C"],
							"S" => gamestate[:costs]["S"] }
				@board = AIPlayer::Board.new(gamestate[:board])
				@player = AIPlayer::Player.new(gamestate[:players][:you], :self)
				@others = gamestate[:players][:others].map{|o| AIPlayer::Player.new(o)}
				choose_move!
			end
		end

		def choose_move!
			#if inventory is running low, head home
			#elsif neighbors with opponents is nonempty
			# => check if you can be a dick to any neighbors
			#elsif any neighbor is unexplored
			# => explore a random unexplored neighbor
			#else
			# => find the closest unexplored point move towards it
			#end
		end

		def take_action!(tilestate)
			#if the tile sucks and the previous tile is better
			# => run away
			#elsif there is a player adjacent
			#	if there are more zombies than can be killed at once
			#		kill zombies
			#	elsif there are fewer zombies than can be killed at once
			#		only kill zombies if the payoff from killing them is greater than the loss of letting the other player take the customers
			#else
			# => kill if there are zombies present
			# => else sell to as many customers as possible, nonsinks first
			#end
		end

		def player_location
			@player.location
		end

		def other_player_locations
			@others.map(&:location)
		end
	end

	class Board
		attr_accessor :client, :dimensions, :tiles, :stores

		def initialize(hashdata, client)
			@client = client
			@stores = []
			@dimensions = {:x => hashdata[:size].first, :y => hashdata[:size].last}
			@tiles = hashdata[:known].map{|t| Tile.new(t, self)}
		end

		def add_store(store)
			@stores << store
		end

		def tiles_adjacent_to(tile)
			x, y = tile.x, tile.y
			adjacency = [[+1,0], [-1,0], [0,+1], [0,-1]]
			adjacency.map do |(dx, dy)|
				if x+dx > 0 && x+dx < @dimensions[:x] && y+dy > 0 && y+dy < @dimensions[:y]
					@tiles.select{|t| t.x == x+dx && t.y == y+dy}.first || Tile.new({:x => x+dx, :y => y+dy}, @board, false)
				end
			end.compact
		end

		def visible_tiles_adjacent_to(tile)
			tiles_adjacent_to(tile).select{|t| t.visible}
		end

		def not_visible_tiles_adjacent_to(tile)
			tiles_adjacent_to(tile).select{|t| !t.visible}
		end

		def other_player_locations
			@client.other_player_locations
		end

		def base_prices
			@client.base_prices
		end
	end

	class Tile
		attr_accessor :board, :x, :y, :zombies, :customers, :visible, :store

		def initialize(hashdata, board, visible = true)
			@board = board
			@visible = visible
			@x = hashdata[:x]
			@y = hashdata[:y]
			@zombies = hashdata[:zombies]
			@customers = hashdata[:customers].map{|c| Customer.new(c, self)}
			if hashdata[:store]
				@board.add_store(self)
				@store = true
			end
		end

		def location
			{:x => @x, :y => @y}
		end

		def neighbors
			@board.tiles_adjacent_to(self)
		end

		def visible_neighbors
			@board.visible_tiles_adjacent_to(self)
		end

		def not_visible_neighbors
			@board.not_visible_tiles_adjacent_to(self)
		end

		def neighbors_with_other_players
			other_player_locations = @board.other_player_locations
			visible_neighbors.select{|n| other_player_locations.include? n.location }
		end

		def neighbors_with_no_other_players
			other_player_locations = @board.other_player_locations
			visible_neighbors.reject{|n| other_player_locations.include? n.location }
		end

		def base_prices
			@board.base_prices
		end
	end

	class Customer
		attr_accessor :tile, :id, :favorite

		def initialize(hashdata, tile)
			@tile = tile
			@id = hashdata[:id]
			@favorite = {
				:type => hashdata[:favorite_type],
				:price => hashdata[:favorite_price],
				:number => hashdata[:favorite_number],
			}
		end

		def base_prices
			@tile.base_prices
		end
	end

	class Player
		attr_accessor :who, :location, :prev_location, :score, :money, :inventory, :kills, :sales, :turns_remaining, :can_act, :can_move

		def initialize(hashdata, who = :other)
			@who = who
			@location = {:x => hashdata[:x], :y => hashdata[:y]}
			@prev_location = {:x => hashdata[:prev_x], :y => hashdata[:prev_y]}
			@score = hashdata[:score]
			@money = hashdata[:money]
			@inventory = if (who == :self)
				{	:v => hashdata[:vanilla],
					:c => hashdata[:chocolate],
					:s => hashdata[:strawberry] }
			else
				nil
			end
			@kills = hashdata[:kills]
			@sales = hashdata[:sales]
			@turns_remaining = hashdata[:turns_remaining]
			@can_act = hashdata[:can_act]
			@can_move = hashdata[:can_move]
		end
	end
end
