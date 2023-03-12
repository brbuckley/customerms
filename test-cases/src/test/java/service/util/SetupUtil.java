package service.util;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.serenitybdd.rest.SerenityRest.rest;
import static net.serenitybdd.rest.SerenityRest.then;
import static org.awaitility.Awaitility.await;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.hamcrest.Matchers;

public class SetupUtil {

  static String payload;
  static EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
  static int maximumWait = Integer.parseInt(variables.getProperty("application.wait.maximum"));
  static String clientId = variables.getProperty("oauth.client.id");
  static String clientSecret = variables.getProperty("oauth.client.secret");

  public static void setup() {
    // Wait 2 minutes, or until spring-boot app starts.
    await()
        .timeout(maximumWait, SECONDS)
        .until(
            () -> {
              try {
                rest().get("http://localhost:8080/healthcheck");
                then().body("status", Matchers.equalTo("UP"));
              } catch (Exception e) {
                // If the server is down a RuntimeException is thrown.
                return false;
              }
              return true;
            });
  }

  public static String getToken() {
    getPayload();
    Response response =
        given()
            .contentType(ContentType.JSON)
            .body(payload)
            .post("https://a1-liquor.us.auth0.com/oauth/token")
            .then()
            .extract()
            .response();
    return response.jsonPath().getString("access_token");
  }

  private static void getPayload() {
    payload =
        "{\"client_id\":\""
            + clientId
            + "\",\"client_secret\":\""
            + clientSecret
            + "\",\"audience\":\"https://customer-ms.com\",\"grant_type\":\"client_credentials\"}";
  }
}
