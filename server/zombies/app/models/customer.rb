class Customer < ActiveRecord::Base
  
  TYPES = ["C", "S", "V", "C,S,V", "C,S", "C,V", "V,S", "C,C", "V,V", "S,S", "C,S,V,S,V", "S,S,S,S"]
  NUMS = [-1, 1, 2, 3, 4, 5]
  attr_accessible :favorite_type, :favorite_price, :favorite_number
  belongs_to :tile, :foreign_key => :tile_id
  def self.generate
    c = new()
    c.favorite_type = TYPES[rand(TYPES.length - 1)]
    c.favorite_price = rand(40) + 2
    c.favorite_number = NUMS[rand(NUMS.length - 1)]
    c
  end
  
  def to_hash
    return {
      :id => id,
      :favorite_type => favorite_type,
      :favorite_price => favorite_price,
      :favorite_number => favorite_number
    }
    
  end
  
  def can_consume?(flavor, number)
    if flavor == favorite_type
      return true if favorite_number == -1 || favorite_number >= number
    else
      return true if number == 1 && Game::PRICES.keys.include?(flavor)
    end
    return false
  end
  
  
end
