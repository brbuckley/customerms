package customerms.api.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import customerms.CustomerMsResponseUtil;
import customerms.persistence.repository.CustomerRepository;
import customerms.persistence.repository.OrderLineRepository;
import customerms.persistence.repository.OrderRepository;
import customerms.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(CustomerController.class)
public class CustomerControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CustomerService customerService;
  // For some reason I need to mock ALL the repos for the EM to work.
  @MockBean private OrderLineRepository orderLineRepository;
  @MockBean private OrderRepository orderRepository;
  @MockBean private CustomerRepository customerRepository;

  MediaType MEDIA_TYPE_JSON_UTF8 =
      new MediaType("application", "json", java.nio.charset.Charset.forName("UTF-8"));

  @Test
  public void testGetCustomer_whenValid_then200() throws Exception {
    given(customerService.getCustomer("CST0000001"))
        .willReturn(CustomerMsResponseUtil.defaultCustomerResponse());
    mockMvc
        .perform(get("/CST0000001").with(jwt()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(
                    "{\"firstname\":\"Bob\",\"lastname\":\"Smith\",\"gender\":\"male\",\"dob\":\"12-25-1980\",\"id\":\"CST0000001\"}"));
  }

  @Test
  public void testGetCustomer_whenInvalidCustomerId_then400() throws Exception {
    mockMvc.perform(get("/invalid").with(jwt())).andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void testPutCustomer_whenValid_then200() throws Exception {
    given(
            customerService.updateCustomer(
                "CST0000001", CustomerMsResponseUtil.updateCustomerRequest()))
        .willReturn(CustomerMsResponseUtil.updateCustomerResponse());
    MockHttpServletRequestBuilder request = put("/CST0000001");
    request.content("{\"firstname\":\"Felipe\",\"lastname\":\"Smith\",\"gender\":\"male\"}");
    request.contentType(MEDIA_TYPE_JSON_UTF8);
    mockMvc
        .perform(request.with(jwt()))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(
                    "{\"firstname\":\"Felipe\",\"lastname\":\"Smith\",\"gender\":\"male\",\"id\":\"CST0000001\"}"));
  }

  @Test
  public void testPutCustomer_whenInvalidCustomerId_then400() throws Exception {
    MockHttpServletRequestBuilder request = put("/invalid");
    request.content("{\"firstname\":\"Felipe\",\"lastname\":\"Smith\",\"gender\":\"male\"}");
    request.contentType(MEDIA_TYPE_JSON_UTF8);
    mockMvc.perform(request.with(jwt())).andExpect(status().isBadRequest());
  }

  @Test
  public void testPostCustomer_whenValid_then201() throws Exception {
    given(customerService.postCustomer(CustomerMsResponseUtil.updateCustomerRequest()))
        .willReturn(CustomerMsResponseUtil.updateCustomerResponse());
    MockHttpServletRequestBuilder request = post("/");
    request.content("{\"firstname\":\"Felipe\",\"lastname\":\"Smith\",\"gender\":\"male\"}");
    request.contentType(MEDIA_TYPE_JSON_UTF8);
    mockMvc
        .perform(request.with(jwt()))
        .andExpect(status().is(201))
        .andExpect(
            content()
                .string(
                    "{\"firstname\":\"Felipe\",\"lastname\":\"Smith\",\"gender\":\"male\",\"id\":\"CST0000001\"}"));
  }

  @Test
  public void testPostCustomer_whenInvalidGender_then400() throws Exception {
    MockHttpServletRequestBuilder request = post("/");
    request.content("{\"firstname\":\"Felipe\",\"lastname\":\"Smith\",\"gender\":\"invalid\"}");
    request.contentType(MEDIA_TYPE_JSON_UTF8);
    mockMvc.perform(request.with(jwt())).andExpect(status().isBadRequest());
  }
}
