package customerms.api.model.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ProductCategoryTest {

  @Test
  public void testFromValue_whenValid_thenCreate() {
    ProductCategory category = ProductCategory.fromValue("beer");
    assertEquals("beer", category.getValue());
  }

  @Test
  public void testFromValue_whenInvalid_thenThrow() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          ProductCategory category = ProductCategory.fromValue("invalid");
        });
  }
}
