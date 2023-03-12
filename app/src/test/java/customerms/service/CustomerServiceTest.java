package customerms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import customerms.CustomerMsResponseUtil;
import customerms.api.model.customer.CustomerRequest;
import customerms.api.model.customer.CustomerResponse;
import customerms.exception.NotExistException;
import customerms.mapper.CustomerMapper;
import customerms.persistence.domain.CustomerEntity;
import customerms.persistence.repository.CustomerRepository;
import java.text.ParseException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CustomerServiceTest {

  CustomerService service;

  @Test
  public void testPostCustomer_whenValid_thenReturnCustomer() throws ParseException {
    // Mocks
    CustomerEntity entity = new CustomerEntity();
    new CustomerMapper().toCustomerEntity(CustomerMsResponseUtil.defaultCustomerRequest(), entity);
    CustomerRepository repository = Mockito.mock(CustomerRepository.class);
    when(repository.save(entity)).thenReturn(CustomerMsResponseUtil.defaultCustomerEntity());

    service = new CustomerService(repository, new ObjectMapper(), new CustomerMapper());
    CustomerResponse customer =
        service.postCustomer(CustomerMsResponseUtil.defaultCustomerRequest());

    assertEquals("Bob", customer.getFirstname());
  }

  @Test
  public void testGetCustomerEntity_whenValid_thenReturnCustomer()
      throws ParseException, NotExistException {
    // Mocks
    CustomerRepository repository = Mockito.mock(CustomerRepository.class);
    when(repository.findCustomerObjectByCustomerId("CST0000001"))
        .thenReturn(CustomerMsResponseUtil.defaultCustomerEntity());

    service = new CustomerService(repository, new ObjectMapper(), new CustomerMapper());
    CustomerEntity customer = service.getCustomerEntity("CST0000001");

    assertEquals("Bob", customer.getFirstname());
  }

  @Test
  public void testGetCustomerEntity_whenNotExists_thenThrow() {
    // Mocks
    CustomerRepository repository = Mockito.mock(CustomerRepository.class);
    when(repository.findCustomerObjectByCustomerId("CST0000001")).thenReturn(null);

    service = new CustomerService(repository, new ObjectMapper(), new CustomerMapper());
    assertThrows(NotExistException.class, () -> service.getCustomerEntity("CST0000001"));
  }

  @Test
  public void testUpdateCustomer_whenValid_thenReturnCustomer()
      throws ParseException, NotExistException {
    // Mocks
    CustomerRepository repository = Mockito.mock(CustomerRepository.class);
    when(repository.save(CustomerMsResponseUtil.defaultCustomerEntity()))
        .thenReturn(CustomerMsResponseUtil.defaultCustomerEntity());
    when(repository.findCustomerObjectByCustomerId("CST0000001"))
        .thenReturn(CustomerMsResponseUtil.defaultCustomerEntity());

    service = new CustomerService(repository, new ObjectMapper(), new CustomerMapper());
    CustomerResponse customer =
        service.updateCustomer("CST0000001", CustomerMsResponseUtil.defaultCustomerRequest());

    assertEquals("Smith", customer.getLastname());
  }

  @Test
  public void testGetCustomer_whenValid_thenReturnCustomerResponse()
      throws ParseException, NotExistException {
    // Mocks
    CustomerRepository repository = Mockito.mock(CustomerRepository.class);
    when(repository.findCustomerObjectByCustomerId("CST0000001"))
        .thenReturn(CustomerMsResponseUtil.defaultCustomerEntity());

    service = new CustomerService(repository, new ObjectMapper(), new CustomerMapper());
    CustomerResponse customer = service.getCustomer("CST0000001");

    assertEquals("Bob", customer.getFirstname());
  }

  @Test
  public void testUpdateCustomer_whenDobAndGenderAreNull_thenReturnCustomer()
      throws ParseException, NotExistException {
    // Mocks
    CustomerRepository repository = Mockito.mock(CustomerRepository.class);
    when(repository.save(CustomerMsResponseUtil.minimalCustomerEntity()))
        .thenReturn(CustomerMsResponseUtil.minimalCustomerEntity());
    when(repository.findCustomerObjectByCustomerId(any()))
        .thenReturn(CustomerMsResponseUtil.minimalCustomerEntity());

    service = new CustomerService(repository, new ObjectMapper(), new CustomerMapper());
    CustomerRequest customerRequest = new CustomerRequest();
    customerRequest.setFirstname("Bob");
    customerRequest.setLastname("Smith");
    customerRequest.setGender(null);
    customerRequest.setDob(null);
    CustomerResponse customer = service.updateCustomer("CST0000001", customerRequest);

    assertNull(customer.getDob());
  }
}
