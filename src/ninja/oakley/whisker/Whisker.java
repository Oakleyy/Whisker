package ninja.oakley.whisker;

import java.io.IOException;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.stage.Stage;
import ninja.oakley.whisker.media.Media;
import ninja.oakley.whisker.media.Movie;
import ninja.oakley.whisker.media.WhiskerPlayer;
import ninja.oakley.whisker.media.WhiskerPlayer.AudioOutput;
import ninja.oakley.whisker.menu.MenuSceneController;

public class Whisker extends Application {

    private MenuSceneController menuSceneController;

    /**
     * Loads any processes that need to be initialized before the interface
     * loads.
     */
    @Override
    public void init() {

        menuSceneController = new MenuSceneController();

        try {
            menuSceneController.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Starts to display information on the display in the form of a
     * graphical interface.
     * 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setFullScreen(true);
        //primaryStage.setScene(mediaPlayerController.getScene());

        //primaryStage.show();
      
        Media media = new Movie(Paths.get("/home/pi/whisker/test.mp4"));
        
        WhiskerPlayer player = WhiskerPlayer
                .newBuilder(media)
                .setAudioOutput(AudioOutput.HDMI)
                .build();
        
        player.play();
        
    }
    
    /**
     * Launches the application, also serves as the entrance method
     * 
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(args);
    }


}
