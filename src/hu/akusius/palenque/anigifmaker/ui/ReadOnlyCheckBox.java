package hu.akusius.palenque.anigifmaker.ui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;

/**
 *
 * @author Bujdosó Ákos
 */
public class ReadOnlyCheckBox extends JCheckBox {

  /**
   *
   */
  public ReadOnlyCheckBox() {
  }

  /**
   *
   * @param text
   * @param selected
   */
  public ReadOnlyCheckBox(String text, boolean selected) {
    super(text, selected);
  }

  @Override
  protected void processKeyEvent(KeyEvent e) {
  }

  @Override
  protected void processMouseEvent(MouseEvent e) {
  }
}
