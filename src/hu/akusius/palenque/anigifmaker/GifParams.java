package hu.akusius.palenque.anigifmaker;

import java.io.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Osztály a GIF-paraméterek kezeléséhez.
 * @author Bujdosó Ákos
 */
public class GifParams {

  public static final ParamRange<Integer> QUALITY_RANGE = new ParamRange<>(1, 20, 10);

  private final int quality;

  public static final ParamRange<Boolean> REPEAT_RANGE = new ParamRange<>(false, true, true);

  private final boolean repeat;

  public static final ParamRange<Boolean> EMBED_PARAMS_RANGE = new ParamRange<>(false, true, true);

  private final boolean embedParams;

  /**
   * @return A minőség.
   */
  public int getQuality() {
    return quality;
  }

  /**
   * @return Az ismétlés értéke.
   */
  public boolean isRepeat() {
    return repeat;
  }

  /**
   * @return {@code true} esetén a paraméterek beágyazásra kerülnek a GIF-fájlba.
   */
  public boolean isEmbedParams() {
    return embedParams;
  }

  public GifParams() {
    this(QUALITY_RANGE.getDef(), REPEAT_RANGE.getDef(), EMBED_PARAMS_RANGE.getDef());
  }

  public GifParams(int quality, boolean repeat, boolean embedParams) {
    QUALITY_RANGE.checkMinMax(quality);
    this.quality = quality;

    REPEAT_RANGE.checkMinMax(repeat);
    this.repeat = repeat;

    EMBED_PARAMS_RANGE.checkMinMax(embedParams);
    this.embedParams = embedParams;
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
    jo.put("quality", quality);
    jo.put("repeat", repeat);
    jo.put("embedParams", embedParams);

    return jo;
  }

  /**
   * Egy új példány beolvasása a korábban szerializált JSON-objektumból.
   * @param jo A JSON-objektum.
   * @return A deszerializált objektum.
   * @throws IllegalArgumentException Érvénytelen formátumú a forrás.
   */
  public static GifParams deserialize(JSONObject jo) throws IllegalArgumentException {
    int quality = jo.optInt("quality", QUALITY_RANGE.getDef());
    boolean repeat = jo.optBoolean("repeat", REPEAT_RANGE.getDef());
    boolean embedParams = jo.optBoolean("embedParams", EMBED_PARAMS_RANGE.getDef());

    return new GifParams(quality, repeat, embedParams);
  }

  /**
   * Egy új példány beolvasása a korábban szerializált formából.
   * @param source A beolvasás forrása.
   * @return A deszerializált objektum.
   * @throws IllegalArgumentException Érvénytelen formátumú a forrás.
   */
  public static GifParams deserialize(String source) throws IllegalArgumentException {
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
    dos.writeByte(1);  // Verzió
    dos.writeByte(quality & 0xff);
    dos.writeBoolean(repeat);
    dos.writeBoolean(embedParams);
  }

  /**
   * Bináris deszerializálás.
   * @param dis A deszerializálás forrása.
   * @return A deszerializált objektum.
   * @throws java.io.IOException Hiba történt a deszerializálás során.
   */
  public static GifParams deserialize(DataInputStream dis) throws IOException {
    if (dis.readByte() != 1) {
      throw new IOException("Invalid format!");
    }

    int quality = dis.readUnsignedByte();
    boolean repeat = dis.readBoolean();
    boolean embedParams = dis.readBoolean();

    return new GifParams(quality, repeat, embedParams);
  }
//</editor-fold>
}
