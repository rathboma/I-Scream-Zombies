class CreateCustomers < ActiveRecord::Migration
  def self.up
    create_table :customers do |t|
      t.column :favorite_type, :string
      t.column :favorite_price, :double
      t.column :favorite_number, :int
      t.column :tile_id, :int
      t.timestamps
    end
    add_index :customers, :tile_id
  end

  def self.down
    drop_table :customers
  end
end
