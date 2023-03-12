@Smoke
Feature: Get Customer from MS

  Scenario: Request to find a Customer that exists
    Given I want to get a Customer
    And I ask the system for the customer CST0000001
    When I call the system
    Then The system gives me a customer with id: CST0000001

  Scenario: Request to find a Customer with invalid id
    Given I want to get a Customer
    And I ask the system for the customer invalid
    When I call the system
    Then The system shows: "Missing required request parameters"

  Scenario: Request to find a Customer that does not exist
    Given I want to get a Customer
    And I ask the system for the customer CST1234567
    When I call the system
    Then The system shows: "Resource Not Found"