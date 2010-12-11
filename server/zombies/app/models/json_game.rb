class JsonGame
  attr_accessor :players, :game_board, :your_turn, :game_over, :win
  
  def initialize(player, game_board, other_player, game_over, win)
    @players = {:you => player, :other =>[other_player.sanitize]}
    @game_board = game_board
    @game_over = game_over
    @win = win
  end
  
end
