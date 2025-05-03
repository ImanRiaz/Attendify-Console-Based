
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;

public class WebcamReader {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static BufferedImage captureFrame() {
        VideoCapture camera = new VideoCapture(0);
        Mat frame = new Mat();

        if (camera.isOpened()) {
            camera.read(frame);
            if (!frame.empty()) {
                camera.release();
                return matToBufferedImage(frame);
            }
        }

        camera.release();
        return null;
    }

    public static BufferedImage matToBufferedImage(Mat mat) {
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
