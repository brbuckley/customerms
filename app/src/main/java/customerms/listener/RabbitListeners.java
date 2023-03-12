package customerms.listener;

import customerms.service.OrderService;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/** Rabbit listener, works like a controller. */
@AllArgsConstructor
@Component
@Slf4j
public class RabbitListeners {

  private final OrderService orderService;

  /**
   * Listen for purchase-customer queue.
   *
   * @param message Purchase Response.
   */
  @RabbitListener(id = "purchase", queues = "purchase-customer")
  public void listenPurchase(Message message) {
    String correlation = message.getMessageProperties().getHeader("X-Correlation-Id");
    orderService.updateStatus(correlation, new String(message.getBody(), StandardCharsets.UTF_8));
  }
}
