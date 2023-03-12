package customerms.api.model.orderline;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import customerms.api.model.product.ProductResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** OrderLine Response. */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonPropertyOrder({"quantity", "product"})
public class OrderLineResponse {

  @Positive private int quantity;

  @Valid @NotNull private ProductResponse product;
}
