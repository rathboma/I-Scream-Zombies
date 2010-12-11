class Player < ActiveRecord::Base
  
  attr_accessible :uuid, :x, :y, :money, :vanilla, :chocolate, :strawberry, :kills, :sales, :turns_remaining, :can_act, :can_move, :prev_x, :prev_y
  
  before_save :set_uuid
  belongs_to :game, :foreign_key => :game_id
  
  def set_uuid
    self.uuid ||= random_string()
  end
  
  
  def turn?
    return can_move || can_act
  end
  
  def update_position(new_x, new_y)
    prev_x = x
    prev_y = y
    x = new_x
    y = new_y
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
