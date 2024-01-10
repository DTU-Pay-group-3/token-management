Feature: Token generation
  Scenario: Successful Token Generated
    Given a customer with id "cid1"
    When the customer initiates a token generation with "cid1"
    Then the customer receives a generated token that includes "cid1"

