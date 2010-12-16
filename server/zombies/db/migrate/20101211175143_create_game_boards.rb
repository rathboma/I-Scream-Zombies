class CreateGameBoards < ActiveRecord::Migration
  def self.up
    create_table :game_boards do |t|
      t.column :x, :int
      t.column :y, :int
      t.column :game_id, :int
      t.timestamps
    end
    add_index :game_boards, :game_id
  end

  def self.down
    drop_table :game_boards
  end
end
