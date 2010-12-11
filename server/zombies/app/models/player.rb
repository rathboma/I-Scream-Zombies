class Player < ActiveRecord::Base
  
  attr_accessible :uuid, :x, :y, :money, :vanilla, :chocolate, :strawberry, :kills, :sales, :turns_remaining, :can_act, :can_move
  
  before_save :set_uuid
  belongs_to :game, :foreign_key => :game_id
  
  
  def set_uuid
    self.uuid ||= random_string()
  end
  
  
  def sanitize
    results = {
      :x => self.x,
      :y => self.y,
      :kills => self.kills
      :sales => self.kills
      :money => self.money
    }
    results
  end
  
  
  private
  
  def random_string
    (0...16).map{65.+(rand(25)).chr}.join
  end
  
end
