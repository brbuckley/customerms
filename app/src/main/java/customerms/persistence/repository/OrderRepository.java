package customerms.persistence.repository;

import customerms.persistence.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Order repository. */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

  public OrderEntity findOrderObjectByOrderIdAndCustomer_CustomerId(
      String orderId, String customerId);

  public OrderEntity findByOrderId(String orderId);
}
