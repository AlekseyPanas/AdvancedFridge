import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Fridge extends Application {
    // Creates Subclass objects
    // ------------------------
    private Intake intake;
    private Database db;
    private StorageManager store;

    // Constructor
    public Fridge() {}

    // Startup method for fridge
    public void runFridge(String[] args) {
        // Launches JavaFX
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Creates class instances
        db = new Database();
        intake = new Intake();
        store = new StorageManager();

        Parent root = FXMLLoader.load(getClass().getResource("main_scene.fxml"));

        // Saves fridge contents upon closing
        stage.setOnCloseRequest(e -> {
            store.saveStorage();
        });

        stage.setScene(new Scene(root, 700, 700));
        stage.show();
    }
}
