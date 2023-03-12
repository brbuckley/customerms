package customerms.api.model.customer;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CustomerGenderTest {

  @Test
  public void testFromValue_whenInvalid_thenThrow() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          CustomerGender gender = CustomerGender.fromValue("invalid");
        });
  }
}
