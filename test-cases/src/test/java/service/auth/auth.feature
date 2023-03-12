@Smoke
Feature: Authenticate to Customer MS

  Scenario: I have an access token, and I try to make a request
    Given I have an access token
    When I make a request
    Then The system gives me a customer with id: CST0000001

  Scenario: I do not have an access token, and I try to make a request
    Given I do not have an access token
    When I make a request anyways
    Then The system shows: "Invalid System Token in request"