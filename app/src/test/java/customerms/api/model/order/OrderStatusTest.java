package customerms.api.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class OrderStatusTest {

  @Test
  public void testFromValue_whenInvalid_thenThrow() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          OrderStatus status = OrderStatus.fromValue("invalid");
        });
  }

  @Test
  public void testFromValue_whenValid_thenCreate() {
    OrderStatus status = OrderStatus.fromValue("ordered");
    assertEquals("ordered", status.getValue());
  }
}
