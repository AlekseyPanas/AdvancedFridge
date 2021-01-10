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

    // Constructor
    public Fridge() {
        Intake intake = new Intake();
        Database db = new Database();
    }

    // Main method for fridge
    public void runFridge(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main_scene.fxml"));
        stage.setScene(new Scene(root, 700, 700));
        stage.show();
    }
}
