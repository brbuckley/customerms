@Smoke
Feature: Create new Customer at MS

  Scenario: Request to create a new Customer
    Given I want to create a new Customer
    And The new Customer should have firstname: Bob, lastname: Smith, gender: male, birthday: 12-25-1980
    When The new customer is received by the system
    Then The system gives me a customer with a new id, and firstname: Bob

  Scenario Outline: Request to create a Customer with invalid info
    Given I want to create a new Customer
    And The new Customer should be:
      | firstname | <firstname> |
      | lastname | <lastname> |
      | gender | <gender> |
      | dob | <dob> |
    When The new customer is received by the system
    Then The system shows: "Missing required request parameters"

  Examples:
    | firstname | lastname    | gender  | dob        |
    | Bob       | Smith       | invalid | 12-25-1980 |
    | Bob       | Smith       | male    | 99-99-1999 |
    | JOSÉ-VICTOR-BRAGA-DE-MELO-DE-FARIAS-BARRETO-DE-MACEDO | Smith       | male    | 99-99-1999 |

  Scenario: Request to create a Customer missing information
    Given I want to create a new Customer
    And The new Customer should have firstname: Bob
    When The new customer is received by the system
    Then The system shows: "Missing required request parameters"