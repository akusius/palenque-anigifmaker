package hu.akusius.palenque.anigifmaker.ui;

import hu.akusius.palenque.anigifmaker.FrameGenerator;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Panel egy képkocka megjelenítéséhez.
 * @author Bujdosó Ákos
 */
public class FramePanel extends JPanel {

  private boolean drawFrameNum = true;

  /**
   * @return {@code true}, ha rajzolja rá a kockaszámot is.
   */
  public boolean isDrawFrameNum() {
    return drawFrameNum;
  }

  /**
   * @param drawFrameNum {@code true}, ha rajzolja rá a kockaszámot is.
   */
  public void setDrawFrameNum(boolean drawFrameNum) {
    this.drawFrameNum = drawFrameNum;
  }

  private BufferedImage image;

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (image != null) {
      g.drawImage(image, 0, 0, null);
    }
  }

  /**
   * Egy képkocka megjelenítése.
   * @param fg
   * @param frame
   */
  public void drawFrame(FrameGenerator fg, int frame) {
    try {
      int width = this.getWidth();
      int height = this.getHeight();
      image = fg.generateFrame(frame, width, height);
      if (drawFrameNum) {
        String text = String.format("%d", frame);
        Graphics g = image.getGraphics();
        FontMetrics fm = g.getFontMetrics();
        int stringWidth = fm.stringWidth(text);
        int stringHeight = fm.getHeight();
        g.setColor(new Color(125, 125, 125, 125));
        g.drawString(text, width - stringWidth - 5, stringHeight);
      }
      repaint();
    } catch (Exception e) {
      clearFrame();
      throw e;
    }
  }

  /**
   * A képkocka törlése.
   */
  public void clearFrame() {
    image = null;
    repaint();
  }
}
