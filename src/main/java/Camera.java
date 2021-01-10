import com.google.zxing.NotFoundException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Camera {
    // Creates manager for OpenCV operations
    private final VideoCapture capture;
    private final Mat matrix;
    private String id;

    public Camera() {
        // Loading the OpenCV core library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        capture = new VideoCapture(0, Videoio.CAP_DSHOW);
        matrix = new Mat();
        id = null;
    }

    public WritableImage next() throws NotFoundException {
        capture.read(matrix);
        if (capture.isOpened()) {
            if (capture.read(matrix)) {
                BufferedImage image = new BufferedImage(matrix.width(), matrix.height(), BufferedImage.TYPE_3BYTE_BGR);
                DataBufferByte dataBuffer = (DataBufferByte) image.getRaster().getDataBuffer();
                matrix.get(0, 0, dataBuffer.getData());
                try {
                    id = VisionManager.decode(image);
                } catch (NotFoundException e) {
                    id = null;
                }
                System.out.println(id);
                return SwingFXUtils.toFXImage(image, null);
            }
        }
        return null;
    }
}