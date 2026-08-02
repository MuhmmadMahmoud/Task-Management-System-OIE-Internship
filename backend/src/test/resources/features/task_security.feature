Feature: Every user sees only his own tasks
  A user must never read or change the tasks of another user.

  Background:
    Given a user is registered with username "ahmed", email "ahmed@test.com" and password "123456"
    And a user is registered with username "ali", email "ali@test.com" and password "123456"

  Scenario: I can not see the tasks of another user in the list
    Given I am logged in as "ali" with password "123456"
    And I have a task with title "Ali private task", status "TODO" and priority "HIGH"
    When I am logged in as "ahmed" with password "123456"
    And I ask for all my tasks
    Then the response status should be 200
    And I should get 0 tasks

  Scenario: I can not open the task of another user by its id
    Given I am logged in as "ali" with password "123456"
    And I have a task with title "Ali private task", status "TODO" and priority "HIGH"
    When I am logged in as "ahmed" with password "123456"
    And I ask for the task "Ali private task"
    Then the response status should be 404

  Scenario: I can not delete the task of another user
    Given I am logged in as "ali" with password "123456"
    And I have a task with title "Ali private task", status "TODO" and priority "HIGH"
    When I am logged in as "ahmed" with password "123456"
    And I delete the task "Ali private task"
    Then the response status should be 404

  Scenario: I can not get the tasks without a token
    Given I am not logged in
    When I ask for all my tasks
    Then the response status should be 401

  Scenario: I can not create a task with a wrong token
    Given I am logged in with a wrong token
    When I create a task with title "Hack task", status "TODO" and priority "HIGH"
    Then the response status should be 401
