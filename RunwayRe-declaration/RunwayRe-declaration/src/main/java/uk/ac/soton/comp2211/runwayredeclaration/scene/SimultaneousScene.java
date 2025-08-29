package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.runwayredeclaration.Component.EmptyVBox;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimultaneousScene extends BaseScene{

    private static final Logger logger = LogManager.getLogger(SimultaneousScene.class);

    private VBox left_box;
    private VBox right_box;
    private HBox menuBox;

    private StackPane middleDisplayBox;

    private DoubleProperty topRunwayHeight = new SimpleDoubleProperty(50);




    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param homeWindow the home window
     */
    public SimultaneousScene(HomeWindow homeWindow) {
        super(homeWindow);
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new HomePane(homeWindow.getWidth(),homeWindow.getHeight());

        var simultaneousViewPane = new StackPane();
        simultaneousViewPane.setMaxWidth(homeWindow.getWidth());
        simultaneousViewPane.setMaxHeight(homeWindow.getHeight());

        root.getChildren().add(simultaneousViewPane);

        var mainPane = new BorderPane();
        simultaneousViewPane.getChildren().add(mainPane);
        mainPane.getStyleClass().add("simultaneous-background");

        // Left Box Set-up
        left_box = new VBox(2, makeResultsTPane(), makeCalcBreakTPane());
        left_box.getStyleClass().add("left-box");
        mainPane.setLeft(left_box);
        BorderPane.setAlignment(left_box, Pos.CENTER);


        // Right Box Set-up
        right_box = new VBox(2, makeAirportTPane(), makeAirplaneTPane(), makeObstacleTPane());
        right_box.getStyleClass().add("right-box");
        mainPane.setRight(right_box);
        BorderPane.setAlignment(right_box, Pos.CENTER);

        // Menu Set-up
        menuBox = new HBox(makeMenuBox());
        menuBox.getStyleClass().add("menu-box");
        BorderPane.setAlignment(menuBox, Pos.TOP_CENTER);
        mainPane.setTop(menuBox);

        // Middle Display Box Set-up
        middleDisplayBox = new StackPane(makeMiddleDisplayBox());
        middleDisplayBox.getStyleClass().add("sideView-background");
        BorderPane.setAlignment(middleDisplayBox, Pos.CENTER);
        mainPane.setCenter(middleDisplayBox);




        this.initialise();

    }

    /**
     * Create the menu box
     * @return HBox the Menu Bar
     */

    private HBox makeMenuBox(){
        // Create Menus
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(new MenuItem("Import XML"), new MenuItem("Export XML"), new MenuItem("Export Report"));

        // View Menu
        Menu viewMenu = new Menu("View");
        // Switch to side view
        MenuItem topView = new MenuItem("Top View");
        topView.setOnAction(e -> homeWindow.startTopView());
        // Switch to simultaneous view
        MenuItem sideView = new MenuItem("Side View");
        sideView.setOnAction(e -> homeWindow.startSideView());
        // Add menu items
        viewMenu.getItems().addAll(topView, sideView);


        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem colourSettings = new MenuItem("Colour Schemes");

        helpMenu.getItems().addAll(new MenuItem("About"), new MenuItem("Contact"), colourSettings);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);

        // Create Log out button
        Button logoutButton = new Button("Log out");
        //logoutButton.setOnAction(e -> homeWindow.startLogin());
        logoutButton.getStyleClass().add("logout-button");


        // Empty box to push the logout button to the right
        HBox empty = new HBox();
        empty.getStyleClass().add("empty");


        HBox.setHgrow(empty, Priority.ALWAYS); // This will push the logout button to the right

        HBox everything = new HBox(menuBar, empty, logoutButton);
        HBox.setHgrow(everything, Priority.ALWAYS);
        return (everything);
    }



    /**
     * Create the middle display box
     * @return StackPane Middle display box
     */

    public StackPane makeMiddleDisplayBox(){
        StackPane displayStackPane = new StackPane();
        BorderPane displayBorderPane = new BorderPane();
        displayStackPane.getChildren().add(displayBorderPane);

        // Direction Pane
        BorderPane directionPane = new BorderPane();
        displayStackPane.getChildren().add(directionPane);

        // Landing Direction
        Image arrow1 = new Image(getClass().getResource("/images/Arrow2.png").toExternalForm());
        ImageView landingArrow = new ImageView(arrow1);
        landingArrow.setPreserveRatio(true);
        landingArrow.setFitWidth(100);
        landingArrow.setRotate(180);
        VBox landingArrowBox = new VBox();
        landingArrowBox.setAlignment(Pos.CENTER);
        VBox arrowEmpty1 = new VBox();
        arrowEmpty1.getStyleClass().add("empty");
        VBox.setVgrow(arrowEmpty1, Priority.ALWAYS);
        Text landingText = new Text("Landing");
        landingText.getStyleClass().add("arrow-text");
        landingArrowBox.getChildren().addAll(landingArrow, arrowEmpty1);
        directionPane.setRight(landingArrowBox);

        // Takeoff Direction
        Image arrow2 = new Image(getClass().getResource("/images/Arrow1.png").toExternalForm());
        ImageView takeoffArrow = new ImageView(arrow2);
        takeoffArrow.setPreserveRatio(true);
        takeoffArrow.setFitWidth(100);
        VBox takeoffArrowBox = new VBox();
        takeoffArrowBox.setAlignment(Pos.CENTER);
        VBox arrowEmpty2 = new VBox();
        arrowEmpty2.getStyleClass().add("empty");
        VBox.setVgrow(arrowEmpty2, Priority.ALWAYS);
        Text takeoffText = new Text("Take off");
        takeoffText.getStyleClass().add("arrow-text");
        takeoffArrowBox.getChildren().addAll(takeoffArrow, arrowEmpty2);
        directionPane.setLeft(takeoffArrowBox);


        // Top-View Part
        StackPane topViewPane = new StackPane();


        topViewPane.getStyleClass().add("topView-background");
        displayBorderPane.setTop(topViewPane);
        topViewPane.prefHeightProperty().bind(displayBorderPane.heightProperty().divide(2));


        // Top-View Runway Image
        Image toprunwayImage = new Image(getClass().getResource("/images/Runway1.png").toExternalForm());
        ImageView toprunwayImageView = new ImageView(toprunwayImage);
        toprunwayImageView.setFitHeight(topRunwayHeight.getValue());
        toprunwayImageView.setFitWidth(displayRunwayLength.getValue());


        // Stop Ways
        HBox stopWayTop1 = new HBox();
        stopWayTop1.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWayTop1.prefWidthProperty().bind(displayStopWayLength);
        stopWayTop1.setAlignment(Pos.CENTER_LEFT);
        HBox stopWayTop2 = new HBox();
        stopWayTop2.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWayTop2.prefWidthProperty().bind(displayStopWayLength);
        stopWayTop2.setAlignment(Pos.CENTER_LEFT);

        if (displayStopWayLength.getValue() == 0){
            stopWayTop1.setVisible(false);
            stopWayTop2.setVisible(false);
        }

        HBox runwayTopBox = new HBox();
        runwayTopBox.getStyleClass().add("empty");
        runwayTopBox.setAlignment(Pos.CENTER_LEFT);
        runwayTopBox.setMaxHeight(Region.USE_PREF_SIZE);
        runwayTopBox.getChildren().addAll(stopWayTop1, toprunwayImageView, stopWayTop2);

        // Empty boxes to push the runway to the center
        HBox borderToTopRunway1 = new HBox();
        borderToTopRunway1.getStyleClass().add("empty");
        borderToTopRunway1.setPrefWidth(displayBorderToRunway.getValue());
        HBox borderToTopRunway2 = new HBox();
        borderToTopRunway2.getStyleClass().add("empty");
        borderToTopRunway2.setPrefWidth(displayBorderToRunway.getValue());

        HBox runwayTopPaneBox = new HBox(borderToTopRunway1, runwayTopBox, borderToTopRunway2);
        runwayTopPaneBox.setAlignment(Pos.CENTER_LEFT);


        // Plane Image
        Image planeImageTop = new Image(getClass().getResource("/images/Plane-TopView1.png").toExternalForm());
        ImageView planeImageViewTop = new ImageView(planeImageTop);
        planeImageViewTop.setFitWidth(50);
        planeImageViewTop.setPreserveRatio(true);


        // Empty boxes to push the plane to the center
        HBox frontPlaneTopEmpty = new HBox();
        frontPlaneTopEmpty.getStyleClass().add("empty");
        frontPlaneTopEmpty.prefWidthProperty().bind(Bindings.add(displayBorderToRunway,displayRunwayToPlane));


        // HBox distance between the plane and obstacle
        HBox planeObstacleTopDistanceBox = new HBox();
        planeObstacleTopDistanceBox.getStyleClass().add("empty");
        planeObstacleTopDistanceBox.setPrefWidth(displayPlaneToObstacle.getValue());

        // Obstacle Image
        Image obstacleImageTop = new Image(getClass().getResource("/images/Obstacle.png").toExternalForm());
        ImageView obstacleImageViewTop = new ImageView(obstacleImageTop);
        obstacleImageViewTop.setFitWidth(30);
        obstacleImageViewTop.setPreserveRatio(true);

        // Plane & Obstacle HBox
        HBox planeObstacleTopBox = new HBox(frontPlaneTopEmpty ,planeImageViewTop, planeObstacleTopDistanceBox, obstacleImageViewTop);
        planeObstacleTopBox.setAlignment(Pos.CENTER_LEFT);




        // Graded Area
        Image gradeArea = new Image(getClass().getResource("/images/GradedArea.png").toExternalForm());
        ImageView gradeAreaImageView = new ImageView(gradeArea);
        gradeAreaImageView.setFitHeight(200);
        gradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);
        // Border for the views
        topViewPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 2, 0))));

        VBox topViewLabelBox = new VBox();
        topViewLabelBox.setAlignment(Pos.BOTTOM_CENTER);
        VBox topViewEmpty = new VBox();
        topViewEmpty.getStyleClass().add("empty");
        VBox.setVgrow(topViewEmpty, Priority.ALWAYS);
        Label topViewLabel = new Label("Top View");
        topViewLabel.getStyleClass().add("view-label");
        topViewLabelBox.getChildren().addAll(topViewLabel, topViewEmpty);







        // Add the runway, plane, obstacle and graded area to the top view pane
        topViewPane.getChildren().addAll(gradeAreaImageView, runwayTopPaneBox, planeObstacleTopBox, topViewLabelBox);





        // Side-View Part
        BorderPane sideViewPane = new BorderPane();
        displayBorderPane.setBottom(sideViewPane);

        // Sky Part
        BorderPane bluePane = new BorderPane();
        bluePane.getStyleClass().add("sideView-background");
        bluePane.prefHeightProperty().bind(displayBorderPane.heightProperty().divide(4));
        sideViewPane.setTop(bluePane);



        HBox sideViewEmpty1 = new HBox();
        sideViewEmpty1.getStyleClass().add("empty");
        HBox.setHgrow(sideViewEmpty1, Priority.ALWAYS);
        HBox sideViewEmpty2 = new HBox();
        sideViewEmpty2.getStyleClass().add("empty");
        HBox.setHgrow(sideViewEmpty2, Priority.ALWAYS);

        Label sideViewLabel = new Label("Side View");
        sideViewLabel.setAlignment(Pos.CENTER);
        sideViewLabel.getStyleClass().add("view-label");


        bluePane.setTop(new HBox(sideViewEmpty1, sideViewLabel, sideViewEmpty2));



        // Plane Image
        Image planeImageSide = new Image(getClass().getResource("/images/Plane1.png").toExternalForm());
        ImageView planeImageViewSide = new ImageView(planeImageSide);
        planeImageViewSide.setPreserveRatio(true);
        planeImageViewSide.setFitWidth(50);
        // HBox distance between the plane and obstacle
        HBox planeObstacleSideDistance = new HBox();
        planeObstacleSideDistance.getStyleClass().add("empty");
        planeObstacleSideDistance.setPrefWidth(displayPlaneToObstacle.getValue());
        // Obstacle Image
        Image obstacleImageSide = new Image(getClass().getResource("/images/Obstacle.png").toExternalForm());
        ImageView obstacleImageViewSide = new ImageView(obstacleImageSide);
        obstacleImageViewSide.setPreserveRatio(true);
        obstacleImageViewSide.setFitWidth(30);




        // Plane & Obstacle Pane (Might change)
        HBox planeObstacleSideBox = new HBox();
        planeObstacleSideBox.setAlignment(Pos.BOTTOM_LEFT);
        HBox frontPlaneEmpty = new HBox();
        frontPlaneEmpty.getStyleClass().add("empty");
        frontPlaneEmpty.prefWidthProperty().bind(Bindings.add(displayBorderToRunway,displayRunwayToPlane));

        // Plane & Obstacle Pane

        //planeObstacleSideBox.setMaxWidth(displayRunwayLength.getValue());
        planeObstacleSideBox.getChildren().addAll(frontPlaneEmpty, planeImageViewSide, planeObstacleSideDistance, obstacleImageViewSide);

        bluePane.setBottom(planeObstacleSideBox);


        // Ground Part
        BorderPane groundPane = new BorderPane();
        groundPane.prefHeightProperty().bind(displayBorderPane.heightProperty().divide(4));
        groundPane.setBackground(new Background(new BackgroundFill(Color.web("#A9D18E"), CornerRadii.EMPTY, Insets.EMPTY)));
        sideViewPane.setBottom(groundPane);

        // Empty boxes to push the runway to the center
        HBox empty1 = new HBox();
        empty1.getStyleClass().add("empty");
        HBox.setHgrow(empty1, Priority.ALWAYS);
        HBox empty2 = new HBox();
        empty2.getStyleClass().add("empty");
        HBox.setHgrow(empty2, Priority.ALWAYS);
        // Runway Image
        Image siderunwayImage = new Image(getClass().getResource("/images/Runway2.png").toExternalForm());
        ImageView siderunwayImageView = new ImageView(siderunwayImage);
        siderunwayImageView.setPreserveRatio(true);
        siderunwayImageView.setFitWidth(displayRunwayLength.getValue());


        HBox runwayBox = new HBox();
        runwayBox.getStyleClass().add("empty");
        runwayBox.setAlignment(Pos.CENTER_LEFT);



        // Stop ways
        HBox stopWay1 = new HBox();
        stopWay1.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay1.setMaxHeight(Region.USE_COMPUTED_SIZE);
        stopWay1.prefWidthProperty().bind(displayStopWayLength);
        stopWay1.setAlignment(Pos.CENTER_LEFT);
        HBox stopWay2 = new HBox();
        stopWay2.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay2.prefWidthProperty().bind(displayStopWayLength);
        stopWay2.setAlignment(Pos.CENTER_RIGHT);
        stopWay2.setMaxHeight(Region.USE_COMPUTED_SIZE);


        runwayBox.getChildren().addAll(stopWay1, siderunwayImageView, stopWay2);

        // Empty boxes to push the runway to the center
        HBox borderToRunway1 = new HBox();
        borderToRunway1.getStyleClass().add("empty");
        borderToRunway1.setPrefWidth(displayBorderToRunway.getValue());
        HBox borderToRunway2 = new HBox();
        borderToRunway2.getStyleClass().add("empty");
        borderToRunway2.setPrefWidth(displayBorderToRunway.getValue());
        HBox runwayPaneBox = new HBox(borderToRunway1, runwayBox, borderToRunway2);
        runwayPaneBox.setAlignment(Pos.CENTER_LEFT);



        groundPane.setTop(runwayPaneBox);


        return displayStackPane;
    }






}
