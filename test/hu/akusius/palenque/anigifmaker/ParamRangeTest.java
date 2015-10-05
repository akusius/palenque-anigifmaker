package hu.akusius.palenque.anigifmaker;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author Bujdosó Ákos
 */
public class ParamRangeTest {

  /**
   *
   */
  @Test
  public void test1() {
    ParamRange<Integer> r = new ParamRange<>(1, 5, 3);
    assertThat(r.getMin(), equalTo(1));
    assertThat(r.getMax(), equalTo(5));
    assertThat(r.getDef(), equalTo(3));
  }

  /**
   *
   */
  @Test(expected = IllegalArgumentException.class)
  public void test1b() {
    ParamRange<Integer> r = new ParamRange<>(1, 5, 6);
  }

  /**
   *
   */
  @Test(expected = IllegalArgumentException.class)
  public void test2() {
    ParamRange<Integer> r = new ParamRange<>(10, 30, 20);
    r.checkMinMax(5);
  }

  /**
   *
   */
  @Test(expected = IllegalArgumentException.class)
  public void test3() {
    ParamRange<Integer> r = new ParamRange<>(10, 30, 20);
    r.checkMinMax(50);
  }

  /**
   *
   */
  @Test
  public void test4() {
    ParamRange<Integer> r = new ParamRange<>(1, 5, 3);
    r.checkMinMax(2);
    r.checkMinMax(1, 2, 3, 4, 5);
  }

  /**
   *
   */
  @Test(expected = IllegalArgumentException.class)
  public void test5() {
    ParamRange<Integer> r = new ParamRange<>(8, 7, 7);
  }

  /**
   *
   */
  @Test
  public void test6() {
    ParamRange<Integer> r = new ParamRange<>(7, 9, 8);
    r.checkMinMax(7);
    r.checkMinMax(7, 8, 9);

    try {
      r.checkMinMax(7, 8, 9, 10);
      fail();
    } catch (IllegalArgumentException ex) {
    }
  }

  /**
   *
   */
  @Test
  public void test7() {
    ParamRange<Boolean> r = new ParamRange<>(false, true, false);
    assertThat(r.getMin(), equalTo(false));
    assertThat(r.getMax(), equalTo(true));
    assertThat(r.getDef(), equalTo(false));

    r = new ParamRange<>(false, true, true);
    assertThat(r.getMin(), equalTo(false));
    assertThat(r.getMax(), equalTo(true));
    assertThat(r.getDef(), equalTo(true));
  }
}
