package service.postOrder;

import static java.util.concurrent.TimeUnit.SECONDS;
import static net.serenitybdd.rest.SerenityRest.rest;
import static net.serenitybdd.rest.SerenityRest.then;
import static org.awaitility.Awaitility.await;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.File;
import java.net.URL;
import org.hamcrest.Matchers;
import service.util.JacksonValidator;
import service.util.RabbitClient;
import service.util.SetupUtil;

public class PostOrderStepDefinitions {

  private String api;
  private String productId, customerId;
  private int quantity;
  private static String token;

  @Before
  public void setup() {
    SetupUtil.setup();
  }

  @BeforeAll
  public static void auth() {
    token = SetupUtil.getToken();
  }

  @Given("I want to create a new Order")
  public void iWantToCreateANewOrder() {
    api = "http://localhost:8080/{customerId}/orders";
  }

  @And("The new Order should be {int} from product {word} for customer {word}")
  public void theNewOrderShouldBeFromProductForCustomer(
      int quantity, String productId, String customerId) {
    this.quantity = quantity;
    this.productId = productId;
    this.customerId = customerId;
  }

  @And("The new Order should be product {word} for customer {word}")
  public void theNewOrderShouldBeForCustomer(String productId, String customerId) {
    this.productId = productId;
    this.customerId = customerId;
  }

  @Then("The system gives me an order with a new id, and send message to queue")
  public void theSystemGivesMeAnOrderWithANewIdThatHasProductAndCustomer() {
    then().body("id", Matchers.matchesRegex("ORD[0-9]{7}"));
    RabbitClient rabbitClient = new RabbitClient();
    // Tests are faster than rabbit, so I keep reading till the message arrives
    await()
        .timeout(60, SECONDS)
        .until(
            () -> {
              String message = rabbitClient.getMessage();
              if (message != null) {
                JacksonValidator validator = new JacksonValidator();
                URL url = getClass().getResource("/json/RabbitMessageSchema.json");
                assert url != null;
                File schema = new File(url.getPath());
                return validator.validate(message, schema);
              }
              return false;
            });
  }

  @Then("The system shows: {string}")
  public void theSystemShows(String status) {
    then().body("description", Matchers.equalTo(status));
  }

  @When("The incomplete order is received by the system")
  public void theIncompleteOrderIsReceivedByTheSystem() {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .body(
            "{\n"
                + "  \"items\": [\n"
                + "    {\n"
                + "      \"product\": {\n"
                + "        \"id\": \""
                + productId
                + "\"\n"
                + "      }\n"
                + "    }\n"
                + "  ]\n"
                + "}")
        .post(api, customerId);
  }

  @When("The order is received by the system")
  public void theOrderIsReceivedByTheSystem() {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .body(
            "{\n"
                + "  \"items\": [\n"
                + "    {\n"
                + "      \"quantity\": "
                + quantity
                + ",\n"
                + "      \"product\": {\n"
                + "        \"id\": \""
                + productId
                + "\"\n"
                + "      }\n"
                + "    }\n"
                + "  ]\n"
                + "}")
        .post(api, customerId);
  }
}
