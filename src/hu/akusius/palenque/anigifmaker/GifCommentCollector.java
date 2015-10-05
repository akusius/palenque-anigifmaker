package hu.akusius.palenque.anigifmaker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Osztály egy GIF-fájl kommentjeinek a kigyűjtéséhez.
 * @author Bujdosó Ákos
 */
public final class GifCommentCollector {

  /**
   * A megadott fájlból a kommentek összegyűjtése.
   * @param source A GIF-fájl útvonala.
   * @return A kigyűjtött kommentek listája.
   * @throws java.io.IOException Hiba történt a beolvasás során.
   */
  public static List<String> collectComments(Path source) throws IOException {
    try (InputStream is = new BufferedInputStream(Files.newInputStream(source))) {
      return collectComments(is);
    }
  }

  /**
   * A megadott forrásból a kommentek összegyűjtése.
   * Lezárja a stream-et a futás végén.
   * @param is A forrás.
   * @return A kigyűjtött kommentek listája.
   * @throws java.io.IOException Hiba történt a beolvasás során.
   */
  public static List<String> collectComments(InputStream is) throws IOException {
    List<String> result = new ArrayList<>(1);

    try (GifStream gs = new GifStream(is)) {
      gs.processHeader();
      gs.processLSD();
      while (true) {
        int code = gs.read();
        if (code == 0x3B) {
          break;
        } else if (code == 0x2C) {
          gs.processImage();
        } else if (code == 0x21) {
          code = gs.read();
          if (code == 0xF9) {
            gs.processGCE();
          } else if (code == 0x01) {
            gs.processPTE();
          } else if (code == 0xFF) {
            gs.processAE();
          } else if (code == 0xFE) {
            byte[] commentBytes = gs.processComment();
            String comment = new String(commentBytes, PalenqueGifEncoder.COMMENT_ENCODING);
            result.add(comment);
          } else {
            throw new IOException("Invalid format!");
          }
        } else {
          throw new IOException("Invalid format!");
        }
      }
    }

    return result;
  }

  private GifCommentCollector() {
  }

  private static class GifStream extends FilterInputStream {

    GifStream(InputStream in) {
      super(in);
    }

    private final byte[] header = new byte[]{'G', 'I', 'F', '8', '9', 'a'};

    public void processHeader() throws IOException {
      for (int i = 0; i < header.length; i++) {
        if (read() != header[i]) {
          throw new IOException("Invalid format!");
        }
      }
    }

    public void processLSD() throws IOException {
      skip(4);
      int packed = read();
      skip(2);

      boolean hasGCT = (packed & 0x80) != 0;
      if (hasGCT) {
        int size = 3 * (1 << ((packed & 0x07) + 1));
        skip(size); // Global Color Table
      }
    }

    public void processImage() throws IOException {
      skip(8);
      int packed = read();
      boolean hasLCT = (packed & 0x80) != 0;
      if (hasLCT) {
        int size = 3 * (1 << ((packed & 0x07) + 1));
        skip(size); // Local Color Table
      }
      read(); // LZW Minimum Code Size
      skipData();
    }

    public void processGCE() throws IOException {
      if (skipData() != 6) {
        throw new IOException("Invalid format!");
      }
    }

    public void processPTE() throws IOException {
      skipData();
    }

    public void processAE() throws IOException {
      skipData();
    }

    public byte[] processComment() throws IOException {
      return readData();
    }

    public byte[] readData() throws IOException {
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream(1000)) {
        while (true) {
          int size = read();  // Block Size
          if (size == 0) {
            return bos.toByteArray();
          }
          for (; size > 0; size--) {
            bos.write(read());
          }
        }
      }
    }

    public long skipData() throws IOException {
      long skipped = 0;

      while (true) {
        int size = read();  // Block Size
        skipped++;
        if (size == 0) {
          return skipped;
        }
        skipped += skip(size);
      }
    }

    @Override
    public int read() throws IOException {
      int val = super.read();
      if (val < 0) {
        throw new IOException("Read error!");
      }
      return val;
    }

    @Override
    public long skip(long n) throws IOException {
      long skipped = 0;
      while (skipped < n) {
        long sk = super.skip(n - skipped);
        if (sk == 0) {
          throw new IOException("Skip error!");
        }
        skipped += sk;
      }
      return n;
    }
  }
}
