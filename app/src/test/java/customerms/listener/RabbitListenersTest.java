package customerms.listener;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import customerms.service.OrderService;
import customerms.util.Config;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {Config.class})
public class RabbitListenersTest {

  private RabbitListeners underTest;

  private static final String LISTENER_CONTAINER_ID = "purchase";

  @Autowired private RabbitListenerTestHarness harness;
  @Autowired private TestRabbitTemplate testRabbitTemplate;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private Message message;

  @MockBean private OrderService orderService;

  @BeforeEach
  void setUp() {
    underTest = harness.getSpy(LISTENER_CONTAINER_ID);
    assertNotNull(underTest);
  }

  @AfterEach
  void tearDown() {
    reset(underTest);
  }

  @Test
  void testListenPurchase_whenMockMessage_ThenListen() {
    MessageProperties mock = Mockito.mock(MessageProperties.class);
    when(message.getMessageProperties()).thenReturn(mock);
    when(mock.getHeader("X-Correlation-Id")).thenReturn("correlation");
    when(message.getBody())
        .thenReturn(
            "{\"order_id\":\"ORD0000001\",\"purchases\":[\"PUR0000001\"]}"
                .getBytes(StandardCharsets.UTF_8));

    testRabbitTemplate.convertAndSend("purchase-customer", "purchase-customer", message);

    verify(orderService)
        .updateStatus(
            "correlation", "{\"order_id\":\"ORD0000001\",\"purchases\":[\"PUR0000001\"]}");
  }
}
