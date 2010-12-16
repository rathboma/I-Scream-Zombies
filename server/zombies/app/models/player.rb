class Player < ActiveRecord::Base
  
  attr_accessible :uuid, :x, :y, :money, :vanilla, :chocolate, :strawberry, :kills, :sales, :turns_remaining, :can_act, :can_move, :prev_x, :prev_y
  
  before_save :set_uuid
  belongs_to :game, :foreign_key => :game_id
  
  def set_uuid
    self.uuid ||= random_string()
  end
  
  def setup(tile)
    self.x = tile.x
    self.y = tile.y
    self.vanilla = GameConfig::STARTING_VANILLA
    self.chocolate = GameConfig::STARTING_CHOCOLATE
    self.strawberry = GameConfig::STARTING_STRAWBERRY
    self.money = GameConfig::STARTING_MONEY
    self.turns_remaining = GameConfig::STARTING_TURNS
    puts "done setup"
  end
  
  
  def as_hash    
    return {
      :x => x,
      :y => y,
      :money => money,
      :chocolate => chocolate,
      :strawberry => strawberry,
      :kills => kills,
      :sales => sales,
      :turns_remaining => turns_remaining,
      :can_act => can_act,
      :can_move => can_move,
      :uuid => uuid,
      :prev_x => prev_x,
      :turn => turn?,
      :prev_y => prev_y
    }
    
  end
  
  def turn?
    return can_move || can_act
  end
  
  def update_position(new_x, new_y)
    self.prev_x = x
    self.prev_y = y
    self.x = new_x
    self.y = new_y
  end
  
  def sanitize
    results = {
      :x => self.x,
      :y => self.y,
      :kills => self.kills,
      :sales => self.kills,
      :money => self.money
    }
    results
  end
  
  def get_score
    return kills + money / 3 + sales
  end
  
  
  private
  
  def random_string
    (0...16).map{65.+(rand(25)).chr}.join
  end
  
end
