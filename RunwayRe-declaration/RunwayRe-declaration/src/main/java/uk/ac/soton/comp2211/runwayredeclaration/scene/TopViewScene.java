package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.runwayredeclaration.Component.DashedLine;
import uk.ac.soton.comp2211.runwayredeclaration.Component.EmptyVBox;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
public class TopViewScene extends BaseScene{

    private static final Logger logger = LogManager.getLogger(TopViewScene.class);

    private VBox left_box;
    private VBox right_box;

    private HBox menuBox;

    private StackPane middleDisplayBox;

    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param homeWindow the home window
     */
    public TopViewScene(HomeWindow homeWindow) {
        super(homeWindow);
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new HomePane(homeWindow.getWidth(),homeWindow.getHeight());

        var topViewPane = new StackPane();
        topViewPane.setMaxWidth(homeWindow.getWidth());
        topViewPane.setMaxHeight(homeWindow.getHeight());

        root.getChildren().add(topViewPane);

        var mainPane = new BorderPane();
        mainPane.getStyleClass().add("topView-background");
        topViewPane.getChildren().add(mainPane);




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

        menuBox = new HBox(makeMenuBox());
        menuBox.getStyleClass().add("menu-box");
        mainPane.setTop(menuBox);
        BorderPane.setAlignment(menuBox, Pos.TOP_CENTER);

        // Middle Display Box Set-up
        middleDisplayBox = new StackPane(makeMiddleDisplayBox());
        middleDisplayBox.getStyleClass().add("sideView-background");
        BorderPane.setAlignment(middleDisplayBox, Pos.CENTER);
        mainPane.setCenter(middleDisplayBox);


        this.initialise();
    }

    /**
     * Create the results box
     * @return StackPane the middle display
     */
    private StackPane makeMiddleDisplayBox() {
        StackPane displayStackPane = new StackPane();


        BorderPane displayBorderPane = new BorderPane();
        BorderPane directionPane = new BorderPane();
        displayStackPane.getChildren().add(displayBorderPane);
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


        // Top-View Runway
        Image runwayImage = new Image(getClass().getResource("/images/Runway1.png").toExternalForm());
        ImageView runwayImageView = new ImageView(runwayImage);
        runwayImageView.setPreserveRatio(true);
        runwayImageView.setFitWidth(displayRunwayLength.getValue());
        // Graded Area
        Image gradeArea = new Image(getClass().getResource("/images/GradedArea.png").toExternalForm());
        ImageView gradeAreaImageView = new ImageView(gradeArea);
        gradeAreaImageView.setPreserveRatio(true);
        gradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);


        // Runway HBox
        HBox runwayBox = new HBox();
        runwayBox.getStyleClass().add("empty");
        runwayBox.setAlignment(Pos.CENTER_LEFT);


        // Stop Ways
        HBox stopWay1 = new HBox();
        stopWay1.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay1.prefWidthProperty().bind(displayStopWayLength);
        stopWay1.prefHeightProperty().bind(runwayImageView.fitHeightProperty());
        stopWay1.visibleProperty().bind(stopWayLength2.greaterThan(0));
        HBox stopWay2 = new HBox();
        stopWay2.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay2.prefWidthProperty().bind(displayStopWayLength);
        stopWay1.prefHeightProperty().bind(runwayImageView.fitHeightProperty());
        stopWay2.visibleProperty().bind(stopWayLength1.greaterThan(0));


        stopWay1.setMaxHeight(runwayImageView.getFitWidth() / runwayImage.getWidth() * runwayImage.getHeight());
        stopWay2.setMaxHeight(runwayImageView.getFitWidth() / runwayImage.getWidth() * runwayImage.getHeight());

        HBox stopWayBox = new HBox();
        HBox emptyBoxBetweenStopWays = new HBox();
        emptyBoxBetweenStopWays.getStyleClass().add("empty");
        emptyBoxBetweenStopWays.setPrefHeight(displayStackPane.getHeight());
        emptyBoxBetweenStopWays.setPrefWidth(displayRunwayLength.get());
        stopWayBox.getChildren().addAll(stopWay1, emptyBoxBetweenStopWays, stopWay2);
        stopWayBox.setAlignment(Pos.CENTER);

        stopWayBox.setPrefHeight(100);



        runwayBox.getChildren().add(runwayImageView);


        // Empty boxes to push the runway to the center
        HBox borderToRunway1 = new HBox();
        borderToRunway1.getStyleClass().add("empty");
        borderToRunway1.setPrefWidth(displayBorderToRunway.getValue());
        HBox borderToRunway2 = new HBox();
        borderToRunway2.getStyleClass().add("empty");
        borderToRunway2.setPrefWidth(displayBorderToRunway.getValue());

        HBox runwayPaneBox = new HBox(borderToRunway1, runwayBox, borderToRunway2);
        runwayPaneBox.setAlignment(Pos.CENTER_LEFT);





        // Plane Images
        Image planeImage = new Image(getClass().getResource("/images/Plane-TopView1.png").toExternalForm());
        ImageView planeImageView = new ImageView(planeImage);
        planeImageView.setPreserveRatio(true);
        planeImageView.setFitWidth(60);
        // HBox distance between plane and obstacle
        HBox planeObstacleDistance = new HBox();
        planeObstacleDistance.getStyleClass().add("empty");
        planeObstacleDistance.setPrefWidth(displayPlaneToObstacle.getValue());
        // Obstacle Images
        Image obstacleImage = new Image(getClass().getResource("/images/Obstacle.png").toExternalForm());
        ImageView obstacleImageView = new ImageView(obstacleImage);
        obstacleImageView.setPreserveRatio(true);
        obstacleImageView.setFitWidth(30);

        // Plane & Obstacle Pane (Might change)
        HBox planeObstacleBox = new HBox();
        planeObstacleBox.setAlignment(Pos.CENTER_LEFT);
        HBox frontPlaneEmpty = new HBox();
        frontPlaneEmpty.getStyleClass().add("empty");
        HBox.setHgrow(frontPlaneEmpty, Priority.ALWAYS);
        HBox backPlaneEmpty = new HBox();
        backPlaneEmpty.getStyleClass().add("empty");
        HBox.setHgrow(backPlaneEmpty, Priority.ALWAYS);

        HBox borderToPlane = new HBox();
        borderToPlane.getStyleClass().add("empty");
        borderToPlane.prefWidthProperty().bind(Bindings.subtract ( Bindings.add(displayBorderToRunway, displayRunwayToPlane)  , planeImageView.fitWidthProperty()));
//        borderToPlane.prefWidthProperty().bind(Bindings.add(displayBorderToRunway,displayRunwayToPlane));


        planeObstacleBox.getChildren().addAll(borderToPlane, planeImageView, planeObstacleDistance, obstacleImageView);


        // Clearways
        HBox clearWayBox = new HBox();
        clearWayBox.getStyleClass().add("empty");
        clearWayBox.setAlignment(Pos.CENTER);

        HBox clearWay1 = new HBox();
        clearWay1.getStyleClass().add("clearway-box");
        clearWay1.visibleProperty().bind(clearWayLength2.greaterThan(0));
        clearWay1.prefWidthProperty().bind(displayClearWayLength);

        HBox distanceBetweenStopways = new HBox();
        distanceBetweenStopways.getStyleClass().add("empty");
        distanceBetweenStopways.setPrefWidth(displayRunwayLength.get());

        HBox clearWay2 = new HBox();
        clearWay2.getStyleClass().add("clearway-box");
        clearWay2.visibleProperty().bind(clearWayLength1.greaterThan(0));
        clearWay2.prefWidthProperty().bind(displayClearWayLength);


        clearWayBox.getChildren().addAll(clearWay1, distanceBetweenStopways, clearWay2);



        displayStackPane.getChildren().addAll(gradeAreaImageView, runwayPaneBox, stopWayBox, clearWayBox, planeObstacleBox);

        // Take Off Indicators: TORA, TODA, ASDA


        // TORA HBox
        toraBox = new HBox();
        toraBox.getStyleClass().add("empty");
        toraBox.setAlignment(Pos.CENTER_LEFT);
        HBox borderToTORA = new HBox();
        borderToTORA.getStyleClass().add("empty");
        borderToTORA.setPrefWidth(displayBorderToRunway.getValue() + displayStopWayLength.getValue() - 1);
        DashedLine toraStart = new DashedLine(0.1, 500);
        DashedLine toraEnd = new DashedLine(0.1, 500);
        // TORA Distance Box (Line + Text)
        VBox toraDistanceBox = new VBox();
        toraDistanceBox.getStyleClass().add("empty");
        toraDistanceBox.prefWidthProperty().bindBidirectional(displayTORA);
        toraDistanceBox.setMaxHeight(500);
        toraDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine toraArrow = new DashedLine(displayTORA.get(), 0.1, false);
        Text toraText = new Text("TORA");
        toraText.getStyleClass().add("arrow-text");
        toraDistanceBox.getChildren().addAll(toraArrow, toraText, new EmptyVBox(0.1, 50));

        toraBox.getChildren().addAll(borderToTORA, toraStart, toraDistanceBox, toraEnd);



        // TODA HBox
        todaBox = new HBox();
        todaBox.getStyleClass().add("empty");
        todaBox.setAlignment(Pos.CENTER_LEFT);
        DashedLine todaStart = new DashedLine(0.1, 500);
        DashedLine todaEnd = new DashedLine(0.1, 500);

        VBox todaDistanceBox = new VBox();
        todaDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        todaDistanceBox.setMaxHeight(500);
        todaDistanceBox.getStyleClass().add("empty");
        todaDistanceBox.prefWidthProperty().bindBidirectional(displayTODA);
        DashedLine todaArrow = new DashedLine(displayTODA.get(), 0.1, false);
        Text todaText = new Text("TODA");
        todaText.getStyleClass().add("arrow-text");
        todaDistanceBox.getChildren().addAll(todaArrow, todaText, new EmptyVBox(0.1, 100));

        HBox borderToTODA = new HBox();
        borderToTODA.getStyleClass().add("empty");
        borderToTODA.setPrefWidth(displayBorderToRunway.getValue() + displayStopWayLength.getValue() - 1);

        todaBox.getChildren().addAll(borderToTODA, todaStart, todaDistanceBox, todaEnd);


        // ASDA HBox
        asdaBox = new HBox();
        asdaBox.getStyleClass().add("empty");
        asdaBox.setAlignment(Pos.CENTER_LEFT);

        DashedLine asdaStart = new DashedLine(0.1, 500);
        DashedLine asdaEnd = new DashedLine(0.1, 500);

        VBox asdaDistanceBox = new VBox();
        asdaDistanceBox.getStyleClass().add("empty");
        asdaDistanceBox.prefWidthProperty().bindBidirectional(displayASDA);
        asdaDistanceBox.setMaxHeight(500);
        asdaDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine asdaArrow = new DashedLine(displayASDA.get(), 0.1, false);
        Text asdaText = new Text("ASDA");
        asdaText.getStyleClass().add("arrow-text");
        asdaDistanceBox.getChildren().addAll(asdaArrow, asdaText, new EmptyVBox(0.1, 150));


        HBox borderToASDA = new HBox();
        borderToASDA.getStyleClass().add("empty");
        borderToASDA.setPrefWidth(displayBorderToRunway.getValue() + displayStopWayLength.getValue() - 1);

        asdaBox.getChildren().addAll(borderToASDA, asdaStart, asdaDistanceBox, asdaEnd);

        // Landing Indicators: LDA
        StackPane landingIndicators = new StackPane();
        landingIndicators.getStyleClass().add("empty");
        landingIndicators.setAlignment(Pos.CENTER);
        displayStackPane.getChildren().add(landingIndicators);


        // LDA HBox
        ldaBox = new HBox();
        ldaBox.getStyleClass().add("empty");
        ldaBox.setAlignment(Pos.CENTER_LEFT);
        landingIndicators.getChildren().add(ldaBox);

        DashedLine ldaStart = new DashedLine(0.1, 500);
        DashedLine ldaEnd = new DashedLine(0.1, 500);

        VBox ldaDistanceBox = new VBox();
        ldaDistanceBox.getStyleClass().add("empty");
        ldaDistanceBox.prefWidthProperty().bindBidirectional(displayLDA);
        ldaDistanceBox.setMaxHeight(500);
        ldaDistanceBox.setAlignment(Pos.TOP_CENTER);
        DashedLine ldaArrow = new DashedLine(displayLDA.get(), 0.1, false);
        Text ldaText = new Text("LDA");
        ldaText.getStyleClass().add("arrow-text");
        ldaDistanceBox.getChildren().addAll(new EmptyVBox(0.1, 150), ldaArrow, ldaText);

        HBox borderToLDA = new HBox();
        borderToLDA.getStyleClass().add("empty");
        borderToLDA.setPrefWidth(displayBorderToRunway.getValue() + displayStopWayLength.getValue() - 1);

        ldaBox.getChildren().addAll(borderToLDA, ldaStart, ldaDistanceBox, ldaEnd);

        // RESA HBox
        resaBox = new HBox();
        resaBox.getStyleClass().add("empty");
        resaBox.setAlignment(Pos.CENTER_LEFT);

        DashedLine resaStart = new DashedLine(0.1, 150);
        DashedLine resaEnd = new DashedLine(0.1, 150);

        VBox resaDistanceBox = new VBox();
        resaDistanceBox.getStyleClass().add("empty");
        resaDistanceBox.prefWidthProperty().bindBidirectional(displayRESA);
        resaDistanceBox.setMaxHeight(500);
        resaDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine resaArrow = new DashedLine(displayRESA.get(), 0.1, false);
        Text resaText = new Text("RESA");
        resaText.getStyleClass().add("arrow-text");
        resaText.setStyle("-fx-font-size: 10");
        resaDistanceBox.getChildren().addAll(resaArrow, resaText, new EmptyVBox(0.1, 200));

        HBox borderToRESA = new HBox();
        borderToRESA.getStyleClass().add("empty");
        borderToRESA.prefWidthProperty().bind(Bindings.subtract(Bindings.subtract(Bindings.add(displayBorderToRunway, Bindings.add(displayRunwayToPlane, Bindings.add(planeImageView.getFitWidth(), displayPlaneToObstacle))), displayRESA),1));

        resaBox.getChildren().addAll(borderToRESA, resaStart, resaDistanceBox, resaEnd);

        // Blast Allowance HBox
        blastAllowanceBox = new HBox();
        blastAllowanceBox.getStyleClass().add("empty");
        blastAllowanceBox.setAlignment(Pos.CENTER_LEFT);

        DashedLine blastAllowanceStart = new DashedLine(0.1, 150);
        DashedLine blastAllowanceEnd = new DashedLine(0.1, 150);

        VBox blastAllowanceDistanceBox = new VBox();
        blastAllowanceDistanceBox.getStyleClass().add("empty");
        blastAllowanceDistanceBox.prefWidthProperty().bindBidirectional(displayBlastAllowance);
        blastAllowanceDistanceBox.setMaxHeight(500);
        blastAllowanceDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine blastAllowanceArrow = new DashedLine(displayBlastAllowance.get(), 0.1, false);
        Text blastAllowanceText = new Text("Blast\nAllowance");
        blastAllowanceText.getStyleClass().add("arrow-text");
        blastAllowanceText.setStyle("-fx-font-size: 10");
        blastAllowanceDistanceBox.getChildren().addAll(blastAllowanceArrow, blastAllowanceText, new EmptyVBox(0.1, 200));

        HBox borderToBlastAllowance = new HBox();
        borderToBlastAllowance.getStyleClass().add("empty");
        borderToBlastAllowance.prefWidthProperty().bind(Bindings.add(displayBorderToRunway, Bindings.add(displayRunwayToPlane, planeImageView.getFitWidth())));
        blastAllowanceBox.getChildren().addAll(borderToBlastAllowance, blastAllowanceStart, blastAllowanceDistanceBox, blastAllowanceEnd);


        return displayStackPane;
    }

    /**
     * Create the menu box
     * @return the menu box
     */

    private HBox makeMenuBox() {
        // Create Menus
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(new MenuItem("Import XML"), new MenuItem("Export XML"), new MenuItem("Export Report"));

        // View Menu
        Menu viewMenu = new Menu("View");
        // Switch to side view
        MenuItem sideView = new MenuItem("Side View");
        sideView.setOnAction(e -> homeWindow.startSideView());
        // Switch to simultaneous view
        MenuItem simultaneous = new MenuItem("Simultaneous");
        simultaneous.setOnAction(e -> homeWindow.startSimultaneousView());
        // Add menu items
        viewMenu.getItems().addAll(sideView, simultaneous);

        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem colourSettings = new MenuItem("Colour Schemes");

        helpMenu.getItems().addAll(new MenuItem("About"), new MenuItem("Contact"), colourSettings);

        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);

        // Create Log out button
        Button logoutButton = new Button("Log out");
        //logoutButton.setOnAction(e -> homeWindow.startLogin());
        logoutButton.getStyleClass().add("logout-button");

        // Empty box to push the logout button to the right
        HBox empty = new HBox();
        empty.getStyleClass().add("empty");


        HBox.setHgrow(empty, Priority.ALWAYS); // This will push the logout button to the right

        HBox result = new HBox(menuBar, empty, logoutButton);
        HBox.setHgrow(result, Priority.ALWAYS);
        return (result);
    }







}
