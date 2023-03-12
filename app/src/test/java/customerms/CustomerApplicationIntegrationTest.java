package customerms;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(
    properties = {
      "spring.main.allow-bean-definition-overriding=true",
      "server.servlet.context-path=/"
    })
public class CustomerApplicationIntegrationTest {

  @LocalServerPort private int port;

  @Test
  public void testApplication_whenInfo_then200() {
    given().get("http://localhost:" + port + "/info").then().statusCode(200);
  }
}
