class GameBoard < ActiveRecord::Base
  attr_accessible :x, :y
  has_many :tiles
  
  
  def to_json
    return {
      :size => [self.x, self.y],
      :known => self.tiles.all
    }.to_json
  end
  
  
end
