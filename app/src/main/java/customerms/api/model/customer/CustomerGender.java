package customerms.api.model.customer;

import java.util.Arrays;
import java.util.Locale;

/** The gender of a customer. */
public enum CustomerGender {
  MALE,
  FEMALE;

  public String getValue() {
    return this.toString().toLowerCase(Locale.ROOT);
  }

  /**
   * Builds a Category from a name.
   *
   * @param text Category name lowercase.
   * @return Category.
   */
  public static CustomerGender fromValue(String text) {
    String normalized = text.toUpperCase();
    return Arrays.stream(CustomerGender.values())
        .filter(g -> g.name().startsWith(normalized))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No gender starting with " + text));
  }
}
