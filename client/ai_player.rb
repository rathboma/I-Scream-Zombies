require 'rubygems'
require 'json'
require 'net/http'

module AIPlayer
	class Client
		attr_accessor :board, :player, :others
		def initialize()
		end

		def make_move(gamestate)
			@board = AIPlayer::Board.new(gamestate[:board])
			@player = AIPlayer::Player.new(gamestate[:players][:you], :self)
			@others = gamestate[:players][:others].map{|o| AIPlayer::Player.new(o)}
			choose_move!
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

		def take_action(tilestate)
			#if the tile sucks and the previous tile is better\
			# => run away
			#elsif there is a player adjacent
			#	if there are more zombies than can be killed at once
			#		kill zombies
			#	elsif there are fewer zombies than can be killed at once
			#		only kill zombies if the payoff from killing them is greater than the loss of letting the other player take the customers
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
	end

	class Tile
		attr_accessor :board, :x, :y, :zombies, :customers, :visible

		def initialize(hashdata, board, visible = true)
			@board = board
			@visible = visible
			@x = hashdata[:x]
			@y = hashdata[:y]
			@zombies = hashdata[:zombies]
			@customers = hashdata[:customers].map{|c| Customer.new(c, self)}
			@board.add_store(self) if hashdata[:store]
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
	end

	class Customer
		@@base_prices = { "v" => 2, "c" => 3, "s" => 3 }
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
	end

	class Player
		attr_accessor :who, :location, :score, :money, :inventory, :kills, :sales, :turns_remaining, :can_act, :can_move

		def initialize(hashdata, who = :other)
			@who = who
			@location = {:x => hashdata[:x], :y => hashdata[:y]}
			@score = hashdata[:score]
			@money = hashdata[:money]
			if who == :self
				@inventory = {
					:v => hashdata[:vanilla],
					:c => hashdata[:chocolate],
					:s => hashdata[:strawberry]
				}
			else
				@inventory = nil
			end
			@kills = hashdata[:kills]
			@sales = hashdata[:sales]
			@turns_remaining = hashdata[:turns_remaining]
			@can_act = hashdata[:can_act]
			@can_move = hashdata[:can_move]
		end
	end
end
