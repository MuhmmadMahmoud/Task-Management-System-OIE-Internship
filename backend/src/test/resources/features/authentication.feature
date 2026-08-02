Feature: User registration and login
  As a user I want to make an account and log in
  so that I can use the task manager.

  Scenario: Register a new user
    Given there is no user with username "ahmed"
    When I register with username "ahmed", email "ahmed@test.com" and password "123456"
    Then the response status should be 201
    And I should receive a token

  Scenario: Cannot register with a username that is already taken
    Given a user is registered with username "ahmed", email "ahmed@test.com" and password "123456"
    When I register with username "ahmed", email "another@test.com" and password "123456"
    Then the response status should be 400
    And the error message should be "Username is already taken"

  Scenario: Cannot register with an email that is already used
    Given a user is registered with username "ahmed", email "ahmed@test.com" and password "123456"
    When I register with username "ali", email "ahmed@test.com" and password "123456"
    Then the response status should be 400
    And the error message should be "Email is already used"

  Scenario: Cannot register with a short password
    Given there is no user with username "ahmed"
    When I register with username "ahmed", email "ahmed@test.com" and password "123"
    Then the response status should be 400
    And the error message should be "Password must be at least 6 characters"

  Scenario: Login with the correct password
    Given a user is registered with username "ahmed", email "ahmed@test.com" and password "123456"
    When I login with username "ahmed" and password "123456"
    Then the response status should be 200
    And I should receive a token

  Scenario: Login with a wrong password
    Given a user is registered with username "ahmed", email "ahmed@test.com" and password "123456"
    When I login with username "ahmed" and password "wrongpassword"
    Then the response status should be 400
    And the error message should be "Username or password is wrong"

  Scenario: Login with a username that does not exist
    Given there is no user with username "ahmed"
    When I login with username "ahmed" and password "123456"
    Then the response status should be 400
    And the error message should be "Username or password is wrong"
