# This file is used by Rack-based servers to start the application.

WEBSITE_SUBDIR = 'server/zombies'
require "#{WEBSITE_SUBDIR}/config/environment"
run Zombies::Application
