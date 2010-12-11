class GameController < ApplicationController
  
  #post
  def join
    @p = Player.new(:name => params[:name])
    g = Game.waiting.first()
    g = Game.new if !g
    @p.save!
    g.add_player(@p)
    g.save!
    @p.game_id = g.id
    @p.save!
    respond_to do |format|
      format.json {render :json => {:uuid => p.uuid}}
    end
    
  end
  
  #get
  def get_turn
    @player = Player.find_by_uuid(params[:id])
    respond_to do |format|
      format.json {render :json => {:turn => @player.turn}}
    end
  end
  
  
  #sends player.uuid
  def get_game_state
    @player = Player.find_by_uuid(params[:uuid])
    @game = @player.game
    respond_to do |format|
      format.json{render :json => JsonGame.new(@player, @game.game_board, @game.other_player(@player), @game.game_over?, @game.won?(@player))}
    end
    # get game.gamestate
  end
  
  
  def post_make_move
    #moves to a new tile : if tile doesn't exist, create the tile and add it to the game_state
  end
  
  
  def post_perform_action
    #interacts with the players current tile based on the type of action performed
    
  end
  
  
  
end
