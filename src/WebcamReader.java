import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;

public class WebcamReader {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final VideoCapture camera = new VideoCapture(0);

    public static BufferedImage captureFrame() {
        if (!camera.isOpened()) {
            System.out.println("Failed to open webcam.");
            return null;
        }

        Mat frame = new Mat();
        boolean success = camera.read(frame);

        if (success && !frame.empty()) {
            return matToBufferedImage(frame);
        } else {
            System.out.println("Failed to capture frame.");
            return null;
        }
    }

    public static void releaseCamera() {
        if (camera.isOpened()) {
            camera.release();
        }
    }

    private static BufferedImage matToBufferedImage(Mat mat) {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, mob);
        byte[] ba = mob.toArray();
        try {
            return ImageIO.read(new ByteArrayInputStream(ba));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
