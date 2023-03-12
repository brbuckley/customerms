package customerms.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import customerms.persistence.domain.OrderLineEntity;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class OrderLineRepositoryIntegrationTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private OrderLineRepository orderLineRepository;

  @Test
  public void testFindCustomerObjectByCustomerId_whenCustomerExists_thenReturnCustomer() {
    // Insert Test OrderLine
    OrderLineEntity orderLine = new OrderLineEntity();
    orderLine.setQuantity(2);
    orderLine.setPrice(new BigDecimal("1.23"));
    orderLine.setProductId("PRD0000123");
    orderLine = entityManager.persist(orderLine);

    OrderLineEntity found = orderLineRepository.findById(orderLine.getId()).get();

    assertEquals(2, found.getQuantity());
  }
}
