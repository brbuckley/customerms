package customerms.api.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import customerms.CustomerMsResponseUtil;
import customerms.persistence.repository.CustomerRepository;
import customerms.persistence.repository.OrderLineRepository;
import customerms.persistence.repository.OrderRepository;
import customerms.service.CustomerService;
import customerms.service.OrderService;
import customerms.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(OrderController.class)
public class OrderControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ProductService productService;
  @MockBean private CustomerService customerService;
  @MockBean private OrderService orderService;
  // For some reason I need to mock ALL the repos for the EM to work.
  @MockBean private OrderLineRepository orderLineRepository;
  @MockBean private OrderRepository orderRepository;
  @MockBean private CustomerRepository customerRepository;

  MediaType MEDIA_TYPE_JSON_UTF8 =
      new MediaType("application", "json", java.nio.charset.Charset.forName("UTF-8"));

  @Test
  public void testGetOrder_whenValid_then200() throws Exception {
    given(orderService.getOrder("CST0000001", "ORD0000001"))
        .willReturn(CustomerMsResponseUtil.defaultOrderResponse());
    mockMvc
        .perform(get("/CST0000001/orders/ORD0000001").with(jwt()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            content()
                .string(
                    "{\"id\":\"ORD0000001\",\"status\":\"processing\",\"total\":9.98,\"customer\":{\"firstname\":\"Bob\",\"lastname\":\"Smith\",\"gender\":\"male\",\"dob\":\"12-25-1980\",\"id\":\"CST0000001\"},\"items\":[{\"quantity\":2,\"product\":{\"id\":\"PRD0000001\",\"name\":\"heineken\",\"price\":4.99,\"category\":\"beer\"}}]}"));
  }

  @Test
  public void testGetOrder_whenInvalidOrderId_then400() throws Exception {
    mockMvc
        .perform(get("/CST0000001/orders/invalid").with(jwt()))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testPostOrder_whenValid_then201() throws Exception {
    given(customerService.getCustomerEntity("CST0000001"))
        .willReturn(CustomerMsResponseUtil.defaultCustomerEntity());
    List<String> ids = new ArrayList<>();
    ids.add("PRD0000001");
    given(productService.getProductsList(ids))
        .willReturn(CustomerMsResponseUtil.defaultProductList());
    given(
            orderService.postOrder(
                CustomerMsResponseUtil.defaultCustomerEntity(),
                CustomerMsResponseUtil.defaultOrderRequest(),
                CustomerMsResponseUtil.defaultProductList(),
                "correlation"))
        .willReturn(CustomerMsResponseUtil.defaultOrderResponse());

    MockHttpServletRequestBuilder request = post("/CST0000001/orders");
    request.content("{\"items\":[{\"quantity\":2,\"product\":{\"id\":\"PRD0000001\"}}]}");
    request.contentType(MEDIA_TYPE_JSON_UTF8);
    request.header("X-Correlation-Id", "correlation");
    mockMvc
        .perform(request.with(jwt()))
        .andExpect(status().is(201))
        .andExpect(
            content()
                .string(
                    "{\"id\":\"ORD0000001\",\"status\":\"processing\",\"total\":9.98,\"customer\":{\"firstname\":\"Bob\",\"lastname\":\"Smith\",\"gender\":\"male\",\"dob\":\"12-25-1980\",\"id\":\"CST0000001\"},\"items\":[{\"quantity\":2,\"product\":{\"id\":\"PRD0000001\",\"name\":\"heineken\",\"price\":4.99,\"category\":\"beer\"}}]}"));
  }

  @Test
  public void testPostOrder_whenInvalidQuantity_then400() throws Exception {
    MockHttpServletRequestBuilder request = post("/CST0000001/orders");
    request.content("{\"items\":[{\"quantity\":0,\"product\":{\"id\":\"PRD0000001\"}}]}");
    request.contentType(MEDIA_TYPE_JSON_UTF8);
    mockMvc.perform(request.with(jwt())).andExpect(status().isBadRequest());
  }
}
