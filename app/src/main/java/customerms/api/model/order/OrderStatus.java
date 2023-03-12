package customerms.api.model.order;

import java.util.Arrays;
import java.util.Locale;

/** Order Status enum. */
public enum OrderStatus {
  PROCESSING,
  ORDERED;

  public String getValue() {
    return this.toString().toLowerCase(Locale.ROOT);
  }

  /**
   * Builds a Status from a name.
   *
   * @param text Status name lowercase.
   * @return Status.
   */
  public static OrderStatus fromValue(String text) {
    String normalized = text.toUpperCase();
    return Arrays.stream(OrderStatus.values())
        .filter(g -> g.name().startsWith(normalized))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No status starting with " + text));
  }
}
