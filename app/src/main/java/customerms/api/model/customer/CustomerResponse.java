package customerms.api.model.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.sql.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/** Customer Response. */
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"firstname", "lastname", "gender", "dob", "id"})
public class CustomerResponse {

  @Getter @Setter private String id;
  @Getter @Setter private String firstname;
  @Getter @Setter private String lastname;
  @Getter @Setter private String gender;

  @DateTimeFormat(pattern = "MM-dd-yyyy")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
  private Date dob;

  /**
   * Custom getter for Date. Avoids expose representation bug and deals with nulls.
   *
   * @return Date of Birth.
   */
  public Date getDob() {
    return this.dob == null ? null : new Date(this.dob.getTime());
  }

  /**
   * Custom setter for Date. Avoids expose representation bug and deals with nulls.
   *
   * @param dob Date of Birth.
   */
  public void setDob(Date dob) {
    this.dob = dob == null ? null : new Date(dob.getTime());
  }
}
