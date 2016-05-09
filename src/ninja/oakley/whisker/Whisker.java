package ninja.oakley.whisker;

import javafx.application.Application;
import javafx.stage.Stage;
import ninja.oakley.whisker.media.USBMountingService;
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

        //JoystickController controller = new JoystickController(RaspiPin.GPIO_21, RaspiPin.GPIO_22, RaspiPin.GPIO_23, RaspiPin.GPIO_24);
        USBMountingService service = new USBMountingService();
        
    }

    /**
     * Starts to display information on the display in the form of a
     * graphical interface.
     * 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        //primaryStage.setFullScreen(true);
        primaryStage.setScene(menuSceneController.getScene());
        menuSceneController.initScene();

        primaryStage.show();
        
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
