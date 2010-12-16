class Game < ActiveRecord::Base
  scope :waiting, :conditions => ["player1_id IS NULL OR player2_id IS NULL"]
  has_one :game_board
  attr_accessible :player1_id, :player2_id
  
  COSTS = { "V" => 1, "C" => 2, "S" => 3 }
  PRICES = { "V" => 1.5, "C" => 4, "S" => 4.5 }
  
  
  def self.new_with_game_board
    g = new
    g.game_board = GameBoard.new(:x => 75, :y => 75)
    g
  end

  def move_error
    return @error
  end
  
  def add_player!(p)
    if !player1_id
      puts "adding player: #{p.inspect}"
      self.player1_id = p.id
    else
      self.player2_id = p.id
    end
    self.save!
  end
  
  def turns_remaining
    return player1.turns_remaining + player2.turns_remaining if player1 && player2
    return nil
  end
  
  
  def player1
    @player1 ||= Player.find(self.player1_id) if player1_id
  end
  def player2
    @player2 ||= Player.find(self.player2_id) if player2_id
  end
  def other_player(p)
    return player2 if p == player1
    return player1
  end
  
  def game_over?
    return turns_remaining != nil && self.turns_remaining <= 0
  end
  
  def won?(p)
    other = other_player(p)
    return false if p.nil? || other.nil?
    return false if self.turns_remaining > 0
    return p.get_score > other.get_score
  end
  
  def move(player, x, y)
    unless x < game_board.x && x >= 0 && y >= 0 && y < game_board.y
      @error = "co-ordinates not on the game board"
      return nil
    end
    
    unless player.can_move?
      @error = "it is not this players turn to move"
      return nil
    end
    player.can_move = false
    player.can_act = true
    player.update_position(x, y)
    player.save!
    
    tile = game_board.tiles.with_coordinates(x, y) || Tile.generate!(x, y)
    return tile
    #check x, y are valid
    #try to find tile
    # if tile doesn't exist - generate tile, save it
    # return tile
    
  end
  
  def ready?
    return player1_id && player2_id
  end
  
  def start_game
    player1.update_attributes(:can_move => true)
  end
  
  
  def buy(player, flavor, num)
    
    # validate the player is on a store
    # validate the player has enough money
    
    if player.money >= PRICES[flavor]*num
      #ok
    else
      #not ok
    end
    
  end
  

  
end
