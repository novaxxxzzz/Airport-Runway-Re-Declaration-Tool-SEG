package uk.ac.soton.comp2211.runwayredeclaration;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.awt.Taskbar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;

import javax.imageio.ImageIO;
import javax.swing.*;


public class App extends Application{
    private final int width = 1400;
    private final int height = 800;
    private static App instance;
    private Stage stage;
    private static final Logger logger = LogManager.getLogger(App.class);


    /**
     * Called by JavaFX with the primary stage as a parameter. Begins the game by opening the Game Window
     * @param stage the default stage, main window
     */
    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        this.stage = stage;

        //Open home window
        openHome();
    }

    /**
     * Create the HomeWindow with the specified width and height
     */
    public void openHome() {
        logger.info("Opening home window");

        var homeWindow = new HomeWindow(stage,width,height);
        //set the windows taskbar icon
        this.stage.getIcons().add(new Image(getClass().getResource("/images/planeIcon.png").toExternalForm()));
        //set the mac taskbar icon
        if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_IMAGE)) {
            try {
                java.awt.Image awtImage = ImageIO.read(getClass().getResourceAsStream("/images/planeIcon.png")); // Adjust path as needed
                Taskbar.getTaskbar().setIconImage(awtImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Show the home window
        stage.show();
    }


    /**
     * Shutdown the game
     */
    public void shutdown() {
        logger.info("Shutting down");
        System.exit(0);
    }


    /**
     * Get the singleton App instance
     * @return the app
     */
    public static App getInstance() {
        return instance;
    }


    /**
     * Start the app
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        logger.info("Starting client");
        launch();
    }
}
