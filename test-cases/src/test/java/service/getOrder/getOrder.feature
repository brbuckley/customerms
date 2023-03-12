@Smoke
Feature: Get Order from MS

  Scenario: Request to find an Order that exists
    Given I want to get an Order
    And I ask the system for the order ORD0000001 with customer CST0000001
    When I call the system
    Then The system gives me the order

  Scenario: Request to find an Order that does not exist
    Given I want to get an Order
    And I ask the system for the order ORD1234567 with customer CST7654321
    When I call the system
    Then The system shows: "Resource Not Found"

  Scenario: Request to find an Order with invalid id
    Given I want to get an Order
    And I ask the system for the order invalid with customer alsoInvalid
    When I call the system
    Then The system shows: "Missing required request parameters"