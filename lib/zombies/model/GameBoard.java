package zombies.model;

public class GameBoard {
  // Put list of players here (for now)
  private String UUID;
  private Player you;
  private Player[] others;
  
  /*
   * :game_board = {
          :size = [x,y]
          :known = [{
            :x,
            :y,
            :store,
            :zombies = int,
            :customers = [
              { :id,
                :favorite_type = [C,C,S, etc],
                :favorite_price,
                :favorite_number,
              }, {cust2}, ..., {custN}
            ]
          }...]
        },
   */
  
  /*
   * Not sure where to put this
   * UUID (which is a key for a particular player in a particular game)
   *        :your_turn = boolean,
        :game_over = boolean,
        :win = boolean
   */
}
