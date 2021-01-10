import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class Intake extends Application {
    // Creates sensors
    private final Camera rCamera;
    private final Scale rScale;

    public Intake() {
        rCamera = new Camera();
        rScale = new Scale();
    }

    public void doLaunch(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Setting the image view
        ImageView imageView = new ImageView(rCamera.next());

        // setting the fit height and width of the image view
        imageView.setFitHeight(400);
        imageView.setFitWidth(600);

        // Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);

        // Creating a Group object
        Group root = new Group(imageView);

        // Creating a Scene object
        Scene scene = new Scene(root, 600, 400);

        // Setting title of the Stage
        stage.setTitle("Video Capture");

        // Adding scene to the stage
        stage.setScene(scene);

        // Displaying the contents of the stage
        stage.show();
    }
}