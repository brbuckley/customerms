package customerms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import customerms.CustomerMsResponseUtil;
import customerms.api.model.order.OrderResponse;
import customerms.api.model.product.ProductCategory;
import customerms.exception.NotExistException;
import customerms.mapper.CustomerMapper;
import customerms.mapper.OrderLineMapper;
import customerms.mapper.OrderMapper;
import customerms.persistence.domain.OrderEntity;
import customerms.persistence.domain.OrderLineEntity;
import customerms.persistence.repository.OrderLineRepository;
import customerms.persistence.repository.OrderRepository;
import java.math.BigDecimal;
import java.text.ParseException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class OrderServiceTest {

  OrderService service;

  @Test
  public void testPostOrder_whenValid_thenReturnOrder()
      throws ParseException, JsonProcessingException {
    OrderLineEntity orderLine =
        new OrderLineEntity(
            1, 2, "PRD0000001", "Heineken", new BigDecimal("10"), ProductCategory.BEER, null);
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setCustomer(CustomerMsResponseUtil.defaultCustomerEntity());
    // Mocks
    OrderRepository orderRepo = Mockito.mock(OrderRepository.class);
    when(orderRepo.save(orderEntity)).thenReturn(orderEntity);
    OrderLineRepository orderLineRepo = Mockito.mock(OrderLineRepository.class);
    // OrderLine entity has no equals, so any() is needed
    when(orderLineRepo.save(any())).thenReturn(orderLine);
    RabbitTemplate mockRabbit = Mockito.spy(RabbitTemplate.class);
    doNothing().when(mockRabbit).send(any(), anyString(), any(), any());

    // Inject Mocks
    service =
        new OrderService(
            orderRepo,
            orderLineRepo,
            mockRabbit,
            new ObjectMapper(),
            new OrderMapper(new OrderLineMapper(), new CustomerMapper()));

    // Act
    OrderResponse order =
        service.postOrder(
            CustomerMsResponseUtil.defaultCustomerEntity(),
            CustomerMsResponseUtil.defaultOrderRequest(),
            CustomerMsResponseUtil.defaultProductList(),
            "correlationId");

    // Assert
    assertEquals(new BigDecimal("20"), order.getTotal());
  }

  @Test
  public void testGetOrder_whenValid_thenReturnOrder() throws ParseException, NotExistException {
    // Mocks
    OrderRepository orderRepo = Mockito.mock(OrderRepository.class);
    when(orderRepo.findOrderObjectByOrderIdAndCustomer_CustomerId("ORD0000001", "CST0000001"))
        .thenReturn(CustomerMsResponseUtil.defaultOrderEntity());

    // Inject Mocks
    service =
        new OrderService(
            orderRepo,
            null,
            null,
            new ObjectMapper(),
            new OrderMapper(new OrderLineMapper(), new CustomerMapper()));

    OrderResponse order = service.getOrder("CST0000001", "ORD0000001");

    assertEquals("ORD0000001", order.getId());
  }

  @Test
  public void testGetOrder_whenNotExists_thenThrow() {
    // Mocks
    OrderRepository orderRepo = Mockito.mock(OrderRepository.class);
    when(orderRepo.findOrderObjectByOrderIdAndCustomer_CustomerId("ORD0000001", "CST0000001"))
        .thenReturn(null);

    // Inject Mocks
    service =
        new OrderService(
            orderRepo,
            null,
            null,
            new ObjectMapper(),
            new OrderMapper(new OrderLineMapper(), new CustomerMapper()));

    assertThrows(NotExistException.class, () -> service.getOrder("CST0000001", "ORD0000001"));
  }

  @Test
  public void testUpdateStatus_whenValid_thenUpdate() throws ParseException {
    OrderRepository mockRepo = Mockito.spy(OrderRepository.class);
    doReturn(CustomerMsResponseUtil.defaultOrderEntity())
        .when(mockRepo)
        .findByOrderId("ORD0000001");
    doReturn(null).when(mockRepo).save(any()); // any() 'cause: equals() and currentTimeMillis

    service = new OrderService(mockRepo, null, null, new ObjectMapper(), null);
    service.updateStatus(
        "correlation", "{\"order_id\":\"ORD0000001\",\"purchases\":[\"PUR0000001\"]}");

    verify(mockRepo).save(any());
  }

  @Test
  public void testUpdateStatus_whenInvalid_thenThrow() {
    service = new OrderService(null, null, null, new ObjectMapper(), null);
    assertThrows(
        AmqpRejectAndDontRequeueException.class,
        () -> service.updateStatus("correlation", "{\"invalid\":\"invalid\"}"));
  }
}
