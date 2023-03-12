package customerms.configuration;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Request filter. Triggers when receiving a WebRequest (spring) and adds info from a ServletRequest
 * (javax) to get easier access to some extra info like the request body. This filter is also used
 * for logging the requests.
 */
@Component
@Slf4j
public class RequestFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    ReadTwiceHttpServletRequestWrapper readTwiceHttpServletRequestWrapper =
        new ReadTwiceHttpServletRequestWrapper((HttpServletRequest) request);
    String requestEntityAsString =
        readTwiceHttpServletRequestWrapper
            .getBody()
            .replace("\n", "")
            .replace("\r", "")
            .replace("  ", "");

    log.info(
        "Request received for URL: {} with params: {} and body: {}",
        ((HttpServletRequest) request).getRequestURI(),
        ((HttpServletRequest) request).getQueryString(),
        requestEntityAsString);

    chain.doFilter(readTwiceHttpServletRequestWrapper, response);
  }
}
