package ninja.oakley.whisker.media;

import java.io.IOException;
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
import ninja.oakley.whisker.menu.Direction;
import ninja.oakley.whisker.menu.JoystickController.JoystickListener;

public class MediaPlayerController extends AbstractController<AnchorPane> {

    private final JoystickListener joystick;

    private Animation transition;
    private boolean visible = true;

    @FXML private ImageView statusImage;

    @FXML private Label currentTimeLabel;
    @FXML private Label remainingTimeLabel;

    @FXML private Slider seekSlider;

    private WhiskerPlayer player;

    public MediaPlayerController(){
        this.joystick = new JoystickTest();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transition = new FadeTransition(Duration.millis(3000), this.getRootNode());
    }

    @Override
    public String getFileName() {
        return "ControlMenu.fxml";
    }

    public boolean isRunning(){
        return (this.player != null && this.player.getStatus() != Status.DISPOSED);
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

    public JoystickListener getJoystickListener(){
        return this.joystick;
    }

    private class JoystickTest implements JoystickListener {

        @Override
        public void execute(Direction dir) {
            if(getRootNode().isVisible() && isRunning()){
                switch(dir){
                    case BOTTOM:
                        try {
                            player.pause();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LEFT:
                        break;
                    case RIGHT:
                        break;
                    case TOP:
                        try {
                            player.play();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        throw new RuntimeException("No such direction.");

                }
            }
        }
    }

}
