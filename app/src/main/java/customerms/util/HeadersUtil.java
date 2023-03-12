package customerms.util;

import java.util.UUID;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

/** Utility class to build headers. */
@NoArgsConstructor
public class HeadersUtil {

  /**
   * Build the default headers.
   *
   * @param correlationId Optional Correlation id.
   * @return Headers.
   */
  public static HttpHeaders defaultHeaders(String correlationId) {
    HttpHeaders responseHeaders = new HttpHeaders();
    if (correlationId == null) {
      correlationId = UUID.randomUUID().toString();
    }
    responseHeaders.set("X-Correlation-Id", correlationId);
    responseHeaders.set("X-Request-Id", UUID.randomUUID().toString());
    return responseHeaders;
  }
}
