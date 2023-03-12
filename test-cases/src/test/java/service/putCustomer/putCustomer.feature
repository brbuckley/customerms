@Smoke
Feature: Update Customer from MS

  Scenario: Request to update a Customer
    Given I want to update a Customer
    And The Customer CST0000001 should have firstname: Billy, lastname: Smith, gender: male, birthday: 12-25-1980
    When The customer is received by the system
    Then The system gives me a customer with a id CST0000001 and firstname: Billy

  Scenario Outline: Request to update a Customer with invalid info
    Given I want to update a Customer
    And The Customer should be:
      | customerId | <customerId> |
      | firstname | <firstname> |
      | lastname | <lastname> |
      | gender | <gender> |
      | dob | <dob> |
    When The customer is received by the system
    Then The system shows: "Missing required request parameters"

  Examples:
    | customerId | firstname | lastname    | gender  | dob        |
    | CST0000001 | Bob       | Smith       | invalid | 12-25-1980 |
    | CST0000001 | Bob       | Smith       | male    | 99-99-1999 |
    | CST0000001 | JOSÉ-VICTOR-BRAGA-DE-MELO-DE-FARIAS-BARRETO-DE-MACEDO | Smith       | male    | 99-99-1999 |

  Scenario: Request to update a Customer missing information
    Given I want to update a Customer
    And The Customer CST0000001 should have firstname: Bob
    When The incomplete customer is received by the system
    Then The system shows: "Missing required request parameters"

  Scenario: Request to update a Customer that does not exist
    Given I want to update a Customer
    And The Customer CST1234567 should have firstname: Billy, lastname: Smith, gender: male, birthday: 12-25-1980
    When The customer is received by the system
    Then The system shows: "Resource Not Found"