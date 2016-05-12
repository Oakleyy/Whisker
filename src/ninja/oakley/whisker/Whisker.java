package ninja.oakley.whisker;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.io.gpio.RaspiPin;

import javafx.application.Application;
import javafx.stage.Stage;
import ninja.oakley.whisker.media.MediaPlayerController;
import ninja.oakley.whisker.media.USBMountingService;
import ninja.oakley.whisker.menu.JoystickController;
import ninja.oakley.whisker.menu.MenuSceneController;
import ninja.oakley.whisker.stepper.StepperMotorController;

public class Whisker extends Application {

    private static final Logger logger = LogManager.getLogger(Whisker.class);
    
    private Configuration config;
    private JoystickController joystickController;
    private StepperMotorController stepperController;
   
    private MenuSceneController menuSceneController;
    private MediaPlayerController mediaPlayerController;

    /**
     * Loads any processes that need to be initialized before the interface
     * loads.
     */
    @Override
    public void init() {

        this.joystickController = new JoystickController(RaspiPin.GPIO_21, RaspiPin.GPIO_22, RaspiPin.GPIO_23, RaspiPin.GPIO_24);
        this.stepperController = new StepperMotorController(RaspiPin.GPIO_00, RaspiPin.GPIO_02, RaspiPin.GPIO_03, RaspiPin.GPIO_04);
        
        try {
            config = new Configuration();
        } catch (ConfigurationException e) {
            logger.error("Configuration File couldn't load: " + e.getMessage());
        }
        
        menuSceneController = new MenuSceneController();
        joystickController.registerListener(menuSceneController.getJoystickListener());
        
        mediaPlayerController = new MediaPlayerController();
        joystickController.registerListener(mediaPlayerController.getJoystickListener());

        USBMountingService service = new USBMountingService(this);
        service.startService();
        
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
    
    public Configuration getConfiguration(){
        return this.config;
    }

    public StepperMotorController getStepperController(){
        return this.stepperController;
    }
    
}
