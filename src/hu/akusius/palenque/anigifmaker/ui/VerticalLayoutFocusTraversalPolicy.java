package hu.akusius.palenque.anigifmaker.ui;

import java.awt.Component;
import java.awt.Window;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.LayoutFocusTraversalPolicy;

/**
 * Olyan {@link LayoutFocusTraversalPolicy}, amelyik függőlegesen veszi az elemeket.
 * @author Bujdosó Ákos
 */
public class VerticalLayoutFocusTraversalPolicy extends LayoutFocusTraversalPolicy {

  /**
   *
   */
  public VerticalLayoutFocusTraversalPolicy() {
    super();
    setComparator(new VerticalComponentComparator());
  }

  /**
   * Összehasonlító a vezérlőelemek függőleges végigjárásához.
   * Kód átvéve a {@link LayoutComparator}-ból.
   * Azért kellett így csinálni, mert a {@link ComponentOrientation} jelenleg nem kezeli a nem horizontális elrendezést.
   * @author Bujdosó Ákos
   */
  private static class VerticalComponentComparator implements Comparator<Component> {

    private static final int ROW_TOLERANCE = 100;

    @Override
    @SuppressWarnings("AssignmentToMethodParameter")
    public int compare(Component a, Component b) {
      if (a == b) {
        return 0;
      }

      // Row/Column algorithm only applies to siblings. If 'a' and 'b'
      // aren't siblings, then we need to find their most inferior
      // ancestors which share a parent. Compute the ancestory lists for
      // each Component and then search from the Window down until the
      // hierarchy branches.
      if (a.getParent() != b.getParent()) {
        LinkedList<Component> aAncestory = new LinkedList<>();

        for (; a != null; a = a.getParent()) {
          aAncestory.add(a);
          if (a instanceof Window) {
            break;
          }
        }
        if (a == null) {
          // 'a' is not part of a Window hierarchy. Can't cope.
          throw new ClassCastException();
        }

        LinkedList<Component> bAncestory = new LinkedList<>();

        for (; b != null; b = b.getParent()) {
          bAncestory.add(b);
          if (b instanceof Window) {
            break;
          }
        }
        if (b == null) {
          // 'b' is not part of a Window hierarchy. Can't cope.
          throw new ClassCastException();
        }

        for (ListIterator<Component> aIter = aAncestory.listIterator(aAncestory.size()),
                bIter = bAncestory.listIterator(bAncestory.size());;) {
          if (aIter.hasPrevious()) {
            a = aIter.previous();
          } else {
            // a is an ancestor of b
            return -1;
          }

          if (bIter.hasPrevious()) {
            b = bIter.previous();
          } else {
            // b is an ancestor of a
            return 1;
          }

          if (a != b) {
            break;
          }
        }
      }

      int ax = a.getX(), ay = a.getY(), bx = b.getX(), by = b.getY();

      int zOrder = a.getParent().getComponentZOrder(a) - b.getParent().getComponentZOrder(b);

      // TL - Mongolian
      if (Math.abs(ax - bx) < ROW_TOLERANCE) {
        return (ay < by) ? -1 : ((ay > by) ? 1 : zOrder);
      } else {
        return (ax < bx) ? -1 : 1;
      }
    }
  }
}
