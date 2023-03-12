package customerms.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import customerms.util.HeadersUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

public class CustomControllerAdviceTest {

  CustomControllerAdvice advice;

  @BeforeEach
  public void setup() {
    advice = new CustomControllerAdvice();
  }

  @Test
  public void testHandleNullPointerExceptions_whenNullPointer_then400() {
    // Mocks
    MockedStatic<HeadersUtil> util = Mockito.mockStatic(HeadersUtil.class);
    util.when(() -> HeadersUtil.defaultHeaders("correlationId")).thenReturn(new HttpHeaders());
    WebRequest request = Mockito.mock(WebRequest.class);
    when(request.getHeader("X-Correlation-Id")).thenReturn("correlationId");

    // Checking for null bugs
    int status =
        (advice
            .handleValidationExceptions(new NullPointerException(), request)
            .getStatusCodeValue());

    assertEquals(400, status);
    util.close();
  }

  @Test
  public void testHandleExceptions_whenOtherExceptions_then500() {
    // Mocks
    MockedStatic<HeadersUtil> util = Mockito.mockStatic(HeadersUtil.class);
    util.when(() -> HeadersUtil.defaultHeaders("correlationId")).thenReturn(new HttpHeaders());
    WebRequest request = Mockito.mock(WebRequest.class);
    when(request.getHeader("X-Correlation-Id")).thenReturn("correlationId");

    // Checking for null bugs
    int status = (advice.handleExceptions(new Exception(), request).getStatusCodeValue());

    assertEquals(500, status);
    util.close();
  }

  @Test
  public void testHandleNoHandlerFound_whenOtherExceptions_then500()
      throws JsonProcessingException {
    // Mocks
    MockedStatic<HeadersUtil> util = Mockito.mockStatic(HeadersUtil.class);
    util.when(() -> HeadersUtil.defaultHeaders("correlationId")).thenReturn(new HttpHeaders());
    WebRequest request = Mockito.mock(WebRequest.class);
    when(request.getHeader("X-Correlation-Id")).thenReturn("correlationId");

    // Checking for null bugs
    int status =
        (advice
            .handleNoHandlerFound(new NoHandlerFoundException("", "", new HttpHeaders()), request)
            .getStatusCodeValue());

    assertEquals(404, status);
    util.close();
  }
}
