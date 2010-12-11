class GameController < ApplicationController
  
  respond_to :json
  
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
    
    respond_with {:uuid => p.uuid}    
  end
  
  #get
  def get_turn
    @player = Player.find_by_uuid(params[:id])
    
    respond_with {:turn => @player.turn}
  end
  
  
  #sends player.uuid
  def get_game_state
    @player = Player.find_by_uuid(params[:uuid])
    @game = @player.game
    
    respond_with JsonGame.new(@player, @game.game_board, @game.other_player(@player), @game.game_over?, @game.won?(@player))
    # get game.gamestate
  end
  
  #params: uuid, x, y
  def post_make_move
    #moves to a new tile : if tile doesn't exist, create the tile and add it to the game_state
    @player = Player.find_by_uuid(params[:uuid])
    @game = @player.game
      if @tile = @game.move(@player, params[:x], params[:y])
        respond_with {:tile => @tile, :player => @player}
      else
        respond_with {:error => @game.move_error}
      end
  end
  
  
  def post_perform_action
    
    
    #interacts with the players current tile based on the type of action performed
    
  end
  
  
  
end
