import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.time.LocalDate;
import java.time.Period;

import static java.lang.Math.abs;

public class GUIutils {

    // Gets the hour difference between now and desired date
    public static int getDateDifference(LocalDate date) {
        LocalDate now = LocalDate.now();
        return Period.between(now, date).getDays() * 24;
    }

    // Gets new Product Node
    public static Node createListItemNode(ActualProduct product) {
        int timeToExpire = getDateDifference(product.expiration);
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
            String[] id_and_date = takeButton.getStyleClass().get(2).split("\\.");
            String[] splitDate = id_and_date[1].split("-");
            LocalDate expireDate = LocalDate.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]));

            Fridge.store.remove(Integer.parseInt(id_and_date[0]), expireDate);
            Main.fridge.populateProductList(Main.fridge.searchBar.getText());
            Main.fridge.populateExpireList();
        });

        mainContainer.getChildren().addAll(prodTitle, prodExpire, takeButton);

        return mainContainer;
    }

    // Returns a populated HBox element row
    public static Node createExpireNode(ActualProduct product) {
        int timeToExpire = getDateDifference(product.expiration);
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
            Main.fridge.runTakeButton(takeButton.getStyleClass().get(2).split("\\."));
        });

        mainContainer.getChildren().addAll(prodTitle, prodExpire, takeButton);

        return mainContainer;
    }

}
