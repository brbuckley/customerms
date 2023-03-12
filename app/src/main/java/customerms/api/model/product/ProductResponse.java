package customerms.api.model.product;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Product Response. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonPropertyOrder({"id", "name", "price", "category"})
public class ProductResponse {

  @Pattern(regexp = "^PRD[0-9]{7}$")
  @NotBlank
  private String id;

  private String name;
  @Positive @NotNull private BigDecimal price;

  @Pattern(regexp = "(beer|wine)")
  private String category;
}
