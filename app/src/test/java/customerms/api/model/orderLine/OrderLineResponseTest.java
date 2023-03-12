package customerms.api.model.orderLine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import customerms.CustomerMsResponseUtil;
import customerms.api.model.orderline.OrderLineResponse;
import java.text.ParseException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderLineResponseTest {

  private Validator validator;
  private OrderLineResponse orderLineResponse;

  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void testOrderResponse_whenNullCustomer_thenThrow() throws ParseException {
    orderLineResponse = CustomerMsResponseUtil.defaultOrderResponse().getItems().get(0);
    orderLineResponse.setQuantity(0);
    Set<ConstraintViolation<OrderLineResponse>> violations = validator.validate(orderLineResponse);
    assertEquals(
        "quantity must be greater than 0",
        violations.iterator().next().getPropertyPath()
            + " "
            + violations.iterator().next().getMessage());
  }
}
