# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20101211190947) do

  create_table "customers", :force => true do |t|
    t.string   "favorite_type"
    t.float    "favorite_price"
    t.integer  "favorite_number"
    t.integer  "tile_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "customers", ["tile_id"], :name => "index_customers_on_tile_id"

  create_table "game_boards", :force => true do |t|
    t.integer  "x",          :default => 0
    t.integer  "y",          :default => 0
    t.integer  "game_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "game_boards", ["game_id"], :name => "index_game_boards_on_game_id"

  create_table "games", :force => true do |t|
    t.integer  "player1_id"
    t.integer  "player2_id"
    t.integer  "game_board_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "json_games", :force => true do |t|
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "players", :force => true do |t|
    t.string   "uuid"
    t.integer  "x",               :default => 0
    t.integer  "y",               :default => 0
    t.integer  "prev_x",          :default => 0
    t.integer  "prev_y",          :default => 0
    t.float    "money",           :default => 0.0
    t.integer  "vanilla",         :default => 0
    t.integer  "chocolate",       :default => 0
    t.integer  "strawberry",      :default => 0
    t.integer  "kills",           :default => 0
    t.integer  "sales",           :default => 0
    t.integer  "turns_remaining", :default => 0
    t.boolean  "turn",            :default => false
    t.boolean  "can_act",         :default => false
    t.boolean  "can_move",        :default => false
    t.integer  "game_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "players", ["uuid"], :name => "index_players_on_uuid"

  create_table "somethings", :force => true do |t|
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "tiles", :force => true do |t|
    t.integer  "zombies"
    t.integer  "x"
    t.integer  "y"
    t.boolean  "store"
    t.integer  "game_board_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "tiles", ["x", "y", "game_board_id"], :name => "index_tiles_on_x_and_y_and_game_board_id"

end
