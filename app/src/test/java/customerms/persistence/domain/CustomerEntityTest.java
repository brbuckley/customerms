package customerms.persistence.domain;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;
import org.junit.jupiter.api.Test;

public class CustomerEntityTest {

  @Test
  public void testSetDob_whenNull_thenSetNull() {
    CustomerEntity entity = new CustomerEntity();
    Date date = null;
    entity.setDob(date);
    assertNull(entity.getDob());
  }
}
