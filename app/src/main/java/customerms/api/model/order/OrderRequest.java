package customerms.api.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import customerms.api.model.orderline.OrderLineRequest;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** The order being placed by the user with minimal customer and products information. */
@Getter
@ToString
@EqualsAndHashCode
public class OrderRequest {

  @Pattern(regexp = "^ORD[0-9]{7}$")
  @Setter
  @JsonProperty("order_id")
  String orderId;

  @Valid @NotEmpty private List<OrderLineRequest> items = new ArrayList<OrderLineRequest>();

  /**
   * Add a new orderLine.
   *
   * @param itemsItem OrderLine.
   * @return Updated Order Minimal Object.
   */
  public OrderRequest addItemsItem(OrderLineRequest itemsItem) {
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Gets a list of product ids from the order. Helper method for OrderController.
   *
   * @return List of product ids.
   */
  @JsonIgnore
  public List<String> getProducts() {
    List<String> products = new ArrayList<>();
    for (OrderLineRequest orderLine : items) {
      products.add(orderLine.getProduct().getId());
    }
    return products;
  }
}
