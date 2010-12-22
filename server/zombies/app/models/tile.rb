class Tile < ActiveRecord::Base
  attr_accessible :x, :y, :store, :zombies, :game_board_id
  belongs_to :game_board
  has_many :customers
  
  scope :with_coordinates, lambda {|x, y| where(:x => x, :y => y)}
  
  def self.generate!(x, y, options = {})
    t = new(options)
    t.x = x
    t.y = y
    t.store = true if rand(100) == 1
    unless t.store
      t.zombies = rand(4)
      cust = rand(4)
      (0..cust).each do |i|
        t.customers << Customer.generate
      end
    end
    t.zombies ||= 0
    t.save!
    t
  end
  
  def to_hash
    return {
      :x => x,
      :y => y,
      :store => store,
      :zombies => zombies,
      :customers => customer_list
    }
  end
  
  def customer_list
    results = []
    self.customers.each {|c| results << c.to_hash}
    return results
  end
  
  
end
