package hu.akusius.palenque.anigifmaker;

import java.io.*;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author Bujdosó Ákos
 */
public class GenerateParamsTest {

  public GenerateParamsTest() {
  }

  @Test
  public void test1() {
    AnimParams ap = new AnimParams(100, 200, 3, 5, 7, 10, true, false, 1.3, -0.3, 3.1, -1.4, 2.4, 12, 11, 9, 8, "Desc");
    GifParams gp = new GifParams(7, false, true);

    GenerateParams gnp = new GenerateParams(ap, gp);
    assertThat(gnp.getAnimParams(), sameInstance(ap));
    assertThat(gnp.getGifParams(), sameInstance(gp));

    String serialized = gnp.serialize();
    GenerateParams gnp2 = GenerateParams.deserialize(serialized);

    AnimParamsTest.testEqual(ap, gnp2.getAnimParams());
    GifParamsTest.testEqual(gp, gnp2.getGifParams());
  }

  @Test
  public void test2() {
    AnimParams ap = new AnimParams(100, 200, 3, 5, 7, 10, true, true, 1.3, -0.3, 3.1, -1.4, 2.4, 12, 11, 9, 8, "Desc");
    GifParams gp = new GifParams(7, false, false);

    GenerateParams gnp = new GenerateParams(ap, gp);
    assertThat(gnp.getAnimParams(), sameInstance(ap));
    assertThat(gnp.getGifParams(), sameInstance(gp));

    JSONObject jo = gnp.serializeJSON();
    GenerateParams gnp2 = GenerateParams.deserialize(jo);

    AnimParamsTest.testEqual(ap, gnp2.getAnimParams());
    GifParamsTest.testEqual(gp, gnp2.getGifParams());

    jo.remove("anim");
    GenerateParams gnp3 = GenerateParams.deserialize(jo);

    AnimParamsTest.testEqual(new AnimParamsBuilder().createAnimParams(), gnp3.getAnimParams());
    GifParamsTest.testEqual(gp, gnp3.getGifParams());
  }

  @Test
  public void test3() throws IOException {
    GifParams gp = new GifParams(7, false, true);
    AnimParams ap = new AnimParams(1000, 200, 3, 20, 100, 45, false, true, 0, 0, 0, -1.4, 10.0, 987, 123, 20, 0,
            "\"áRVÍZTŰRŐ 'TÜKÖR'FÚRÓGÉP\"");

    GenerateParams params = new GenerateParams(ap, gp);

    byte[] serialized;
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos)) {
      params.serialize(dos);
      dos.flush();
      serialized = bos.toByteArray();
    }

    try (ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
            DataInputStream dis = new DataInputStream(bis)) {
      GenerateParams params2 = GenerateParams.deserialize(dis);

      AnimParamsTest.testEqual(ap, params2.getAnimParams());
      GifParamsTest.testEqual(gp, params2.getGifParams());
    }
  }

  @Test
  public void test4() throws IOException {
    GifParams gp = new GifParams(7, false, false);
    AnimParams ap = new AnimParams(100, 200, 3, 5, 7, 10, true, true, 67.89, -31.32, 3.1, -1.4, 1.0, 12, 11, 9, 8,
            "\"Árvíztűrő 'tükör'fúrógép\"");

    GenerateParams params = new GenerateParams(ap, gp);
    String serialized = params.serializeBinaryASCII();
    assertTrue(serialized.startsWith(GenerateParams.BINARY_ASCII_PREFIX));
    GenerateParams params2 = GenerateParams.deserializeBinaryASCII(serialized);

    AnimParamsTest.testEqual(ap, params2.getAnimParams());
    GifParamsTest.testEqual(gp, params2.getGifParams());

    GenerateParams params3
            = GenerateParams.deserializeBinaryASCIIOptional(null, "", "Dummy", serialized, "foo", "bar", null, serialized);
    AnimParamsTest.testEqual(ap, params3.getAnimParams());
    GifParamsTest.testEqual(gp, params3.getGifParams());

    GenerateParams params4
            = GenerateParams.deserializeBinaryASCIIOptional(new String[]{null, "", serialized, "Dummy"});
    AnimParamsTest.testEqual(ap, params4.getAnimParams());
    GifParamsTest.testEqual(gp, params4.getGifParams());

    GenerateParams params5
            = GenerateParams.deserializeBinaryASCIIOptional(new String[]{null, "", "foo", "Dummy"});
    assertNull(params5);

    assertFalse(GenerateParams.isSerializedBinaryASCII(null));
    assertFalse(GenerateParams.isSerializedBinaryASCII("foo"));
    assertTrue(GenerateParams.isSerializedBinaryASCII(serialized));
  }
}
