package hu.akusius.palenque.anigifmaker;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.OutputStream;
import org.other.AnimatedGifEncoder;

/**
 * A {@link AnimatedGifEncoder} kiterjesztése és egyszerűsítése.
 * Indexelt képeket vár, és feltételezi, hogy mindegyiknek azonos a színmodellje.
 * Az egymás utáni képeket nem optimalizálja, ehhez külön programot érdemes használni (pl. gifsicle).
 * @author Bujdosó Ákos
 * @see http://www.lcdf.org/gifsicle/
 */
public final class PalenqueGifEncoder extends AnimatedGifEncoder {

  public static final String COMMENT_ENCODING = "UTF-8";

  private String comment;

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  protected void writeExtensions() throws IOException {
    if (comment != null && !comment.isEmpty()) {
      writeCommentExt(comment, out);
    }
  }

  /**
   * Egy kommentblokk kiírása.
   * @param comment A kiírandó komment.
   * @param os A kiírás célja.
   * @throws java.io.IOException Hiba történt a kiírás során.
   */
  private void writeCommentExt(String comment, OutputStream os) throws IOException {
    byte[] data = comment.getBytes(COMMENT_ENCODING);

    os.write(0x21); // Extension Introducer
    os.write(0xFE); // Comment Label

    int off = 0;
    while (off < data.length) {
      int len = Math.min(255, data.length - off);

      os.write(len);
      os.write(data, off, len);
      off += len;
    }

    os.write(0);  // Block Terminator
  }

  @Override
  protected void getImagePixels() {
    int w = image.getWidth();
    int h = image.getHeight();
    int type = image.getType();
    if (w != width || h != height || type != BufferedImage.TYPE_BYTE_INDEXED) {
      throw new RuntimeException("Invalid frame!");
    }
    pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
  }

  @Override
  protected void analyzePixels() {
    if (firstFrame) {
      int colorNum = 256;
      colorTab = new byte[colorNum * 3];
      IndexColorModel cm = (IndexColorModel) image.getColorModel();
      if (cm.getPixelSize() != 8 || cm.getMapSize() != colorNum) {
        throw new RuntimeException("Invalid palette!");
      }
      for (int pixel = 0; pixel < colorNum; pixel++) {
        int index = pixel * 3;
        colorTab[index] = (byte) cm.getRed(pixel);
        colorTab[index + 1] = (byte) cm.getGreen(pixel);
        colorTab[index + 2] = (byte) cm.getBlue(pixel);
      }
    }

    indexedPixels = pixels;
    pixels = null;
    colorDepth = 8;
    palSize = 7;
    if (transparent != null) {
      throw new RuntimeException("Invalid frame!");
    }
  }

  @Override
  protected void writeImageDesc() throws IOException {
    boolean ff = firstFrame;
    try {
      firstFrame = true;  // Nincs LCT
      super.writeImageDesc();
    } finally {
      firstFrame = ff;
    }
  }

  @Override
  protected void writePalette() throws IOException {
    if (!firstFrame) {
      // Nincs Local Color Table (csak Global)
      return;
    }
    super.writePalette();
  }
}
