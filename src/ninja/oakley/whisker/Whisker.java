package ninja.oakley.whisker;

import java.awt.AWTException;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import com.pi4j.component.motor.MotorState;
import com.pi4j.io.gpio.RaspiPin;

import javafx.application.Application;
import javafx.stage.Stage;
import ninja.oakley.whisker.hardware.ButtonController;
import ninja.oakley.whisker.hardware.ButtonController.ButtonListener;
import ninja.oakley.whisker.hardware.Direction;
import ninja.oakley.whisker.hardware.JoystickController;
import ninja.oakley.whisker.hardware.StepperMotorController;
import ninja.oakley.whisker.hardware.USBMountingService;
import ninja.oakley.whisker.hardware.JoystickController.JoystickListener;
import ninja.oakley.whisker.media.Media;
import ninja.oakley.whisker.media.Media.MediaType;
import ninja.oakley.whisker.media.MediaPlayerController;
import ninja.oakley.whisker.media.WhiskerPlayer;
import ninja.oakley.whisker.media.WhiskerPlayer.AudioOutput;
import ninja.oakley.whisker.menu.MenuSceneController;

/**
 * The main class of a JavaFX program that manages and plays media files using omxplayer on
 * a RaspberryPi. It also controlle
 * 
 * @author oakley
 *
 */
public class Whisker extends Application {

    private static final Logger logger = LogManager.getLogger(Whisker.class);

    private Stage primaryStage;

    private final Configuration config;
    private final USBMountingService usbService;

    private JoystickController joystickController;
    private ButtonController buttonController;
    private StepperMotorController stepperController;

    private final MenuSceneController menuSceneController;
    private final MediaPlayerController mediaPlayerController;

    public Whisker(){
        menuSceneController = new MenuSceneController(this);
        mediaPlayerController = new MediaPlayerController();

        usbService = new USBMountingService(this);
        config = new Configuration();
    }

    /**
     * Loads any processes that need to be initialized before the interface
     * loads.
     */
    @Override
    public void init() {

        //usbService.startService();

        this.joystickController = new JoystickController(RaspiPin.GPIO_21, RaspiPin.GPIO_22, RaspiPin.GPIO_23, RaspiPin.GPIO_24);
        this.stepperController = new StepperMotorController(RaspiPin.GPIO_00, RaspiPin.GPIO_02, RaspiPin.GPIO_03, RaspiPin.GPIO_04);
        //this.buttonController = new ButtonController(RaspiPin.GPIO_08);

        joystickController.registerListener(new JoystickTest(stepperController));
        //buttonController.registerListener(new ButtonTest(buttonController));
        //buttonController.registerListener(menuSceneController.getButtonListener());

        joystickController.registerListener(menuSceneController.getJoystickListener());

        joystickController.registerListener(mediaPlayerController.getJoystickListener());

        config.getMedia();

        // config.saveMedia(new Media("test2", Paths.get("/Users/oakley/Documents/workspace/Whisker/test.mp4"), MediaType.MOVIE, new ObjectId()));

        try {
            menuSceneController.load();
            mediaPlayerController.load();
        } catch (IOException e) {
            logger.fatal("JavaFX scenes couldn't load.");
            exit();
        }

    }

    public void exit(){

        //Exit for each class


        System.exit(0);
    }

    /**
     * Starts to display information on the display in the form of a
     * graphical interface.
     * 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        switchScene(menuSceneController);

        primaryStage.setFullScreen(true);
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

    /**
     * Returns the configuration loaded at runtime.
     * 
     * @return configuration
     */
    public Configuration getConfiguration(){
        return this.config;
    }

    /**
     * Returns the stepper controller instance. Used to control the only motor present in the Whisker.
     * 
     * @return stepper motor controller
     */
    public StepperMotorController getStepperController(){
        return this.stepperController;
    }

    public void playMedia(Media media) throws AWTException, IOException {

        switchScene(mediaPlayerController);

        WhiskerPlayer player = WhiskerPlayer.newBuilder(media).setAudioOutput(AudioOutput.HDMI).build();

        mediaPlayerController.preparePlayer(player);
    }
    
    public USBMountingService getUSBMountingService(){
        return this.usbService;
    }

    private void switchScene(Controller<?> controller){
        primaryStage.setScene(controller.getScene());
    }

    private class JoystickTest implements JoystickListener {

        private StepperMotorController controller;

        public JoystickTest(StepperMotorController controller){
            this.controller = controller;
        }

        @Override
        public void execute(Direction dir) {
            switch(dir){
                case BOTTOM:
                    logger.debug("Stopping Motor");
                    controller.setMotorState(MotorState.STOP);
                    break;
                case LEFT:
                    logger.debug("Reversing Motor");
                    controller.setMotorState(MotorState.REVERSE);
                    break;
                case RIGHT:
                    logger.debug("Forwarding Motor");
                    controller.setMotorState(MotorState.FORWARD);
                    break;
                case TOP:
                    logger.debug("Unused");
                    break;
                default:
                    throw new RuntimeException("No such direction.");

            }

        }
    }

    private class ButtonTest implements ButtonListener {

        private ButtonController controller;

        public ButtonTest(ButtonController controller){
            this.controller = controller;
        }

        @Override
        public void execute() {
            logger.info("Pressed");
        }


    }

}
