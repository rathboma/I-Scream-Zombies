class CreateGames < ActiveRecord::Migration
  def self.up
    create_table :games do |t|
      t.column :player1_id, :int
      t.column :player2_id, :int
      t.column :game_board_id, :int
      t.timestamps
    end

  end

  def self.down
    drop_table :games
  end
end
