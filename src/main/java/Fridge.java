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

import java.sql.SQLException;
import java.time.LocalDate;

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
        System.out.println("hello");
        // Clears container
        allProductsContainer.getChildren().clear();

        // Retrieves storage
        LocalDate[] dates = store.getDates();
        int[] ids = store.getIds();

        for (int i = 0; i < ids.length; i++) {
            // Gets product from ID
            Product prod = db.getItemFromID(ids[i]);
            // Adds entry with product name and calculated time to expiration
            allProductsContainer.getChildren().add(createListItemNode(prod.product_name,
                    getDateDifference(dates[i])));
        }

    }

    // ======================================

    // TODO -- Make function return difference in dates
    // Gets the date different between now and desired date
    private LocalDate getDateDifference (LocalDate date) {
        LocalDate now = LocalDate.now();
        return now;
    }

    // TODO -- Fix Expiration dates
    private Node createListItemNode (String productName, LocalDate expireDate) {
        HBox mainContainer = new HBox();
        mainContainer.getStyleClass().add("exItemContainer");

        Font davidFont = new Font("David", 20.0);
        Font systemFont = new Font("System Bold", 12.0);

        Label prodTitle = new Label(productName);
        prodTitle.getStyleClass().add("exTitle");
        prodTitle.setFont(davidFont);

        Label prodExpire = new Label("(0d)");
        prodExpire.setPrefWidth(34.0);
        prodExpire.setTextAlignment(TextAlignment.CENTER);

        Button takeButton = new Button();
        takeButton.getStyleClass().add("takeButton");
        takeButton.setFont(systemFont);
        takeButton.setText("Take");

        mainContainer.getChildren().addAll(prodTitle, prodExpire, takeButton);

        return mainContainer;
    }

    // Returns a populated HBox element row
    private Node createExpireNode (String productName, LocalDate expireDate) {
        HBox mainContainer = new HBox();
        mainContainer.getStyleClass().add("exItemContainer");

        Font davidFont = new Font("David", 20.0);
        Font systemFont = new Font("System Bold", 12.0);

        Label prodTitle = new Label(productName);
        prodTitle.getStyleClass().add("exTitle");
        prodTitle.setFont(davidFont);

        Label prodExpire = new Label("(0d)");
        prodExpire.getStyleClass().add("exExpire");
        prodExpire.setFont(davidFont);

        Button takeButton = new Button();
        takeButton.getStyleClass().add("takeButton");
        takeButton.setFont(systemFont);
        takeButton.setText("Take");

        mainContainer.getChildren().addAll(prodTitle, prodExpire, takeButton);

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

        stage.setScene(new Scene(root, 700, 700));
        stage.show();
    }
}
