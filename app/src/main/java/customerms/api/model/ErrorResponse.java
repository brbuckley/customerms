package customerms.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** ErrorResponse. */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ErrorResponse {

  private String errorCode;
  private String description;
}
