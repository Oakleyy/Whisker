package ninja.oakley.whisker.player;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaView;
import ninja.oakley.whisker.AbstractController;
import ninja.oakley.whisker.Whisker;

public class MediaPlayerController extends AbstractController<BorderPane> {

    private Whisker instance;
    
    @FXML
    public MediaView mediaView;
    
    
    
    public MediaPlayerController(Whisker instance) {
        this.instance = instance;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }


    @Override
    public String getFileName() {
        return "MediaPlayerView.fxml";
    }


    @Override
    public void openWindow() {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void closeWindow() {
        // TODO Auto-generated method stub
        
    }

}
