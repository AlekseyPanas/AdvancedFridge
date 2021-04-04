import com.google.zxing.NotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class scan_scene_controller implements Initializable {
    private Timeline cameraTimer;

    public ImageView cameraFeed;
    public Button confirmButton;

    // Is the confirm button currently available (product selected)
    public boolean isConfirmActive = false;

    // Currently selected item from list
    public Product selectedItem = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Creates timeline to update camera feed
        cameraTimer = new Timeline(
                new KeyFrame(Duration.seconds(.1), e -> {
                    try {
                        cameraFeed.setImage(Fridge.intake.getCamera().next());
                    } catch (NotFoundException notFoundException) {
                        notFoundException.printStackTrace();
                    }
                })
        );
        cameraTimer.setCycleCount(Timeline.INDEFINITE);
    }

    // Called by intake scene controller when switching to this scene
    public void onSwitch() {
        // Deactivated Confirm
        deactivateConfirm();
        isConfirmActive = false;

        // Starts timer
        cameraTimer.play();
    }

    // Handles back button
    public void backButtonHandler(ActionEvent event) {
        // Return to intake menu scene
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(Fridge.intake_menu);

        // Stops timer
        cameraTimer.stop();
    }

    // Updates CSS classes to visually deactivate button
    public void deactivateConfirm () {
        confirmButton.getStyleClass().clear();
        confirmButton.getStyleClass().add("confirmButtonGray");
    }

    // Updates CSS classes to visually activate button
    public void activateConfirm () {
        confirmButton.getStyleClass().clear();
        confirmButton.getStyleClass().add("confirmButton");
    }

    // Handles confirm button
    public void confirmButtonHandler(ActionEvent event) {
        // TO SAZZAD: MODIFY THE CONDITION IF NECESSARY
        if (isConfirmActive) {
            // Set selected product of confirm scene
            confirm_scene_controller.selectedProduct = selectedItem;

            // Calls onSwitch for expiration date scene
            ((confirm_scene_controller) Fridge.confirm_scene_loader.getController()).onSwitch();

            // Move to expiration date confirm scene
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(Fridge.confirm_scene);

            // Stops camera
            cameraTimer.stop();
        }

    }
}
