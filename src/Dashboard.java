import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Dashboard extends Application {

    // Scene Components
    Button button;

    public Dashboard () {}

    public void doLaunch (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Started");

        // Scene Components
        button = new Button("Example");

        // Layout for scene
        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        // Scene object
        Scene scene = new Scene(layout, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

        stage.setScene(scene);
        stage.show();

        System.out.println("Ended");
    }

}
