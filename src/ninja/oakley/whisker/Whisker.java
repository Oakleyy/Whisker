package ninja.oakley.whisker;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import ninja.oakley.whisker.menu.MenuSceneController;
import ninja.oakley.whisker.player.MediaPlayerController;

public class Whisker extends Application {

    
    private MenuSceneController menuSceneController;
    private MediaPlayerController mediaPlayerController;
    
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void init() {
        
        menuSceneController = new MenuSceneController();
        mediaPlayerController = new MediaPlayerController(this);
        
        try {
            menuSceneController.load();
            mediaPlayerController.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        primaryStage.setFullScreen(true);
        primaryStage.setScene(menuSceneController.getScene());
        
        primaryStage.show();
    }

}
