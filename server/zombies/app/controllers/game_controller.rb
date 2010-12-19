class GameController < ApplicationController
  
  respond_to :json
  skip_before_filter :verify_authenticity_token
  #post
  before_filter :set_default_response_format

  

  def set_default_response_format
    request.format = :json if params[:format].nil?
  end
  
  
  def join
    g = Game.waiting.first()
    g = Game.new_with_game_board if !g
    g.save!
    @p = Player.new(:name => params[:name])
    @p.setup(g.game_board.initial_tile)
    @p.save!
    g.add_player!(@p)
    g.start_game if g.ready?
    g.save!
    @p.game_id = g.id
    puts @p.save!
    render :json => {:uuid => @p.uuid}
  end
  
  #get
  def get_turn
    @player = Player.find_by_uuid(params[:id])
    render :json => {:turn => @player.turn}
  end
  
  
  #sends player.uuid
  def get_game_state
    @player = Player.find_by_uuid(params[:uuid])
    unless @player
      respond_with({:error => "player not found"}) 
      return
    end
    @game = @player.game
    resp_obj  = JsonGame.new(@player, @game.game_board, @game.other_player(@player), @game.game_over?, @game.won?(@player))
    respond_with (resp_obj)
    # get game.gamestate
  end
  
  #params: uuid, x, y
  def post_make_move
    #moves to a new tile : if tile doesn't exist, create the tile and add it to the game_state
    @player = Player.find_by_uuid(params[:uuid])
    if !@player
      render :json => {:error => "not a valid player UUID"}
      return
    end
    
    @game = @player.game
      if @tile = @game.move(@player, params[:x], params[:y])
         render :json => {:tile => @tile.to_hash, :player => @player.to_hash}
      else
        render :json => {:error => @game.move_error}
      end
  end
  
  #params = uuid, type, details


  def validate_action!
    uuid = params[:uuid]
    if !uuid || !(@player = Player.find_by_uuid(uuid))
      render :json => {:error => "you didn't supply a valid UUID"}
      return false
    end
    if !@player.can_act
      render :json => {:error => "this player is not allowed to act"}
      return false
    end
  end

  def kill
    return if !validate_action!()
    tile = @player.game.game_board.tiles.with_coordinates(@player.x, @player.y).first()
    tile.update_attributes(:zombies => tile.zombies - 1) if tile.zombies > 0
    @player.update_attributes(:can_act => false, :turns_remaining => @player.turns_remaining - 1)
    @player.game.other_player(@player).update_attributes(:can_move => true)
  end

  def sell
    return if !validate_action!()
    
    flavors = params[:flavors]
    number = Math.abs(params[:number].to_i)
    customer_id = params[:customer_id].to_i
    response = @player.game.buy(@player, flavor, num)
    render :json => response.nil? ? {:error => @player.game.error} : response
  end

  def buy
    return unless validate_action!() && validate_buy_sell!()
    flavor = params[:flavors]
    num = params[:number]
    uuid = params[:uuid]

    if !flavor || !num
      respond_with({:error => "didn't supply either the flavor, or the number to buy"})
      return
    end
    
    response = @player.game.buy(@player, flavor, num)
    respond_with response.nil? ? {:error => @player.game.error} : response


  end
  

  def run
    
  end
end
