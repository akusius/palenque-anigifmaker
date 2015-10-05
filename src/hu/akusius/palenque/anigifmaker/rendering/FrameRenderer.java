package hu.akusius.palenque.anigifmaker.rendering;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import org.other.Matrix;

/**
 * Egy képkocka kirajzolását végző osztály.
 * @author Bujdosó Ákos
 */
public final class FrameRenderer {

  /**
   * A legmagasabb lehetséges lépésszám.
   */
  public static final int MAX_STEP = 21;

  private final int step;

  private final double percent;

  private final Graphics2D graphics;

  private final Matrix transMatrix;

  private final double zoom;

  private final Dimension dim;

  private final float frameAlpha;

  /**
   * Egy képkocka kirajzolása.
   * @param step A kirajzolandó képkocka lépésszáma.
   * @param percent A kirajzolandó képkocka százaléka.
   * @param g A kirajzolás célja.
   * @param transMatrix A transzformációs mátrix.
   * @param zoom A nagyítás mértéke.
   * @param dim A kirajzolás dimenziói.
   * @param frameAlpha A teljes képkocka alfa értéke.
   */
  public static void renderFrame(int step, double percent, Graphics2D g,
          Matrix transMatrix, double zoom, Dimension dim, float frameAlpha) {
    FrameRenderer fr = new FrameRenderer(step, percent, g, transMatrix, zoom, dim, frameAlpha);
    fr.renderFrame();
  }

  private FrameRenderer(int step, double percent, Graphics2D graphics,
          Matrix transMatrix, double zoom, Dimension dim, float frameAlpha) {
    this.step = step;
    this.percent = percent;
    this.graphics = (Graphics2D) graphics.create();
    this.transMatrix = transMatrix;
    this.zoom = zoom;
    this.dim = dim;
    this.frameAlpha = frameAlpha;
  }

  private void renderFrame() {
    assert step >= 0 && step <= MAX_STEP;

    if (step <= 6) {
      renderStep_0_6();
    } else if (step <= 8) {
      renderStep_7_8();
    } else if (step == 9) {
      renderStep_9();
    } else if (step == 10) {
      renderStep_10();
    } else if (step <= 16) {
      renderStep_11_16();
    } else if (step == 17) {
      renderStep_17();
    } else if (step <= 19) {
      renderStep_18_19();
    } else {
      renderStep_20_21();
    }
  }

  private void renderStep_0_6() {
    assert step >= 0 && step <= 6;

    Grid grid = new Grid(51);
    grid.addTriplets(new int[]{-5, 6, -5, -4, 4, -4, -10, 1, 9, 1, -16, 17, 6, 20, 20, 20, 20, 5, -16, -23});
    grid.addItem(Grid.ITEM_SUN, -11, -20);
    grid.addItem(Grid.ITEM_STAR, 20, -2);

    if (step == 0) {
      if (percent < 50.0) {
        LidRenderer.render(transMatrix, graphics, zoom, dim);
      } else if (percent < 90.0) {
        float alpha = (float) (percent - 50f) / 40f;
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (1f - alpha) * frameAlpha));
        LidRenderer.render(transMatrix, graphics, zoom, dim);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * frameAlpha));
        GridRenderer.render(grid, transMatrix, graphics, zoom, dim);
      } else {
        GridRenderer.render(grid, transMatrix, graphics, zoom, dim);
      }
    } else if (step < 4) {
      Grid grRot = grid.cloneGrid();

      if (step >= 2) {
        grid.rotate(step - 1, true);
        grRot.rotate(step - 1, false);
      }
      GridRenderer.render(grid, transMatrix, graphics, zoom, dim);

      if (percent >= 20.0) {
        Matrix m = new Matrix();
        Matrix.identity(m);
        m.rotateZ(-Math.PI / 2.0 / 100.0 * (percent - 20.0) * 1.25);
        m.postMultiply(this.transMatrix);
        GridRenderer.render(grRot, m, graphics, zoom, dim);
      }
    } else if (step == 4) {
      // Várakozás
      grid.rotate(3, true);
      GridRenderer.render(grid, transMatrix, graphics, zoom, dim);
    } else if (step == 5) {
      Matrix m = new Matrix();
      Matrix.identity(m);
      m.rotateY(Math.PI / 100.0 * percent);
      grid.rotate(3, true);
      GridRenderer.render(grid, transMatrix, graphics, zoom, dim);
      m.postMultiply(this.transMatrix);
      GridRenderer.render(grid, m, graphics, zoom, dim);
    } else if (step == 6) {
      grid.octuple();
      GridRenderer.render(grid, transMatrix, graphics, zoom, dim);
    }
  }

  private void renderStep_7_8() {
    assert step >= 7 && step <= 8;

    Grid g11 = new Grid(15, 17, 17);
    g11.addItems(Grid.ITEM_SQUARE, new int[]{
      16, 16, 17, 16, 18, 16, 22, 16, 23, 16, 24, 16,
      16, 17, 16, 18, 16, 22, 16, 23, 16, 24, 19, 20, 20, 20, 21, 20, 20, 19, 20, 21
    });
    g11.addItems(Grid.ITEM_SUN, new int[]{20, 11, 11, 20});
    Grid g12 = new Grid(5, 5, 5);
    g12.addItems(Grid.ITEM_SQUARE, new int[]{
      5, 7, 5, 6, 5, 5, 5, 4, 5, 3, 7, 5, 6, 5, 4, 5, 3, 5, 4, 4, 4, 3, 3, 4
    });

    Matrix m = new Matrix();
    Matrix.identity(m);

    if (step == 7) {
      m.translate(-.02 * percent, -.02 * percent, 0.);
    } else {
      m.translate(-2, -2, 0.);
    }
    renderGridsRotated(m, g11, g12);

    Grid g2 = new Grid(15, 15, 0);
    g2.addItems(Grid.ITEM_SQUARE, new int[]{
      19, 6, 20, 6, 21, 6, 20, 5, 20, 4,
      19, -6, 20, -6, 21, -6, 20, -5, 20, -4,
      9, -2, 9, -1, 9, 0, 9, 1, 9, 2,
      10, -2, 10, -1, 10, 0, 10, 1, 10, 2
    });
    g2.addItems(Grid.ITEM_STAR, new int[]{20, 2, 20, -2});

    Matrix.identity(m);

    if (step == 8) {
      m.translate(-0.05 * percent, 0., 0.);
    }
    renderGridsRotated(m, g2);
  }

  private void renderStep_9() {
    assert step == 9;

    Grid g1 = new Grid(39, 0, 0);
    g1.addItems(Grid.ITEM_SQUARE, new int[]{
      2, 1, 2, 2, 2, 3, 3, 1, 3, 2, 3, 3, 4, 0, 4, 1, 4, 2, 4, 3, 5, 0, 5, 1, 5, 2, 5, 3,
      2, -1, 2, -2, 2, -3, 3, -1, 3, -2, 3, -3, 4, -1, 4, -2, 4, -3, 5, -1, 5, -2, 5, -3,
      15, 4, 15, 5, 15, 6, 14, 6, 16, 6,
      15, -4, 15, -5, 15, -6, 14, -6, 16, -6
    });
    g1.addItems(Grid.ITEM_STAR, new int[]{15, 2, 15, -2});
    g1.addItems(Grid.ITEM_SUN, new int[]{18, 9, 18, -9});
    g1.setCenter(0, 0);
    g1.rotate(3, true);
    GridRenderer.render(g1, transMatrix, graphics, zoom, dim);

    float alpha = 1f - (float) percent / 100f;
    alpha *= alpha;
    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * frameAlpha));

    Grid g2 = new Grid(9, 0, 0);
    g2.addItems(Grid.ITEM_SQUARE, new int[]{
      0, 0, 0, 1, 0, -1, -1, 0, 1, 0,
      -4, 4, -4, 3, -4, 2, -4, -2, -4, -3, -4, -4,
      -3, -4, -2, -4, 2, -4, 3, -4, 4, -4
    });
    g2.setCenter(18, 18);
    renderGridsRotated(null, g2);
  }

  private void renderStep_10() {
    assert step == 10;

    Grid g1 = new Grid(3, 2, 2);
    g1.addItems(Grid.ITEM_SQUARE, new int[]{
      1, 2, 1, 3, 2, 1, 2, 2, 2, 3, 3, 1, 3, 2, 3, 3
    });
    renderGridsRotated(null, g1);

    Matrix m = new Matrix();

    Grid g2 = new Grid(7, 4, 0);
    g2.addItems(Grid.ITEM_SQUARE, new int[]{
      4, -3, 4, -2, 4, -1, 4, 0, 4, 1, 4, 2, 4, 3,
      5, -3, 5, -2, 5, -1, 5, 0, 5, 1, 5, 2, 5, 3
    });

    g2.setCenter(0, 0);

    Matrix.identity(m);
    m.translate(3.5, 0, 0.0);
    m.rotateY(-Math.PI / 100.0 * percent);
    m.translate(0.5, 0, 0.0);
    renderGridsRotated(m, g2);

    Grid g3 = new Grid(21, 10, 0);
    g3.addItems(Grid.ITEM_SQUARE, new int[]{
      15, 4, 15, 5, 15, 6, 14, 6, 16, 6,
      15, -4, 15, -5, 15, -6, 14, -6, 16, -6
    });
    g3.addItems(Grid.ITEM_STAR, new int[]{15, 2, 15, -2});
    g3.addItems(Grid.ITEM_SUN, new int[]{18, 9, 18, -9});

    g3.setCenter(-4, 0);
    Matrix.identity(m);
    m.translate(13.5, 0, 0.0);
    m.rotateY(-Math.PI / 100.0 * percent);
    m.translate(0.5, 0, 0.0);
    renderGridsRotated(m, g3);
  }

  private void renderStep_11_16() {
    assert step >= 11 && step <= 16;

    Grid g1 = new Grid(3, 0, 0);
    g1.addItem(Grid.ITEM_SUN, 0, 0);
    g1.setCenter(9, 9);
    renderGridsRotated(null, g1);

    Grid g2h = new Grid(13, 0, 0);
    g2h.addItems(Grid.ITEM_SQUARE, new int[]{
      -4, 0, -5, 0, -6, -1, -6, 0, -6, 1,
      4, 0, 5, 0, 6, -1, 6, 0, 6, 1
    });
    g2h.addItems(Grid.ITEM_STAR, new int[]{-2, 0, 2, 0});
    Grid g2v = g2h.cloneGrid();
    g2v.rotate(1, false);

    Grid gt = new Grid(21);

    Matrix m = new Matrix();

    if (step == 11) {
      // Külső
      g2h.setCenter(0, 12);
      renderGridsRotated(null, g2h);

      // Belső
      Grid g3 = new Grid(7, 0, 0);
      g3.addItems(Grid.ITEM_SQUARE, new int[]{
        2, 1, 2, 0, 2, -1, 2, -2, 2, -3,
        3, 1, 3, 0, 3, -1, 3, -2, 3, -3
      });
      g3.rotate(3, true);
      GridRenderer.render(g3, transMatrix, graphics, zoom, dim);
    } else if (step == 12) {  // Felső
      // Külső
      g2h.setCenter(0, 3);
      Matrix.identity(m);
      m.translate(0, 9, 0.0);
      m.rotateX(Math.PI / 100.0 * percent);
      m.postMultiply(transMatrix);
      GridRenderer.render(g2h, m, graphics, zoom, dim);

      g2v.setCenter(12, 0);
      GridRenderer.render(g2v, transMatrix, graphics, zoom, dim);

      g2h.setCenter(0, -12);
      GridRenderer.render(g2h, transMatrix, graphics, zoom, dim);

      g2v.setCenter(-12, 0);
      GridRenderer.render(g2v, transMatrix, graphics, zoom, dim);

      // Belső
      Grid g = new Grid(7, 0, 0);
      g.addItems(Grid.ITEM_SQUARE, new int[]{
        -3, 2, -2, 2, -1, 2, 0, 2, 1, 2, 2, 2, 3, 2,
        -3, 3, -2, 3, -1, 3, 0, 3, 1, 3, 2, 3, 3, 3
      });
      g.setCenter(0, -2);
      Matrix.identity(m);
      m.translate(0, 1.5, 0.0);
      m.rotateX(Math.PI / 100.0 * percent);
      m.translate(0, 0.5, 0.0);
      m.postMultiply(transMatrix);
      GridRenderer.render(g, m, graphics, zoom, dim);

      g = new Grid(7, 0, 0);
      g.addItems(Grid.ITEM_SQUARE, new int[]{
        2, 1, 2, 0, 2, -1, 2, -2, 2, -3,
        3, 1, 3, 0, 3, -1, 3, -2, 3, -3
      });
      g.rotate(1, true);
      g.addItems(Grid.ITEM_SQUARE, new int[]{
        -2, 1, -2, 0, -2, -1,
        -3, 1, -3, 0, -3, -1,});
      GridRenderer.render(g, transMatrix, graphics, zoom, dim);
    } else if (step == 13) {  // Jobb
      // Külső
      g2h.setCenter(0, 6);
      GridRenderer.render(g2h, transMatrix, graphics, zoom, dim);

      g2v.setCenter(3, 0);
      Matrix.identity(m);
      m.translate(9, 0, 0.0);
      m.rotateY(-Math.PI / 100.0 * percent);
      m.postMultiply(transMatrix);
      GridRenderer.render(g2v, m, graphics, zoom, dim);

      g2h.setCenter(0, -12);
      GridRenderer.render(g2h, transMatrix, graphics, zoom, dim);

      g2v.setCenter(-12, 0);
      GridRenderer.render(g2v, transMatrix, graphics, zoom, dim);

      // Belső
      Grid g = new Grid(5, 0, 0);
      g.addItems(Grid.ITEM_SQUARE, new int[]{
        1, -2, 1, -1, 1, 0, 1, 1, 1, 2,
        2, -2, 2, -1, 2, 0, 2, 1, 2, 2
      });
      g.setCenter(-1, -1);
      Matrix.identity(m);
      m.translate(1.5, 0.0, 0.0);
      m.rotateY(-Math.PI / 100.0 * percent);
      m.translate(0.5, 0.0, 0.0);
      m.postMultiply(transMatrix);
      GridRenderer.render(g, m, graphics, zoom, dim);

      g = new Grid(5, 0, 0);
      g.addItems(Grid.ITEM_SQUARE, new int[]{
        -2, -1, -1, -1, 0, -1, 1, -1, 2, -1,
        -2, -2, -1, -2, 0, -2, 1, -2, 2, -2
      });
      g.rotate(2, true);
      g.setCenter(-1, -1);
      GridRenderer.render(g, transMatrix, graphics, zoom, dim);
    } else if (step == 14) {  // Alsó
      // Külső
      g2h.setCenter(0, 6);
      gt.merge(g2h);
      gt.rotate(1, true);
      GridRenderer.render(gt, transMatrix, graphics, zoom, dim);

      g2h.setCenter(0, -3);
      Matrix.identity(m);
      m.translate(0, -9, 0.0);
      m.rotateX(-Math.PI / 100.0 * percent);
      m.postMultiply(transMatrix);
      GridRenderer.render(g2h, m, graphics, zoom, dim);

      g2v.setCenter(-12, 0);
      GridRenderer.render(g2v, transMatrix, graphics, zoom, dim);

      // Belső
      Grid g = new Grid(5, 0, 0);
      g.addItems(Grid.ITEM_SQUARE, new int[]{
        -2, -1, -1, -1, 0, -1, 1, -1, 2, -1,
        -2, 0, -1, 0, 0, 0, 1, 0, 2, 0
      });
      g.setCenter(-1, -1);
      Matrix.identity(m);
      m.translate(0, -1.5, 0.0);
      m.rotateX(-Math.PI / 100.0 * percent);
      m.translate(0, 0.5, 0.0);
      m.postMultiply(transMatrix);
      GridRenderer.render(g, m, graphics, zoom, dim);

      g = new Grid(5, 0, 0);
      g.addItems(Grid.ITEM_SQUARE, new int[]{
        -2, -1, -1, -1, 1, -1, 2, -1,
        -2, 0, -1, 0, 0, 0, 1, 0, 2, 0,
        -2, 1, -1, 1, 0, 1, 1, 1, 2, 1
      });
      g.setCenter(-1, 0);
      GridRenderer.render(g, transMatrix, graphics, zoom, dim);
    } else if (step == 15) {  // Bal
      // Külső
      g2h.setCenter(0, 6);
      gt.merge(g2h);
      gt.rotate(2, true);
      GridRenderer.render(gt, transMatrix, graphics, zoom, dim);

      g2v.setCenter(-3, 0);
      Matrix.identity(m);
      m.translate(-9, 0, 0.0);
      m.rotateY(Math.PI / 100.0 * percent);
      m.postMultiply(transMatrix);
      GridRenderer.render(g2v, m, graphics, zoom, dim);

      // Belső
      Grid g = new Grid(3, 0, 0);
      g.addItems(Grid.ITEM_SQUARE, new int[]{
        -1, -1, 0, -1,
        -1, 0, 0, 0,
        -1, 1, 0, 1
      });
      g.setCenter(-1, 0);
      Matrix.identity(m);
      m.translate(-1.5, 0.0, 0.0);
      m.rotateY(Math.PI / 100.0 * percent);
      m.translate(0.5, 0.0, 0.0);
      m.postMultiply(transMatrix);
      GridRenderer.render(g, m, graphics, zoom, dim);

      g = new Grid(3, 0, 0);
      g.addItems(Grid.ITEM_SQUARE, new int[]{
        -1, -1, 0, -1, 1, -1,
        -1, 0, 0, 0, 1, 0,
        -1, 1, 0, 1, 1, 1
      });
      GridRenderer.render(g, transMatrix, graphics, zoom, dim);
    } else if (step == 16) {
      // Külső
      g2h.setCenter(0, 6);
      gt.merge(g2h);
      gt.rotate(3, true);
      GridRenderer.render(gt, transMatrix, graphics, zoom, dim);

      // Belső
      Grid g = new Grid(3, 0, 0);
      g.addItems(Grid.ITEM_SQUARE, new int[]{
        -1, -1, 0, -1, 1, -1,
        -1, 0, 0, 0, 1, 0,
        -1, 1, 0, 1, 1, 1
      });
      GridRenderer.render(g, transMatrix, graphics, zoom, dim);
    }
  }

  private void renderStep_17() {
    assert step == 17;

    Grid g1 = new Grid(11);
    g1.addItems(Grid.ITEM_SQUARE, new int[]{
      -1, 0, 0, 0, 1, 0, 2, 0,
      0, -2, 0, -1, 0, 1
    });
    g1.addItem(Grid.ITEM_SUN, -3, 3);
    g1.addItems(Grid.ITEM_STAR, new int[]{0, -4, 4, 0});

    g1.setCenter(-6, 6);
    GridRenderer.render(g1, transMatrix, graphics, zoom, dim);

    Grid g2 = new Grid(21);
    g2.merge(g1);
    g2.rotate(1, false);
    g2.rotate(2, true);
    g2.addItems(Grid.ITEM_SQUARE, new int[]{
      -1, -1, 0, -1, 1, -1,
      -1, 0, 0, 0, 1, 0,
      -1, 1, 0, 1, 1, 1
    });

    float alpha = 1f - (float) percent / 100f;
    alpha *= alpha * alpha * alpha;
    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * frameAlpha));

    GridRenderer.render(g2, transMatrix, graphics, zoom, dim);
  }

  private void renderStep_18_19() {
    assert step >= 18 && step <= 19;

    Grid g1 = new Grid(11);
    g1.addItem(Grid.ITEM_SUN, -3, 3);
    g1.addItems(Grid.ITEM_STAR, new int[]{0, -4, 4, 0});

    g1.setCenter(-6, 6);
    GridRenderer.render(g1, transMatrix, graphics, zoom, dim);

    if (step == 18) {
      Grid g2 = new Grid(5);
      g2.addItems(Grid.ITEM_SQUARE, new int[]{
        -1, 0, 0, 0, 1, 0, 2, 0,
        0, -2, 0, -1, 0, 1
      });
      g2.setCenter(-6, 6);

      float alpha = 1f - (float) percent / 100f;
      graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * frameAlpha));

      GridRenderer.render(g2, transMatrix, graphics, zoom, dim);

      graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (1f - alpha) * frameAlpha));
    }

    // Vonalak
    Matrix m = new Matrix();
    m.identity();
    m.postMultiply(transMatrix);

    drawLineCellToCell(-7, 6, -4, 6, m);
    drawCellMarker(-7, 6, true, m);
    drawCellMarker(-6, 6, true, m);
    drawCellMarker(-4, 6, true, m);
    drawLineCellToCell(-6, 7, -6, 4, m);
    drawCellMarker(-6, 7, false, m);
    drawCellMarker(-6, 6, false, m);
    drawCellMarker(-6, 4, false, m);
  }

  private void renderStep_20_21() {
    assert step >= 20 && step <= 21;

    Grid g1 = new Grid(3);
    g1.addItem(Grid.ITEM_SUN, 0, 0);
    g1.setCenter(-9, 9);
    GridRenderer.render(g1, transMatrix, graphics, zoom, dim);

    Grid g2 = new Grid(3);
    g2.addItem(Grid.ITEM_STAR, 0, 0);
    g2.setCenter(4, 0);

    double angle = Math.PI / 4.0;
    if (step == 20) {
      angle *= percent / 100.0;
    }

    Matrix m = new Matrix();

    for (int i = 1; i <= 2; i++) {
      m.identity();
      m.translate(-6.0, 6.0, 0);
      m.rotateZ(i == 1 ? -angle : angle - Math.PI / 2.0);
      m.postMultiply(transMatrix);

      GridRenderer.render(g2, m, graphics, zoom, dim);
      drawLineCellToCell(-1, 0, 2, 0, m);
      drawCellMarker(-1, 0, true, m);
      drawCellMarker(0, 0, true, m);
      drawCellMarker(2, 0, true, m);
    }
  }

  private void drawLineCellToCell(int x1, int y1, int x2, int y2, Matrix ftm) {
    double[] c1 = Transformer.getCenter(x1, y1);
    double[] c2 = Transformer.getCenter(x2, y2);

    c1 = Transformer.transform(c1, ftm);
    c2 = Transformer.transform(c2, ftm);

    int[] p1 = Transformer.project(c1, zoom, dim);
    int[] p2 = Transformer.project(c2, zoom, dim);

    if (p1 != null && p2 != null) {
      graphics.drawLine(p1[0], p1[1], p2[0], p2[1]);
    }
  }

  private void drawCellMarker(int x, int y, boolean vertical, Matrix ftm) {
    double[] c = Transformer.getCenter(x, y);
    double hl = Transformer.CELL_SIZE / 4d;
    double[][] lp = vertical
            ? new double[][]{
              {c[0], c[1] - hl, 0.0d},
              {c[0], c[1] + hl, 0.0d}
            } : new double[][]{
              {c[0] - hl, c[1], 0.0d},
              {c[0] + hl, c[1], 0.0d}
            };

    lp = Transformer.transform(lp, ftm);

    int[][] ps = Transformer.project(lp, zoom, dim);

    if (ps != null) {
      graphics.drawLine(ps[0][0], ps[0][1], ps[1][0], ps[1][1]);
    }
  }

  private void renderGridsRotated(Matrix tm, Grid... grids) {
    for (int i = 0; i < 4; i++) {
      Matrix m1 = new Matrix();
      Matrix.identity(m1);
      m1.rotateZ(Math.PI / 2.0 * i);

      Matrix m2 = new Matrix();
      if (tm != null) {
        m2.copy(tm);
      } else {
        Matrix.identity(m2);
      }
      m2.postMultiply(m1);
      m2.postMultiply(transMatrix);

      for (Grid grid : grids) {
        GridRenderer.render(grid, m2, graphics, zoom, dim);
      }
    }
  }

}
