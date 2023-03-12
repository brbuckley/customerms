package customerms.persistence.domain;

import customerms.api.model.product.ProductCategory;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** The item being ordered. */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_line")
@Getter
public class OrderLineEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Setter private int quantity;
  private String productId;
  @Setter private String name;
  @Setter private BigDecimal price;
  private ProductCategory category;

  @Setter @ManyToOne OrderEntity order;

  public String getCategory() {
    return this.category == null ? null : this.category.getValue();
  }

  public void setCategory(String gender) {
    this.category = gender == null ? null : ProductCategory.fromValue(gender);
  }

  public void setCategory(ProductCategory category) {
    this.category = category;
  }

  // This is more of a utility setter
  public void setProductId(int id) {
    this.productId = "PRD" + String.format("%07d", id);
  }

  public void setProductId(String id) {
    this.productId = id;
  }
}
