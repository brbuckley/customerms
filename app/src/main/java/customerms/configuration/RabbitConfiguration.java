package customerms.configuration;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Rabbit queue declaration and configuration. */
@AllArgsConstructor
@Configuration
public class RabbitConfiguration {

  private final RabbitConfig rabbitConfig;

  @Bean
  public CachingConnectionFactory connectionFactory() {
    return new CachingConnectionFactory(rabbitConfig.getHost());
  }

  @Bean
  public RabbitAdmin amqpAdmin() {
    return new RabbitAdmin(connectionFactory());
  }

  @Bean
  public RabbitTemplate rabbitTemplate() {
    return new RabbitTemplate(connectionFactory());
  }

  @Bean
  public Queue customerQueue() {
    return new Queue("customer-purchase");
  }

  @Bean
  public Queue purchaseQueue() {
    return new Queue("purchase-customer");
  }
}
