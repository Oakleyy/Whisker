package ninja.oakley.whisker;

import java.io.IOException;

import javafx.scene.Scene;

/**
 * Controller is an interface for loading and interacting with an fxml controller.
 * 
 * @author oakley
 *
 * @param <T>
 */
public interface Controller<T> {

    /**
     * Gets the scene associated with the controller.
     * 
     * @return scene
     */
    public Scene getScene();

    /**
     * Load the FXML file specified by the getFileName() method.
     * 
     * @throws IOException
     */
    public void load() throws IOException;

    /**
     * Get the name of the file for loading the FXML.
     * 
     * @return string representing the filename (and extension) in resources/javafx/
     */
    public String getFileName();

}
