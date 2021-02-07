import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class intake_scene_controller implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void StartScan(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(Fridge.scan_scene);
    }
}
