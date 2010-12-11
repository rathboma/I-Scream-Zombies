class Game < ActiveRecord::Base
  scope :waiting, :conditions => ["player1_id IS NULL OR player2_id IS NULL"]
  
  has_one :game_board, :foreign_key => :game_board_id
  
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
  

  
end
