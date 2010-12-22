class CreateGameBoards < ActiveRecord::Migration
  def self.up
    create_table :game_boards do |t|
      t.column :x, :int, :default => 0
      t.column :y, :int, :default => 0
      t.column :game_id, :int
      t.timestamps
    end
    add_index :game_boards, :game_id
  end

  def self.down
    drop_table :game_boards
  end
end
