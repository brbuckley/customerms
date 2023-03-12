package customerms.api.model.product;

import java.util.Arrays;
import java.util.Locale;

/** The category of the product. */
public enum ProductCategory {
  BEER,
  WINE;

  public String getValue() {
    return this.toString().toLowerCase(Locale.ROOT);
  }

  /**
   * Builds a Category from a name.
   *
   * @param text Category name lowercase.
   * @return Category.
   */
  public static ProductCategory fromValue(String text) {
    String normalized = text.toUpperCase();
    return Arrays.stream(ProductCategory.values())
        .filter(g -> g.name().startsWith(normalized))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No category starting with " + text));
  }
}
