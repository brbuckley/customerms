package customerms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import customerms.api.model.customer.CustomerRequest;
import customerms.api.model.customer.CustomerResponse;
import customerms.exception.NotExistException;
import customerms.mapper.CustomerMapper;
import customerms.persistence.domain.CustomerEntity;
import customerms.persistence.repository.CustomerRepository;
import java.text.ParseException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Customer services. */
@AllArgsConstructor
@Service
@Slf4j
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final ObjectMapper mapper;
  private final CustomerMapper customerMapper;

  /**
   * Persists a new customer to the DataBase.
   *
   * @param customer Customer Minimal Object.
   * @return Customer created.
   * @throws ParseException Parse Exception.
   */
  public CustomerResponse postCustomer(CustomerRequest customer) throws ParseException {
    CustomerEntity customerEntity = new CustomerEntity();
    customerMapper.toCustomerEntity(customer, customerEntity);
    customerEntity = customerRepository.save(customerEntity);
    log.info("Added new Customer: {}", customerEntity.getCustomerId());
    return customerMapper.fromCustomerEntity(customerEntity);
  }

  /**
   * Gets a customer from the DataBase.
   *
   * @param customerId CustomerId.
   * @return Customer.
   * @throws NotExistException Not Exist Exception.
   */
  public CustomerEntity getCustomerEntity(String customerId) throws NotExistException {
    CustomerEntity customer = customerRepository.findCustomerObjectByCustomerId(customerId);
    if (customer == null) {
      log.info("Customer Not Found {}", customerId);
      throw new NotExistException("Customer");
    } else {
      log.info("Found Customer: {}", customerId);
    }
    return customer;
  }

  /**
   * Gets a Customer Response. Calls get customer entity and parses into response.
   *
   * @param customerId Customer id.
   * @return Customer Response.
   * @throws NotExistException Not Exist Exception.
   */
  public CustomerResponse getCustomer(String customerId) throws NotExistException {
    return customerMapper.fromCustomerEntity(getCustomerEntity(customerId));
  }

  /**
   * Updates a customer from the DataBase.
   *
   * @param customerId CustomerId.
   * @param customer Updated Customer.
   * @return Updated Customer.
   * @throws ParseException Parse Exception.
   * @throws NotExistException Not Exist Exception.
   */
  public CustomerResponse updateCustomer(String customerId, CustomerRequest customer)
      throws NotExistException, ParseException {
    CustomerEntity customerEntity = getCustomerEntity(customerId);
    customerMapper.toCustomerEntity(customer, customerEntity);
    customerEntity = customerRepository.save(customerEntity);
    log.info("Updated Customer: {}", customerEntity.getCustomerId());
    return customerMapper.fromCustomerEntity(customerEntity);
  }
}
