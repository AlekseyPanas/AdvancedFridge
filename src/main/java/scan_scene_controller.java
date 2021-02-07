import com.google.zxing.NotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class scan_scene_controller implements Initializable {
    private Timeline cameraTimer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Creates timeline to update camera feed
        cameraTimer = new Timeline(
                new KeyFrame(Duration.seconds(.1), e -> {
                    try {
                        ImageView imageView = null;
                        imageView.setImage(Fridge.intake.getCamera().next());
                    } catch (NotFoundException notFoundException) {
                        notFoundException.printStackTrace();
                    }
                })
        );
        cameraTimer.setCycleCount(Timeline.INDEFINITE);
        cameraTimer.play();
    }
}
