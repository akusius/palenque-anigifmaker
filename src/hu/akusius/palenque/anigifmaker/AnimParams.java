package hu.akusius.palenque.anigifmaker;

import hu.akusius.palenque.anigifmaker.rendering.FrameRenderer;
import java.io.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Osztály az animációs paraméterek kezeléséhez.
 * @author Bujdosó Ákos
 */
public class AnimParams {

  /**
   *
   */
  public static final ParamRange<Integer> WH_RANGE = new ParamRange<>(10, 1200, 500);

  private final int width;

  private final int height;

  /**
   *
   */
  public static final ParamRange<Integer> STEP_RANGE = new ParamRange<>(0, FrameRenderer.MAX_STEP, 0);

  private final int firstStep;

  private final int lastStep;

  /**
   *
   */
  public static final ParamRange<Integer> SPEED_RANGE = new ParamRange<>(1, 100, 10);

  private final int speed;

  /**
   *
   */
  public static final ParamRange<Integer> FPS_RANGE = new ParamRange<>(1, 50, 20);

  private final int framesPerSecond;

  /**
   *
   */
  public static final ParamRange<Boolean> SHOW_GRID_RANGE = new ParamRange<>(false, true, false);

  private final boolean showGrid;

  /**
   *
   */
  public static final ParamRange<Boolean> AUTO_CAM_RANGE = new ParamRange<>(false, true, false);

  private final boolean autoCam;

  /**
   *
   */
  public static final ParamRange<Double> TRANSLATE_RANGE = new ParamRange<>(-100.0, 100.0, 0.0);

  private final double translateX;

  private final double translateY;

  /**
   *
   */
  public static final ParamRange<Double> THETA_PHI_RANGE = new ParamRange<>(-5.0, 5.0, 0.0);

  private final double theta;

  private final double phi;

  /**
   *
   */
  public static final ParamRange<Double> ZOOM_RANGE = new ParamRange<>(0.1, 10.0, 1.0);

  private final double zoom;

  /**
   *
   */
  public static final ParamRange<Integer> HOLDINOUT_RANGE = new ParamRange<>(0, 1000, 0);

  private final int holdInLength;

  private final int holdOutLength;

  /**
   *
   */
  public static final ParamRange<Integer> FADEINOUT_RANGE = new ParamRange<>(0, 1000, 0);

  private final int fadeInLength;

  private final int fadeOutLength;

  private final String desc;

  /**
   * @return A szélesség.
   */
  public int getWidth() {
    return width;
  }

  /**
   * @return A magasság.
   */
  public int getHeight() {
    return height;
  }

  /**
   * @return Az első lépés.
   */
  public int getFirstStep() {
    return firstStep;
  }

  /**
   * @return Az utolsó lépés.
   */
  public int getLastStep() {
    return lastStep;
  }

  /**
   * @return A lejátszási sebesség.
   */
  public int getSpeed() {
    return speed;
  }

  /**
   * @return A kockák száma másodpercenként.
   */
  public int getFramesPerSecond() {
    return framesPerSecond;
  }

  /**
   * @return {@code true}, ha mutatjuk a koordinátarendszert.
   */
  public boolean isShowGrid() {
    return showGrid;
  }

  /**
   * @return {@code true}, ha automatikus a kameramozgás.
   */
  public boolean isAutoCam() {
    return autoCam;
  }

  /**
   * @return A kezdeti vízszintes eltolás.
   */
  public double getTranslateX() {
    return translateX;
  }

  /**
   * @return A kezdeti függőleges eltolás.
   */
  public double getTranslateY() {
    return translateY;
  }

  /**
   * @return A kezdeti vízszintes elforgatás.
   */
  public double getTheta() {
    return theta;
  }

  /**
   * @return A kezdeti függőleges elforgatás.
   */
  public double getPhi() {
    return phi;
  }

  /**
   * @return A nagyítás mértéke.
   */
  public double getZoom() {
    return zoom;
  }

  /**
   * @return A kezdeti kitartás hossza.
   */
  public int getHoldInLength() {
    return holdInLength;
  }

  /**
   * @return A befejező kitartás hossza.
   */
  public int getHoldOutLength() {
    return holdOutLength;
  }

  /**
   * @return A kezdeti áttűnés hossza.
   */
  public int getFadeInLength() {
    return fadeInLength;
  }

  /**
   * @return A befejező áttűnés hossza.
   */
  public int getFadeOutLength() {
    return fadeOutLength;
  }

  /**
   * @return A leírás.
   */
  public String getDesc() {
    return desc;
  }

  /**
   * Egy példány létrehozása a megadott adatokkal.
   * @param width
   * @param height
   * @param firstStep
   * @param lastStep
   * @param speed
   * @param framesPerSecond
   * @param showGrid
   * @param autoCam
   * @param translateX
   * @param translateY
   * @param theta
   * @param phi
   * @param zoom
   * @param holdInLength
   * @param holdOutLength
   * @param fadeInLength
   * @param fadeOutLength
   * @param desc
   * @throws IllegalArgumentException Hibás adatok lettek megadva.
   */
  public AnimParams(int width, int height, int firstStep, int lastStep, int speed, int framesPerSecond,
          boolean showGrid, boolean autoCam,
          double translateX, double translateY, double theta, double phi, double zoom,
          int holdInLength, int holdOutLength, int fadeInLength, int fadeOutLength, String desc) {
    WH_RANGE.checkMinMax(width, height);
    this.width = width;
    this.height = height;

    STEP_RANGE.checkMinMax(firstStep, lastStep);
    if (lastStep < firstStep) {
      throw new IllegalArgumentException();
    }
    this.firstStep = firstStep;
    this.lastStep = lastStep;

    SPEED_RANGE.checkMinMax(speed);
    this.speed = speed;

    FPS_RANGE.checkMinMax(framesPerSecond);
    this.framesPerSecond = framesPerSecond;

    SHOW_GRID_RANGE.checkMinMax(showGrid);
    this.showGrid = showGrid;

    AUTO_CAM_RANGE.checkMinMax(autoCam);
    this.autoCam = autoCam;

    TRANSLATE_RANGE.checkMinMax(translateX, translateY);
    this.translateX = translateX;
    this.translateY = translateY;

    THETA_PHI_RANGE.checkMinMax(theta, phi);
    this.theta = theta;
    this.phi = phi;

    ZOOM_RANGE.checkMinMax(zoom);
    this.zoom = zoom;

    HOLDINOUT_RANGE.checkMinMax(holdInLength, holdOutLength);
    this.holdInLength = holdInLength;
    this.holdOutLength = holdOutLength;

    FADEINOUT_RANGE.checkMinMax(fadeInLength, fadeOutLength);
    this.fadeInLength = fadeInLength;
    this.fadeOutLength = fadeOutLength;

    this.desc = desc != null ? desc : "";
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
    jo.put("width", width);
    jo.put("height", height);
    jo.put("firstStep", firstStep);
    jo.put("lastStep", lastStep);
    jo.put("speed", speed);
    jo.put("framesPerSecond", framesPerSecond);
    jo.put("showGrid", showGrid);
    jo.put("autoCam", autoCam);
    jo.put("translateX", translateX);
    jo.put("translateY", translateY);
    jo.put("theta", theta);
    jo.put("phi", phi);
    jo.put("zoom", zoom);
    jo.put("holdInLength", holdInLength);
    jo.put("holdOutLength", holdOutLength);
    jo.put("fadeInLength", fadeInLength);
    jo.put("fadeOutLength", fadeOutLength);
    jo.put("desc", desc);

    return jo;
  }

  /**
   * Egy új példány beolvasása a korábban szerializált JSON-objektumból.
   * @param jo A JSON-objektum.
   * @return A deszerializált objektum.
   * @throws IllegalArgumentException Érvénytelen formátumú a forrás.
   */
  public static AnimParams deserialize(JSONObject jo) throws IllegalArgumentException {
    AnimParamsBuilder builder = new AnimParamsBuilder();
    if (jo.has("width")) {
      builder.setWidth(jo.getInt("width"));
    }
    if (jo.has("height")) {
      builder.setHeight(jo.getInt("height"));
    }
    if (jo.has("firstStep")) {
      builder.setFirstStep(jo.getInt("firstStep"));
    }
    if (jo.has("lastStep")) {
      builder.setLastStep(jo.getInt("lastStep"));
    }
    if (jo.has("speed")) {
      builder.setSpeed(jo.getInt("speed"));
    }
    if (jo.has("framesPerSecond")) {
      builder.setFramesPerSecond(jo.getInt("framesPerSecond"));
    }
    if (jo.has("showGrid")) {
      builder.setShowGrid(jo.getBoolean("showGrid"));
    }
    if (jo.has("autoCam")) {
      builder.setAutoCam(jo.getBoolean("autoCam"));
    }
    if (jo.has("translateX")) {
      builder.setTranslateX(jo.getDouble("translateX"));
    }
    if (jo.has("translateY")) {
      builder.setTranslateY(jo.getDouble("translateY"));
    }
    if (jo.has("theta")) {
      builder.setTheta(jo.getDouble("theta"));
    }
    if (jo.has("phi")) {
      builder.setPhi(jo.getDouble("phi"));
    }
    if (jo.has("zoom")) {
      builder.setZoom(jo.getDouble("zoom"));
    }
    if (jo.has("holdInLength")) {
      builder.setHoldInLength(jo.getInt("holdInLength"));
    }
    if (jo.has("holdOutLength")) {
      builder.setHoldOutLength(jo.getInt("holdOutLength"));
    }
    if (jo.has("fadeInLength")) {
      builder.setFadeInLength(jo.getInt("fadeInLength"));
    }
    if (jo.has("fadeOutLength")) {
      builder.setFadeOutLength(jo.getInt("fadeOutLength"));
    }
    if (jo.has("desc")) {
      builder.setDesc(jo.getString("desc"));
    }

    return builder.createAnimParams();
  }

  /**
   * Egy új példány beolvasása a korábban szerializált formából.
   * @param source A beolvasás forrása.
   * @return A deszerializált objektum.
   * @throws IllegalArgumentException Érvénytelen formátumú a forrás.
   */
  public static AnimParams deserialize(String source) throws IllegalArgumentException {
    try {
      JSONObject jo = new JSONObject(source);
      return AnimParams.deserialize(jo);
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
    dos.writeShort(width);
    dos.writeShort(height);
    dos.writeByte(firstStep);
    dos.writeByte(lastStep);
    dos.writeByte(speed);
    dos.writeByte(framesPerSecond);
    dos.writeBoolean(showGrid);
    dos.writeBoolean(autoCam);
    if (translateX == TRANSLATE_RANGE.getDef() && translateY == translateX) {
      dos.writeBoolean(true);
    } else {
      dos.writeBoolean(false);
      dos.writeDouble(translateX);
      dos.writeDouble(translateY);
    }
    if (theta == THETA_PHI_RANGE.getDef() && phi == theta) {
      dos.writeBoolean(true);
    } else {
      dos.writeBoolean(false);
      dos.writeDouble(theta);
      dos.writeDouble(phi);
    }
    if (zoom == ZOOM_RANGE.getDef()) {
      dos.writeBoolean(true);
    } else {
      dos.writeBoolean(false);
      dos.writeDouble(zoom);
    }
    dos.writeShort(holdInLength);
    dos.writeShort(holdOutLength);
    dos.writeShort(fadeInLength);
    dos.writeShort(fadeOutLength);
    dos.writeUTF(desc);
  }

  /**
   * Bináris deszerializálás.
   * @param dis A deszerializálás forrása.
   * @return A deszerializált objektum.
   * @throws java.io.IOException Hiba történt a deszerializálás során.
   */
  public static AnimParams deserialize(DataInputStream dis) throws IOException {
    if (dis.readByte() != 1) {
      throw new IOException("Invalid format!");
    }

    int width = dis.readShort();
    int height = dis.readShort();
    int firstStep = dis.readUnsignedByte();
    int lastStep = dis.readUnsignedByte();
    int speed = dis.readUnsignedByte();
    int framesPerSecond = dis.readUnsignedByte();
    boolean showGrid = dis.readBoolean();
    boolean autoCam = dis.readBoolean();

    double translateX, translateY;
    if (dis.readBoolean()) {
      translateX = translateY = TRANSLATE_RANGE.getDef();
    } else {
      translateX = dis.readDouble();
      translateY = dis.readDouble();
    }

    double theta, phi;
    if (dis.readBoolean()) {
      theta = phi = THETA_PHI_RANGE.getDef();
    } else {
      theta = dis.readDouble();
      phi = dis.readDouble();
    }

    double zoom;
    if (dis.readBoolean()) {
      zoom = ZOOM_RANGE.getDef();
    } else {
      zoom = dis.readDouble();
    }

    int holdInLength = dis.readShort();
    int holdOutLength = dis.readShort();
    int fadeInLength = dis.readShort();
    int fadeOutLength = dis.readShort();

    String desc = dis.readUTF();

    return new AnimParams(width, height, firstStep, lastStep, speed, framesPerSecond, showGrid, autoCam,
            translateX, translateY, theta, phi, zoom, holdInLength, holdOutLength, fadeInLength, fadeOutLength, desc);
  }
//</editor-fold>
}
