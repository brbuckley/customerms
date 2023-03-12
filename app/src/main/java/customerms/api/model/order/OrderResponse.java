package customerms.api.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import customerms.api.model.customer.CustomerResponse;
import customerms.api.model.orderline.OrderLineResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Order Response. */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@JsonPropertyOrder({"id", "status", "total", "customer", "items"})
public class OrderResponse {

  @Pattern(regexp = "^ORD[0-9]{7}$")
  private String id;

  private String status;
  @Positive private BigDecimal total;
  @Valid @NotNull private CustomerResponse customer;

  @Valid
  @NotEmpty
  @Setter(AccessLevel.NONE)
  private List<OrderLineResponse> items = new ArrayList<OrderLineResponse>();

  /**
   * Add a new orderLine.
   *
   * @param orderLine OrderLine.
   * @return Updated Order Minimal Object.
   */
  public OrderResponse addOrderLine(OrderLineResponse orderLine) {
    this.items.add(orderLine);
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
    for (OrderLineResponse orderLine : items) {
      products.add(orderLine.getProduct().getId());
    }
    return products;
  }
}
