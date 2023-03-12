package customerms.security;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/** Auth0 security configuration. */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.exceptionHandling()
        .authenticationEntryPoint(
            (request, response, exception) -> {
              response.setStatus(401);
              response.setContentType("application/json");
              String correlation = request.getHeader("X-Correlation-Id");
              if (correlation != null) {
                correlation = correlation.replace("\r\n", "");
                URLEncoder.encode(correlation, StandardCharsets.UTF_8);
              } else {
                correlation = UUID.randomUUID().toString();
              }
              response.addHeader("X-Correlation-Id", correlation);
              response.addHeader("X-Request-Id", UUID.randomUUID().toString());
              response
                  .getWriter()
                  .write(
                      "{\n"
                          + "    \"error_code\": \"E_CST_401\",\n"
                          + "    \"description\": \"Invalid System Token in request\"\n"
                          + "}");
              response.getWriter().flush();
              response.flushBuffer();
            });
    http.authorizeRequests()
        .antMatchers("/info", "/healthcheck")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt();
  }

  @Bean
  JwtDecoder jwtDecoder(
      @Value("${auth0.audience}") String audience,
      @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuer) {
    NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuer);

    OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
    OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
    OAuth2TokenValidator<Jwt> withAudience =
        new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

    jwtDecoder.setJwtValidator(withAudience);

    return jwtDecoder;
  }
}
