class CreateGameBoards < ActiveRecord::Migration
  def self.up
    create_table :game_boards do |t|

      t.timestamps
    end
  end

  def self.down
    drop_table :game_boards
  end
end
