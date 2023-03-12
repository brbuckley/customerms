package customerms.configuration;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;

/**
 * Custom implementation of RequestWrapper to make possible to read the body twice for logging. "You
 * can't use the InputStream twice, you need to create a wrapper class which keeps a repeatable copy
 * of the InputStream."
 */
@Slf4j
public class ReadTwiceHttpServletRequestWrapper extends HttpServletRequestWrapper {

  private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  /**
   * Custom Constructor.
   *
   * @param request Http Servlet Request
   */
  public ReadTwiceHttpServletRequestWrapper(HttpServletRequest request) {
    super(request);
    try {
      IOUtils.copy(request.getInputStream(), outputStream);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(
        new InputStreamReader(
            new ByteArrayInputStream(outputStream.toByteArray()), StandardCharsets.UTF_8));
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    return new ServletInputStream() {

      @Override
      public int readLine(byte[] b, int off, int len) throws IOException {
        return inputStream.read(b, off, len);
      }

      @Override
      public boolean isFinished() {
        return inputStream.available() > 0;
      }

      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setReadListener(ReadListener arg0) {
        // No implementation needed
      }

      @Override
      public int read() throws IOException {
        return inputStream.read();
      }
    };
  }

  public String getBody() {
    return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
  }
}
