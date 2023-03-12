package customerms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import customerms.api.model.order.OrderRequest;
import customerms.api.model.order.OrderResponse;
import customerms.api.model.order.OrderStatus;
import customerms.api.model.orderline.OrderLineRequest;
import customerms.api.model.product.ProductResponse;
import customerms.api.model.purchase.PurchaseResponse;
import customerms.exception.NotExistException;
import customerms.mapper.OrderMapper;
import customerms.persistence.domain.CustomerEntity;
import customerms.persistence.domain.OrderEntity;
import customerms.persistence.domain.OrderLineEntity;
import customerms.persistence.repository.OrderLineRepository;
import customerms.persistence.repository.OrderRepository;
import customerms.util.JavaxValidator;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/** Order services. */
@AllArgsConstructor
@Service
@Slf4j
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderLineRepository orderLineRepository;
  private final RabbitTemplate rabbitTemplate;
  private final ObjectMapper mapper;
  private final OrderMapper orderMapper;

  /**
   * Send a message to PurchaseMs via RabbitMQ queue.
   *
   * @param order Order Minimal Object.
   * @param correlationId Correlation id.
   * @return Copy of Json sent.
   * @throws JsonProcessingException Json Processing Exception.
   */
  private String sendToQueue(OrderRequest order, String correlationId)
      throws JsonProcessingException {
    String json = mapper.writeValueAsString(order);
    log.info("Sending message with id: {} & body: {}", correlationId, json);
    rabbitTemplate.convertAndSend(
        "customer-purchase",
        json,
        m -> {
          m.getMessageProperties().getHeaders().put("X-Correlation-Id", correlationId);
          return m;
        });
    return json;
  }

  /**
   * Persists and links a new order to an existing customer.
   *
   * @param customer Customer.
   * @param order Minimal Order.
   * @param products Products.
   * @param correlationId Correlation id.
   * @return Order persisted.
   * @throws JsonProcessingException Json Processing Exception.
   */
  public OrderResponse postOrder(
      CustomerEntity customer, OrderRequest order, ProductResponse[] products, String correlationId)
      throws JsonProcessingException {
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setCustomer(customer);
    orderEntity = orderRepository.save(orderEntity);
    orderEntity.setOrderId(orderEntity.getId());
    orderEntity.setDatetime(new Timestamp(System.currentTimeMillis()));
    orderEntity.setStatus(OrderStatus.PROCESSING);
    log.info("Created Order: {}", orderEntity.getOrderId());
    // Sorting both arrays
    List<OrderLineRequest> items = order.getItems();
    items.sort(Comparator.comparing(a -> a.getProduct().getId()));
    Arrays.sort(products, Comparator.comparing(ProductResponse::getId));

    int count = 0;
    BigDecimal total = new BigDecimal(0);
    for (OrderLineRequest item : items) {
      ProductResponse product = products[count];
      OrderLineEntity orderLine = new OrderLineEntity();
      // Product
      orderLine.setProductId(product.getId());
      orderLine.setName(product.getName());
      orderLine.setPrice(product.getPrice());
      orderLine.setCategory(product.getCategory());
      // Quantity
      orderLine.setQuantity(item.getQuantity());
      // Relationships
      orderLine.setOrder(orderEntity);
      orderEntity.addOrderLine(orderLine);
      // Persist
      orderLine = orderLineRepository.save(orderLine);
      log.info("Created OrderLine: {}", orderLine.getId());
      // Total price
      BigDecimal aux = product.getPrice().multiply(new BigDecimal(item.getQuantity()));
      total = total.add(aux);
      count++;
    }
    // Persist total price
    orderEntity.setTotal(total);
    orderRepository.flush();
    log.info("Updated Order: {}", orderEntity.getOrderId());
    // Send order message
    order.setOrderId(orderEntity.getOrderId());
    sendToQueue(order, correlationId);

    return orderMapper.fromOrderEntity(orderEntity);
  }

  /**
   * Gets an order from the DataBase.
   *
   * @param customerId CustomerId.
   * @param orderId OrderId.
   * @return Order.
   * @throws NotExistException Not Exist Exception.
   */
  public OrderResponse getOrder(String customerId, String orderId) throws NotExistException {
    OrderEntity order =
        orderRepository.findOrderObjectByOrderIdAndCustomer_CustomerId(orderId, customerId);
    if (order == null) {
      log.info("Order Not Found for id {} and customer {}", orderId, customerId);
      throw new NotExistException("Order");
    } else {
      log.info("Found Order: {}", orderId);
    }
    return orderMapper.fromOrderEntity(order);
  }

  PurchaseResponse parsePurchase(String json) throws JsonProcessingException {
    PurchaseResponse request = mapper.readerFor(PurchaseResponse.class).readValue(json);
    JavaxValidator.validate(request);
    log.info("Successfully parsed the message into the object: {}", request);
    return request;
  }

  /**
   * Updates the status of an Order.
   *
   * @param correlation Correlation id.
   * @param json Json payload from message.
   */
  public void updateStatus(String correlation, String json) {
    log.info("Message received with Correlation: {} | Body: {}", correlation, json);
    try {
      PurchaseResponse response = parsePurchase(json);
      OrderEntity entity = orderRepository.findByOrderId(response.getOrderId());
      entity.setStatus(OrderStatus.ORDERED);
      entity.setUpdated(new Timestamp(System.currentTimeMillis()));
      orderRepository.save(entity);
    } catch (Exception e) {
      log.error("Poisoned message! error: {}", e.getMessage());
      throw new AmqpRejectAndDontRequeueException("Poisoned Message");
    }
  }
}
