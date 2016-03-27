package ninja.oakley.whisker;

import java.io.IOException;

import javafx.scene.Scene;

public interface Controller<T> {

    public Scene getScene();
    
    public void load() throws IOException;
    
    public void openWindow();
    
    public void closeWindow();
    
}
