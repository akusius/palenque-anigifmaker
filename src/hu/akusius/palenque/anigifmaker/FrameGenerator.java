package hu.akusius.palenque.anigifmaker;

import hu.akusius.palenque.anigifmaker.rendering.FrameRenderer;
import hu.akusius.palenque.anigifmaker.rendering.GridSystemRenderer;
import hu.akusius.palenque.anigifmaker.rendering.Transformer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.*;
import java.util.List;
import org.other.Matrix;

/**
 * Ez az osztály végzi az animáció képkockákra való felosztását,
 * valamint általa generálható le egy képkocka.
 * @author Bujdosó Ákos
 */
public class FrameGenerator {

  /**
   * Az egyes lépések hosszai animációs időben.
   * Az animációs idő olyan hipotetikus kockaszám, ami 40-es FPS és 10-es sebesség mellett érvényes.
   */
  private static final int[] STEPS_ANIM_LENGTH = new int[]{
    200, // 0: Kiindulás
    50, // 1: Első forgatás
    50, // 2: Második forgatás
    50, // 3: Harmadik forgatás
    25, // 4: Várakozás
    50, // 5: Tükrözés
    25, // 6: Várakozás
    60, // 7: Sarkok befelé tolása
    100, // 8: Középső elemek befelé tolása
    75, // 9: Sarkok eltüntetése
    50, // 10: Első behajtás
    50, // 11: Várakozás
    50, // 12: Második behajtás 1.
    50, // 13: Második behajtás 2.
    50, // 14: Második behajtás 3.
    50, // 15: Második behajtás 4.
    50, // 16: Várakozás
    100, // 17: Ráközelítés egyikre és a többi eltüntetése
    50, // 18: Négyzetek helyett vonalak
    20, // 19: Várakozás
    75, // 20: Összezárás
    50, // 21: Program vége, várakozás
  };

  private static final int REF_SPEED = 10;

  private static final int REF_FPS = 40;

  private static final double FADE_RATE = 1.5;

  private final AnimParams ap;

  private final Program program;

  private final int[] stepsAnimLength;

  private final int numberOfFrames;

  private final double lengthInSeconds;

  private final int firstNonFadedFrame;

  private final int lastNonFadedFrame;

  private final int firstNonHeldFrame;

  private final int lastNonHeldFrame;

  private final int speed;

  private final int fps;

  private final Map<Integer, FrameInfo> frameInfoCache;

  private final AutoCam autoCam;

  /**
   * Egy új példány létrehozása a megadott animációs paraméterekkel és az alapértelmezett lépéshosszokkal.
   * @param ap Az animáció összeállításához szükséges paraméterek.
   */
  public FrameGenerator(AnimParams ap) {
    this(ap, STEPS_ANIM_LENGTH);
  }

  /**
   * Egy új példány létrehozása a megadott animációs paraméterekkel és lépéshosszokkal.
   * @param ap Az animáció összeállításához szükséges paraméterek.
   * @param stepsAnimLength Az egyes lépések hosszai animációs időben (speed=10, FPS=40).
   */
  FrameGenerator(AnimParams ap, int[] stepsAnimLength) {
    if (ap == null || stepsAnimLength == null || stepsAnimLength.length != FrameRenderer.MAX_STEP + 1) {
      throw new IllegalArgumentException();
    }

    this.ap = ap;
    this.stepsAnimLength = stepsAnimLength;

    program = new Program();

    this.speed = ap.getSpeed();
    this.fps = ap.getFramesPerSecond();

    this.numberOfFrames = fromAnimTimeInt(program.programLength);
    this.lengthInSeconds = (double) numberOfFrames / fps;

    this.firstNonFadedFrame = fromAnimTimeInt(ap.getFadeInLength());
    this.lastNonFadedFrame = fromAnimTimeInt(program.programLength - ap.getFadeOutLength()) - 1;

    if (firstNonFadedFrame > numberOfFrames || lastNonFadedFrame < 0) {
      // Hibás fade értékek lettek megadva
      throw new IllegalArgumentException();
    }

    assert 0 <= firstNonFadedFrame && firstNonFadedFrame < numberOfFrames;
    assert 0 <= lastNonFadedFrame && lastNonFadedFrame < numberOfFrames;

    this.firstNonHeldFrame = ap.getHoldInLength() > 0 ? fromAnimTimeInt(program.entries.get(1).start) : 0;
    this.lastNonHeldFrame = ap.getHoldOutLength() > 0
            ? fromAnimTimeInt(program.entries.get(program.entries.size() - 2).end) - 1 : numberOfFrames - 1;

    assert 0 <= firstNonHeldFrame && firstNonHeldFrame < numberOfFrames;
    assert 0 <= lastNonHeldFrame && lastNonHeldFrame < numberOfFrames;

    this.frameInfoCache = new HashMap<>(numberOfFrames);

    this.autoCam = ap.isAutoCam() ? new AutoCam() : null;
  }

  /**
   * @return Az animációs paraméterek.
   */
  public AnimParams getAnimParams() {
    return ap;
  }

  /**
   * @return Az egyes lépések hosszai animációs időben.
   */
  public int[] getStepsAnimLength() {
    return Arrays.copyOf(stepsAnimLength, stepsAnimLength.length);
  }

  /**
   * @return Az összes képkocka száma.
   */
  public int getNumberOfFrames() {
    return numberOfFrames;
  }

  /**
   * @return Az animáció teljes hossza másodpercben.
   */
  public double getLengthInSeconds() {
    return lengthInSeconds;
  }

  /**
   * @return Az első nem áttűnő kocka.
   */
  public int getFirstNonFadedFrame() {
    return this.firstNonFadedFrame;
  }

  /**
   * @return Az utolsó nem áttűnő kocka.
   */
  public int getLastNonFadedFrame() {
    return this.lastNonFadedFrame;
  }

  /**
   * @return Az első nem kitartott képkocka.
   */
  public int getFirstNonHeldFrame() {
    return firstNonHeldFrame;
  }

  /**
   * @return Az utolsó nem kitartott képkocka.
   */
  public int getLastNonHeldFrame() {
    return lastNonHeldFrame;
  }

  /**
   * A megadott képkockához a {@link FrameInfo} visszaadása.
   * @param frame A képkocka.
   * @return A képkockához tartozó információkat tartalmazó {@link FrameInfo}.
   */
  public FrameInfo getFrameInfo(int frame) {
    if (frame < 0 || frame >= numberOfFrames) {
      throw new IllegalArgumentException();
    }

    if (frameInfoCache.containsKey(frame)) {
      return frameInfoCache.get(frame);
    }

    double animTime = toAnimTime((double) frame);
    ProgramEntry entry = program.getEntryForAnimTime(animTime);

    double seconds = (double) frame / fps;
    int stepNum = entry.stepNum;
    boolean held = entry.holdType != null;
    double percent = !held ? getPercent(entry.start, entry.end, animTime) : entry.holdType == HoldType.HoldIn ? 0.0 : 100.0;
    boolean faded = false;
    double opacity = 1.0;
    if (frame < firstNonFadedFrame) {
      faded = true;
      opacity = getPercent(0, firstNonFadedFrame - 1, frame) / 100.0 * FADE_RATE;
    } else if (frame > lastNonFadedFrame) {
      faded = true;
      opacity = 1 - getPercent(lastNonFadedFrame + 1, numberOfFrames - 1, frame) / 100.0 * FADE_RATE;
    }

    if (opacity < 0.0) {
      opacity = 0.0;
    } else if (opacity > 1.0) {
      opacity = 1.0;
    }

    assert percent >= 0.0 && percent <= 100.0;
    assert opacity >= 0.0 && opacity <= 1.0;
    assert frame >= firstNonHeldFrame || entry.holdType == HoldType.HoldIn;
    assert frame <= lastNonHeldFrame || entry.holdType == HoldType.HoldOut;

    FrameInfo fi = new FrameInfo(frame, seconds, stepNum, percent, animTime, opacity, faded, held);
    frameInfoCache.put(frame, fi);

    return fi;
  }

  /**
   * A megadott képkocka legenerálása.
   * @param frame A legenerálandó képkocka.
   * @return A legenerált képkocka.
   */
  public BufferedImage generateFrame(int frame) {
    return generateFrame(frame, ap.getWidth(), ap.getHeight());
  }

  /**
   * A megadott képkocka legenerálása a megadott méretben.
   * @param frame A legenerálandó képkocka.
   * @param width A képkocka szélessége.
   * @param height A képkocka magassága.
   * @return A legenerált képkocka.
   */
  public BufferedImage generateFrame(int frame, int width, int height) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, colorModel);
    Dimension d = new Dimension(image.getWidth(), image.getHeight());
    Graphics2D g = image.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);

    Transformer.adjustFL(d, autoCam == null ? ap.getZoom() : 1.0);
    drawFrame(frame, g, d);

    return image;
  }

  private void drawFrame(int frame, Graphics2D g, Dimension d) {
    FrameInfo fi = getFrameInfo(frame);

    g.setColor(Color.WHITE);
    g.fillRect(0, 0, d.width, d.height);

    boolean ac = autoCam != null;

    Matrix cam = new Matrix();
    cam.identity();
    Matrix tmp = new Matrix();
    tmp.identity();
    tmp.rotateY(ac ? autoCam.getTheta(fi) : ap.getTheta());
    cam.postMultiply(tmp);
    tmp.identity();
    tmp.rotateX(ac ? autoCam.getPhi(fi) : ap.getPhi());
    cam.postMultiply(tmp);
    tmp.identity();
    tmp.translate(ac ? autoCam.getTranslateX(fi) : ap.getTranslateX(),
            ac ? autoCam.getTranslateY(fi) : ap.getTranslateY(), 0.0);
    cam.postMultiply(tmp);

    if (fi.getOpacity() < 1.0) {
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) fi.getOpacity()));
    }

    double zoom = ac ? autoCam.getZoom(fi) : ap.getZoom();

    if (ap.isShowGrid()) {
      GridSystemRenderer.render(g, cam, zoom, d);
    }

    g.setColor(Color.BLACK);
    FrameRenderer.renderFrame(fi.getStepNum(), fi.getPercent(), g, cam, zoom, d, (float) fi.getOpacity());

    if (FOOTER_TEXT != null) {
      drawFooterText(g, d, FOOTER_TEXT);
    }
  }

  private static final IndexColorModel colorModel;

  static {
    int size = 256;
    byte[] r = new byte[size];
    byte[] g = new byte[size];
    byte[] b = new byte[size];

    for (int i = 0; i < size; i++) {
      r[i] = g[i] = b[i] = (byte) i;
    }

    colorModel = new IndexColorModel(8, size, r, g, b);
  }

  private static final String FOOTER_TEXT = null;

  private static final Color footerColor = new Color(100, 100, 255, 65);

  private static Font footerFont;

  private static void drawFooterText(Graphics g, Dimension d, String footerText) {
    g = g.create();
    int fontSize = Math.min(d.height, d.width) / 30;
    if (footerFont == null || footerFont.getSize() != fontSize) {
      footerFont = g.getFont().deriveFont(Font.ITALIC, (float) fontSize);
    }
    g.setFont(footerFont);

    FontMetrics fm = g.getFontMetrics();
    int width = fm.stringWidth(footerText);

    g.setColor(footerColor);
    g.drawString(footerText, d.width - width - 5, d.height - 5);
  }

  /**
   * Az animációs időbe (REF_SPEED, REF_FPS) átváltása valós időbe (kockaszám).
   * @param animTime Az animációs idő.
   * @return A valós idő (kockaszámban).
   */
  private double fromAnimTime(double animTime) {
    return animTime * fps * REF_SPEED / REF_FPS / speed;
  }

  private int fromAnimTimeInt(double animTime) {
    return (int) Math.ceil(fromAnimTime(animTime));
  }

  /**
   * A valós idő (kockaszám) átváltása az animációs időbe (REF_SPEED, REF_FPS).
   * @param realTime A valós idő (kockaszámban).
   * @return Az animációs idő.
   */
  private double toAnimTime(double realTime) {
    return realTime * REF_FPS * speed / fps / REF_SPEED;
  }

  private static double getPercent(int start, int end, double value) {
    assert start <= value && value <= end;

    int len = end - start;
    return (value - start) * 100.0 / (double) len;
  }

  private class Program {

    final List<ProgramEntry> entries;

    final int programLength;

    Program() {
      final int firstStep = ap.getFirstStep();
      final int lastStep = ap.getLastStep();
      final int holdInLength = ap.getHoldInLength();
      final int holdOutLength = ap.getHoldOutLength();

      entries = new ArrayList<>(FrameRenderer.MAX_STEP);

      int pos = 0;

      if (holdInLength > 0) {
        entries.add(new ProgramEntry(pos, pos += holdInLength, firstStep, HoldType.HoldIn));
      }

      for (int step = firstStep; step <= lastStep; step++) {
        entries.add(new ProgramEntry(pos, pos += stepsAnimLength[step], step, null));
      }

      if (holdOutLength > 0) {
        entries.add(new ProgramEntry(pos, pos += holdOutLength, lastStep, HoldType.HoldOut));
      }

      // Ellenőrizzük a tömböt
      for (int i = 0; i < entries.size(); i++) {
        ProgramEntry e = entries.get(i);
        assert i == 0 || e.start == entries.get(i - 1).end;
      }

      programLength = pos;
    }

    ProgramEntry getEntryForAnimTime(double animTime) {
      for (ProgramEntry entry : entries) {
        if (entry.start <= animTime && animTime < entry.end) {
          return entry;
        }
      }
      return null;
    }

    ProgramEntry getEntryForRealTime(double realTime) {
      return getEntryForAnimTime(toAnimTime(realTime));
    }
  }

  private static class ProgramEntry {

    /**
     * A kezdet (animációs időben).
     */
    final int start;

    /**
     * A vég (animációs időben).
     */
    final int end;

    /**
     * A bejegyzéshez tartozó lépésszám.
     */
    final int stepNum;

    /**
     * A kitartás típusa, vagy {@code null}, ha a programelem nem kitartás típusú.
     */
    final HoldType holdType;

    int getLength() {
      return end - start;
    }

    ProgramEntry(int start, int end, int stepNum, HoldType holdType) {
      this.start = start;
      this.end = end;
      this.stepNum = stepNum;
      this.holdType = holdType;
    }
  }

  private class AutoCam {

    final private CamPoint[] camPoints = new CamPoint[]{
      new CamPoint(0, 0.0, 0.0, 20.0, 0.0, 0.0, .5),
      new CamPoint(0, 75.0),
      new CamPoint(9, 0.0),
      new CamPoint(10, 0.0, 0.0, 0.0, 0.0, -1.0, 1.25),
      new CamPoint(11, 0.0, 0.0, 0.0, 0.0, -1.0, 1.25),
      new CamPoint(12, 30.0, 0.0, 0.0, 0.0, -1.0, 1.75),
      new CamPoint(15, 50.0, 0.0, 0.0, 0.0, -1.0, 1.75),
      new CamPoint(17, 15.0, 0.0, 0.0, 0.0, 0.0, 1.75),
      new CamPoint(18, 40.0, 6.0, -6.0, 0.0, 0.0, 3.0)
    };

    final int[] stepsCumulatedLength;

    AutoCam() {
      Arrays.sort(camPoints, new Comparator<CamPoint>() {
        @Override
        public int compare(CamPoint cp1, CamPoint cp2) {
          int c = Integer.compare(cp1.step, cp2.step);
          if (c == 0) {
            c = Double.compare(cp1.percent, cp2.percent);
          }
          if (c == 0) {
            throw new IllegalArgumentException();
          }
          return c;
        }
      });
      if (camPoints.length == 0 || camPoints[0].step != 0 || camPoints[0].percent != 0) {
        throw new IllegalArgumentException();
      }
      for (int i = 0; i < camPoints.length - 1; i++) {
        CamPoint cp = camPoints[i];
        cp.next = camPoints[i + 1];
      }

      stepsCumulatedLength = new int[stepsAnimLength.length];
      int length = 0;
      for (int i = 0; i < stepsAnimLength.length; i++) {
        stepsCumulatedLength[i] = length;
        length += stepsAnimLength[i];
      }
    }

    double getTranslateX(FrameInfo fi) {
      return getInterpolatedValue(fi, "tx");
    }

    double getTranslateY(FrameInfo fi) {
      return getInterpolatedValue(fi, "ty");
    }

    double getTheta(FrameInfo fi) {
      return getInterpolatedValue(fi, "th");
    }

    double getPhi(FrameInfo fi) {
      return getInterpolatedValue(fi, "ph");
    }

    double getZoom(FrameInfo fi) {
      return getInterpolatedValue(fi, "zo");
    }

    private CamPoint[] getCamPoints(FrameInfo fi) {
      for (CamPoint cp : camPoints) {
        if (cp.isBefore(fi)
                && (cp.next == null || cp.next.isAfter(fi))) {
          return new CamPoint[]{cp, cp.next == null ? cp : cp.next};
        }
      }
      throw new IllegalStateException();
    }

    private double getInterpolatedValue(FrameInfo fi, String variable) {
      CamPoint[] cps = getCamPoints(fi);
      if (cps[0] == cps[1]) {
        return cps[0].getValue(variable);
      }
      return interpolate(getPos(fi), getPos(cps[0]), getPos(cps[1]),
              cps[0].getValue(variable), cps[1].getValue(variable));
    }

    private int getPos(FrameInfo fi) {
      return getPos(fi.getStepNum(), fi.getPercent());
    }

    private int getPos(CamPoint cp) {
      return getPos(cp.step, cp.percent);
    }

    private int getPos(int step, double percent) {
      return stepsCumulatedLength[step] + (int) (stepsAnimLength[step] * percent / 100.0);
    }

    private double interpolate(int pos, int start, int end, double startValue, double endValue) {
      assert start < end && pos >= start && pos <= end;

      if (startValue == endValue) {
        return startValue;
      }

      double c = endValue - startValue;
      double d = end - start;
      double t = pos - start;

      // Quadratic easing in/out
      t /= d / 2;
      if (t < 1) {
        return c / 2 * t * t + startValue;
      }
      t--;
      return -c / 2 * (t * (t - 2) - 1) + startValue;
    }

    private class CamPoint {

      final int step;

      final double percent;

      final double translateX;

      final double translateY;

      final double theta;

      final double phi;

      final double zoom;

      CamPoint next;

      CamPoint(int step, double percent) {
        this(step, percent, 0.0, 0.0, 0.0, 0.0, 1.0);
      }

      CamPoint(int step, double percent, double translateX, double translateY, double theta, double phi, double zoom) {
        this.step = step;
        this.percent = percent;
        this.translateX = translateX;
        this.translateY = translateY;
        this.theta = theta;
        this.phi = phi;
        this.zoom = zoom;
      }

      boolean isBefore(FrameInfo fi) {
        return fi.getStepNum() > step || (fi.getStepNum() == step && fi.getPercent() >= percent);
      }

      boolean isAfter(FrameInfo fi) {
        return fi.getStepNum() < step || (fi.getStepNum() == step && fi.getPercent() < percent);
      }

      double getValue(String variable) {
        switch (variable) {
          case "tx":
            return translateX;
          case "ty":
            return translateY;
          case "th":
            return theta;
          case "ph":
            return phi;
          case "zo":
            return zoom;
          default:
            throw new AssertionError();
        }
      }
    }
  }

  private static enum HoldType {

    HoldIn,
    HoldOut,
  }
}
