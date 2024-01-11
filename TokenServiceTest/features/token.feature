Feature: Token generation
  Scenario: Successful Token Generated
    Given a customer with id "cid1"
    When the customer initiates a token generation with "cid1"
    Then the customer receives a generated token that includes "cid1"

  Scenario: Generate list of tokens
    Given a customer with id "cid1"
    When the customer initiates a token list generation with "cid1"
    Then the customer receives a generated list of token that includes "cid1"

  Scenario: Validate a token
    Given a customer with id "cid1" and a token list
    When  a valid token is sent for validation
    Then the token service
    And the token is no longer usable

