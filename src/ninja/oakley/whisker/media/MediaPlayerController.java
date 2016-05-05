package ninja.oakley.whisker.media;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import ninja.oakley.whisker.AbstractController;
import ninja.oakley.whisker.media.WhiskerPlayer.Status;

public class MediaPlayerController extends AbstractController<AnchorPane> {

    private Animation transition;
    private boolean visible = true;
    
    @FXML private ImageView statusImage;
    
    @FXML private Label currentTimeLabel;
    @FXML private Label remainingTimeLabel;
    
    @FXML private Slider seekSlider;
    
    private WhiskerPlayer player;

    public MediaPlayerController(){
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transition = new FadeTransition(Duration.millis(3000), this.getRootNode());
    }

    @Override
    public String getFileName() {
        return "ControlMenu.fxml";
    }

    private synchronized void setVisible(boolean status){
        if(status == !visible){
            FadeTransition t = (FadeTransition) transition;
            if(status){
                t.setFromValue(0.0);
                t.setToValue(1.0);
            } else if(!status) {
                t.setFromValue(1.0);
                t.setToValue(0.0);
            }
            t.play();
            visible = status;
        }
    }

    public boolean isVisible(){
        return visible;
    }
    
    public void preparePlayer(WhiskerPlayer player){
        if(isPlayerFree()){
            this.player = player;
        } else {
            throw new RuntimeException("Player isn't free.");
        }
    }
    
    public boolean isPlayerFree(){
        return player == null || player.getStatus() == Status.DISPOSED;
    }


}
