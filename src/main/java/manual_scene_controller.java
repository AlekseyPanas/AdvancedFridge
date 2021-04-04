import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class manual_scene_controller implements Initializable {

    public ListView dbSearchList;
    public TextField dbSearchBar;
    public Button confirmButton;

    private ArrayList<Product> searchedProducts;

    // Is the confirm button currently available (product selected)
    public boolean isConfirmActive = false;

    // Amount of list items showing
    public int listLength = 0;

    // Currently selected item from list
    public Product selectedItem = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("MANUAL INIT CALLED");

        // Calls onSwitch to clear the scene and prep it
        onSwitch();

        // Adds list selected item listener
        dbSearchList.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Product>) (observable,
                                                                                                       oldValue,
                                                                                                       newValue) -> {
            // Toggles confirm button state
            if (newValue != null) {
                activateConfirm();

            } else if (listLength > 0) {
                deactivateConfirm();
            }

            // Updates selectedItem with newly selected item (null if not selected)
            selectedItem = newValue;
        });
    }

    // Called by intake scene controller when switching to this scene
    public void onSwitch() {
        // Deactivated Confirm
        deactivateConfirm();

        // Clears selected item
        selectedItem = null;

        // Fills list initially
        populateList("");

        // Clears text
        dbSearchBar.setText("");

        // Resets button text
        confirmButton.setText("Confirm");
    }

    // Handles back button
    public void backButtonHandler(ActionEvent event) {
        // Return to intake menu scene
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(Fridge.intake_menu);
    }

    // Handles confirm button
    public void confirmButtonHandler(ActionEvent event) {
        // If confirm is active
        if (isConfirmActive) {

            boolean proceed = false;

            // Selected list item
            if (selectedItem != null) {
                // Set selected product of confirm scene
                confirm_scene_controller.selectedProduct = selectedItem;
                proceed = true;

            // Custom item entered
            } else if (listLength <= 0 && !dbSearchBar.getText().equals("")) {
                // Set custom selected product of confirm scene
                confirm_scene_controller.selectedProduct = new Product(
                        Fridge.store.getNextCustomID(),
                        "N/A",
                        dbSearchBar.getText().strip().toUpperCase(Locale.ROOT),
                        true);
                proceed = true;
            }

            if (proceed) {
                // Calls onSwitch for expiration date scene
                ((confirm_scene_controller) Fridge.confirm_scene_loader.getController()).onSwitch();

                // Move to expiration date confirm scene
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(Fridge.confirm_scene);
            }

        }
    }

    // Updates CSS classes to visually deactivate button
    public void deactivateConfirm () {
        confirmButton.getStyleClass().clear();
        confirmButton.getStyleClass().add("confirmButtonGray");
        isConfirmActive = false;
    }

    // Updates CSS classes to visually activate button
    public void activateConfirm () {
        confirmButton.getStyleClass().clear();
        confirmButton.getStyleClass().add("confirmButton");
        isConfirmActive = true;
    }

    // Repopulates the listview
    public void populateList(String searchString) {
        // Removes all list items
        dbSearchList.getItems().clear();

        // Retrieves all products that fit the search string and saved them
        ArrayList<Product> searchedProducts = Fridge.db.searchSelect(searchString);

        // Updates size
        listLength = searchedProducts.size();

        // Adds product to search list (toString method of product allows it to appear in list)
        for (int i = 0; i < searchedProducts.size(); i++) {
            dbSearchList.getItems().add(searchedProducts.get(i));
        }
    }

    // Runs when changes are made to the search bar text
    public void searchFunction() {
        // Updates list
        populateList(dbSearchBar.getText());

        // Activates button if entered search text yields no results (allows for custom entry)
        if (listLength <= 0 && !dbSearchBar.getText().equals("")) {
            confirmButton.setText("Use Custom");
            activateConfirm();
        } else if (confirmButton.getText().equals("Use Custom")) {
            confirmButton.setText("Confirm");
            deactivateConfirm();
        }

        System.out.println(isConfirmActive);
        System.out.println(selectedItem == null ? "null": selectedItem.product_name);
    }
}
