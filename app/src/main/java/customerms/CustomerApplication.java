package customerms;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** Springboot main class. */
@SpringBootApplication
@EnableJpaRepositories
@ConfigurationPropertiesScan("customerms.configuration")
public class CustomerApplication {

  @PostConstruct
  void started() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  /**
   * Springboot main method.
   *
   * @param args CLI arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(CustomerApplication.class, args);
  }
}
