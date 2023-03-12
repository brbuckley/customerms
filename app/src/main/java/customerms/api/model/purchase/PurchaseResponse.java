package customerms.api.model.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Purchase Response Model. */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PurchaseResponse {

  @Pattern(regexp = "^ORD[0-9]{7}$")
  @JsonProperty("order_id")
  private String orderId;

  @NotEmpty private List<@Pattern(regexp = "^PUR[0-9]{7}$") String> purchases;
}
