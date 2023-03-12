package customerms.api.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import customerms.CustomerMsResponseUtil;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderRequestTest {

  private Validator validator;
  private OrderRequest orderRequest;

  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void testOrderRequest_whenInvalid_thenThrow() {
    orderRequest = CustomerMsResponseUtil.defaultOrderRequest();
    orderRequest.setOrderId("invalid");
    Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);
    assertEquals(
        "orderId must match \"^ORD[0-9]{7}$\"",
        violations.iterator().next().getPropertyPath()
            + " "
            + violations.iterator().next().getMessage());
  }
}
