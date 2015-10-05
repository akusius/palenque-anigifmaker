package hu.akusius.palenque.anigifmaker;

/**
 * Egy adott képkocka programbeli adatai.
 * @author Bujdosó Ákos
 */
public class FrameInfo {

  private final int frameNum;

  private final double seconds;

  private final int stepNum;

  private final double percent;

  private final double animTime;

  private final double opacity;

  private final boolean faded;

  private final boolean held;

  /**
   * @return A képkocka száma.
   */
  public int getFrameNum() {
    return this.frameNum;
  }

  /**
   * @return A képkocka pozíciója másodpercben.
   */
  public double getSeconds() {
    return this.seconds;
  }

  /**
   * @return A képkocka melyik lépéshez tartozik az animációban.
   */
  public int getStepNum() {
    return this.stepNum;
  }

  /**
   * @return A képkocka a lépésen ({@link #getStepNum()}) belül hány százaléknál található.
   */
  public double getPercent() {
    return this.percent;
  }

  /**
   * @return A képkocka az animáció saját idejében hol található.
   */
  public double getAnimTime() {
    return this.animTime;
  }

  /**
   * @return A képkocka telítettsége (0-1).
   */
  public double getOpacity() {
    return this.opacity;
  }

  /**
   * @return {@code true}, ha a képkocka (valamilyen szinten) fade-elve van.
   */
  public boolean isFaded() {
    return faded;
  }

  /**
   * @return {@code true}, ha a képkocka ki van tartva.
   */
  public boolean isHeld() {
    return held;
  }

  /**
   *
   * @param frameNum
   * @param seconds
   * @param stepNum
   * @param percent
   * @param animTime
   * @param opacity
   * @param faded
   * @param held
   */
  public FrameInfo(int frameNum, double seconds, int stepNum, double percent, double animTime, double opacity,
          boolean faded, boolean held) {
    this.frameNum = frameNum;
    this.seconds = seconds;
    this.stepNum = stepNum;
    this.percent = percent;
    this.animTime = animTime;
    this.opacity = opacity;
    this.faded = faded;
    this.held = held;
  }
}
