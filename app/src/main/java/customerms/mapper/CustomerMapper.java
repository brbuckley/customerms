package customerms.mapper;

import customerms.api.model.customer.CustomerRequest;
import customerms.api.model.customer.CustomerResponse;
import customerms.persistence.domain.CustomerEntity;
import java.text.ParseException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/** Mapper for Customer related objects. */
@NoArgsConstructor
@Component
public class CustomerMapper {

  /**
   * Parses from CustomerRequest to CustomerEntity.
   *
   * @param request Customer Request.
   * @param entity Customer Entity.
   * @throws ParseException setDob throws Parse Exception for invalid Date.
   */
  public void toCustomerEntity(CustomerRequest request, CustomerEntity entity)
      throws ParseException {
    entity.setFirstname(request.getFirstname());
    entity.setLastname(request.getLastname());
    entity.setDob(request.getDob());
    entity.setGender(request.getGender());
  }

  /**
   * Parses from CustomerEntity to CustomerResponse.
   *
   * @param entity Customer Entity.
   * @return Customer Response.
   */
  public CustomerResponse fromCustomerEntity(CustomerEntity entity) {
    CustomerResponse response = new CustomerResponse();
    response.setId(entity.getCustomerId());
    response.setFirstname(entity.getFirstname());
    response.setLastname(entity.getLastname());
    response.setGender(entity.getGender());
    // Use the custom setter for Dob to avoid EI_EXPOSE_REP
    response.setDob(entity.getDob());
    return response;
  }
}
