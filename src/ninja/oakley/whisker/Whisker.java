package ninja.oakley.whisker;

import java.awt.AWTException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import ninja.oakley.whisker.data.Configuration;
import ninja.oakley.whisker.data.LibraryManager;
import ninja.oakley.whisker.data.MediaSearchingService;
import ninja.oakley.whisker.data.USBMountingService;
import ninja.oakley.whisker.hardware.StepperMotorController;
import ninja.oakley.whisker.media.Media;
import ninja.oakley.whisker.media.MediaPlayerController;
import ninja.oakley.whisker.media.WhiskerPlayer;
import ninja.oakley.whisker.media.WhiskerPlayer.AudioOutput;
import ninja.oakley.whisker.menu.MenuSceneController;

/**
 * The main class of a JavaFX program that manages and plays media files using
 * omxplayer on a RaspberryPi.
 * 
 * @author oakley
 *
 */
public class Whisker extends Application {

    private static final Logger logger = LogManager.getLogger(Whisker.class);

    private Stage primaryStage;

    private final LibraryManager libManager;

    private final Configuration config;
    private final USBMountingService usbService;
    private final MediaSearchingService searchService;

    private final StepperMotorController stepperController;

    private final MenuSceneController menuSceneController;
    private final MediaPlayerController mediaPlayerController;

    public Whisker() {
        this.config = new Configuration();

        // this.stepperController = new StepperMotorController(RaspiPin.GPIO_00,
        // RaspiPin.GPIO_02, RaspiPin.GPIO_03, RaspiPin.GPIO_04);
        this.stepperController = null;

        // this.libManager = new LibraryManager();
        this.libManager = null;

        this.menuSceneController = new MenuSceneController(this);
        this.mediaPlayerController = new MediaPlayerController();

        this.usbService = new USBMountingService(this);
        this.searchService = new MediaSearchingService(this);
    }

    /**
     * Loads any processes that need to be initialized before the interface
     * loads.
     */
    @Override
    public void init() {

        // libManager.initMedia();
        // libManager.initLibraries();

        // usbService.startService();

        // searchService.startService();

        try {
            menuSceneController.load();
            mediaPlayerController.load();
        } catch (IOException e) {
            logger.fatal("JavaFX scenes couldn't load.");
            exit();
        }
    }

    public void exit() {

        // Exit for each class

        System.exit(0);
    }

    /**
     * Starts to display information on the display in the form of a graphical
     * interface.
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
    public Configuration getConfiguration() {
        return this.config;
    }

    /**
     * Returns the stepper controller instance. Used to control the only motor
     * present in the Whisker.
     * 
     * @return stepper motor controller
     */
    public StepperMotorController getStepperController() {
        return this.stepperController;
    }

    public LibraryManager getLibraryManager() {
        return this.libManager;
    }

    public void playMedia(Media media) throws AWTException, IOException {

        switchScene(mediaPlayerController);

        WhiskerPlayer player = WhiskerPlayer.newBuilder(media).setAudioOutput(AudioOutput.HDMI).build();

        mediaPlayerController.preparePlayer(player);
    }

    public USBMountingService getUSBMountingService() {
        return this.usbService;
    }

    private void switchScene(Controller<?> controller) {
        primaryStage.setScene(controller.getScene());
    }

}
