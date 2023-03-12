package customerms.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/** Property injection for ProductMs. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "productms")
public class ProductMsConfig {

  private String endpoint;
  private String tokenEndpoint;
  private String audience;
  private String clientId;
  private String clientSecret;
}
