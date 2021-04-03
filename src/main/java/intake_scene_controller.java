import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class intake_scene_controller implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void StartManual(ActionEvent event) {
        // Calls switch method in manual scene
        ((manual_scene_controller) Fridge.manual_scene_loader.getController()).onSwitch();

        // Switches Scene
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(Fridge.manual_scene);
    }

    public void StartScan(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(Fridge.scan_scene);
    }

    public void backButton(ActionEvent event) {
        Fridge.intake.getScale().setWeight(0);

        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(Fridge.main_scene);
    }
}
