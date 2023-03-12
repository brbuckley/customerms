package customerms.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import customerms.persistence.domain.CustomerEntity;
import customerms.persistence.domain.OrderEntity;
import customerms.persistence.domain.OrderLineEntity;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class OrderRepositoryIntegrationTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private OrderRepository orderRepository;

  @Test
  public void testFindCustomerObjectByCustomerId_whenCustomerExists_thenReturnCustomer() {
    // Insert Test Order
    OrderLineEntity orderLine = new OrderLineEntity();
    orderLine.setQuantity(2);
    orderLine.setPrice(new BigDecimal("1.23"));
    orderLine.setProductId("PRD0000123");
    orderLine = entityManager.persist(orderLine);

    CustomerEntity customer = new CustomerEntity();
    customer.setFirstname("Test");
    customer.setLastname("Subject");
    customer = entityManager.persist(customer);

    OrderEntity order = new OrderEntity();
    order.setCustomer(customer);
    order.setOrderId("ORD0000123");
    order.addOrderLine(orderLine);
    order.setTotal(new BigDecimal("2.46"));
    entityManager.persist(order);

    OrderEntity found =
        orderRepository.findOrderObjectByOrderIdAndCustomer_CustomerId(
            "ORD0000123", customer.getCustomerId());

    assertEquals(new BigDecimal("2.46"), found.getTotal());
  }
}
