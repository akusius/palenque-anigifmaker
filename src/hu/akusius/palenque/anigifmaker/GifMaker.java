package hu.akusius.palenque.anigifmaker;

import java.io.*;
import javax.swing.SwingWorker;

/**
 * Osztály a teljes GIF-animáció kiírásához.
 * @author Bujdosó Ákos
 */
public class GifMaker {

  /**
   * A GIF legenerálása a megadott célfájlba.
   * @param params A generálási paraméterek.
   * @param dest A célfájl.
   * @throws Exception
   */
  public static void makeGif(GenerateParams params, File dest) throws Exception {
    SwingWorker<Void, Void> sw = makeGifAsyncInternal(params, new BufferedOutputStream(new FileOutputStream(dest)), true);
    sw.get();
  }

  /**
   * A GIF legenerálása a megadott célstream-be.
   * @param params A generálási paraméterek.
   * @param os A célstream.
   * @throws Exception
   */
  public static void makeGif(GenerateParams params, OutputStream os) throws Exception {
    SwingWorker<Void, Void> sw = makeGifAsyncInternal(params, os, false);
    sw.get();
  }

  /**
   * A GIF legenerálásának aszinkron elindítása a megadott célfájlba.
   * @param params A generálási paraméterek.
   * @param dest A célfájl.
   * @return A {@link SwingWorker} a legeneráláshoz.
   * @throws IOException
   */
  public static SwingWorker<Void, Void> makeGifAsync(GenerateParams params, File dest) throws IOException {
    return makeGifAsyncInternal(params, new BufferedOutputStream(new FileOutputStream(dest)), true);
  }

  /**
   * A GIF legenerálásának aszinkron elindítása a megadott célstream-be.
   * @param params A generálási paraméterek.
   * @param os A célstream.
   * @return A {@link SwingWorker} a legeneráláshoz.
   */
  public static SwingWorker<Void, Void> makeGifAsync(GenerateParams params, OutputStream os) {
    return makeGifAsyncInternal(params, os, false);
  }

  private static SwingWorker<Void, Void> makeGifAsyncInternal(final GenerateParams params,
          final OutputStream os, final boolean closeStream) {
    SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        AnimParams ap = params.getAnimParams();
        GifParams gp = params.getGifParams();

        PalenqueGifEncoder encoder = new PalenqueGifEncoder();
        encoder.start(os);
        encoder.setFrameRate((float) ap.getFramesPerSecond());
        if (gp.isRepeat()) {
          encoder.setRepeat(0);
        }
        if (gp.isEmbedParams()) {
          encoder.setComment(params.serializeBinaryASCII());
        }
        encoder.setQuality(gp.getQuality());

        FrameGenerator fg = new FrameGenerator(ap);

        int numberOfFrames = fg.getNumberOfFrames();
        for (int frame = 0; frame < numberOfFrames; frame++) {
          if (isCancelled()) {
            break;
          }
          encoder.addFrame(fg.generateFrame(frame));
          setProgress(frame * 100 / numberOfFrames);
        }
        encoder.finish();
        if (closeStream) {
          os.close();
        }

        return null;
      }
    };

    sw.execute();
    return sw;
  }

  private GifMaker() {
  }
}
