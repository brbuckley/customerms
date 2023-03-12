package customerms.persistence.domain;

import customerms.api.model.customer.CustomerGender;
import customerms.persistence.domain.listener.CustomerListener;
import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** The object representing the customer. */
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@EntityListeners(CustomerListener.class)
@Table(name = "customer")
public class CustomerEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private int id;

  @Getter private String customerId;

  @Getter @Setter private String firstname;

  @Getter @Setter private String lastname;

  private CustomerGender gender;

  @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
  @Getter
  @Setter
  List<OrderEntity> orders = new ArrayList<>();

  /**
   * Custom getter to return gender value.
   *
   * @return Gender value.
   */
  public String getGender() {
    return this.gender == null ? null : this.gender.getValue();
  }

  public void setGender(String gender) {
    this.gender = gender == null ? null : CustomerGender.fromValue(gender);
  }

  public void setGender(CustomerGender gender) {
    this.gender = gender;
  }

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

  public void setDob(String dob) throws ParseException {
    this.dob =
        dob == null ? null : new Date(new SimpleDateFormat("MM-dd-yyyy").parse(dob).getTime());
  }

  // This is more of a utility setter
  public void setCustomerId(int id) {
    this.customerId = "CST" + String.format("%07d", id);
  }
}
