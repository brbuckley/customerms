package customerms.api.controller;

import customerms.api.model.ErrorResponse;
import customerms.exception.NotExistException;
import customerms.util.HeadersUtil;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

/** Customized exception handler. */
@ControllerAdvice
@Slf4j
public class CustomControllerAdvice {

  /**
   * Custom 400 message. Deals with validation related exceptions.
   *
   * @param e Exception.
   * @param request Web Request.
   * @return Response.
   */
  @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
  public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception e, WebRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST; // 400

    log.error("E_CST_400 | {}", e.getMessage());

    return new ResponseEntity<>(
        new ErrorResponse("E_CST_400", "Missing required request parameters"),
        HeadersUtil.defaultHeaders(request.getHeader("X-Correlation-Id")),
        status);
  }

  /**
   * Custom 500 message. This is a fallback method.
   *
   * @param e Exception.
   * @param request Web Request.
   * @return Response.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleExceptions(Exception e, WebRequest request) {

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

    log.error("E_CST_500 | {}", e.getMessage());

    return new ResponseEntity<>(
        new ErrorResponse("E_CST_500", "An Internal Server Error occurred"),
        HeadersUtil.defaultHeaders(request.getHeader("X-Correlation-Id")),
        status);
  }

  /**
   * Custom 404 message. Handles generic 404s and object not found exceptions.
   *
   * @param e Exception.
   * @param request Web Request.
   * @return Response.
   */
  @ExceptionHandler({NoHandlerFoundException.class, NotExistException.class})
  public ResponseEntity<ErrorResponse> handleNoHandlerFound(Exception e, WebRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND; // 404

    log.error("E_CST_404 | {}", e.getMessage());

    return new ResponseEntity<>(
        new ErrorResponse("E_CST_404", "Resource Not Found"),
        HeadersUtil.defaultHeaders(request.getHeader("X-Correlation-Id")),
        status);
  }
}
