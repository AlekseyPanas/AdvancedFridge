import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

public class Camera {
    // Creates manager for OpenCV operations
    private final VideoCapture capture;
    private final Mat matrix;

    public Camera() {
        // Loading the OpenCV core library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        capture = new VideoCapture(0);
        matrix = new Mat();
    }

    public WritableImage next() {
        capture.read(matrix);
        if (capture.isOpened()) {
            if (capture.read(matrix)) {
                BufferedImage image = new BufferedImage(matrix.width(), matrix.height(), BufferedImage.TYPE_3BYTE_BGR);
                WritableRaster raster = image.getRaster();
                DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
                matrix.get(0, 0, dataBuffer.getData());
                return SwingFXUtils.toFXImage(image, null);
            }
        }
        return null;
    }
}