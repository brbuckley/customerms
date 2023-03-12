package customerms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import customerms.api.model.product.ProductResponse;
import customerms.configuration.ProductMsConfig;
import customerms.exception.NotExistException;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** Product services. */
@AllArgsConstructor
@Service
@Slf4j
public class ProductService {

  private final ObjectMapper mapper;
  private final ProductMsConfig config;
  private final RestTemplate restTemplate;

  /**
   * Fetches the products from the given order from the DataBase.
   *
   * @param ids List of product ids.
   * @return List of Products.
   * @throws NotExistException Not Exist Exception.
   */
  public ProductResponse[] getProductsList(List<String> ids) throws NotExistException {
    StringBuilder builder = new StringBuilder();
    for (String id : ids) {
      if (builder.length() != 0) {
        builder.append(",");
      }
      builder.append(id);
    }
    String idsString = builder.toString();

    try {
      // This next line throws if Product not found at ProductMS.
      ProductResponse[] products =
          mapper.readerFor(ProductResponse[].class).readValue(callForProduct(idsString));
      log.info("ProductMS Response with Products: {}", Arrays.toString(products));
      return products;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new NotExistException("Product");
    }
  }

  private String callForProduct(String param) throws JsonProcessingException {
    String token = getToken();
    String url = config.getEndpoint() + "?ids=" + param;
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + token);
    HttpEntity<Void> request = new HttpEntity<>(headers);
    return this.restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();
  }

  private String getToken() throws JsonProcessingException {
    String payload =
        "{\"client_id\":\""
            + config.getClientId()
            + "\",\"client_secret\":\""
            + config.getClientSecret()
            + "\",\"audience\":\""
            + config.getAudience()
            + "\",\"grant_type\":\"client_credentials\"}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<String>(payload, headers);

    String responseBody =
        this.restTemplate.postForObject(config.getTokenEndpoint(), request, String.class);
    JsonNode root = mapper.readTree(responseBody);
    return root.path("access_token").asText();
  }
}
