package customerms.util;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@RabbitListenerTest(capture = true)
public class Config {

  @Bean
  public TestRabbitTemplate testRabbitTemplate() throws IOException {
    return new TestRabbitTemplate(mockConnectionFactory());
  }

  @Bean
  public ConnectionFactory mockConnectionFactory() throws IOException {
    ConnectionFactory factory = mock(ConnectionFactory.class);
    Connection connection = mock(Connection.class);
    Channel channel = mock(Channel.class);
    willReturn(connection).given(factory).createConnection();
    willReturn(channel).given(connection).createChannel(anyBoolean());
    given(channel.isOpen()).willReturn(true);
    return factory;
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() throws IOException {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(mockConnectionFactory());
    return factory;
  }
}
