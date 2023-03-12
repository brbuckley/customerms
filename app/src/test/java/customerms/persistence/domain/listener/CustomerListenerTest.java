package customerms.persistence.domain.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;

import customerms.CustomerMsResponseUtil;
import customerms.persistence.domain.CustomerEntity;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

public class CustomerListenerTest {

  @Test
  public void testProcess_whenValid_thenSetCustomerId() throws ParseException {
    // In this scenario the customer initially has id = 0 and customerId = CST0000001
    // After processing, the customerId should be based of the id number, which means CST0000000
    CustomerEntity entity = CustomerMsResponseUtil.defaultCustomerEntity();
    assertEquals("CST0000001", entity.getCustomerId());
    CustomerListener listener = new CustomerListener();
    listener.process(entity);
    assertEquals("CST0000000", entity.getCustomerId());
  }
}
