import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;

public class Fridge extends Application {
    // Creates Subclass objects
    // ------------------------
    //private Intake intake;
    private Database db;
    private StorageManager store;

    public VBox expireContainer = new VBox();

    // Constructor
    public Fridge() {}

    // EVENT HANDLERS
    // ======================================

    // Search field event
    public void searchFunction () {
        System.out.println("hello");

        expireContainer.getChildren().add(createExpireNode("Test1", LocalDate.now()));
    }

    // ======================================

    // Returns a populated HBox element row
    private Node createExpireNode (String productName, LocalDate expireDate) {
        HBox mainContainer = new HBox();
        mainContainer.getStyleClass().add("exItemContainer");

        Font davidFont = new Font("David", 20.0);
        Font systemFont = new Font("System Bold", 12.0);

        Label prodTitle = new Label(productName);
        prodTitle.getStyleClass().add("exTitle");
        prodTitle.setFont(davidFont);

        Label prodExpire = new Label("0d");
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

        Parent root = FXMLLoader.load(getClass().getResource("main_scene.fxml"));

        // Saves fridge contents upon closing
        stage.setOnCloseRequest(e -> {/*e.consume();*/ store.saveStorage();});

        stage.setScene(new Scene(root, 700, 700));
        stage.show();
    }
}
