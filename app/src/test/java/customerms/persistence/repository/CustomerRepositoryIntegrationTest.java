package customerms.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import customerms.persistence.domain.CustomerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class CustomerRepositoryIntegrationTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private CustomerRepository customerRepository;

  @Test
  public void testFindCustomerObjectByCustomerId_whenCustomerExists_thenReturnCustomer() {
    // Insert Test Customer
    CustomerEntity entity = new CustomerEntity();
    entity.setFirstname("Test");
    entity.setLastname("Subject");
    entity = entityManager.persist(entity);

    CustomerEntity found =
        customerRepository.findCustomerObjectByCustomerId(entity.getCustomerId());

    assertEquals("Test", found.getFirstname());
  }
}
