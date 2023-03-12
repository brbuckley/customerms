package service.postCustomer;

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

public class PostCustomerStepDefinitions {

  private String api;
  private Customer customer = new Customer();
  private String firstname;
  private static String token;

  @Before
  public void setup() {
    SetupUtil.setup();
  }

  @BeforeAll
  public static void auth() {
    token = SetupUtil.getToken();
  }

  @Given("I want to create a new Customer")
  public void iWantToCreateANewCustomer() {
    api = "http://localhost:8080/";
  }

  @Then("The system gives me a customer with a new id, and firstname: {word}")
  public void theSystemGivesMeACustomerWithIdAndFirstname(String firstname) {
    then().body("id", Matchers.matchesRegex("CST[0-9]{7}"));
    then().body("firstname", Matchers.equalTo(firstname));
  }

  @When("The new Customer should have firstname: {word}")
  public void theNewCustomerShouldHaveFirstname(String firstname) {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .body("{\n" + "  \"firstname\": \"" + firstname + "\"\n" + "}")
        .post(api);
  }

  @Then("The system shows: {string}")
  public void theSystemShows(String status) {
    then().body("description", Matchers.equalTo(status));
  }

  @When("The new customer is received by the system")
  public void theNewCustomerIsReceivedByTheSystem() {
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
        .post(api);
  }

  @And("The new Customer should be:")
  public void theNewCustomerShouldBe(DataTable table) {
    customer.setFirstname(table.row(0).get(1));
    customer.setLastname(table.row(1).get(1));
    customer.setGender(table.row(2).get(1));
    customer.setDob(table.row(3).get(1));
  }

  @When("The incomplete customer is received by the system")
  public void theIncompleteCustomerIsReceivedByTheSystem() {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .body("{\n" + "  \"firstname\": \"" + firstname + "\"\n" + "}")
        .post(api);
  }

  @And(
      "The new Customer should have firstname: {word}, lastname: {word}, gender: {word}, birthday: {word}")
  public void theNewCustomerShouldHaveFirstnameLastnameGenderBirthday(
      String firstname, String lastname, String gender, String dob) {
    customer.setFirstname(firstname);
    customer.setLastname(lastname);
    customer.setGender(gender);
    customer.setDob(dob);
  }
}
