package hu.akusius.palenque.anigifmaker;

import java.io.*;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.beans.SamePropertyValuesAs.*;
import static org.junit.Assert.*;

/**
 *
 * @author Bujdosó Ákos
 */
public class AnimParamsTest {

  /**
   *
   */
  public AnimParamsTest() {
  }

  public static void testEqual(AnimParams ap1, AnimParams ap2) {
    assertThat(ap1, allOf(not(sameInstance(ap2)), samePropertyValuesAs(ap2)));
  }

  private static void testBuilder(AnimParams ap) {
    AnimParamsBuilder b = new AnimParamsBuilder();
    b.setWidth(ap.getWidth());
    b.setHeight(ap.getHeight());
    b.setFirstStep(ap.getFirstStep());
    b.setLastStep(ap.getLastStep());
    b.setSpeed(ap.getSpeed());
    b.setFramesPerSecond(ap.getFramesPerSecond());
    b.setShowGrid(ap.isShowGrid());
    b.setAutoCam(ap.isAutoCam());
    b.setTranslateX(ap.getTranslateX());
    b.setTranslateY(ap.getTranslateY());
    b.setTheta(ap.getTheta());
    b.setPhi(ap.getPhi());
    b.setZoom(ap.getZoom());
    b.setHoldInLength(ap.getHoldInLength());
    b.setHoldOutLength(ap.getHoldOutLength());
    b.setFadeInLength(ap.getFadeInLength());
    b.setFadeOutLength(ap.getFadeOutLength());
    b.setDesc(ap.getDesc());
    testEqual(ap, b.createAnimParams());

    testEqual(ap, new AnimParamsBuilder(ap).createAnimParams());
  }

  /**
   *
   */
  @Test
  public void test1() {
    AnimParams ap = new AnimParams(100, 200, 3, 5, 7, 10, true, false, 1.3, -0.3, 3.1, -1.4, 2.4, 12, 11, 9, 8, "Desc");
    assertThat(ap.getWidth(), equalTo(100));
    assertThat(ap.getHeight(), equalTo(200));
    assertThat(ap.getFirstStep(), equalTo(3));
    assertThat(ap.getLastStep(), equalTo(5));
    assertThat(ap.getSpeed(), equalTo(7));
    assertThat(ap.getFramesPerSecond(), equalTo(10));
    assertThat(ap.isShowGrid(), is(true));
    assertThat(ap.isAutoCam(), is(false));
    assertThat(ap.getTranslateX(), equalTo(1.3));
    assertThat(ap.getTranslateY(), equalTo(-0.3));
    assertThat(ap.getTheta(), equalTo(3.1));
    assertThat(ap.getPhi(), equalTo(-1.4));
    assertThat(ap.getHoldInLength(), equalTo(12));
    assertThat(ap.getHoldOutLength(), equalTo(11));
    assertThat(ap.getFadeInLength(), equalTo(9));
    assertThat(ap.getFadeOutLength(), equalTo(8));
    assertThat(ap.getDesc(), equalTo("Desc"));

    String serialized = ap.serialize();
    AnimParams ap2 = AnimParams.deserialize(serialized);
    testEqual(ap, ap2);

    testBuilder(ap);
  }

  @Test
  public void test2() {
    AnimParams ap = new AnimParams(100, 200, 3, 5, 7, 10, true, true, 1.3, -0.3, 3.1, -1.4, 2.4, 12, 11, 9, 8, "Desc");

    JSONObject jo = ap.serializeJSON();
    AnimParams ap2 = AnimParams.deserialize(jo);
    testEqual(ap, ap2);
    testBuilder(ap);

    jo.remove("width");
    jo.remove("translateX");
    ap2 = AnimParams.deserialize(jo);
    assertThat(ap2.getWidth(), equalTo(AnimParams.WH_RANGE.getDef()));
    assertThat(ap2.getTranslateX(), equalTo(AnimParams.TRANSLATE_RANGE.getDef()));

    AnimParams ap3 = new AnimParamsBuilder(ap)
            .setWidth(AnimParams.WH_RANGE.getDef())
            .setTranslateX(AnimParams.TRANSLATE_RANGE.getDef())
            .createAnimParams();
    testEqual(ap2, ap3);
  }

  @Test
  public void test3() throws IOException {
    AnimParams ap = new AnimParams(100, 200, 3, 5, 7, 10, true, true, 67.89, -31.32, 3.1, -2.4, 1.0, 12, 11, 9, 8,
            "\"Árvíztűrő 'tükör'fúrógép\"");

    byte[] serialized;
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos)) {
      ap.serialize(dos);
      dos.flush();
      serialized = bos.toByteArray();
    }
    try (ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
            DataInputStream dis = new DataInputStream(bis)) {
      AnimParams ap2 = AnimParams.deserialize(dis);
      testEqual(ap, ap2);
    }
  }

  @Test
  public void test4() throws IOException {
    AnimParams ap = new AnimParams(1000, 200, 3, 20, 100, 45, false, true, 0, 0, 0, 2.4, 10.0, 987, 123, 20, 0,
            "\"áRVÍZTŰRŐ 'TÜKÖR'FÚRÓGÉP\"");

    byte[] serialized;
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos)) {
      ap.serialize(dos);
      dos.flush();
      serialized = bos.toByteArray();
    }
    try (ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
            DataInputStream dis = new DataInputStream(bis)) {
      AnimParams ap2 = AnimParams.deserialize(dis);
      testEqual(ap, ap2);
    }
  }

  @Test
  public void test5() throws IOException {
    AnimParams ap = new AnimParams(1000, 200, 3, 20, 100, 45, false, true, 0, 0, 0, 3.4, 10.0, 987, 123, 20, 0, null);

    byte[] serialized;
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos)) {
      ap.serialize(dos);
      dos.flush();
      serialized = bos.toByteArray();
    }
    try (ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
            DataInputStream dis = new DataInputStream(bis)) {
      AnimParams ap2 = AnimParams.deserialize(dis);
      testEqual(ap, ap2);
    }
  }
}
