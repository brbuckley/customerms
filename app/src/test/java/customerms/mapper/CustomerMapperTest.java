package customerms.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import customerms.CustomerMsResponseUtil;
import customerms.api.model.customer.CustomerRequest;
import customerms.api.model.customer.CustomerResponse;
import customerms.persistence.domain.CustomerEntity;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

public class CustomerMapperTest {

  CustomerMapper mapper;

  @Test
  public void testToCustomerEntity_whenValid_thenUpdate() throws ParseException {
    mapper = new CustomerMapper();
    CustomerEntity entity = CustomerMsResponseUtil.defaultCustomerEntity();
    CustomerRequest request = CustomerMsResponseUtil.defaultCustomerRequest();
    request.setFirstname("Test");
    assertEquals("Bob", entity.getFirstname());
    mapper.toCustomerEntity(request, entity);
    assertEquals("Test", entity.getFirstname());
  }

  @Test
  public void testFromCustomerEntity_whenValid_thenCreateResponse() throws ParseException {
    mapper = new CustomerMapper();
    CustomerEntity entity = CustomerMsResponseUtil.defaultCustomerEntity();
    CustomerResponse response = mapper.fromCustomerEntity(entity);
    assertEquals("Bob", response.getFirstname());
  }
}
