package dtu.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.ResponseMessage;
import static org.junit.jupiter.api.Assertions.*;

public class TokenGenerationSteps {

    ResponseMessage responseMessage = new ResponseMessage();
    TokenService tokenService = new TokenService();

    String customerId = null;

    @Given("a customer with id {string}")
    public void aCustomerWithId(String arg0) {
        customerId = arg0;
    }

    @When("the customer initiates a token generation with {string}")
    public void theCustomerInitiatesATokenGenerationWith(String arg0) {
        responseMessage = tokenService.generateToken(arg0);
    }

    @Then("the customer receives a generated token that includes {string}")
    public void theCustomerReceivesAGeneratedTokenThatIncludes(String arg0)
        {
            assertTrue(responseMessage.getMessage().contains(arg0));
            assertTrue(responseMessage.isSuccessful());
        }
}
