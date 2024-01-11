package dtu.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.ResponseMessage;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TokenGenerationSteps {

    ResponseMessage responseMessage = new ResponseMessage();
    TokenService tokenService = new TokenService();
    String[] tokenList = new String[6];
    String customerId = null;


    @Given("a customer with id {string}")
    public void aCustomerWithId(String arg0) {
        customerId = arg0;
    }

    @When("the customer initiates a token generation with {string}")
    public void theCustomerInitiatesATokenGenerationWith(String arg0) {
//        responseMessage = tokenService.generateToken(arg0);
    }

    @Then("the customer receives a generated token that includes {string}")
    public void theCustomerReceivesAGeneratedTokenThatIncludes(String arg0)
        {
            assertTrue(responseMessage.getMessage().contains(arg0));
            assertTrue(responseMessage.isSuccessful());
        }

    @When("the customer initiates a token list generation with {string}")
    public void theCustomerInitiatesATokenListGenerationWith(String arg0) {
//        responseMessage = tokenService.generateToken(arg0);
        tokenList = tokenService.generateToken(arg0);
    }

    @Then("the customer receives a generated list of token that includes {string}")
    public void theCustomerReceivesAGeneratedListOfTokenThatIncludes(String arg0) {
//        assertTrue(responseMessage.getMessage().getClass().isInstance(String[].class));
        assertTrue(tokenList.length>0);
        System.out.println(Arrays.toString(tokenList));
    }

    @Given("a customer with id {string} and a token list")
    public void aCustomerWithIdAndATokenList(String arg0) {
        customerId = arg0;
        tokenList = tokenService.generateToken(arg0);
    }

    @When("a valid token is sent for validation")
    public void aValidTokenIsSentForValidation() {
        responseMessage = tokenService.validateToken(tokenList[0]);
    }

    @Then("the token service")
    public void theTokenService() {
        assertTrue(responseMessage.isSuccessful());
        assertEquals(responseMessage.getMessage(), customerId);

    }

    @And("the token is no longer usable")
    public void theTokenIsNoLongerUsable() {

       responseMessage= tokenService.validateToken(tokenList[0]);
        assertFalse(responseMessage.isSuccessful());
        assertTrue(responseMessage.getMessage().equals("Token not found"));

    }

    @Given("a customer with id {string} and the list of tokens with length {int}")
    public void aCustomerWithIdAndTheListOfTokensWithLength(String arg0, int arg1) {
        customerId = arg0;
        tokenList=tokenService.generateToken(arg0);
       int x = tokenList.length-arg1;
        for(int i = 0; i<x; i++) {
            tokenService.validateToken(tokenList[0]);
        }
    }

    @When("the customer asks for tokens")
    public void theCustomerAsksForTokens() {
        tokenService.generateToken(customerId);
    }

    @Then("the customer receives list of {int} tokens")
    public void theCustomerReceivesListOfTokens(int arg0) {
        assertTrue(tokenList.length == 6 );
    }
}
