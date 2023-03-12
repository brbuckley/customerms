package service.auth;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.serenitybdd.rest.SerenityRest.rest;
import static net.serenitybdd.rest.SerenityRest.then;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hamcrest.Matchers;
import service.util.SetupUtil;

public class AuthStepDefinitions {

  private static String token;

  @Given("I have an access token")
  public void iHaveAnAccessToken() {
    token = SetupUtil.getToken();
  }

  @Then("The system gives me a customer with id: {word}")
  public void theSystemGivesMeACustomerWithId(String customerId) {
    then().body("id", Matchers.equalTo(customerId));
    then().assertThat().body(matchesJsonSchemaInClasspath("json/CustomerResponseSchema.json"));
  }

  @Then("The system shows: {string}")
  public void theSystemShows(String status) {
    then().body("description", Matchers.equalTo(status));
  }

  @When("I make a request")
  public void iMakeARequest() {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .get("http://localhost:8080/{customerId}", "CST0000001");
  }

  @Given("I do not have an access token")
  public void iDoNotHaveAnAccessToken() {
    // do nothing
  }

  @When("I make a request anyways")
  public void iMakeARequestAnyways() {
    rest()
        .header("Content-Type", "application/json")
        .get("http://localhost:8080/{customerId}", "CST0000001");
  }
}
