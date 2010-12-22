class CreatePlayers < ActiveRecord::Migration
  def self.up
    create_table :players do |t|
      t.column :uuid, :string, :unique => true
      t.column :x, :int, :default => 0
      t.column :y, :int, :default => 0
      t.column :prev_x, :int, :default => 0
      t.column :prev_y, :int, :default => 0
      t.column :money, :double, :default => 0.0
      t.column :vanilla, :int, :default => 0
      t.column :chocolate, :int, :default => 0
      t.column :strawberry, :int, :default => 0
      t.column :kills, :int, :default => 0
      t.column :sales, :int, :default => 0
      t.column :turns_remaining, :int, :default => 0
      t.column :turn, :boolean, :default => false
      t.column :can_act, :boolean, :default => false
      t.column :can_move, :boolean, :default => false
      t.column :game_id, :int
      t.timestamps
    end
    add_index :players, :uuid
  end

  def self.down
    drop_table :players
  end
end
