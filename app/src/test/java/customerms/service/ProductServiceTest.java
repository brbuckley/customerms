package customerms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import customerms.CustomerMsResponseUtil;
import customerms.api.model.product.ProductResponse;
import customerms.configuration.ProductMsConfig;
import customerms.exception.NotExistException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ProductServiceTest {

  ProductService service;

  @Test
  public void testGetProductsList_whenValid_thenReturnProducts() throws NotExistException {
    String payload =
        "{\"client_id\":\"id\",\"client_secret\":\"secret\",\"audience\":\"audience\",\"grant_type\":\"client_credentials\"}";
    HttpHeaders tokenHeaders = new HttpHeaders();
    tokenHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> tokenRequest = new HttpEntity<String>(payload, tokenHeaders);
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer token");
    HttpEntity<Void> request = new HttpEntity<>(headers);
    ResponseEntity<String> response =
        new ResponseEntity<String>(CustomerMsResponseUtil.productMsResponse(), HttpStatus.OK);
    // Mocks
    RestTemplate mocked = Mockito.mock(RestTemplate.class);
    when(mocked.exchange("endpoint?ids=PRD0000001", HttpMethod.GET, request, String.class))
        .thenReturn(response);
    when(mocked.postForObject("tokenEndpoint", tokenRequest, String.class))
        .thenReturn("{\"access_token\":\"token\"}");
    ProductResponse[] testProducts = {CustomerMsResponseUtil.defaultProductResponse()};

    service =
        new ProductService(
            new ObjectMapper(),
            new ProductMsConfig("endpoint", "tokenEndpoint", "audience", "id", "secret"),
            mocked);

    ProductResponse[] products = service.getProductsList(CustomerMsResponseUtil.productIdList());

    assertEquals(1, products.length);
    assertEquals(testProducts[0], products[0]);
  }

  @Test
  public void testGetProductsList_whenNotFound_thenThrow() {
    service = new ProductService(new ObjectMapper(), new ProductMsConfig(), null);

    assertThrows(
        NotExistException.class,
        () -> service.getProductsList(CustomerMsResponseUtil.productIdList()));
  }

  @Test
  public void testGetProductsList_whenMultipleIds_thenGet() throws NotExistException {
    List<String> ids = new ArrayList<>();
    ids.add("PRD0000001");
    ids.add("PRD0000002");
    String payload =
        "{\"client_id\":\"id\",\"client_secret\":\"secret\",\"audience\":\"audience\",\"grant_type\":\"client_credentials\"}";
    HttpHeaders tokenHeaders = new HttpHeaders();
    tokenHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> tokenRequest = new HttpEntity<String>(payload, tokenHeaders);
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer token");
    HttpEntity<Void> request = new HttpEntity<>(headers);
    ResponseEntity<String> response =
        new ResponseEntity<String>(
            "[{\"id\":\"PRD0000001\",\"name\":\"Heineken\",\"price\":10,\"category\":\"beer\"},{\"id\":\"PRD0000002\",\"name\":\"Amstel\",\"price\":4.99,\"category\":\"beer\"}]",
            HttpStatus.OK);

    RestTemplate mocked = mock(RestTemplate.class);
    when(mocked.exchange(
            "endpoint?ids=PRD0000001,PRD0000002", HttpMethod.GET, request, String.class))
        .thenReturn(response);
    when(mocked.postForObject("tokenEndpoint", tokenRequest, String.class))
        .thenReturn("{\"access_token\":\"token\"}");

    service =
        new ProductService(
            new ObjectMapper(),
            new ProductMsConfig("endpoint", "tokenEndpoint", "audience", "id", "secret"),
            mocked);

    service.getProductsList(ids);
  }
}
