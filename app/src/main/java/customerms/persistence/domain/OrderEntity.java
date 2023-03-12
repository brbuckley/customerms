package customerms.persistence.domain;

import customerms.api.model.order.OrderStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** The order placed by the user with all relevant information. */
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "customer_order")
@Getter
public class OrderEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String orderId;
  @Setter private BigDecimal total;
  private Timestamp datetime;
  private Timestamp updated;
  private OrderStatus status;
  @Setter @ManyToOne private CustomerEntity customer;

  @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
  private List<OrderLineEntity> items = new ArrayList<OrderLineEntity>();

  // This is more of a utility setter
  public void setOrderId(int id) {
    this.orderId = "ORD" + String.format("%07d", id);
  }

  public void setOrderId(String id) {
    this.orderId = id;
  }

  public String getStatus() {
    return this.status == null ? null : this.status.getValue();
  }

  public void setStatus(String status) {
    this.status = status == null ? null : OrderStatus.fromValue(status);
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public Timestamp getDatetime() {
    return this.datetime == null ? null : new Timestamp(this.datetime.getTime());
  }

  public void setDatetime(Timestamp datetime) {
    this.datetime = datetime == null ? null : new Timestamp(datetime.getTime());
  }

  public Timestamp getUpdated() {
    return this.updated == null ? null : new Timestamp(this.updated.getTime());
  }

  public void setUpdated(Timestamp updated) {
    this.updated = updated == null ? null : new Timestamp(updated.getTime());
  }

  /**
   * Add orderLine.
   *
   * @param orderLine OrderLine.
   * @return Updated Order.
   */
  public OrderEntity addOrderLine(OrderLineEntity orderLine) {
    this.items.add(orderLine);
    return this;
  }
}
