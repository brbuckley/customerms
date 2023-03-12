package customerms.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import customerms.CustomerMsResponseUtil;
import customerms.api.model.orderline.OrderLineResponse;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

public class OrderLineMapperTest {

  OrderLineMapper mapper;

  @Test
  public void testFromOrderLineEntity_whenValid_thenReturnResponse() throws ParseException {
    mapper = new OrderLineMapper();
    OrderLineResponse response =
        mapper.fromOrderLineEntity(CustomerMsResponseUtil.defaultOrderEntity().getItems().get(0));
    assertEquals(2, response.getQuantity());
  }
}
