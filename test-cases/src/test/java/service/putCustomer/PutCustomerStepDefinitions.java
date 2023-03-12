package service.putCustomer;

import static net.serenitybdd.rest.SerenityRest.rest;
import static net.serenitybdd.rest.SerenityRest.then;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hamcrest.Matchers;
import service.model.Customer;
import service.util.SetupUtil;

public class PutCustomerStepDefinitions {

  private String api;
  private Customer customer = new Customer();
  private static String token;

  @Before
  public void setup() {
    SetupUtil.setup();
  }

  @BeforeAll
  public static void auth() {
    token = SetupUtil.getToken();
  }

  @Given("I want to update a Customer")
  public void iWantToUpdateACustomer() {
    api = "http://localhost:8080/{customerId}";
  }

  @And("The Customer {word} should have firstname: {word}")
  public void theCustomerShouldHaveFirstname(String customerId, String firstname) {
    customer.setCustomerId(customerId);
    customer.setFirstname(firstname);
  }

  @Then("The system gives me a customer with a id {word} and firstname: {word}")
  public void theSystemGivesMeACustomerWithAIdAndFirstname(String customerId, String firstname) {
    then().body("id", Matchers.equalTo(customerId));
    then().body("firstname", Matchers.equalTo(firstname));
  }

  @Then("The system shows: {string}")
  public void theSystemShows(String status) {
    then().body("description", Matchers.equalTo(status));
  }

  @When("The customer is received by the system")
  public void theCustomerIsReceivedByTheSystem() {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .body(
            "{\n"
                + "  \"firstname\": \""
                + customer.getFirstname()
                + "\",\n"
                + "  \"lastname\": \""
                + customer.getLastname()
                + "\",\n"
                + "  \"gender\": \""
                + customer.getGender()
                + "\",\n"
                + "  \"dob\": \""
                + customer.getDob()
                + "\"\n"
                + "}")
        .put(api, customer.getCustomerId());
  }

  @And(
      "The Customer {word} should have firstname: {word}, lastname: {word}, gender: {word}, birthday: {word}")
  public void theCustomerShouldHaveFirstnameLastnameGenderBirthday(
      String customerId, String firstname, String lastname, String gender, String dob) {
    customer.setCustomerId(customerId);
    customer.setFirstname(firstname);
    customer.setLastname(lastname);
    customer.setGender(gender);
    customer.setDob(dob);
  }

  @When("The incomplete customer is received by the system")
  public void theIncompleteCustomerIsReceivedByTheSystem() {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .body("{\n" + "  \"firstname\": \"" + customer.getFirstname() + "\"\n" + "}")
        .put(api, customer.getCustomerId());
  }

  @And("The Customer should be:")
  public void theCustomerShouldBe(DataTable table) {
    customer.setCustomerId(table.row(0).get(1));
    customer.setFirstname(table.row(1).get(1));
    customer.setLastname(table.row(2).get(1));
    customer.setGender(table.row(3).get(1));
    customer.setDob(table.row(4).get(1));
  }
}
