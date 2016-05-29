package ninja.oakley.whisker;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Callback;

/**
 * AbstractController is an abstract implemntation of a JavaFX controller that
 * will load the correct fxml file and provide methods for interacting with the
 * nodes that scene contains.
 * 
 * @author oakley
 *
 * @param <T>
 */
public abstract class AbstractController<T> implements Initializable, Controller<T> {

    private T rootNode;
    private Scene scene;

    /**
     * Get the root node of the FXML file. This is of the type T.
     * 
     * @return root node
     */
    public final T getRootNode() {
        return rootNode;
    }

    public final Scene getScene(){
        return scene;
    }

    public abstract String getFileName();

    public final void load() throws IOException {
        FXMLLoader loader = loadFxmlFile(getFileName());
        setController(loader, this);

        rootNode = loader.load();
        scene = new Scene((Parent) rootNode);
        
        init();
    }
    
    @Override
    public final void initialize(URL location, ResourceBundle resources){ }

    public abstract void init();
    
    /**
     * Returns true if the fxml had loaded, root node doesn't equal null.
     * 
     * @return true if the scene is loaded
     */
    public boolean isLoaded(){
        return (rootNode != null);
    }


    /**
     * Get the FXMLLoader for a specific file
     *
     * @param clazz
     *            class you want to search from
     * @param name
     *            of the file
     * @return FXMLLoader loaded from file
     * @throws IOException
     */
    private FXMLLoader loadFxmlFile(String name) throws IOException {
        return new FXMLLoader(getClass().getResource("/javafx/" + name));
    }

    /**
     * Allows you to initialize your own controller for an FXML file
     *
     * @return loader with the controller assigned
     */
    private FXMLLoader setController(FXMLLoader loader, Object obj) {
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> paramClass) {
                return obj;
            }
        });
        return loader;
    }

}