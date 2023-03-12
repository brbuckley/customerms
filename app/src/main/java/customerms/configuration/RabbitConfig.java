package customerms.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/** Property injection for RabbitMq. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitConfig {

  private String host;
}
