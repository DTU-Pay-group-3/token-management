Feature: Token
  Scenario: Tokens Generated Successfully for new customer
    Given A "GenerateToken" event is published for customer with id "cid"
    When The customer has null tokens
    Then The token success event is pushed

  Scenario: Tokens Generated Successfully
    Given A "GenerateToken" event for the customer with id "cid1"
    And the customer has only 2 tokens left
    When A "GenerateToken" event is published for customer with id "cid"
    Then The token success event is pushed with the rest of the tokens


  Scenario: Token Validated Successfully
    Given A "GenerateToken" event for the customer with id "cid1"
    And A "ValidateToken" event is published with a token from the list
    When The token is found and removed for the specific customer
    Then The validation success event is pushed
