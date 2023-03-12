package customerms.persistence.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import customerms.CustomerMsResponseUtil;
import customerms.api.model.product.ProductCategory;
import java.text.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderLineEntityTest {

  OrderLineEntity orderLine;

  @BeforeEach
  public void setup() throws ParseException {
    orderLine = CustomerMsResponseUtil.defaultOrderEntity().getItems().get(0);
  }

  @Test
  public void testGetCategory_whenNull_thenGet() {
    ProductCategory category = null;
    orderLine.setCategory(category);
    assertNull(orderLine.getCategory());
  }

  @Test
  public void testSetCategory_whenValid_thenSet() {
    orderLine.setCategory(ProductCategory.BEER);
    assertEquals("beer", orderLine.getCategory());
  }

  @Test
  public void testSetCategory_whenNull_thenSet() {
    String category = null;
    orderLine.setCategory(category);
    assertNull(orderLine.getCategory());
  }
}
