package customerms.mapper;

import customerms.api.model.order.OrderResponse;
import customerms.persistence.domain.OrderEntity;
import customerms.persistence.domain.OrderLineEntity;
import java.util.ArrayList;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Mapper for Order related objects. */
@NoArgsConstructor
@Component
public class OrderMapper {

  private OrderLineMapper orderLineMapper;
  private CustomerMapper customerMapper;

  @Autowired
  public OrderMapper(OrderLineMapper mapper, CustomerMapper customerMapper) {
    this.orderLineMapper = mapper;
    this.customerMapper = customerMapper;
  }

  /**
   * Parses from OrderEntity to OderResponse.
   *
   * @param entity Order Entity.
   * @return Order Response.
   */
  public OrderResponse fromOrderEntity(OrderEntity entity) {
    OrderResponse response =
        new OrderResponse(
            entity.getOrderId(),
            entity.getStatus(),
            entity.getTotal(),
            customerMapper.fromCustomerEntity(entity.getCustomer()),
            new ArrayList<>());
    for (OrderLineEntity orderLine : entity.getItems()) {
      response.addOrderLine(orderLineMapper.fromOrderLineEntity(orderLine));
    }
    return response;
  }
}
