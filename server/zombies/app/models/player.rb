class Player < ActiveRecord::Base
  
  attr_accessible :uuid, :x, :y, :money, :vanilla, :chocolate, :strawberry, :kills, :sales, :turns_remaining, :can_act, :can_move, :prev_x, :prev_y
  
  before_save :set_uuid
  
#  belongs_to :game, :foreign_key => :game_id
  
  def game
    @game ||= Game.find(game_id)
    @game
  end
  
  
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
      :vanilla => vanilla,
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
  
  def to_hash
    return as_hash
  end
  
  def [](flavor)
    case flavor.to_s.upcase
      when 'V' then self.vanilla
      when 'S' then self.strawberry
      when 'C' then self.chocolate
      else nil
    end
  end
  
  def []=(flavor, value)
    case flavor.upcase
    when 'V' then self.vanilla = value
    when 'S' then self.strawberry = value
    when 'C' then self.chocolate = value
    end
  end
  
  def decrement(flavor, number)
    if self[flavor] #base flavor
      self[flavor] -= number
      
    end
    
  end
  
  
  
  def turn?
    return can_move || can_act ? true : false
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
