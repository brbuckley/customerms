package customerms.api.model.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerRequestTest {

  private Validator validator;
  private CustomerRequest customerRequest;

  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void testCustomerRequest_whenFirstnameNull_thenThrow() {
    customerRequest = new CustomerRequest();
    customerRequest.setLastname("Smith");
    Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(customerRequest);
    assertEquals(
        "firstname must not be blank",
        violations.iterator().next().getPropertyPath()
            + " "
            + violations.iterator().next().getMessage());
  }

  @Test
  public void testCustomerRequest_whenFirstnameTooBig_thenThrow() {
    customerRequest = new CustomerRequest();
    customerRequest.setFirstname("JOSÉ-VICTOR-BRAGA-DE-MELO-DE-FARIAS-BARRETO-DE-MACEDO");
    customerRequest.setLastname("Smith");
    Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(customerRequest);
    assertEquals(
        "firstname size must be between 1 and 50",
        violations.iterator().next().getPropertyPath()
            + " "
            + violations.iterator().next().getMessage());
  }

  @Test
  public void testCustomerRequest_whenGenderInvalid_thenThrow() {
    customerRequest = new CustomerRequest();
    customerRequest.setFirstname("Bob");
    customerRequest.setLastname("Smith");
    customerRequest.setGender("invalid");
    Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(customerRequest);
    assertEquals(
        "gender must match \"male|female\"",
        violations.iterator().next().getPropertyPath()
            + " "
            + violations.iterator().next().getMessage());
  }

  @Test
  public void testCustomerRequest_whenDobInvalid_thenThrow() {
    customerRequest = new CustomerRequest();
    customerRequest.setFirstname("Bob");
    customerRequest.setLastname("Smith");
    customerRequest.setDob("99-99-1999");
    Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(customerRequest);
    assertEquals(
        "dob must match \"(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])-[0-9]{4}\"",
        violations.iterator().next().getPropertyPath()
            + " "
            + violations.iterator().next().getMessage());
  }

  @Test
  public void testCustomerRequest_whenEverythingIsWrong_thenThrow() {
    customerRequest = new CustomerRequest();
    customerRequest.setFirstname("JOSÉ-VICTOR-BRAGA-DE-MELO-DE-FARIAS-BARRETO-DE-MACEDO");
    customerRequest.setGender("invalid");
    customerRequest.setDob("99-99-1999");
    Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(customerRequest);
    Iterator<ConstraintViolation<CustomerRequest>> errors = violations.iterator();
    ConstraintViolation<CustomerRequest> error;
    List<String> messages = new ArrayList<>();
    while (errors.hasNext()) {
      error = errors.next();
      messages.add(error.getPropertyPath() + " " + error.getMessage());
    }
    String messageString = messages.toString();
    assertTrue(messageString.contains("firstname size must be between 1 and 50"));
    assertTrue(messageString.contains("lastname must not be blank"));
    assertTrue(messageString.contains("gender must match \"male|female\""));
    assertTrue(
        messageString.contains("dob must match \"(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])-[0-9]{4}\""));
  }
}
