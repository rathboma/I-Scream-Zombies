class Tile < ActiveRecord::Base
  attr_accessible :x, :y, :store, :zombies
  
  has_many :customers
  
  scope :with_coordinates, lambda {|x, y| where(:x => x, :y => y)}
  
  def self.generate!(x, y)
    t = new()
    t.x = x
    t.y = y
    t.zombies = rand(3)
    cust = rand(3)
    (0..cust).each do |i|
      t.customers.add(Customer.generate)
    end
    t.save!
    t
  end
  
  
end
