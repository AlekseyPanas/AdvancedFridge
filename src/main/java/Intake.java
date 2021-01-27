import com.google.zxing.NotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Intake {
    // Creates sensors
    private final Camera rCamera;
    private final Scale rScale;

    public Intake() {
        rCamera = new Camera();
        rScale = new Scale();
    }

    public Camera getCamera() {
        return rCamera;
    }

    public Scene getScene() throws NotFoundException {
        ImageView imageView = new ImageView(rCamera.next());

        // Creates timeline to update camera feed
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(.1), e -> {
                    try {
                        imageView.setImage(rCamera.next());
                    } catch (NotFoundException notFoundException) {
                        notFoundException.printStackTrace();
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // setting the fit height and width of the image view
        imageView.setFitHeight(400);
        imageView.setFitWidth(600);

        // Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);

        // Creating a Group object
        Group root = new Group(imageView);

        // Creating a Scene object
        return new Scene(root, 600, 400);
    }

    // public void doLaunch(String[] args) {
    //     launch(args);
    // }

    // @Override
    // public void start(Stage stage) throws IOException, NotFoundException {
    //     // Setting the image view
    //     ImageView imageView = new ImageView(rCamera.next());
    //
    //     // Creates timeline to update camera feed
    //     Timeline timeline = new Timeline(
    //             new KeyFrame(Duration.seconds(.1), e -> {
    //                 try {
    //                     imageView.setImage(rCamera.next());
    //                 } catch (NotFoundException notFoundException) {
    //                     notFoundException.printStackTrace();
    //                 }
    //             })
    //     );
    //     timeline.setCycleCount(Timeline.INDEFINITE);
    //     timeline.play();
    //
    //     // setting the fit height and width of the image view
    //     imageView.setFitHeight(400);
    //     imageView.setFitWidth(600);
    //
    //     // Setting the preserve ratio of the image view
    //     imageView.setPreserveRatio(true);
    //
    //     // Creating a Group object
    //     Group root = new Group(imageView);
    //
    //     // Creating a Scene object
    //     Scene scene = new Scene(root, 600, 400);
    //
    //     // Setting title of the Stage
    //     stage.setTitle("Video Capture");
    //
    //     // Adding scene to the stage
    //     stage.setScene(scene);
    //
    //     stage.setOnCloseRequest(e -> {rCamera.releaseCapture();});
    //
    //     // Displaying the contents of the stage
    //     stage.show();
    // }
}
