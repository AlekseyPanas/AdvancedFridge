import com.google.zxing.NotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.*;

import static java.lang.Math.*;

public class Fridge extends Application {
    // Creates Subclass objects
    // ------------------------
    //private Intake intake;
    public static Database db;
    public static StorageManager store;

    public VBox expireContainer;
    public VBox allProductsContainer;
    public TextField searchBar;

    // Constructor
    public Fridge() {}

    // EVENT HANDLERS
    // ======================================

    // Search field event
    public void searchFunction () {
        populateProductList(searchBar.getText());
    }

    // ======================================

    // Populates product list
    private void populateProductList (String searchString) {
        // Clears container
        allProductsContainer.getChildren().clear();

        // Retrieves storage
        ActualProduct[] actualProducts = searchString.equals("") ? store.getActualProducts():
                store.getSearchedActualProducts(searchString);

        for (ActualProduct prod: actualProducts) {
            // Adds entry with product
            allProductsContainer.getChildren().add(createListItemNode(prod));
            //expireContainer.getChildren().add(createExpireNode(prod));
        }
    }

    private void populateExpireList () {
        // Clears container
        System.out.println(expireContainer.getChildren());
        expireContainer.getChildren().clear();

        // Retrieves expiring products
        ActualProduct[] expiringProducts = store.getExpiringProducts(Constants.MAX_DAYS_UNTIL_EXPIRE);
        System.out.println(expiringProducts.length);

        for (ActualProduct prod: expiringProducts) {
            System.out.println(prod);
            // Adds entry with product
            expireContainer.getChildren().add(createExpireNode(prod));
        }
    }

    // Gets the hour difference between now and desired date
    public static int getDateDifference (LocalDate date) {
        LocalDate now = LocalDate.now();
        return Period.between(now, date).getDays() * 24;
    }

    // Gets Product Node
    private Node createListItemNode (ActualProduct product) {
        int timeToExpire = getDateDifference(product.expiration);
        String expireMessage = (abs(timeToExpire) > 24) ? ("(" + (timeToExpire / 24) + "d)"): ("(" + timeToExpire + "h)");

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

        mainContainer.getChildren().addAll(prodTitle, prodExpire, takeButton);

        mainContainer.getStyleClass().add(product.ID + "." + product.expiration.toString());

        return mainContainer;
    }

    // Returns a populated HBox element row
    private Node createExpireNode (ActualProduct product) {
        int timeToExpire = getDateDifference(product.expiration);
        String expireMessage = (abs(timeToExpire) > 24) ? ("(" + (timeToExpire / 24) + "d)"): ("(" + timeToExpire + "h)");

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

        mainContainer.getChildren().addAll(prodTitle, prodExpire, takeButton);

        mainContainer.getStyleClass().add(product.ID + "." + product.expiration.toString());

        return mainContainer;
    }

    // Startup method for fridge
    public void runFridge(String[] args) {
        // Launches JavaFX
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Creates class instances
        db = new Database();
        //intake = new Intake();
        store = new StorageManager();

        // Initializes pointers
        expireContainer = new VBox();
        allProductsContainer = new VBox();
        searchBar = new TextField();

        Parent root = FXMLLoader.load(getClass().getResource("main_scene.fxml"));

        // Saves fridge contents upon closing and closes DB
        stage.setOnCloseRequest(e -> {
            try {
                db.closeDB();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            store.saveStorage();
        });

        // Sets timeline to update expiration list
        // TODO; Fix timeline. Function runs but products not added
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                    populateExpireList();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Starts stage
        stage.setScene(new Scene(root, 700, 700));
        stage.show();
    }

    public void initialize() {
        // Populates products
        populateProductList("");
    }
}
