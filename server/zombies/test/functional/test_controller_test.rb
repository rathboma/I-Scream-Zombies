require 'test_helper'

class TestControllerTest < ActionController::TestCase
  test "should get new" do
    get :new
    assert_response :success
  end

  test "should get hello" do
    get :hello
    assert_response :success
  end

end
