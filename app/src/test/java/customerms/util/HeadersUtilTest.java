package customerms.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;

public class HeadersUtilTest {

  @Test
  public void testDefaultHeaders_whenNullCorrelation_thenRandomCorrelation() {
    assertNotNull(HeadersUtil.defaultHeaders(null).get("X-Correlation-Id"));
  }

  @Test
  public void testDefaultHeaders_whenCorrelation_thenCorrelation() {
    List<String> header = HeadersUtil.defaultHeaders("correlation").get("X-Correlation-Id");
    assert header != null;
    assertEquals("correlation", header.get(0));
  }
}
