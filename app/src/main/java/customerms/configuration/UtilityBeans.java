package customerms.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/** Creates Beans to be injected by spring. */
@Configuration
public class UtilityBeans {

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    return new ObjectMapper().configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }
}
