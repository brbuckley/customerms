package service.getOrder;

import static net.serenitybdd.rest.SerenityRest.rest;
import static net.serenitybdd.rest.SerenityRest.then;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hamcrest.Matchers;
import service.util.SetupUtil;

public class GetOrderStepDefinitions {

  private String api;
  private String orderId, customerId;
  private static String token;

  @Before
  public void setup() {
    SetupUtil.setup();
  }

  @BeforeAll
  public static void auth() {
    token = SetupUtil.getToken();
  }

  @Given("I want to get an Order")
  public void iWantToGetAnOrder() {
    api = "http://localhost:8080/{customerId}/orders/{orderId}";
  }

  @And("I ask the system for the order {word} with customer {word}")
  public void iAskTheSystemForTheOrderWithCustomer(String orderId, String customerId) {
    this.orderId = orderId;
    this.customerId = customerId;
  }

  @Then("The system gives me the order")
  public void theSystemGivesMeAnOrderWithId() {
    then().body("id", Matchers.equalTo(orderId));
    then().body("customer.id", Matchers.equalTo(customerId));
  }

  @Then("The system shows: {string}")
  public void theSystemShows(String status) {
    then().body("description", Matchers.equalTo(status));
  }

  @When("I call the system")
  public void iCallTheSystem() {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .get(api, customerId, orderId);
  }
}
