package hu.akusius.palenque.anigifmaker;

/**
 * Osztály egy paraméter határainak kezeléséhez.
 * @param <T> A paraméter típusa.
 * @author Bujdosó Ákos
 */
public class ParamRange<T extends Comparable<T>> {

  private T min;

  private T max;

  private T def;

  /**
   *
   * @param min
   * @param max
   * @param def
   */
  public ParamRange(T min, T max, T def) {
    if (min == null || max == null || min.compareTo(max) > 0 || def == null) {
      throw new IllegalArgumentException();
    }

    this.min = min;
    this.max = max;
    this.def = def;

    checkMinMax(this.def);
  }

  /**
   * @return Minimum.
   */
  public T getMin() {
    return min;
  }

  /**
   * @return Maximum.
   */
  public T getMax() {
    return max;
  }

  /**
   * @return Alapérték
   */
  public T getDef() {
    return def;
  }

  /**
   * Az érték ellenőrzése.
   * @param value Az ellenőrizendő érték.
   * @throws IllegalArgumentException Az érték kívül esik a határokon.
   */
  public final void checkMinMax(T value) throws IllegalArgumentException {
    if (value == null || min.compareTo(value) > 0 || max.compareTo(value) < 0) {
      throw new IllegalArgumentException("Invalid value.");
    }
  }

  /**
   *
   * @param values
   * @throws IllegalArgumentException
   */
  @SafeVarargs
  public final void checkMinMax(T... values) throws IllegalArgumentException {
    for (T val : values) {
      checkMinMax(val);
    }
  }
}
