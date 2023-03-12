package customerms.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import customerms.api.model.customer.CustomerRequest;
import customerms.exception.NotExistException;
import customerms.service.CustomerService;
import customerms.util.HeadersUtil;
import java.text.ParseException;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/** Controller for the customer services. */
@AllArgsConstructor
@RestController
@Validated
public class CustomerController {

  private final CustomerService customerService;

  /**
   * Post endpoint to add a new customer to the system.
   *
   * @param correlationId Optional correlation id header.
   * @param customer Customer Minimal Object.
   * @return Customer.
   * @throws ParseException Parse Exception.
   * @throws JsonProcessingException Json Processing Exception.
   */
  @PostMapping(value = "/", produces = "application/json")
  public ResponseEntity postCustomer(
      @RequestHeader(name = "X-Correlation-Id", required = false) String correlationId,
      @Valid @RequestBody CustomerRequest customer)
      throws ParseException, JsonProcessingException {
    return ResponseEntity.status(201)
        .headers(HeadersUtil.defaultHeaders(correlationId))
        .body(customerService.postCustomer(customer));
  }

  /**
   * Get endpoint to find a customer saved at the system.
   *
   * @param correlationId Optional correlation id header.
   * @param customerId CustomerId.
   * @return Customer.
   * @throws NotExistException Not Exist Exception.
   */
  @GetMapping(value = "/{customerId}", produces = "application/json")
  public ResponseEntity getCustomer(
      @RequestHeader(name = "X-Correlation-Id", required = false) String correlationId,
      @Pattern(regexp = "^CST[0-9]{7}$") @PathVariable("customerId") String customerId)
      throws NotExistException {
    return ResponseEntity.ok()
        .headers(HeadersUtil.defaultHeaders(correlationId))
        .body(customerService.getCustomer(customerId));
  }

  /**
   * Put endpoint to update a customer from the system.
   *
   * @param correlationId Optional correlation id header.
   * @param customerId CustomerId.
   * @param customer Updated Customer Mininmal Object.
   * @return Updated Customer.
   * @throws ParseException Parse Exception.
   * @throws NotExistException Not Exist Exception.
   * @throws JsonProcessingException Json Processing Exception.
   */
  @PutMapping(value = "/{customerId}", produces = "application/json")
  public ResponseEntity putCustomer(
      @RequestHeader(name = "X-Correlation-Id", required = false) String correlationId,
      @Pattern(regexp = "^CST[0-9]{7}$") @PathVariable("customerId") String customerId,
      @Valid @RequestBody CustomerRequest customer)
      throws NotExistException, ParseException, JsonProcessingException {
    return ResponseEntity.ok()
        .headers(HeadersUtil.defaultHeaders(correlationId))
        .body(customerService.updateCustomer(customerId, customer));
  }
}
