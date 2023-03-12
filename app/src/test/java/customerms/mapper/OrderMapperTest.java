package customerms.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import customerms.CustomerMsResponseUtil;
import customerms.api.model.order.OrderResponse;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

public class OrderMapperTest {

  OrderMapper mapper;

  @Test
  public void testFromOrderEntity_whenValid_thenReturnOrderResponse() throws ParseException {
    mapper = new OrderMapper(new OrderLineMapper(), new CustomerMapper());
    OrderResponse response = mapper.fromOrderEntity(CustomerMsResponseUtil.defaultOrderEntity());
    assertEquals("ORD0000001", response.getId());
  }
}
