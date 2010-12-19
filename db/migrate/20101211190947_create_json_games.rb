class CreateJsonGames < ActiveRecord::Migration
  def self.up
    create_table :json_games do |t|

      t.timestamps
    end
  end

  def self.down
    drop_table :json_games
  end
end
