class CreateTiles < ActiveRecord::Migration
  def self.up
    create_table :tiles do |t|
      t.column :zombies, :int
      t.column :x, :int
      t.column :y, :int
      t.column :store, :boolean
      t.column :game_board_id, :int
      t.timestamps
    end
    add_index :tiles, [:x, :y, :game_board_id]
  end

  def self.down
    drop_table :tiles
  end
end
