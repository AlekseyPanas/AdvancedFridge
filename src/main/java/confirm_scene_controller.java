import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class confirm_scene_controller implements Initializable {

    // Populated by manual/scan scenes
    public static Product selectedProduct;

    public DatePicker expirationPicker;
    public Button confirmButton;
    public Text titleText;

    // Is the add button currently available (date selected)
    public boolean isConfirmActive = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    // Called when switching to this scene
    public void onSwitch () {
        // Resets date
        expirationPicker.setValue(null);

        // Deactivates "Add Product" button
        deactivateConfirm();
        isConfirmActive = false;

        // Sets title text
        titleText.setText("Enter the Expiration Date for " + selectedProduct.product_name.toUpperCase(Locale.ROOT));
    }

    // Handles cancel button
    public void backButtonHandler(ActionEvent event) {
        // Return to intake menu scene
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(Fridge.intake_menu);
    }

    // Handles "Add Product" button
    public void confirmButtonHandler(ActionEvent event) {
        if (isConfirmActive) {
            // Removes weight from scale
            Fridge.intake.getScale().setWeight(0);

            // Adds product
            Fridge.store.add(selectedProduct.ID, expirationPicker.getValue());

            // Calls onSwitch and switches to main scene
            ((main_scene_controller) Fridge.main_scene_loader.getController()).onSwitch();
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(Fridge.main_scene);
        }
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

    public void onDateSelected(ActionEvent event) {
        isConfirmActive = true;
        activateConfirm();
    }

}
