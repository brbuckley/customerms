package customerms.persistence.domain.listener;

import customerms.persistence.domain.CustomerEntity;
import javax.persistence.PostPersist;

/** Listener for the Customer Entity. */
public class CustomerListener {

  /**
   * Creates the customerId based on the DB id. It works like a DB trigger.
   *
   * @param customer Customer persisted.
   */
  @PostPersist
  public void process(CustomerEntity customer) {
    customer.setCustomerId(customer.getId());
  }
}
