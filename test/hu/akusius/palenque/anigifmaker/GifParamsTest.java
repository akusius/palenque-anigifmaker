package hu.akusius.palenque.anigifmaker;

import java.io.*;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.*;

/**
 *
 * @author Bujdosó Ákos
 */
public class GifParamsTest {

  public GifParamsTest() {
  }

  public static void testEqual(GifParams gp1, GifParams gp2) {
    assertThat(gp1, allOf(not(sameInstance(gp2)), samePropertyValuesAs(gp2)));
  }

  @Test
  public void test1() {
    GifParams gp1 = new GifParams(7, false, true);
    assertThat(gp1.getQuality(), equalTo(7));
    assertThat(gp1.isRepeat(), equalTo(false));
    assertThat(gp1.isEmbedParams(), equalTo(true));

    String serialized = gp1.serialize();
    GifParams gp2 = GifParams.deserialize(serialized);
    testEqual(gp1, gp2);
  }

  @Test
  public void test2() {
    GifParams gp1 = new GifParams(7, false, false);

    JSONObject jo = gp1.serializeJSON();
    GifParams gp2 = GifParams.deserialize(jo);
    testEqual(gp1, gp2);

    jo.remove("repeat");
    jo.remove("embedParams");
    gp2 = GifParams.deserialize(jo);

    assertThat(gp1.getQuality(), equalTo(7));
    assertThat(gp2.isRepeat(), equalTo(GifParams.REPEAT_RANGE.getDef()));
    assertThat(gp2.isEmbedParams(), equalTo(GifParams.EMBED_PARAMS_RANGE.getDef()));
  }

  @Test
  public void test3() throws IOException {
    GifParams[] gps = new GifParams[]{
      new GifParams(GifParams.QUALITY_RANGE.getMax(), false, true),
      new GifParams(GifParams.QUALITY_RANGE.getMin(), true, false),
      new GifParams(GifParams.QUALITY_RANGE.getDef(), false, false)
    };

    for (GifParams gp : gps) {
      byte[] serialized;
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
              DataOutputStream dos = new DataOutputStream(bos)) {
        gp.serialize(dos);
        dos.flush();
        serialized = bos.toByteArray();
      }
      try (ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
              DataInputStream dis = new DataInputStream(bis)) {
        GifParams gp2 = GifParams.deserialize(dis);
        testEqual(gp, gp2);
      }
    }
  }
}
