class Game < ActiveRecord::Base
  scope :waiting, :conditions => ["player1_id IS NULL OR player2_id IS NULL"]
  has_one :game_board
  attr_accessible :player1_id, :player2_id
  
  COSTS = { "V" => 1, "C" => 2, "S" => 3 }
  PRICES = { "V" => 1.5, "C" => 4, "S" => 4.5 }
  
  
  def self.new_with_game_board
    g = new
    g.game_board = GameBoard.new(:x => 75, :y => 75)
    g
  end

  def move_error
    return @error
  end
  def error
    return @error
  end
  
  def add_player!(p)
    if !player1_id
      puts "adding player: #{p.inspect}"
      self.player1_id = p.id
    else
      self.player2_id = p.id
    end
    self.save!
  end
  
  def turns_remaining
    return player1.turns_remaining + player2.turns_remaining if player1 && player2
    return nil
  end
  
  
  def player1
    @player1 ||= Player.find(self.player1_id) if player1_id
  end
  def player2
    @player2 ||= Player.find(self.player2_id) if player2_id
  end
  def other_player(p)
    return player2 if p == player1
    return player1
  end
  
  def game_over?
    return turns_remaining != nil && self.turns_remaining <= 0
  end
  
  def won?(p)
    other = other_player(p)
    return false if p.nil? || other.nil?
    return false if self.turns_remaining > 0
    return p.get_score > other.get_score
  end
  
  def move(player, x, y)
    x = x.to_i
    y = y.to_i
    unless x < game_board.x && x >= 0 && y >= 0 && y < game_board.y
      @error = "co-ordinates not on the game board"
      return nil
    end
    
    unless player.can_move?
      @error = "it is not this players turn to move"
      return nil
    end
    
    if (player.x - x ).abs + (player.y - y).abs > 1
      @error = "you can only move one tile at a time"
      return nil
    end
    
    player.can_move = false
    player.can_act = true
    player.update_position(x, y)
    player.save!
    
    tile = game_board.tiles.with_coordinates(x, y).first || Tile.generate!(x, y, :game_board_id => game_board.id)
    puts tile
    return tile
    #check x, y are valid
    #try to find tile
    # if tile doesn't exist - generate tile, save it
    # return tile
    
  end
  
  def ready?
    return player1_id && player2_id
  end
  
  def start_game
    player1.update_attributes(:can_move => true)
  end  
  
  def action_result(player, tile)
    return {
      :player => player.to_hash,
      :tile => tile.to_hash
    }
    
  end
  
  
  def kill(player)
    @player = player
    tile = @player.game.game_board.tiles.with_coordinates(@player.x, @player.y).first()
    unless tile
      @error = "tile not found"
      return nil
    end
    tile.update_attributes(:zombies => tile.zombies - 1) if tile.zombies > 0
    @player.update_attributes(:can_act => false, :turns_remaining => @player.turns_remaining - 1, :kills => @player.kills + 1)
    @player.game.other_player(@player).update_attributes(:can_move => true)
    return action_result(@player, tile)
  end
  
  
  def buy(player, flavor, num)
    num = num.abs
    
    if !PRICES[flavor]
      @error = 'not a valid ice-cream'
      return nil
    end
    @tile = game_board.tiles.with_coordinates(player.x, player.y).first()
    
    unless @tile.store?
      @error = 'you are not on a store'
      return nil
    end
    # validate the player is on a store
    # validate the player has enough money
    amount = COSTS[flavor]*num
    
    if amount > player.money
      @error = 'not enough money'
      return nil
    end
    
    player[flavor] = player[flavor] + num
    player.money -= amount
    finish_action(player)
    player.save!
    return action_result(player, @tile)
    #ok
  end
  
  def sell(player, flavor, number, customer_id)
    @player = player
    tile = @player.game.game_board.tiles.with_coordinates(@player.x, @player.y).first()
    begin
      @customer = tile.customers.find(customer_id)
    rescue ActiveRecord::RecordNotFount
      @error = "Could not find customer"
      return nil
    end
    
    flavors = PRICES.keys + [@customer.favorite_type]
    prices = PRICES.merge({@customer.favorite_type => @customer.favorite_price})
    
    if tile.zombies > 0
      @error = "you cannot sell to customers when there are zombies around!"
      return nil
    end
    
    if !flavors.include?(flavor)
      @error = "invalid ice cream combo specified, only valid: #{base_flavors.inspect}"
      return nil
    end
    
    to_sell = flavor.split(/,\s*/)
    
    if (!@customer.can_consume?(flavor, number))
      @error = "you can't sell that many ice creams"
      return nil
    end
    
    to_sell.each do |flav|#stuff
      @player[flav] -= number
      if @player[flav] < 0
        @error = "you don't have that many ice-creams to sell"
        return nil
      end
    end
    @player.money += prices[flavor]*number
    @player.sales += 1
    @customer.destroy
    finish_action(@player)
    return action_result(@player, tile)
  end
  
  def finish_action(player)
    player.can_act = false
    player.turns_remaining -= 1
    player.save!
    player.game.other_player(player).update_attributes(:can_move => true)
  end
  

  
end
