package uk.ac.soton.comp2211.runwayredeclaration.ui;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.runwayredeclaration.App;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.runwayredeclaration.Component.User;
import uk.ac.soton.comp2211.runwayredeclaration.scene.*;

public class HomeWindow {

    private static final Logger logger = LogManager.getLogger(HomeWindow.class);

    private final int width;
    private final int height;
    private final Stage stage;
    private BaseScene currentScene;
    private Scene scene;

    public HomeWindow(Stage stage, int width, int height) {
        this.width = width;
        this.height = height;

        this.stage = stage;

        //Setup window
        setupStage();

        //Setup resources
        setupResources();

        //Setup default scene
        setupDefaultScene();

        //Go to home
        startLogIn();
    }

    public void startLogIn(){
        loadScene(new LogInScene(this));
    }

    public void startHome(User user) {
        loadScene(new ViewScene(this, user));
    }

    /**
     * Start the side view
     */
    public void startSideView() {
        loadScene(new ViewScene(this));
    }


    /**
     * Start the top view
     */
    public void startTopView() {
        loadScene(new TopViewScene(this));
    }

    /**
     * Start the simultaneous view
     */
    public void startSimultaneousView(){
        loadScene(new SimultaneousScene(this));
    }


    /**
     * Setup the font and any other resources we need
     */
    private void setupResources() {
        logger.info("Loading resources");

        //We need to load fonts here due to the Font loader bug with spaces in URLs in the CSS files
        Font.loadFont(getClass().getResourceAsStream("/style/calibri-regular.ttf"),32);
        Font.loadFont(getClass().getResourceAsStream("/style/calibri-bold.ttf"),32);
        Font.loadFont(getClass().getResourceAsStream("/style/calibri-italic.ttf"),32);

    }



    /**
     * Setup the default settings for the stage itself (the window), such as the title and minimum width and height.
     */
    public void setupStage() {
        stage.setTitle("Runaway Re-Declaration ");
        stage.setMinWidth(width);
        stage.setMinHeight(height + 20);
        stage.setOnCloseRequest(ev -> App.getInstance().shutdown());

    }
    /**
     * Load a given scene which extends BaseScene and switch over.
     * @param newScene new scene to load
     */
    public void loadScene(BaseScene newScene) {
        //Create the new scene and set it up
        newScene.build();
        currentScene = newScene;
        scene = newScene.setScene();
        stage.setScene(scene);

        //Initialise the scene when ready
        Platform.runLater(() -> currentScene.initialise());
    }

    /**
     * Setup the default scene (an empty black scene) when no scene is loaded
     */
    public void setupDefaultScene() {
        this.scene = new Scene(new Pane(), width, height, Color.BLACK);
        stage.setScene(this.scene);
    }


    /**
     * Get the current scene being displayed
     * @return scene
     */
    public Scene getScene() {
        return scene;
    }
    /**
     * Get the width of the Game Window
     * @return width
     */
    public int getWidth() {
        return this.width;
    }
    /**
     * Get the height of the Game Window
     * @return height
     */
    public int getHeight() {
        return this.height;
    }

}
