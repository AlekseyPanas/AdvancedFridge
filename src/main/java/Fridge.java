import com.google.zxing.NotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import static java.lang.Math.abs;
import static java.lang.Math.exp;

public class Fridge extends Application {
    // Creates Subclass objects
    // ------------------------
    private Intake intake;
    public static Database db;
    public static StorageManager store;

    // Scenes
    private Scene main_scene;
    private Scene intake_menu;

    // MOVE TO INTAKE SCENE CONTROLLER
    private Timeline cameraTimer;

    // Constructor
    public Fridge() {
    }

    public Scene TEMPgetscene() throws NotFoundException {
        ImageView imageView = new ImageView(intake.getCamera().next());

        // Creates timeline to update camera feed
        cameraTimer = new Timeline(
                new KeyFrame(Duration.seconds(.1), e -> {
                    try {
                        imageView.setImage(intake.getCamera().next());
                    } catch (NotFoundException notFoundException) {
                        notFoundException.printStackTrace();
                    }
                })
        );
        cameraTimer.setCycleCount(Timeline.INDEFINITE);

        // setting the fit height and width of the image view
        imageView.setFitHeight(Constants.SCENE_HEIGHT);
        imageView.setFitWidth(Constants.SCENE_WIDTH);

        // Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);

        // Creating a Group object
        Group root = new Group(imageView);

        // Creating a Scene object
        return new Scene(root, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
    }

    // Startup method for fridge
    public void runFridge(String[] args) {
        System.out.println("LAUNCH CALLED");

        // Launches JavaFX
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("START CALLED");

        // Creates class instances
        db = new Database();
        intake = new Intake();
        store = new StorageManager();

        Parent root = FXMLLoader.load(getClass().getResource("main_scene.fxml"));

        // Saves fridge contents upon closing and closes DB
        stage.setOnCloseRequest(e -> {
            try {
                db.closeDB();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            store.saveStorage();
            intake.getCamera().releaseCapture();
        });

        // Creates scenes
        main_scene = new Scene(root, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        intake_menu = TEMPgetscene();

        stage.setScene(main_scene);

        intake_menu.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                stage.setScene(main_scene);
                cameraTimer.stop();
            }
        });

        main_scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                stage.setScene(intake_menu);
                cameraTimer.play();
            }
        });

        stage.show();
    }

    public void initialize() { }
}
