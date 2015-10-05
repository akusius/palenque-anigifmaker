package hu.akusius.palenque.anigifmaker;

import java.awt.image.BufferedImage;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;

/**
 *
 * @author Bujdosó Ákos
 */
public class FrameGeneratorTest {

  private final static int[] testStepsAnimLength = new int[]{
    40, // 0: Kiindulás
    40, // 1: Első forgatás
    40, // 2: Második forgatás
    40, // 3: Harmadik forgatás
    40, // 4: Várakozás
    40, // 5: Tükrözés
    40, // 6: Várakozás
    40, // 7: Sarkok befelé tolása
    40, // 8: Középső elemek befelé tolása
    40, // 9: Sarkok eltüntetése
    40, // 10: Első behajtás
    40, // 11: Várakozás
    40, // 12: Második behajtás 1.
    40, // 13: Második behajtás 2.
    40, // 14: Második behajtás 3.
    40, // 15: Második behajtás 4.
    40, // 16: Várakozás
    40, // 17: Ráközelítés egyikre és a többi eltüntetése
    40, // 18: Négyzetek helyett vonalak
    40, // 19: Várakozás
    40, // 20: Összezárás
    40, // 21: Program vége, várakozás
  };

  /**
   *
   */
  public FrameGeneratorTest() {
  }

  /**
   * A hipotetikus alapeset ellenőrzése (speed=10, FPS=40, teljes program, hold és fade nincs).
   */
  @Test
  public void test1() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setSpeed(10);
    apb.setFramesPerSecond(40);
    AnimParams ap = apb.createAnimParams();

    FrameGenerator fg = new FrameGenerator(ap, testStepsAnimLength);
    assertThat(fg.getAnimParams(), sameInstance(ap));
    assertThat(fg.getStepsAnimLength(), equalTo(testStepsAnimLength));

    assertThat(fg.getNumberOfFrames(), equalTo(880));
    assertThat(fg.getLengthInSeconds(), equalTo(22.0));
    assertThat(fg.getFirstNonFadedFrame(), equalTo(0));
    assertThat(fg.getLastNonFadedFrame(), equalTo(879));
    assertThat(fg.getFirstNonHeldFrame(), equalTo(0));
    assertThat(fg.getLastNonHeldFrame(), equalTo(879));

    int numberOfFrames = fg.getNumberOfFrames();

    // Összes kockát ellenőrizzük
    for (int frame = 0; frame < numberOfFrames; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      assertThat(fi.getFrameNum(), equalTo(frame));
      assertThat(fi.getAnimTime(), equalTo((double) frame));
      assertThat(fi.getStepNum(), equalTo(frame / 40));
      assertThat(fi.getPercent(), equalTo((frame % 40) * 100.0 / 40.0));
      assertThat(fi.getSeconds(), equalTo(frame / 40.0));
      assertThat(fi.getOpacity(), equalTo(1.0));
      assertThat(fi.isFaded(), equalTo(false));
      assertThat(fi.isHeld(), equalTo(false));
    }
  }

  /**
   * speed=10, FPS=20, teljes program, hold és fade nincs.
   */
  @Test
  public void test2() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setSpeed(10);
    apb.setFramesPerSecond(20);
    AnimParams ap = apb.createAnimParams();

    FrameGenerator fg = new FrameGenerator(ap, testStepsAnimLength);
    assertThat(fg.getAnimParams(), sameInstance(ap));
    assertThat(fg.getStepsAnimLength(), equalTo(testStepsAnimLength));

    assertThat(fg.getNumberOfFrames(), equalTo(440));
    assertThat(fg.getLengthInSeconds(), equalTo(22.0));
    assertThat(fg.getFirstNonFadedFrame(), equalTo(0));
    assertThat(fg.getLastNonFadedFrame(), equalTo(439));
    assertThat(fg.getFirstNonHeldFrame(), equalTo(0));
    assertThat(fg.getLastNonHeldFrame(), equalTo(439));

    int numberOfFrames = fg.getNumberOfFrames();

    // Összes kockát ellenőrizzük
    for (int frame = 0; frame < numberOfFrames; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      assertThat(fi.getFrameNum(), equalTo(frame));
      assertThat(fi.getAnimTime(), equalTo(frame * 2.0));
      assertThat(fi.getStepNum(), equalTo(frame / 20));
      assertThat(fi.getPercent(), equalTo((frame % 20) * 100.0 / 20.0));
      assertThat(fi.getSeconds(), equalTo(frame / 20.0));
      assertThat(fi.getOpacity(), equalTo(1.0));
      assertThat(fi.isFaded(), equalTo(false));
      assertThat(fi.isHeld(), equalTo(false));
    }
  }

  /**
   * speed=4, FPS=10, teljes program, hold és fade nincs.
   */
  @Test
  public void test3() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setSpeed(4);
    apb.setFramesPerSecond(10);
    AnimParams ap = apb.createAnimParams();

    FrameGenerator fg = new FrameGenerator(ap, testStepsAnimLength);
    assertThat(fg.getAnimParams(), sameInstance(ap));
    assertThat(fg.getStepsAnimLength(), equalTo(testStepsAnimLength));

    assertThat(fg.getNumberOfFrames(), equalTo(550));
    assertThat(fg.getLengthInSeconds(), equalTo(55.0));
    assertThat(fg.getFirstNonFadedFrame(), equalTo(0));
    assertThat(fg.getLastNonFadedFrame(), equalTo(549));
    assertThat(fg.getFirstNonHeldFrame(), equalTo(0));
    assertThat(fg.getLastNonHeldFrame(), equalTo(549));

    int numberOfFrames = fg.getNumberOfFrames();

    // Összes kockát ellenőrizzük
    for (int frame = 0; frame < numberOfFrames; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      assertThat(fi.getFrameNum(), equalTo(frame));
      assertThat(fi.getAnimTime(), closeTo(frame * 1.6, 0.00001));
      assertThat(fi.getStepNum(), equalTo(frame / 25));
      assertThat(fi.getPercent(), closeTo((frame % 25) * 100.0 / 25.0, 0.00001));
      assertThat(fi.getSeconds(), equalTo(frame / 10.0));
      assertThat(fi.getOpacity(), equalTo(1.0));
      assertThat(fi.isFaded(), equalTo(false));
      assertThat(fi.isHeld(), equalTo(false));
    }
  }

  /**
   * speed=20, FPS=50, teljes program, hold és fade nincs.
   */
  @Test
  public void test4() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setSpeed(20);
    apb.setFramesPerSecond(50);
    AnimParams ap = apb.createAnimParams();

    FrameGenerator fg = new FrameGenerator(ap, testStepsAnimLength);
    assertThat(fg.getAnimParams(), sameInstance(ap));
    assertThat(fg.getStepsAnimLength(), equalTo(testStepsAnimLength));

    assertThat(fg.getNumberOfFrames(), equalTo(550));
    assertThat(fg.getLengthInSeconds(), equalTo(11.0));
    assertThat(fg.getFirstNonFadedFrame(), equalTo(0));
    assertThat(fg.getLastNonFadedFrame(), equalTo(549));
    assertThat(fg.getFirstNonHeldFrame(), equalTo(0));
    assertThat(fg.getLastNonHeldFrame(), equalTo(549));

    int numberOfFrames = fg.getNumberOfFrames();

    // Összes kockát ellenőrizzük
    for (int frame = 0; frame < numberOfFrames; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      assertThat(fi.getFrameNum(), equalTo(frame));
      assertThat(fi.getAnimTime(), closeTo(frame * 1.6, 0.00001));
      assertThat(fi.getStepNum(), equalTo(frame / 25));
      assertThat(fi.getPercent(), closeTo((frame % 25) * 100.0 / 25.0, 0.00001));
      assertThat(fi.getSeconds(), equalTo(frame / 50.0));
      assertThat(fi.getOpacity(), equalTo(1.0));
      assertThat(fi.isFaded(), equalTo(false));
      assertThat(fi.isHeld(), equalTo(false));
    }
  }

  /**
   * speed=10, FPS=40, 3-4. lépés, hold és fade nincs.
   */
  @Test
  public void test5() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setSpeed(10);
    apb.setFramesPerSecond(40);
    apb.setFirstStep(3);
    apb.setLastStep(4);
    AnimParams ap = apb.createAnimParams();

    FrameGenerator fg = new FrameGenerator(ap, testStepsAnimLength);
    assertThat(fg.getAnimParams(), sameInstance(ap));
    assertThat(fg.getStepsAnimLength(), equalTo(testStepsAnimLength));

    assertThat(fg.getNumberOfFrames(), equalTo(80));
    assertThat(fg.getLengthInSeconds(), equalTo(2.0));
    assertThat(fg.getFirstNonFadedFrame(), equalTo(0));
    assertThat(fg.getLastNonFadedFrame(), equalTo(79));
    assertThat(fg.getFirstNonHeldFrame(), equalTo(0));
    assertThat(fg.getLastNonHeldFrame(), equalTo(79));

    int numberOfFrames = fg.getNumberOfFrames();

    // Összes kockát ellenőrizzük
    for (int frame = 0; frame < numberOfFrames; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      assertThat(fi.getFrameNum(), equalTo(frame));
      assertThat(fi.getAnimTime(), equalTo((double) frame));
      assertThat(fi.getStepNum(), equalTo(3 + frame / 40));
      assertThat(fi.getPercent(), equalTo((frame % 40) * 100.0 / 40.0));
      assertThat(fi.getSeconds(), equalTo(frame / 40.0));
      assertThat(fi.getOpacity(), equalTo(1.0));
      assertThat(fi.isFaded(), equalTo(false));
      assertThat(fi.isHeld(), equalTo(false));
    }
  }

  /**
   * speed=5, FPS=20, 6-8. lépés, hold és fade nincs.
   */
  @Test
  public void test6() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setSpeed(5);
    apb.setFramesPerSecond(20);
    apb.setFirstStep(6);
    apb.setLastStep(8);
    AnimParams ap = apb.createAnimParams();

    FrameGenerator fg = new FrameGenerator(ap, testStepsAnimLength);
    assertThat(fg.getAnimParams(), sameInstance(ap));
    assertThat(fg.getStepsAnimLength(), equalTo(testStepsAnimLength));

    assertThat(fg.getNumberOfFrames(), equalTo(120));
    assertThat(fg.getLengthInSeconds(), equalTo(6.0));
    assertThat(fg.getFirstNonFadedFrame(), equalTo(0));
    assertThat(fg.getLastNonFadedFrame(), equalTo(119));
    assertThat(fg.getFirstNonHeldFrame(), equalTo(0));
    assertThat(fg.getLastNonHeldFrame(), equalTo(119));

    int numberOfFrames = fg.getNumberOfFrames();

    // Összes kockát ellenőrizzük
    for (int frame = 0; frame < numberOfFrames; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      assertThat(fi.getFrameNum(), equalTo(frame));
      assertThat(fi.getAnimTime(), equalTo((double) frame));
      assertThat(fi.getStepNum(), equalTo(6 + frame / 40));
      assertThat(fi.getPercent(), equalTo((frame % 40) * 100.0 / 40.0));
      assertThat(fi.getSeconds(), equalTo(frame / 20.0));
      assertThat(fi.getOpacity(), equalTo(1.0));
      assertThat(fi.isFaded(), equalTo(false));
      assertThat(fi.isHeld(), equalTo(false));
    }
  }

  /**
   * speed=5, FPS=20, 10-11. lépés, hold elején 40, végén 80, fade nincs.
   */
  @Test
  public void test7() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setSpeed(5);
    apb.setFramesPerSecond(20);
    apb.setFirstStep(10);
    apb.setLastStep(11);
    apb.setHoldInLength(40);
    apb.setHoldOutLength(80);
    AnimParams ap = apb.createAnimParams();

    FrameGenerator fg = new FrameGenerator(ap, testStepsAnimLength);
    assertThat(fg.getAnimParams(), sameInstance(ap));
    assertThat(fg.getStepsAnimLength(), equalTo(testStepsAnimLength));

    assertThat(fg.getNumberOfFrames(), equalTo(200));
    assertThat(fg.getLengthInSeconds(), equalTo(10.0));
    assertThat(fg.getFirstNonFadedFrame(), equalTo(0));
    assertThat(fg.getLastNonFadedFrame(), equalTo(199));
    assertThat(fg.getFirstNonHeldFrame(), equalTo(40));
    assertThat(fg.getLastNonHeldFrame(), equalTo(119));

    int numberOfFrames = fg.getNumberOfFrames();
    int firstNonHeldFrame = fg.getFirstNonHeldFrame();
    int lastNonHeldFrame = fg.getLastNonHeldFrame();

    // Összes kockát ellenőrizzük
    int frame = 0;
    // Kitartás az elején
    for (; frame < firstNonHeldFrame; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      assertThat(fi.getFrameNum(), equalTo(frame));
      assertThat(fi.getAnimTime(), equalTo((double) frame));
      assertThat(fi.getStepNum(), equalTo(10));
      assertThat(fi.getPercent(), equalTo(0.0));
      assertThat(fi.getSeconds(), equalTo(frame / 20.0));
      assertThat(fi.getOpacity(), equalTo(1.0));
      assertThat(fi.isFaded(), equalTo(false));
      assertThat(fi.isHeld(), equalTo(true));
    }
    // Normál kockák
    for (; frame <= lastNonHeldFrame; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      assertThat(fi.getFrameNum(), equalTo(frame));
      assertThat(fi.getAnimTime(), equalTo((double) frame));
      assertThat(fi.getStepNum(), equalTo(10 + (frame - firstNonHeldFrame) / 40));
      assertThat(fi.getPercent(), equalTo(((frame - firstNonHeldFrame) % 40) * 100.0 / 40.0));
      assertThat(fi.getSeconds(), equalTo(frame / 20.0));
      assertThat(fi.getOpacity(), equalTo(1.0));
      assertThat(fi.isFaded(), equalTo(false));
      assertThat(fi.isHeld(), equalTo(false));
    }
    // Kitartás a végén
    for (; frame < numberOfFrames; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      assertThat(fi.getFrameNum(), equalTo(frame));
      assertThat(fi.getAnimTime(), equalTo((double) frame));
      assertThat(fi.getStepNum(), equalTo(11));
      assertThat(fi.getPercent(), equalTo(100.0));
      assertThat(fi.getSeconds(), equalTo(frame / 20.0));
      assertThat(fi.getOpacity(), equalTo(1.0));
      assertThat(fi.isFaded(), equalTo(false));
      assertThat(fi.isHeld(), equalTo(true));
    }
  }

  /**
   * speed=20, FPS=50, 13-14. lépés, hold elején 30, végén 60, fade nincs.
   */
  @Test
  public void test8() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setSpeed(20);
    apb.setFramesPerSecond(50);
    apb.setFirstStep(13);
    apb.setLastStep(14);
    apb.setHoldInLength(30);
    apb.setHoldOutLength(60);
    AnimParams ap = apb.createAnimParams();

    FrameGenerator fg = new FrameGenerator(ap, testStepsAnimLength);
    assertThat(fg.getAnimParams(), sameInstance(ap));
    assertThat(fg.getStepsAnimLength(), equalTo(testStepsAnimLength));

    assertThat(fg.getNumberOfFrames(), equalTo(107));
    assertThat(fg.getLengthInSeconds(), equalTo(2.14));
    assertThat(fg.getFirstNonFadedFrame(), equalTo(0));
    assertThat(fg.getLastNonFadedFrame(), equalTo(106));
    assertThat(fg.getFirstNonHeldFrame(), equalTo(19));
    assertThat(fg.getLastNonHeldFrame(), equalTo(68));

    int numberOfFrames = fg.getNumberOfFrames();
    int firstNonHeldFrame = fg.getFirstNonHeldFrame();
    int lastNonHeldFrame = fg.getLastNonHeldFrame();

    // Összes kockát ellenőrizzük
    int frame = 0;
    // Kitartás az elején
    for (; frame < firstNonHeldFrame; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      assertThat(fi.getFrameNum(), equalTo(frame));
      assertThat(fi.getAnimTime(), closeTo(frame * 1.6, 0.00001));
      assertThat(fi.getStepNum(), equalTo(13));
      assertThat(fi.getPercent(), equalTo(0.0));
      assertThat(fi.getSeconds(), equalTo(frame / 50.0));
      assertThat(fi.getOpacity(), equalTo(1.0));
      assertThat(fi.isFaded(), equalTo(false));
      assertThat(fi.isHeld(), equalTo(true));
    }
    // Normál kockák
    for (; frame <= lastNonHeldFrame; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      assertThat(fi.getFrameNum(), equalTo(frame));
      assertThat(fi.getAnimTime(), closeTo(frame * 1.6, 0.00001));
      assertThat(fi.getStepNum(), equalTo(13 + (frame - firstNonHeldFrame) / 25));
      //assertThat(fi.getPercent(), closeTo(((frame - firstNonHeldFrame) % 25) * 100.0 / 25.0, 0.00001)); // Túl bonyolult lenne kiszámítani
      assertThat(fi.getSeconds(), equalTo(frame / 50.0));
      assertThat(fi.getOpacity(), equalTo(1.0));
      assertThat(fi.isFaded(), equalTo(false));
      assertThat(fi.isHeld(), equalTo(false));
    }
    // Kitartás a végén
    for (; frame < numberOfFrames; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      assertThat(fi.getFrameNum(), equalTo(frame));
      assertThat(fi.getAnimTime(), closeTo(frame * 1.6, 0.00001));
      assertThat(fi.getStepNum(), equalTo(14));
      assertThat(fi.getPercent(), equalTo(100.0));
      assertThat(fi.getSeconds(), equalTo(frame / 50.0));
      assertThat(fi.getOpacity(), equalTo(1.0));
      assertThat(fi.isFaded(), equalTo(false));
      assertThat(fi.isHeld(), equalTo(true));
    }
  }

  /**
   * speed=5, FPS=20, 10-11. lépés, hold elején 40, végén 80, fade elején 25, végén 25.
   */
  @Test
  public void test9() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setSpeed(5);
    apb.setFramesPerSecond(20);
    apb.setFirstStep(10);
    apb.setLastStep(11);
    apb.setHoldInLength(40);
    apb.setHoldOutLength(80);
    apb.setFadeInLength(25);
    apb.setFadeOutLength(25);
    AnimParams ap = apb.createAnimParams();

    FrameGenerator fg = new FrameGenerator(ap, testStepsAnimLength);
    assertThat(fg.getAnimParams(), sameInstance(ap));
    assertThat(fg.getStepsAnimLength(), equalTo(testStepsAnimLength));

    assertThat(fg.getNumberOfFrames(), equalTo(200));
    assertThat(fg.getLengthInSeconds(), equalTo(10.0));
    assertThat(fg.getFirstNonFadedFrame(), equalTo(25));
    assertThat(fg.getLastNonFadedFrame(), equalTo(174));
    assertThat(fg.getFirstNonHeldFrame(), equalTo(40));
    assertThat(fg.getLastNonHeldFrame(), equalTo(119));

    int numberOfFrames = fg.getNumberOfFrames();
    int firstNonFadedFrame = fg.getFirstNonFadedFrame();
    int lastNonFadedFrame = fg.getLastNonFadedFrame();

    // Összes kockánál ellenőrizzük a fade-et
    double lastOpacity = 0.0;
    for (int frame = 0; frame < numberOfFrames; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      if (frame < firstNonFadedFrame) {
        assertThat(fi.getOpacity(), greaterThanOrEqualTo(lastOpacity));
        assertThat(fi.isFaded(), equalTo(true));
      } else if (frame > lastNonFadedFrame) {
        assertThat(fi.getOpacity(), lessThanOrEqualTo(lastOpacity));
        assertThat(fi.isFaded(), equalTo(true));
      } else {
        assertThat(fi.getOpacity(), equalTo(1.0));
        assertThat(fi.isFaded(), equalTo(false));
      }
      lastOpacity = fi.getOpacity();
    }
  }

  /**
   * speed=20, FPS=50, 13-14. lépés, hold elején 30, végén 60, fade elején 12, végén 23.
   */
  @Test
  public void test10() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setSpeed(20);
    apb.setFramesPerSecond(50);
    apb.setFirstStep(13);
    apb.setLastStep(14);
    apb.setHoldInLength(30);
    apb.setHoldOutLength(60);
    apb.setFadeInLength(12);
    apb.setFadeOutLength(23);
    AnimParams ap = apb.createAnimParams();

    FrameGenerator fg = new FrameGenerator(ap, testStepsAnimLength);
    assertThat(fg.getAnimParams(), sameInstance(ap));
    assertThat(fg.getStepsAnimLength(), equalTo(testStepsAnimLength));

    assertThat(fg.getNumberOfFrames(), equalTo(107));
    assertThat(fg.getLengthInSeconds(), equalTo(2.14));
    assertThat(fg.getFirstNonFadedFrame(), equalTo(8));
    assertThat(fg.getLastNonFadedFrame(), equalTo(91));
    assertThat(fg.getFirstNonHeldFrame(), equalTo(19));
    assertThat(fg.getLastNonHeldFrame(), equalTo(68));

    int numberOfFrames = fg.getNumberOfFrames();
    int firstNonFadedFrame = fg.getFirstNonFadedFrame();
    int lastNonFadedFrame = fg.getLastNonFadedFrame();

    // Összes kockánál ellenőrizzük a fade-et
    double lastOpacity = 0.0;
    for (int frame = 0; frame < numberOfFrames; frame++) {
      FrameInfo fi = fg.getFrameInfo(frame);
      if (frame < firstNonFadedFrame) {
        assertThat(fi.getOpacity(), greaterThanOrEqualTo(lastOpacity));
        assertThat(fi.isFaded(), equalTo(true));
      } else if (frame > lastNonFadedFrame) {
        assertThat(fi.getOpacity(), lessThanOrEqualTo(lastOpacity));
        assertThat(fi.isFaded(), equalTo(true));
      } else {
        assertThat(fi.getOpacity(), equalTo(1.0));
        assertThat(fi.isFaded(), equalTo(false));
      }
      lastOpacity = fi.getOpacity();
    }
  }

  /**
   * speed=5, FPS=20, 10-11. lépés, hold nincs, fade elején 50, végén 50 (átfedés) -> hiba
   */
  @Test(expected = IllegalArgumentException.class)
  public void test11() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setSpeed(5);
    apb.setFramesPerSecond(20);
    apb.setFirstStep(10);
    apb.setLastStep(11);
    apb.setFadeInLength(150);
    apb.setFadeOutLength(150);
    AnimParams ap = apb.createAnimParams();

    FrameGenerator fg = new FrameGenerator(ap, testStepsAnimLength);
  }

  /**
   * speed=5, FPS=20, 10-11. lépés, hold nincs, fade elején nincs, végén 100 (túl nagy) -> hiba
   */
  @Test(expected = IllegalArgumentException.class)
  public void test12() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setSpeed(5);
    apb.setFramesPerSecond(20);
    apb.setFirstStep(10);
    apb.setLastStep(11);
    apb.setFadeOutLength(100);
    AnimParams ap = apb.createAnimParams();

    FrameGenerator fg = new FrameGenerator(ap, testStepsAnimLength);
  }

  /**
   * Képméretek tesztelése
   */
  @Test
  public void test13() {
    AnimParamsBuilder apb = new AnimParamsBuilder();
    apb.setWidth(200);
    apb.setHeight(322);

    FrameGenerator fg = new FrameGenerator(apb.createAnimParams(), testStepsAnimLength);
    BufferedImage frame = fg.generateFrame(100);

    assertThat(frame.getWidth(), equalTo(200));
    assertThat(frame.getHeight(), equalTo(322));

    frame = fg.generateFrame(44, 500, 100);
    assertThat(frame.getWidth(), equalTo(500));
    assertThat(frame.getHeight(), equalTo(100));
  }
}
