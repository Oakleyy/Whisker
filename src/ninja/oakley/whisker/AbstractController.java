package ninja.oakley.whisker;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Callback;

public abstract class AbstractController<T> implements Initializable, Controller<T> {

    private T rootNode;
    private Scene scene;
    
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
        //loader.setController(this);
        
        rootNode = loader.load();
        scene = new Scene((Parent) rootNode);
    }
    
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