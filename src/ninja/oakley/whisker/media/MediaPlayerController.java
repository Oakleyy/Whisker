package ninja.oakley.whisker.media;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import ninja.oakley.whisker.AbstractController;
import ninja.oakley.whisker.hardware.Direction;
import ninja.oakley.whisker.hardware.JoystickController.JoystickListener;
import ninja.oakley.whisker.media.WhiskerPlayer.Status;

public class MediaPlayerController extends AbstractController<AnchorPane> {

    private final JoystickListener joystick;
    private final ScheduledExecutorService executor;

    private Animation transition;
    private boolean visible = true;

    @FXML private ImageView statusImage;

    @FXML private Label currentTimeLabel;
    @FXML private Label remainingTimeLabel;

    @FXML private Slider seekSlider;

    private WhiskerPlayer player;

    public MediaPlayerController(){
        this.executor = Executors.newScheduledThreadPool(1);
        this.joystick = new JoystickTest();
    }

    @Override
    public void init() {
        transition = new FadeTransition(Duration.millis(3000), this.getRootNode());
    }

    @Override
    public String getFileName() {
        return "ControlMenu.fxml";
    }

    /**
     * Check if the controller is currently housing a media player
     * 
     * @return
     */
    public boolean isRunning(){
        return (this.player != null && this.player.getStatus() != Status.DISPOSED);
    }

    /**
     * Changes the transparency of the node. Basically, it will change if you can see the status of the movie (play, seek, etc.).
     * 
     * @param status
     */
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

    /**
     * 
     * 
     * @return
     */
    public boolean isVisible(){
        return visible;
    }

    public void preparePlayer(WhiskerPlayer player) throws IOException {
        if(isPlayerFree()){
            this.player = player;
            System.out.println("Made it");
            //player.play();
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
    
    private void startFadeSequence(){
        setVisible(true);
        executor.schedule(new Runnable(){ 
            @Override
            public void run() {
                setVisible(false);
                
            }
        }, 5, TimeUnit.SECONDS);
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
                
                startFadeSequence();
            }
        }
    }

}
