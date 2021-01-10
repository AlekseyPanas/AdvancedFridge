import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Fridge extends Application {
    // Creates Subclass objects
    // ------------------------
    private Intake intake = new Intake();
    private Database db = new Database();

    // Constructor
    public Fridge() {
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
