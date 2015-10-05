package hu.akusius.palenque.anigifmaker;

/**
 *
 * @author Bujdosó Ákos
 */
public class AnimParamsBuilder {

  private int width = AnimParams.WH_RANGE.getDef();

  private int height = AnimParams.WH_RANGE.getDef();

  private int firstStep = AnimParams.STEP_RANGE.getMin();

  private int lastStep = AnimParams.STEP_RANGE.getMax();

  private int speed = AnimParams.SPEED_RANGE.getDef();

  private int framesPerSecond = AnimParams.FPS_RANGE.getDef();

  private boolean showGrid = AnimParams.SHOW_GRID_RANGE.getDef();

  private boolean autoCam = AnimParams.AUTO_CAM_RANGE.getDef();

  private double translateX = AnimParams.TRANSLATE_RANGE.getDef();

  private double translateY = AnimParams.TRANSLATE_RANGE.getDef();

  private double theta = AnimParams.THETA_PHI_RANGE.getDef();

  private double phi = AnimParams.THETA_PHI_RANGE.getDef();

  private double zoom = AnimParams.ZOOM_RANGE.getDef();

  private int holdInLength = AnimParams.HOLDINOUT_RANGE.getDef();

  private int holdOutLength = AnimParams.HOLDINOUT_RANGE.getDef();

  private int fadeInLength = AnimParams.FADEINOUT_RANGE.getDef();

  private int fadeOutLength = AnimParams.FADEINOUT_RANGE.getDef();

  private String desc = null;

  /**
   *
   */
  public AnimParamsBuilder() {
  }

  /**
   *
   * @param width
   * @return
   */
  public AnimParamsBuilder setWidth(int width) {
    this.width = width;
    return this;
  }

  /**
   *
   * @param height
   * @return
   */
  public AnimParamsBuilder setHeight(int height) {
    this.height = height;
    return this;
  }

  /**
   *
   * @param firstStep
   * @return
   */
  public AnimParamsBuilder setFirstStep(int firstStep) {
    this.firstStep = firstStep;
    return this;
  }

  /**
   *
   * @param lastStep
   * @return
   */
  public AnimParamsBuilder setLastStep(int lastStep) {
    this.lastStep = lastStep;
    return this;
  }

  /**
   *
   * @param speed
   * @return
   */
  public AnimParamsBuilder setSpeed(int speed) {
    this.speed = speed;
    return this;
  }

  /**
   *
   * @param framesPerSecond
   * @return
   */
  public AnimParamsBuilder setFramesPerSecond(int framesPerSecond) {
    this.framesPerSecond = framesPerSecond;
    return this;
  }

  /**
   *
   * @param showGrid
   * @return
   */
  public AnimParamsBuilder setShowGrid(boolean showGrid) {
    this.showGrid = showGrid;
    return this;
  }

  /**
   *
   * @param autoCam
   * @return
   */
  public AnimParamsBuilder setAutoCam(boolean autoCam) {
    this.autoCam = autoCam;
    return this;
  }

  /**
   *
   * @param translateX
   * @return
   */
  public AnimParamsBuilder setTranslateX(double translateX) {
    this.translateX = translateX;
    return this;
  }

  /**
   *
   * @param translateY
   * @return
   */
  public AnimParamsBuilder setTranslateY(double translateY) {
    this.translateY = translateY;
    return this;
  }

  /**
   *
   * @param theta
   * @return
   */
  public AnimParamsBuilder setTheta(double theta) {
    this.theta = theta;
    return this;
  }

  /**
   *
   * @param phi
   * @return
   */
  public AnimParamsBuilder setPhi(double phi) {
    this.phi = phi;
    return this;
  }

  /**
   *
   * @param zoom
   * @return
   */
  public AnimParamsBuilder setZoom(double zoom) {
    this.zoom = zoom;
    return this;
  }

  /**
   *
   * @param holdInLength
   * @return
   */
  public AnimParamsBuilder setHoldInLength(int holdInLength) {
    this.holdInLength = holdInLength;
    return this;
  }

  /**
   *
   * @param holdOutLength
   * @return
   */
  public AnimParamsBuilder setHoldOutLength(int holdOutLength) {
    this.holdOutLength = holdOutLength;
    return this;
  }

  /**
   *
   * @param fadeInLength
   * @return
   */
  public AnimParamsBuilder setFadeInLength(int fadeInLength) {
    this.fadeInLength = fadeInLength;
    return this;
  }

  /**
   *
   * @param fadeOutLength
   * @return
   */
  public AnimParamsBuilder setFadeOutLength(int fadeOutLength) {
    this.fadeOutLength = fadeOutLength;
    return this;
  }

  /**
   *
   * @param desc
   * @return
   */
  public AnimParamsBuilder setDesc(String desc) {
    this.desc = desc;
    return this;
  }

  /**
   *
   * @return
   */
  public AnimParams createAnimParams() {
    return new AnimParams(width, height, firstStep, lastStep, speed, framesPerSecond, showGrid, autoCam,
            translateX, translateY, theta, phi, zoom,
            holdInLength, holdOutLength, fadeInLength, fadeOutLength, desc);
  }

  /**
   * Létrehozás már létező paramétek alapján.
   * @param ap A paraméteradatok forrása.
   */
  public AnimParamsBuilder(AnimParams ap) {
    width = ap.getWidth();
    height = ap.getHeight();
    firstStep = ap.getFirstStep();
    lastStep = ap.getLastStep();
    speed = ap.getSpeed();
    framesPerSecond = ap.getFramesPerSecond();
    showGrid = ap.isShowGrid();
    autoCam = ap.isAutoCam();
    translateX = ap.getTranslateX();
    translateY = ap.getTranslateY();
    theta = ap.getTheta();
    phi = ap.getPhi();
    zoom = ap.getZoom();
    holdInLength = ap.getHoldInLength();
    holdOutLength = ap.getHoldOutLength();
    fadeInLength = ap.getFadeInLength();
    fadeOutLength = ap.getFadeOutLength();
    desc = ap.getDesc();
  }
}
