@Smoke
Feature: Create new Order at MS

  Scenario: Request to create a new Order
    Given I want to create a new Order
    And The new Order should be 1 from product PRD0000001 for customer CST0000001
    When The order is received by the system
    Then The system gives me an order with a new id, and send message to queue


  Scenario: Request to create a new Order with unknown product
    Given I want to create a new Order
    And The new Order should be 2 from product PRD0000004 for customer CST0000001
    When The order is received by the system
    Then The system gives me an order with a new id, and send message to queue


  Scenario: Request to create a new Order for a product that does not exist
    Given I want to create a new Order
    And The new Order should be 3 from product PRD0000005 for customer CST0000001
    When The order is received by the system
    Then The system shows: "Resource Not Found"


  Scenario: Request to create a new Order missing quantity
    Given I want to create a new Order
    And The new Order should be product PRD0000001 for customer CST0000001
    When The incomplete order is received by the system
    Then The system shows: "Missing required request parameters"

  Scenario: Request to create a new Order with invalid quantity
    Given I want to create a new Order
    And The new Order should be 0 from product PRD0000001 for customer CST0000001
    When The order is received by the system
    Then The system shows: "Missing required request parameters"

  Scenario: Request to create a new Order for invalid Customer
    Given I want to create a new Order
    And The new Order should be 1 from product PRD0000001 for customer invalid
    When The order is received by the system
    Then The system shows: "Missing required request parameters"