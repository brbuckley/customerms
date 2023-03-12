package customerms.api.model.orderline;

import customerms.api.model.product.ProductRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** The item being ordered. */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrderLineRequest {

  @Positive private int quantity;

  @Valid @NotNull private ProductRequest product;
}
