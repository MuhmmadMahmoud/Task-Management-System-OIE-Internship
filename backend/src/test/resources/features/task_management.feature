Feature: Managing my tasks
  As a logged in user I want to create, read, update and delete my tasks
  so that I can organize my work.

  Background:
    Given a user is registered with username "ahmed", email "ahmed@test.com" and password "123456"
    And I am logged in as "ahmed" with password "123456"

  Scenario: Create a new task
    When I create a task with title "Finish the mini project", status "TODO" and priority "HIGH"
    Then the response status should be 201
    And the task in the response should have title "Finish the mini project"
    And the task in the response should have status "TODO"
    And the task in the response should have priority "HIGH"

  Scenario: A new task takes the default status and priority
    When I create a task with title "Read a book" without status and priority
    Then the response status should be 201
    And the task in the response should have status "TODO"
    And the task in the response should have priority "MEDIUM"

  Scenario: Cannot create a task without a title
    When I create a task with title "" without status and priority
    Then the response status should be 400
    And the error message should be "Title is required"

  Scenario: Get all my tasks
    Given I have a task with title "Task one", status "TODO" and priority "LOW"
    And I have a task with title "Task two", status "DONE" and priority "HIGH"
    When I ask for all my tasks
    Then the response status should be 200
    And I should get 2 tasks

  Scenario: Get one task by its id
    Given I have a task with title "Task one", status "TODO" and priority "LOW"
    When I ask for the task "Task one"
    Then the response status should be 200
    And the task in the response should have title "Task one"

  Scenario: Get a task that does not exist
    When I ask for the task with id 999
    Then the response status should be 404
    And the error message should be "Task not found with id 999"

  Scenario: Update a task
    Given I have a task with title "Task one", status "TODO" and priority "LOW"
    When I update the task "Task one" to title "Task one updated", status "IN_PROGRESS" and priority "HIGH"
    Then the response status should be 200
    And the task in the response should have title "Task one updated"
    And the task in the response should have status "IN_PROGRESS"
    And the task in the response should have priority "HIGH"

  Scenario: Delete a task
    Given I have a task with title "Task one", status "TODO" and priority "LOW"
    And I have a task with title "Task two", status "DONE" and priority "HIGH"
    When I delete the task "Task one"
    Then the response status should be 204
    When I ask for all my tasks
    Then I should get 1 tasks

  Scenario: Filter the tasks by status
    Given I have a task with title "Task one", status "TODO" and priority "LOW"
    And I have a task with title "Task two", status "DONE" and priority "HIGH"
    And I have a task with title "Task three", status "DONE" and priority "LOW"
    When I filter my tasks by status "DONE"
    Then the response status should be 200
    And I should get 2 tasks

  Scenario: Filter the tasks by priority
    Given I have a task with title "Task one", status "TODO" and priority "LOW"
    And I have a task with title "Task two", status "DONE" and priority "HIGH"
    And I have a task with title "Task three", status "DONE" and priority "LOW"
    When I filter my tasks by priority "LOW"
    Then the response status should be 200
    And I should get 2 tasks

  Scenario: Filter the tasks by status and priority together
    Given I have a task with title "Task one", status "TODO" and priority "LOW"
    And I have a task with title "Task two", status "DONE" and priority "HIGH"
    And I have a task with title "Task three", status "DONE" and priority "LOW"
    When I filter my tasks by status "DONE" and priority "LOW"
    Then the response status should be 200
    And I should get 1 tasks
