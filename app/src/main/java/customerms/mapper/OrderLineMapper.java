package customerms.mapper;

import customerms.api.model.orderline.OrderLineResponse;
import customerms.api.model.product.ProductResponse;
import customerms.persistence.domain.OrderLineEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/** Mapper for OrderLine related objects. */
@NoArgsConstructor
@Component
public class OrderLineMapper {

  /**
   * Parses from OrderLineEntity to OrderLineResponse.
   *
   * @param entity OrderLine Entity.
   * @return OrderLine Response.
   */
  public OrderLineResponse fromOrderLineEntity(OrderLineEntity entity) {
    ProductResponse product =
        new ProductResponse(
            entity.getProductId(), entity.getName(), entity.getPrice(), entity.getCategory());
    return new OrderLineResponse(entity.getQuantity(), product);
  }
}
