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
    public static FXMLLoader main_scene_loader;
    public static Scene main_scene;

    public static FXMLLoader intake_menu_loader;
    public static Scene intake_menu;

    public static FXMLLoader scan_scene_loader;
    public static Scene scan_scene;

    public static FXMLLoader manual_scene_loader;
    public static Scene manual_scene;

    public static FXMLLoader confirm_scene_loader;
    public static Scene confirm_scene;

    // Constructor
    public Fridge() { }

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

        // Creates scenes and loaders
        main_scene_loader = new FXMLLoader(getClass().getResource("main_scene.fxml"));
        main_scene = new Scene(main_scene_loader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        intake_menu_loader = new FXMLLoader(getClass().getResource("intake_scene.fxml"));
        intake_menu = new Scene(intake_menu_loader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        scan_scene_loader = new FXMLLoader(getClass().getResource("scan_scene.fxml"));
        scan_scene = new Scene(scan_scene_loader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        manual_scene_loader = new FXMLLoader(getClass().getResource("manual_scene.fxml"));
        manual_scene = new Scene(manual_scene_loader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        confirm_scene_loader = new FXMLLoader(getClass().getResource("confirm_scene.fxml"));
        confirm_scene = new Scene(confirm_scene_loader.load(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

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
