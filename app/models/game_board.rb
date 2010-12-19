class GameBoard < ActiveRecord::Base
  belongs_to :game, :foreign_key => :game_id
  attr_accessible :x, :y
  has_many :tiles
  
  after_create :setup_initial_tiles
  
  
  def setup_initial_tiles
    if tiles.size == 0
      self.tiles << Tile.generate!(self.x / 2, self.y / 2, :store => true)
    end
    
  end
  
  def initial_tile
    self.tiles.where(:store => true).order('id ASC').first()
  end
  
  
  def as_json
    return {
      :size => [self.x, self.y],
      :known => self.tile_list
    }
  end
  
  def to_hash
    return {
      :size => [self.x, self.y],
      :known => self.tile_list
    }
  end
  
  def tile_list
    results = []
    self.tiles.each {|t| results << t.to_hash}
    return results
  end
  
end
