package customerms.api.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import customerms.CustomerMsResponseUtil;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderResponseTest {

  private Validator validator;
  private OrderResponse orderResponse;

  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void testOrderResponse_whenNullCustomer_thenThrow() throws ParseException {
    orderResponse = CustomerMsResponseUtil.defaultOrderResponse();
    orderResponse.setCustomer(null);
    Set<ConstraintViolation<OrderResponse>> violations = validator.validate(orderResponse);
    assertEquals(
        "customer must not be null",
        violations.iterator().next().getPropertyPath()
            + " "
            + violations.iterator().next().getMessage());
  }

  @Test
  public void testOrderRequest_whenEmptyItems_thenThrow() throws ParseException {
    orderResponse = new OrderResponse();
    orderResponse.setCustomer(CustomerMsResponseUtil.defaultCustomerResponse());
    Set<ConstraintViolation<OrderResponse>> violations = validator.validate(orderResponse);
    assertEquals(
        "items must not be empty",
        violations.iterator().next().getPropertyPath()
            + " "
            + violations.iterator().next().getMessage());
  }

  @Test
  public void testGetProducts_whenValid_thenGet() throws ParseException {
    orderResponse = CustomerMsResponseUtil.defaultOrderResponse();
    List<String> products = new ArrayList<>();
    products.add("PRD0000001");
    assertEquals(products.toString(), orderResponse.getProducts().toString());
  }
}
