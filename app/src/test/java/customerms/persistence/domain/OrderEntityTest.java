package customerms.persistence.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import customerms.CustomerMsResponseUtil;
import customerms.api.model.order.OrderStatus;
import java.sql.Timestamp;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

public class OrderEntityTest {

  @Test
  public void testSetOrderId_whenValid_thenSet() {
    OrderEntity entity = new OrderEntity();
    entity.setOrderId("ORD0000001");
    assertEquals("ORD0000001", entity.getOrderId());
  }

  @Test
  public void testSetStatus_whenValid_thenSet() throws ParseException {
    OrderEntity entity = CustomerMsResponseUtil.defaultOrderEntity();
    entity.setStatus(OrderStatus.ORDERED);
    assertEquals("ordered", entity.getStatus());
  }

  @Test
  public void testSetStatus_whenNull_thenSet() throws ParseException {
    OrderEntity entity = CustomerMsResponseUtil.defaultOrderEntity();
    OrderStatus status = null;
    entity.setStatus(status);
    assertNull(entity.getStatus());
  }

  @Test
  public void testSetStatus_whenString_thenSet() {
    OrderEntity entity = new OrderEntity();
    entity.setStatus("ordered");
    assertEquals("ordered", entity.getStatus());
  }

  @Test
  public void testSetStatus_whenNullString_thenSet() {
    OrderEntity entity = new OrderEntity();
    String status = null;
    entity.setStatus(status);
    assertNull(entity.getStatus());
  }

  @Test
  public void testSetPurchaseId_whenInt_thenSet() throws ParseException {
    OrderEntity entity = CustomerMsResponseUtil.defaultOrderEntity();
    entity.setOrderId(1);
    assertEquals("ORD0000001", entity.getOrderId());
  }

  @Test
  public void testSetDatetime_whenValid_thenSet() {
    OrderEntity entity = new OrderEntity();
    entity.setDatetime(Timestamp.valueOf("2022-07-29 12:16:00"));
    assertEquals("2022-07-29 12:16:00.0", entity.getDatetime().toString());
  }

  @Test
  public void testSetDatetime_whenNull_thenSet() {
    OrderEntity entity = new OrderEntity();
    entity.setDatetime(null);
    assertNull(entity.getDatetime());
  }

  @Test
  public void testSetUpdated_whenValid_thenSet() {
    OrderEntity entity = new OrderEntity();
    entity.setUpdated(Timestamp.valueOf("2022-07-29 12:16:00"));
    assertEquals("2022-07-29 12:16:00.0", entity.getUpdated().toString());
  }

  @Test
  public void testSetUpdated_whenNull_thenSet() {
    OrderEntity entity = new OrderEntity();
    entity.setUpdated(null);
    assertNull(entity.getUpdated());
  }
}
