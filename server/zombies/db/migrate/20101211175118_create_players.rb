class CreatePlayers < ActiveRecord::Migration
  def self.up
    create_table :players do |t|
      t.column :uuid, :string, :unique => true
      t.column :x, :int
      t.column :y, :int
      t.column :prev_x, :int
      t.column :prev_y, :int
      t.column :money, :double
      t.column :vanilla, :int
      t.column :chocolate, :int
      t.column :strawberry, :int
      t.column :kills, :int
      t.column :sales, :int
      t.column :turns_remaining, :int
      t.column :turn, :boolean
      t.column :can_act, :boolean
      t.column :can_move, :boolean
      t.column :game_id, :int
      t.timestamps
    end
    add_index :players, :uuid
  end

  def self.down
    drop_table :players
  end
end
