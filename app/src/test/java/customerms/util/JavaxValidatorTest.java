package customerms.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import customerms.api.model.customer.CustomerRequest;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

public class JavaxValidatorTest {

  @Test
  public void testValidate_whenInvalid_thenThrow() {
    assertThrows(
        ConstraintViolationException.class, () -> JavaxValidator.validate(new CustomerRequest()));
  }
}
