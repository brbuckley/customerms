package customerms;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/** Allows configuration of the springboot application. */
public class ServletInitializer extends SpringBootServletInitializer {

  /**
   * Configures the tomcat servlet.
   *
   * @param application Springboot application.
   * @return Springboot Application Builder.
   */
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(CustomerApplication.class);
  }
}
