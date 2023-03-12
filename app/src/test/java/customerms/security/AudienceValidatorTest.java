package customerms.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;

public class AudienceValidatorTest {

  @Test
  public void testValidate_whenValid_thenReturnEmpty() {
    AudienceValidator validator = new AudienceValidator("audience");
    List<String> audience = new ArrayList<>();
    audience.add("audience");
    Jwt mock = Mockito.mock(Jwt.class);
    when(mock.getAudience()).thenReturn(audience);
    assertEquals(true, validator.validate(mock).getErrors().isEmpty());
  }

  @Test
  public void testValidate_whenInvalid_thenReturnErrors() {
    AudienceValidator validator = new AudienceValidator("audience");
    List<String> audience = new ArrayList<>();
    audience.add("invalid");
    Jwt mock = Mockito.mock(Jwt.class);
    when(mock.getAudience()).thenReturn(audience);
    assertEquals(false, validator.validate(mock).getErrors().isEmpty());
  }
}
