import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Fridge extends Application {
    // Creates Subclass objects
    // ------------------------
    public static Intake intake;
    public static Database db;
    public static StorageManager store;

    // Scenes
    public static Scene main_scene;
    public static Scene intake_menu;
    public static Scene scan_scene;
    public static Scene manual_scene;

    // Constructor
    public Fridge() {
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
        main_scene = new Scene(FXMLLoader.load(getClass().getResource("main_scene.fxml")), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        intake_menu = new Scene(FXMLLoader.load(getClass().getResource("intake_scene.fxml")), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        scan_scene = new Scene(FXMLLoader.load(getClass().getResource("scan_scene.fxml")), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        manual_scene = new Scene(FXMLLoader.load(getClass().getResource("manual_scene.fxml")), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        stage.setScene(main_scene);

        main_scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.DELETE)) {
                intake.getScale().setWeight(10);
            }
        });

        stage.show();
    }

    public void initialize() {
    }
}
