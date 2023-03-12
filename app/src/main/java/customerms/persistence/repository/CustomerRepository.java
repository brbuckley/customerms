package customerms.persistence.repository;

import customerms.persistence.domain.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Spring JPA repository for Customer entity. */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

  public CustomerEntity findCustomerObjectByCustomerId(String customerId);
}
