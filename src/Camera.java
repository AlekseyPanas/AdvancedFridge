import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;

public class Camera extends Application {
    // Creates manager for OpenCV operations
    // private final VideoCapture capture;

    Mat matrix = null;

    public Camera() {
        // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // capture = new VideoCapture();
    }

    public void doLaunch (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Capturing the snapshot from the camera
        Camera camera = new Camera();
        WritableImage writableImage = camera.captureSnapShot();

        // Saving the image
        camera.saveImage();

        // Setting the image view
        ImageView imageView = new ImageView(writableImage);

        // setting the fit height and width of the image view
        imageView.setFitHeight(400);
        imageView.setFitWidth(600);

        // Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);

        // Creating a Group object
        Group root = new Group(imageView);

        // Creating a scene object
        Scene scene = new Scene(root, 600, 400);

        // Setting title to the Stage
        stage.setTitle("Capturing an image");

        // Adding scene to the stage
        stage.setScene(scene);

        // Displaying the contents of the stage
        stage.show();
    }

    public WritableImage captureSnapShot() {
        WritableImage WritableImage = null;

        // Loading the OpenCV core library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Instantiating the VideoCapture class (camera:: 0)
        VideoCapture capture = new VideoCapture(0);

        // Reading the next video frame from the camera
        Mat matrix = new Mat();
        capture.read(matrix);

        // If camera is opened
        if (capture.isOpened()) {
            // If there is next video frame
            if (capture.read(matrix)) {
                // Creating BufferedImage from the matrix
                BufferedImage image = new BufferedImage(matrix.width(),
                        matrix.height(), BufferedImage.TYPE_3BYTE_BGR);

                WritableRaster raster = image.getRaster();
                DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
                byte[] data = dataBuffer.getData();
                matrix.get(0, 0, data);
                this.matrix = matrix;

                // Creating the Writable Image
                WritableImage = SwingFXUtils.toFXImage(image, null);
            }
        }
        return WritableImage;
    }

    public void saveImage() {
        // Saving the Image
        String file = "C:/Users/<user>/Desktop/snapshot.jpg";

        // Instantiating the Imgcodecs class
        Imgcodecs imgcodecs = new Imgcodecs();

        // Saving it again
        Imgcodecs.imwrite(file, matrix);
    }

    // public static void main(String[] args) {
    // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    // VideoCapture capture = new VideoCapture(0);
    // Mat matrix = new Mat();
    // capture.read(matrix);
    // }
}
