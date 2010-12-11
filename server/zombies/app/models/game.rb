class Game < ActiveRecord::Base
  scope :waiting, :conditions => ["player1_id IS NULL OR player2_id IS NULL"]
  
  has_one :game_board, :foreign_key => :game_board_id
  
  def move_error
    return @error
  end
  
  def add_player(p)
    if !player1_id
      player1_id = p.id
    else
      player2_id = p.id
    end
  end
  
  
  def player1
    Player.find_by_uuid(self.player1_id)
  end
  def player2
    Player.find_by_uuid(self.player2_id)
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
  

  
end
