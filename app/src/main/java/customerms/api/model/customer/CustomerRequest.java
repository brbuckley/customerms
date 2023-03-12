package customerms.api.model.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** The object representing the customer. */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CustomerRequest {

  @Size(min = 1, max = 50)
  @NotBlank
  private String firstname;

  @Size(min = 1, max = 50)
  @NotBlank
  private String lastname;

  @Pattern(regexp = "male|female")
  private String gender;

  // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
  // @DateTimeFormat(pattern = "MM-dd-yyyy")
  @Pattern(regexp = "(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])-[0-9]{4}")
  private String dob;
}
