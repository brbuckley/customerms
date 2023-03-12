package customerms.api.model.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;

public class CustomerResponseTest {

  private CustomerResponse customerResponse;

  @Test
  public void testCustomerRequest_whenDob_thenParse() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setTimeZone(TimeZone.getDefault());
    customerResponse =
        mapper
            .readerFor(CustomerResponse.class)
            .readValue(
                "{\"id\":\"CST0000001\",\"firstname\":\"Bob\",\"lastname\":\"Smith\",\"gender\":\"male\",\"dob\":\"12-25-1980\"}");
    assertEquals("1980-12-25", customerResponse.getDob().toString());
  }
}
