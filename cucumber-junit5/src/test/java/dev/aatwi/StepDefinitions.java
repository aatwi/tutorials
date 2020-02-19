package dev.aatwi;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {

    private Greeter greeter;
    private String actualGreetMessage;
    private String clientName;

    @Given("{word} is the greeter today")
    public void defineTodayGreeter(String greeterName) {
        greeter = Greeter.aGreeter(greeterName);
    }

    @When("{string} enters the shop")
    public void clientEntersTheShop(String clientName) {
        this.clientName = clientName;
    }

    @Then("The client should be greeted with {string}")
    public void clientShouldBeGreetedWith(String greetingMessage) {
        actualGreetMessage = greeter.greet(clientName);
        assertEquals(greetingMessage, actualGreetMessage);
    }
}
