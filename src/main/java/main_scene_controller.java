import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static java.lang.Math.abs;

public class main_scene_controller implements Initializable {

    // FXML element pointers
    public VBox expireContainer;
    public VBox allProductsContainer;
    public TextField searchBar;

    // Timelines
    private Timeline expireTimer;
    private Timeline pingScaleTimer;

    // EVENT HANDLERS
    // ======================================

    // Search field event
    public void searchFunction() {
        populateProductList(searchBar.getText());
    }

    public void runTakeButton(String[] id_and_date) {
        String[] splitDate = id_and_date[1].split("-");
        LocalDate expireDate = LocalDate.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]));

        Fridge.store.remove(Integer.parseInt(id_and_date[0]), expireDate);
        populateProductList(searchBar.getText());
        populateExpireList();
    }

    // ======================================

    // Populates product list
    public void populateProductList(String searchString) {
        // Clears container
        allProductsContainer.getChildren().clear();

        // Retrieves storage
        ActualProduct[] actualProducts = searchString.equals("") ? Fridge.store.getActualProducts() :
                Fridge.store.getSearchedActualProducts(searchString);

        for (ActualProduct prod : actualProducts) {
            // Adds entry with product
            allProductsContainer.getChildren().add(createListItemNode(prod));
            //expireContainer.getChildren().add(createExpireNode(prod));
        }
    }

    // Populates expire widget
    public void populateExpireList() {
        // Clears container
        expireContainer.getChildren().clear();

        // Retrieves expiring products
        ActualProduct[] expiringProducts = Fridge.store.getExpiringProducts(Constants.MAX_DAYS_UNTIL_EXPIRE);

        for (ActualProduct prod : expiringProducts) {
            // Adds entry with product
            expireContainer.getChildren().add(createExpireNode(prod));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("INIT CALLED");
        System.out.println(expireContainer);

        // Populates products
        populateProductList("");
        populateExpireList();

        // Sets timeline to update expiration list
        expireTimer = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                    populateExpireList();
                })
        );
        expireTimer.setCycleCount(Timeline.INDEFINITE);
        expireTimer.play();

        pingScaleTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    if (Fridge.intake.getScale().getWeight() > 0) {
                        Stage stage = ((Stage) (Fridge.main_scene.getWindow()));
                        if (stage != null) stage.setScene(Fridge.intake_menu);
                    }
                })
        );
        pingScaleTimer.setCycleCount(Timeline.INDEFINITE);
        pingScaleTimer.play();
    }

    // Gets new Product Node
    public Node createListItemNode(ActualProduct product) {
        int timeToExpire = StorageManager.getDateDifference(product.expiration);
        String expireMessage = (abs(timeToExpire) > 24) ? ("(" + (timeToExpire / 24) + "d)") : ("(" + timeToExpire + "h)");

        HBox mainContainer = new HBox();
        mainContainer.getStyleClass().add("exItemContainer");

        Font davidFont = new Font("David", 15.0);
        Font systemFont = new Font("System Bold", 12.0);

        Label prodTitle = new Label(product.product_name);
        prodTitle.getStyleClass().add("pdTitle");
        prodTitle.setFont(davidFont);

        Label prodExpire = new Label(expireMessage);
        prodExpire.getStyleClass().add("pdTitle");
        prodExpire.setTextAlignment(TextAlignment.CENTER);

        Button takeButton = new Button();
        takeButton.getStyleClass().add("takeButton");
        takeButton.setFont(systemFont);
        takeButton.setText("Take");

        takeButton.getStyleClass().add(product.ID + "." + product.expiration.toString());
        takeButton.setOnAction(e -> {
            runTakeButton(takeButton.getStyleClass().get(2).split("\\."));
        });

        mainContainer.getChildren().addAll(prodTitle, prodExpire, takeButton);

        return mainContainer;
    }

    // Returns a populated HBox element row
    public Node createExpireNode(ActualProduct product) {
        int timeToExpire = StorageManager.getDateDifference(product.expiration);
        String expireMessage = (abs(timeToExpire) > 24) ? ("(" + (timeToExpire / 24) + "d)") : ("(" + timeToExpire + "h)");

        String color;
        if (timeToExpire < 3) {
            color = "red";
        } else if (timeToExpire < 7) {
            color = "orange";
        } else if (timeToExpire < 11) {
            color = "yellow";
        } else {
            color = "green";
        }

        HBox mainContainer = new HBox();
        mainContainer.getStyleClass().add("exItemContainer");

        Font davidFont = new Font("David", 15.0);
        Font systemFont = new Font("System Bold", 12.0);

        Label prodTitle = new Label(product.product_name);
        prodTitle.getStyleClass().add("exTitle");
        prodTitle.setFont(davidFont);

        Label prodExpire = new Label(expireMessage);
        prodExpire.getStyleClass().add("exExpire");
        prodExpire.setStyle("-fx-text-fill: " + color);
        prodExpire.setFont(davidFont);

        Button takeButton = new Button();
        takeButton.getStyleClass().add("takeButton");
        takeButton.setFont(systemFont);
        takeButton.setText("Take");

        takeButton.getStyleClass().add(product.ID + "." + product.expiration.toString());
        takeButton.setOnAction(e -> {
            runTakeButton(takeButton.getStyleClass().get(2).split("\\."));
        });

        mainContainer.getChildren().addAll(prodTitle, prodExpire, takeButton);

        return mainContainer;
    }
}
