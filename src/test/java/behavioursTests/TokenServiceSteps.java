package behavioursTests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import main.CorrelationId;
import main.TokenService;
import messaging.Event;
import messaging.MessageQueue;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/*Author Marian s233481 and Sandra s233484*/
public class TokenServiceSteps {

    private MessageQueue q = mock(MessageQueue.class);
    private TokenService service = new TokenService(q);
    private CorrelationId correlationId;

    String[] tokenArr1 = new String[]{};
    String[] tokenArr3 = new String[]{};

    String user;

    @Given("A {string} event is published for customer with id {string}")
    public void aEventIsPublishedForCustomerWithId(String event, String customer) {
        user = customer;
        correlationId = CorrelationId.randomId();
        service.generateToken(new Event(event, new Object[]{customer, correlationId}));
    }

    @When("The customer has null tokens")
    public void theCustomerHasNullTokens() {
        tokenArr3 = service.getAllTokens(user);
    }

    @Then("The token success event is pushed")
    public void theTokenSuccessEventIsPushed() {

        var event = new Event("GenerateTokenCompleted", new Object[]{tokenArr3, correlationId});
        verify(q).publish(event);

    }


    @Given("A {string} event for the customer with id {string}")
    public void aEventForTheCustomerWithId(String arg0, String customer) {
        user = customer;
        correlationId = CorrelationId.randomId();
        service.generateToken(new Event(arg0, new Object[]{customer, correlationId}));
    }

    @And("A {string} event is published with a token from the list")
    public void aEventIsPublishedWithATokenFromTheList(String arg0) {
        tokenArr3 = service.getAllTokens(user);
        correlationId = CorrelationId.randomId();
        service.FindToken(new Event(arg0, new Object[]{tokenArr3[0], correlationId}));

    }

    @When("The token is found and removed for the specific customer")
    public void theTokenIsFoundAndRemovedForTheSpecificCustomer() {
        tokenArr3 = new String[]{tokenArr3[1], tokenArr3[2], tokenArr3[3], tokenArr3[4], tokenArr3[5]};
        tokenArr1 = service.getAllTokens(user);
    }

    @Then("The validation success event is pushed")
    public void theValidationSuccessEventIsPushed() {
        var event = new Event("ValidateTokenCompleted", new Object[]{tokenArr1, correlationId});
        verify(q).publish(event);
        assertEquals(String.join(",", tokenArr1), String.join(",", tokenArr3));
    }

    @And("the customer has only {int} tokens left")
    public void theCustomerHasOnlyTokensLeft(int arg0) {
        tokenArr3 = service.getAllTokens(user);
        tokenArr1 = service.getAllTokens(user);
        int x = tokenArr3.length - arg0;
        for (int i = 0; i < x; i++) {
            var newID = CorrelationId.randomId();
            service.FindToken(new Event("ValidateToken", new Object[]{tokenArr1[0], newID}));
            tokenArr1 = service.getAllTokens(user);
        }

        tokenArr3 = new String[]{tokenArr3[5]};
        assertEquals(String.join(",", tokenArr1), String.join(",", tokenArr3));

    }

    @Then("The token success event is pushed with the rest of the tokens")
    public void theTokenSuccessEventIsPushedWithTheRestOfTheTokens() {
        tokenArr3 = service.getAllTokens(user);
        var event = new Event("GenerateTokenCompleted", new Object[]{tokenArr3, correlationId});
        verify(q).publish(event);

    }
}
