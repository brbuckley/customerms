package customerms.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import customerms.api.model.order.OrderRequest;
import customerms.api.model.product.ProductResponse;
import customerms.exception.NotExistException;
import customerms.persistence.domain.CustomerEntity;
import customerms.service.CustomerService;
import customerms.service.OrderService;
import customerms.service.ProductService;
import customerms.util.HeadersUtil;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/** Controller for the order services. */
@AllArgsConstructor
@RestController
@Validated
public class OrderController {

  private final CustomerService customerService;
  private final OrderService orderService;
  private final ProductService productService;

  /**
   * Post endpoint to add a new order to the system.
   *
   * @param correlationId Optional correlation id header.
   * @param customerId CustomerId.
   * @param order Order Mininmal Object.
   * @return Order.
   * @throws JsonProcessingException Json Processing Exception.
   * @throws NotExistException Not Exists Exception.
   */
  @PostMapping(value = "/{customerId}/orders", produces = "application/json")
  public ResponseEntity postOrder(
      @RequestHeader(name = "X-Correlation-Id", required = false) String correlationId,
      @Pattern(regexp = "^CST[0-9]{7}$") @PathVariable("customerId") String customerId,
      @Valid @RequestBody OrderRequest order)
      throws JsonProcessingException, NotExistException {
    CustomerEntity customer = customerService.getCustomerEntity(customerId);
    ProductResponse[] products = productService.getProductsList(order.getProducts());
    HttpHeaders headers = HeadersUtil.defaultHeaders(correlationId);
    return ResponseEntity.status(201)
        .headers(headers)
        .body(
            orderService.postOrder(
                customer, order, products, headers.getFirst("X-Correlation-Id")));
  }

  /**
   * Get endpoint to find an order saved at the system.
   *
   * @param correlationId Optional correlation id header.
   * @param customerId CustomerId.
   * @param orderId OrderId.
   * @return Order.
   * @throws NotExistException Not Exist Exception.
   */
  @GetMapping(value = "/{customerId}/orders/{orderId}", produces = "application/json")
  public ResponseEntity getOrder(
      @RequestHeader(name = "X-Correlation-Id", required = false) String correlationId,
      @Pattern(regexp = "^CST[0-9]{7}$") @PathVariable("customerId") String customerId,
      @Pattern(regexp = "^ORD[0-9]{7}$") @PathVariable("orderId") String orderId)
      throws NotExistException {
    return ResponseEntity.ok()
        .headers(HeadersUtil.defaultHeaders(correlationId))
        .body(orderService.getOrder(customerId, orderId));
  }
}
