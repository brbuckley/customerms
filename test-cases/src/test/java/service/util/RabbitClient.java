package service.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

@Slf4j
public class RabbitClient {

  EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
  String hostName = variables.getProperty("rabbitmq.host");

  /** Get messages from 'my-queue'. */
  public String getMessage() {
    final AtomicReference<String> message = new AtomicReference<>();
    ConnectionFactory factory = new ConnectionFactory();
    // Now the hostname is passed by serenity.properties, so if you are running locally you can do:
    // mvn -Drabbitmq.host=localhost clean verify
    factory.setHost(hostName);
    try (Connection connection = factory.newConnection()) {
      Channel channel = connection.createChannel();
      channel.queueDeclare("customer-purchase", true, false, false, null);

      DeliverCallback deliverCallback =
          (consumerTag, delivery) -> {
            message.set(new String(delivery.getBody(), "UTF-8"));
            log.info(
                "Message received with id: {} & body: {}",
                delivery.getProperties().getHeaders().get("X-Correlation-Id"),
                message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
          };

      channel.basicConsume("customer-purchase", false, deliverCallback, (consumerTag -> {}));

    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return message.get();
  }
}
