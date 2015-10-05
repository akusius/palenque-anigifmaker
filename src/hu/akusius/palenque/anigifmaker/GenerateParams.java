package hu.akusius.palenque.anigifmaker;

import java.io.*;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;
import org.other.Base64;

/**
 * Osztály a generálási paraméterek kezeléséhez.
 * @author Bujdosó Ákos
 */
public class GenerateParams {

  private final AnimParams animParams;

  private final GifParams gifParams;

  /**
   * @return Az animációs paraméterek.
   */
  public AnimParams getAnimParams() {
    return animParams;
  }

  /**
   * @return A GIF-paraméterek.
   */
  public GifParams getGifParams() {
    return gifParams;
  }

  public GenerateParams(AnimParams animParams, GifParams gifParams) {
    this.animParams = animParams;
    this.gifParams = gifParams;
  }

  /**
   * Az aktuális példány szerializálása sztringgé.
   * @return A szerializált adatok.
   */
  public String serialize() {
    return serializeJSON().toString(2);
  }

  /**
   * Az aktuális példány szerializálása {@link JSONObject}-ként.
   * @return A szerializált {@link JSONObject}.
   */
  public JSONObject serializeJSON() {
    JSONObject jo = new JSONObject();
    jo.put("anim", animParams.serializeJSON());
    jo.put("gif", gifParams.serializeJSON());
    return jo;
  }

  /**
   * Egy új példány beolvasása a korábban szerializált JSON-objektumból.
   * @param jo A JSON-objektum.
   * @return A deszerializált objektum.
   * @throws IllegalArgumentException Érvénytelen formátumú a forrás.
   */
  public static GenerateParams deserialize(JSONObject jo) throws IllegalArgumentException {
    final AnimParams ap;
    if (jo.has("anim")) {
      ap = AnimParams.deserialize(jo.getJSONObject("anim"));
    } else {
      ap = new AnimParamsBuilder().createAnimParams();
    }

    final GifParams gp;
    if (jo.has("gif")) {
      gp = GifParams.deserialize(jo.getJSONObject("gif"));
    } else {
      gp = new GifParams();
    }

    return new GenerateParams(ap, gp);
  }

  /**
   * Egy új példány beolvasása a korábban szerializált formából.
   * @param source A beolvasás forrása.
   * @return A deszerializált objektum.
   * @throws IllegalArgumentException Érvénytelen formátumú a forrás.
   */
  public static GenerateParams deserialize(String source) throws IllegalArgumentException {
    try {
      JSONObject jo = new JSONObject(source);
      return deserialize(jo);
    } catch (JSONException ex) {
      throw new IllegalArgumentException("Error during deserialization.", ex);
    }
  }

//<editor-fold defaultstate="open" desc="Bináris (de)szerializálás">
  /**
   * Bináris szerializálás.
   * @param dos A szerializálás célja.
   * @throws java.io.IOException Hiba történt a szerializálás során.
   */
  public void serialize(DataOutputStream dos) throws IOException {
    dos.writeByte(1);   // Verzió

    animParams.serialize(dos);
    gifParams.serialize(dos);
  }

  /**
   * Bináris deszerializálás.
   * @param dis A deszerializálás forrása.
   * @return A deszerializált objektum.
   * @throws java.io.IOException Hiba történt a deszerializálás során.
   */
  public static GenerateParams deserialize(DataInputStream dis) throws IOException {
    if (dis.readByte() != 1) {
      throw new IOException("Invalid format!");
    }

    AnimParams ap = AnimParams.deserialize(dis);
    GifParams gp = GifParams.deserialize(dis);

    return new GenerateParams(ap, gp);
  }

  public static final String BINARY_ASCII_PREFIX = "PQAGM:";

  /**
   * Szerializálás bináris-alapú ASCII formátumba.
   * A szerializálás Base64-gyel történik, prefix alkalmazásával.
   * @return A szerializált sztring.
   * @throws IOException Hiba történt a szerializálás során.
   */
  public String serializeBinaryASCII() throws IOException {
    byte[] data;
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos)) {
      serialize(dos);
      dos.flush();
      data = bos.toByteArray();
    }
    return BINARY_ASCII_PREFIX + Base64.getEncoder().withoutPadding().encodeToString(data);
  }

  /**
   * Visszaadja, hogy a megadott forrás a {@link #serializeBinaryASCII()} rutinnal szerializált adat-e.
   * @param source A forrás.
   * @return {@code true}, ha a forrás bináris-alapú ASCII szerializálás.
   */
  public static boolean isSerializedBinaryASCII(String source) {
    return source != null && source.startsWith(BINARY_ASCII_PREFIX);
  }

  /**
   * A {@link #serializeBinaryASCII()} rutinnal szerializált adat deszerializálása.
   * @param source A deszerializálás forrása.
   * @return A deszerializált objektum.
   * @throws IOException Hiba történt a deszerializálás során.
   */
  public static GenerateParams deserializeBinaryASCII(String source) throws IOException {
    if (!isSerializedBinaryASCII(source)) {
      throw new IllegalArgumentException();
    }

    source = source.substring(BINARY_ASCII_PREFIX.length());
    byte[] data = Base64.getDecoder().decode(source);
    try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
            DataInputStream dis = new DataInputStream(bis)) {
      return GenerateParams.deserialize(dis);
    }
  }

  /**
   * A megadott források közül az első alkalmas deszerializálása.
   * @param sources A potenciális deszerializálási források.
   * @return A deszerializált objektum, vagy {@code null}, ha nem voltak adatok a deszerializáláshoz.
   * @throws IOException Volt adat a deszerializáláshoz, de hiba történt a deszerializálás során.
   */
  public static GenerateParams deserializeBinaryASCIIOptional(Iterable<String> sources) throws IOException {
    for (String source : sources) {
      if (isSerializedBinaryASCII(source)) {
        return deserializeBinaryASCII(source);
      }
    }
    return null;
  }

  /**
   * A megadott források közül az első alkalmas deszerializálása.
   * @param sources A potenciális deszerializálási források.
   * @return A deszerializált objektum, vagy {@code null}, ha nem voltak adatok a deszerializáláshoz.
   * @throws IOException Volt adat a deszerializáláshoz, de hiba történt a deszerializálás során.
   */
  public static GenerateParams deserializeBinaryASCIIOptional(String... sources) throws IOException {
    return deserializeBinaryASCIIOptional(Arrays.asList(sources));
  }
//</editor-fold>
}
