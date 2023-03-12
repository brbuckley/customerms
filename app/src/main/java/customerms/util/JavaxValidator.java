package customerms.util;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.NoArgsConstructor;

/** Manually validates javax annotations. */
@NoArgsConstructor
public class JavaxValidator {

  static ValidatorFactory factory;
  static Validator validator;

  /**
   * Validates an object.
   *
   * @param object Object.
   * @throws ConstraintViolationException Violation Exceptions.
   */
  public static void validate(Object object) {
    factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
    Set<ConstraintViolation<Object>> exceptions = validator.validate(object);
    if (!exceptions.isEmpty()) {
      throw new ConstraintViolationException(exceptions);
    }
  }
}
