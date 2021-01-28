import com.google.zxing.NotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import static java.lang.Math.abs;
import static java.lang.Math.exp;

public class Fridge extends Application {
    // Creates Subclass objects
    // ------------------------
    private Intake intake;
    public static Database db;
    public static StorageManager store;

    // FXML element pointers
    public VBox expireContainer;
    public VBox allProductsContainer;
    public TextField searchBar;

    // Scenes
    private Scene main_scene;
    private Scene intake_menu;

    // Timelines
    private Timeline cameraTimer;
    private Timeline expireTimer;

    // Constructor
    public Fridge() {
    }

    // EVENT HANDLERS
    // ======================================

    // Search field event
    public void searchFunction() {
        populateProductList(searchBar.getText());
    }

    // ======================================

    //
    public void runTakeButton(String[] id_and_date) {
        String[] splitDate = id_and_date[1].split("-");
        LocalDate expireDate = LocalDate.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]));

        store.remove(Integer.parseInt(id_and_date[0]), expireDate);
        populateProductList(searchBar.getText());
        populateExpireList();
    }

    // Populates product list
    public void populateProductList(String searchString) {
        // Clears container
        allProductsContainer.getChildren().clear();

        // Retrieves storage
        ActualProduct[] actualProducts = searchString.equals("") ? store.getActualProducts() :
                store.getSearchedActualProducts(searchString);

        for (ActualProduct prod : actualProducts) {
            // Adds entry with product
            allProductsContainer.getChildren().add(GUIutils.createListItemNode(prod));
            //expireContainer.getChildren().add(createExpireNode(prod));
        }
    }

    // Populates expire widget
    public void populateExpireList() {
        // Clears container
        expireContainer.getChildren().clear();

        // Retrieves expiring products
        ActualProduct[] expiringProducts = store.getExpiringProducts(Constants.MAX_DAYS_UNTIL_EXPIRE);

        for (ActualProduct prod : expiringProducts) {
            // Adds entry with product
            expireContainer.getChildren().add(GUIutils.createExpireNode(prod));
        }
    }

    public Scene TEMPgetscene() throws NotFoundException {
        ImageView imageView = new ImageView(intake.getCamera().next());

        // Creates timeline to update camera feed
        cameraTimer = new Timeline(
                new KeyFrame(Duration.seconds(.1), e -> {
                    try {
                        imageView.setImage(intake.getCamera().next());
                    } catch (NotFoundException notFoundException) {
                        notFoundException.printStackTrace();
                    }
                })
        );
        cameraTimer.setCycleCount(Timeline.INDEFINITE);

        // setting the fit height and width of the image view
        imageView.setFitHeight(Constants.SCENE_HEIGHT);
        imageView.setFitWidth(Constants.SCENE_WIDTH);

        // Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);

        // Creating a Group object
        Group root = new Group(imageView);

        // Creating a Scene object
        return new Scene(root, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
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
        intake = new Intake();
        store = new StorageManager();

        Parent root = FXMLLoader.load(getClass().getResource("main_scene.fxml"));

        // Saves fridge contents upon closing and closes DB
        stage.setOnCloseRequest(e -> {
            try {
                db.closeDB();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            store.saveStorage();
            intake.getCamera().releaseCapture();
        });

        // Creates scenes
        main_scene = new Scene(root, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        intake_menu = TEMPgetscene();

        stage.setScene(main_scene);

        intake_menu.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                stage.setScene(main_scene);
                cameraTimer.stop();
                expireTimer.play();
            }
        });

        main_scene.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                stage.setScene(intake_menu);
                cameraTimer.play();
                expireTimer.stop();
            }
        });

        stage.show();
    }

    public void initialize() {
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

        // Initializes pointers
        expireContainer = new VBox();
        allProductsContainer = new VBox();
        searchBar = new TextField();
    }
}
