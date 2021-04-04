import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

public class confirm_scene_controller implements Initializable {

    // Populated by manual/scan scenes
    public static Product selectedProduct;

    public DatePicker expirationPicker;
    public Button confirmButton;
    public Text titleText;

    public HBox quantityBox;
    public TextField quantityField = new TextField();

    // Is the add button currently available (date selected)
    public boolean isConfirmActive = false;

    // Is the date field and or text field properly filled in
    public boolean isDateFilled = false;
    public boolean isQuantityFilled = false;

    LocalDate selectedDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        quantityField.setPromptText("Quantity of Item");

        // Adds onKeyTyped event to quantityField
        quantityField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                // if valid number
                if (quantityField.getText().matches("^[0-9]*$") && !(quantityField.getText().equals(""))) {
                    isQuantityFilled = true;

                    if (isDateFilled) {
                        isConfirmActive = true;
                        activateConfirm();
                    }
                } else {
                    isQuantityFilled = false;
                    deactivateConfirm();
                    isConfirmActive = false;
                }
            }
        });

        // Adds textfield listener for numbers only
        quantityField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { // When focus lost
                if(!quantityField.getText().matches("^[0-9]*$")){
                    // When entry not a number, Set the textField empty
                    quantityField.setText("");
                }
            }
        });
    }

    // Called when switching to this scene
    public void onSwitch () {
        quantityBox.getChildren().clear();
        quantityField.setText("");

        // Checks if quantity is a necessary field
        if (selectedProduct.isQuantifiable) {
            quantityBox.getChildren().add(quantityField);
        } else {
            quantityBox.getChildren().clear();
        }

        // Resets date
        expirationPicker.setValue(null);

        // Deactivates "Add Product" button
        isDateFilled = false;
        isQuantityFilled = false;
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
            if (selectedProduct.isQuantifiable) {
                Fridge.store.add(selectedProduct.ID, selectedDate, Integer.parseInt(quantityField.getText()));
            } else {
                Fridge.store.add(selectedProduct.ID, selectedDate, -1);
            }

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
        isDateFilled = true;
        selectedDate = expirationPicker.getValue();

        // If the quantity field is not needed or if its already filled
        if (isQuantityFilled || !selectedProduct.isQuantifiable) {
            isConfirmActive = true;
            activateConfirm();
        }
    }

}
