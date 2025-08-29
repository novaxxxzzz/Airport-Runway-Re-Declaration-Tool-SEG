package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;

import java.io.InputStream;

import javafx.util.Duration;
import uk.ac.soton.comp2211.runwayredeclaration.Calculator.RunwayCalculator;
import uk.ac.soton.comp2211.runwayredeclaration.Component.*;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
import uk.ac.soton.comp2211.runwayredeclaration.XMLHandler.xmlFileLoader;


import java.util.ArrayList;


/**
 * A Base Scene used in the game. Handles common functionality between all scenes.
 */
public abstract class BaseScene {
    protected User currentUser;

    protected final HomeWindow homeWindow;

    protected HomePane root;
    protected Scene scene;

    // Airports
    protected String currentView;
    protected ArrayList<Airport> airportList;
    protected Airport currentAirport;
    protected Runway currentRunway;
    protected SubRunway subRunway1;
    protected SubRunway subRunway2;
    protected SubRunway currentSubRunway;
    protected Obstacle currentObstacle;
    // Obstacles
    protected ArrayList<Obstacle> obstacleList;

    protected DoubleProperty stopWayLength1 = new SimpleDoubleProperty();
    protected DoubleProperty stopWayLength2 = new SimpleDoubleProperty();
    protected DoubleProperty clearWayLength1 = new SimpleDoubleProperty();
    protected DoubleProperty clearWayLength2 = new SimpleDoubleProperty();
    protected DoubleProperty displayStopWayLength = new SimpleDoubleProperty(60); // Need to be rescaled
    protected DoubleProperty displayClearWayLength = new SimpleDoubleProperty(80); // Need to be rescaled
    protected DoubleProperty displayRunwayLength = new SimpleDoubleProperty(550); // FIXED 550


    protected DoubleProperty displayTORA = new SimpleDoubleProperty(displayRunwayLength.getValue());
    protected DoubleProperty displayBorderToTORA = new SimpleDoubleProperty();
    protected DoubleProperty displayTORA2 = new SimpleDoubleProperty(displayRunwayLength.getValue());
    protected DoubleProperty displayBorderToTORA2 = new SimpleDoubleProperty();
    protected DoubleProperty displayTODA = new SimpleDoubleProperty(displayRunwayLength.getValue()  + displayClearWayLength.getValue() );
    protected DoubleProperty displayTODA2 = new SimpleDoubleProperty(displayRunwayLength.getValue()  + displayClearWayLength.getValue() );
    protected DoubleProperty displayBorderToTODA = new SimpleDoubleProperty();
    protected DoubleProperty displayBorderToTODA2 = new SimpleDoubleProperty();
    protected DoubleProperty displayASDA = new SimpleDoubleProperty(displayRunwayLength.getValue()  + displayStopWayLength.getValue() );
    protected DoubleProperty displayASDA2 = new SimpleDoubleProperty(displayRunwayLength.getValue()  + displayStopWayLength.getValue() );
    protected DoubleProperty displayBorderToASDA = new SimpleDoubleProperty();
    protected DoubleProperty displayBorderToASDA2 = new SimpleDoubleProperty();
    protected DoubleProperty displayLDA = new SimpleDoubleProperty(displayRunwayLength.getValue() );
    protected DoubleProperty displayLDA2 = new SimpleDoubleProperty(displayRunwayLength.getValue() );
    protected DoubleProperty displayBorderToLDA = new SimpleDoubleProperty();
    protected DoubleProperty displayBorderToLDA2 = new SimpleDoubleProperty();


    // The blast protection is fixed to be 60m, display length is 50

    protected DoubleProperty displayBlastAllowance = new SimpleDoubleProperty(60);
    protected DoubleProperty displayBorderToBlastAllowance = new SimpleDoubleProperty(0);
    protected DoubleProperty displayBorderToBlastAllowance2 = new SimpleDoubleProperty(0);

    // The RESA is fixed to be 240m. display length is 100

    protected DoubleProperty displayRESA = new SimpleDoubleProperty(80);
    protected DoubleProperty displayBorderToRESA = new SimpleDoubleProperty(0);
    protected DoubleProperty displayBorderToRESA2 = new SimpleDoubleProperty(0);

    // The strip end is fixed to be 60m. display length is 50

    protected DoubleProperty displayStripEnd = new SimpleDoubleProperty(50);
    protected DoubleProperty displayBorderToStripEnd = new SimpleDoubleProperty(0);
    protected DoubleProperty displayBorderToStripEnd2 = new SimpleDoubleProperty(0);


    protected DoubleProperty displayDisplacedThreshold1 = new SimpleDoubleProperty(0);
    protected DoubleProperty displayDisplacedThreshold2 = new SimpleDoubleProperty(0);


    protected DoubleProperty displayRunwayToPlane = new SimpleDoubleProperty(0);
    protected DoubleProperty displayPlaneToObstacle = new SimpleDoubleProperty(0);

    protected DoubleProperty displayBorderToRunway = new SimpleDoubleProperty();
    protected DoubleProperty displayBorderToObstacle = new SimpleDoubleProperty(0);
    protected DoubleProperty displayBorderToPlane = new SimpleDoubleProperty(0);
    protected DoubleProperty displayBorderToPlane2 = new SimpleDoubleProperty(0);
    protected DoubleProperty displayBorderToStopway = new SimpleDoubleProperty();
    protected DoubleProperty displayPlaneWidth = new SimpleDoubleProperty(60);


    protected HBox toraBox;
    protected HBox todaBox;
    protected HBox ldaBox;
    protected HBox asdaBox;
    protected HBox blastAllowanceBox;
    protected HBox resaBox;
    protected HBox stripEndBox;

    protected HBox toraBox2;
    protected HBox todaBox2;
    protected HBox ldaBox2;
    protected HBox asdaBox2;
    protected HBox blastAllowanceBox2;
    protected HBox resaBox2;
    protected HBox stripEndBox2;
    protected HBox displacedThresholdBox = new HBox();


    public String operationSelected = null;
    public SimpleStringProperty directionSelected = new SimpleStringProperty("(...)");

    public ToggleGroup operationButtons = new ToggleGroup();
    public ToggleGroup directionButtons = new ToggleGroup();
    private Text clearwayLengthDisplay;
    private Text stopwayLengthDisplay;
    private Text thresholdLengthDisplay;


    ToggleGroup group = new ToggleGroup();
    RadioButton defaultOption = new RadioButton("Default (Blue/Green)");
    RadioButton option1 = new RadioButton("Blue/Yellow");
    RadioButton option2 = new RadioButton("Magenta/Lime Green");
    RadioButton option3 = new RadioButton("Cyan/Deep Purple");

    protected CurrentState currentState = new CurrentState();

    protected HBox obstacleBox = new HBox();
    protected HBox planeBox = new HBox();
    protected HBox planeBox2 = new HBox();
    protected StackPane indicatorsSubRunway1 = new StackPane();
    protected StackPane indicatorsSubRunway2 = new StackPane();
    protected RadioButton firstDirectionButton;
    protected RadioButton secondDirectionButton;
    private Button buttonTORA;
    private Button buttonASDA;
    private Button buttonTODA;
    private Button buttonLDA;
    private Button[] allButtons;
    private TextArea displayArea;

    private Label lblRecalculated = new Label("New Values");
    private Label lblOriginal = new Label("Original Values");
    protected ComboBox<Airport> comboAirports;
    protected ComboBox<Obstacle> comboObstacles;

    protected SimpleStringProperty notificationMessage = new SimpleStringProperty();
    protected Label notificationLabel = new Label();
    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     * @param homeWindow the home window
     */
    public BaseScene(HomeWindow homeWindow) {
        this.homeWindow = homeWindow;



        InputStream airport_file = BaseScene.class.getResourceAsStream("/predefined/Airport.xml");
        InputStream obstacle_file = BaseScene.class.getResourceAsStream("/predefined/Obstacle.xml");
        // Load Airports
        this.airportList = xmlFileLoader.loadAirports(airport_file);
        // Load Obstacles
        this.obstacleList = xmlFileLoader.loadObstacles(obstacle_file);

        currentAirport = airportList.get(0);
        currentRunway = currentAirport.getRunways().get(0);





        // Set the runway length
        displayBorderToRunway.setValue((homeWindow.getWidth() - 600 - displayRunwayLength.getValue()) / 2);
        displayBorderToStopway.setValue((homeWindow.getWidth() - 600 - displayRunwayLength.getValue() - displayStopWayLength.getValue() * 2) / 2);


        displayBorderToPlane.set(displayBorderToRunway.get() - displayPlaneWidth.get());
        displayBorderToPlane2.set(displayBorderToRunway.get() - displayPlaneWidth.get());

        planeBox.visibleProperty().set(false);
        planeBox2.visibleProperty().set(false);


        notificationLabel.setStyle("-fx-text-fill: grey; -fx-font-size:34px;");


    }

    /**
     * Initialise this scene. Called after creation
     */
    public abstract void initialise();

    /**
     * Build the layout of the scene
     */
    public abstract void build();



    /**
     * Create the results Titled Pane, This is the top box in the left box
     * @return TitledPane the results Titled Pane
     */
    public TitledPane makeResultsTPane(){
        TitledPane tpane = new TitledPane();
        tpane.setText("Results:");
        tpane.setCollapsible(true);



        GridPane gpanething = new GridPane();
        gpanething.setPadding(new Insets(5,0,5,0));
        gpanething.setHgap(2);
        gpanething.setVgap(10);
        gpanething.getStyleClass().add("gridPane");
        String[] headers = {"Runway", "TORA", "TODA", "LDA", "ASDA"};

        // Set column constraints for each header, ensuring they grow to fill space and content is centered
        for (int i = 0; i < headers.length; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.ALWAYS); // Allow column to grow
            columnConstraints.setHalignment(HPos.CENTER); // Center align content
            gpanething.getColumnConstraints().add(columnConstraints); // Add constraints to column
        }

        for (int i = 0; i < headers.length; i++) {
            Label label = new Label(headers[i]);
            label.getStyleClass().add("header-label");
            gpanething.add(label, i, 0);
            GridPane.setHalignment(label, HPos.CENTER); // For horizontal alignment
            GridPane.setValignment(label, VPos.CENTER); // For vertical alignment
        }

//        Label lblOriginal = new Label("Original Values");
        lblOriginal.setMaxWidth(Double.MAX_VALUE);
        lblOriginal.getStyleClass().add("center-label-blue");
        GridPane.setColumnSpan(lblOriginal, GridPane.REMAINING);
        GridPane.setHalignment(lblOriginal, HPos.CENTER);
        gpanething.add(lblOriginal, 0, 1);

        // Original Values:
        Label runway1 = new Label();
        runway1.textProperty().bind(subRunway1.getDesignator());
        runway1.getStyleClass().add("grid-pane-label");
        gpanething.add(runway1, 0, 2);

        Label tora_original1 = new Label();
        tora_original1.textProperty().bind(subRunway1.getOriginalTORA().asString());
        tora_original1.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_original1, 1, 2);

        Label toda_original1 = new Label();
        toda_original1.textProperty().bind(subRunway1.getOriginalTODA().asString());
        toda_original1.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_original1, 2, 2);

        Label lda_original1 = new Label();
        lda_original1.textProperty().bind(subRunway1.getOriginalLDA().asString());
        lda_original1.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_original1, 3, 2);

        Label asda_original1 = new Label();
        asda_original1.textProperty().bind(subRunway1.getOriginalASDA().asString());
        asda_original1.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_original1, 4, 2);


        Label runway2 = new Label();
        runway2.textProperty().bind(subRunway2.getDesignator());
        runway2.getStyleClass().add("grid-pane-label");
        gpanething.add(runway2, 0, 3);

        Label tora_original2 = new Label();
        tora_original2.textProperty().bind(subRunway2.getOriginalTORA().asString());
        tora_original2.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_original2, 1, 3);

        Label toda_original2 = new Label();
        toda_original2.textProperty().bind(subRunway2.getOriginalTODA().asString());
        toda_original2.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_original2, 2, 3);

        Label lda_original2= new Label();
        lda_original2.textProperty().bind(subRunway2.getOriginalLDA().asString());
        lda_original2.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_original2, 3, 3);

        Label asda_original2 = new Label();
        asda_original2.textProperty().bind(subRunway2.getOriginalASDA().asString());
        asda_original2.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_original2, 4, 3);


//        Label lblRecalculated = new Label("New Values");
        lblRecalculated.setMaxWidth(Double.MAX_VALUE);
        lblRecalculated.getStyleClass().add("center-label-blue");
        GridPane.setColumnSpan(lblRecalculated, GridPane.REMAINING);
        GridPane.setHalignment(lblRecalculated, HPos.CENTER);
        gpanething.add(lblRecalculated, 0, 4);

        // Recalculated Values:
        Label runway1_recalculated = new Label();
        runway1_recalculated.textProperty().bind(subRunway1.getDesignator());
        runway1_recalculated.getStyleClass().add("grid-pane-label");
        gpanething.add(runway1_recalculated, 0, 5);

        Label tora_recalculated1 = new Label();
        tora_recalculated1.textProperty().bind(subRunway1.getTORA().asString());
        tora_recalculated1.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_recalculated1, 1, 5);

        Label toda_recalculated1 = new Label();
        toda_recalculated1.textProperty().bind(subRunway1.getTODA().asString());
        toda_recalculated1.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_recalculated1, 2, 5);

        Label lda_recalculated1 = new Label();
        lda_recalculated1.textProperty().bind(subRunway1.getLDA().asString());
        lda_recalculated1.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_recalculated1, 3, 5);

        Label asda_recalculated1 = new Label();
        asda_recalculated1.textProperty().bind(subRunway1.getASDA().asString());
        asda_recalculated1.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_recalculated1, 4, 5);

        Label runway2_recalculated = new Label();
        runway2_recalculated.textProperty().bind(subRunway2.getDesignator());
        runway2_recalculated.getStyleClass().add("grid-pane-label");
        gpanething.add(runway2_recalculated, 0, 6);

        Label tora_recalculated2 = new Label();
        tora_recalculated2.textProperty().bind(subRunway2.getTORA().asString());
        tora_recalculated2.getStyleClass().add("grid-pane-label");
        gpanething.add(tora_recalculated2, 1, 6);

        Label toda_recalculated2 = new Label();
        toda_recalculated2.textProperty().bind(subRunway2.getTODA().asString());
        toda_recalculated2.getStyleClass().add("grid-pane-label");
        gpanething.add(toda_recalculated2, 2, 6);

        Label lda_recalculated2 = new Label();
        lda_recalculated2.textProperty().bind(subRunway2.getLDA().asString());
        lda_recalculated2.getStyleClass().add("grid-pane-label");
        gpanething.add(lda_recalculated2, 3, 6);

        Label asda_recalculated2 = new Label();
        asda_recalculated2.textProperty().bind(subRunway2.getASDA().asString());
        asda_recalculated2.getStyleClass().add("grid-pane-label");
        gpanething.add(asda_recalculated2, 4, 6);



        tpane.setContent(gpanething);
        return tpane;
    }

    /**
     * Create the calculation breakdown Titled Pane, This is the bottom box in the left box
     * @return TitledPane the calculation breakdown Titled Pane
     */
    public TitledPane makeCalcBreakTPane(){
        TitledPane tpane2 = new TitledPane();
        tpane2.setText("Calculation Breakdown:");
        tpane2.setCollapsible(true);

        displayArea = new TextArea();
        displayArea.setEditable(false);
        displayArea.setWrapText(true); // Set wrapText property to true
        displayArea.setPrefHeight(600);

        // Create Buttons
        buttonTORA = new Button("TORA");
        buttonTODA = new Button("TODA");
        buttonLDA = new Button("LDA");
        buttonASDA = new Button("ASDA");

        // Apply the normal style to all buttons initially
        buttonTORA.getStyleClass().add("button-blue");
        buttonTODA.getStyleClass().add("button-blue");
        buttonLDA.getStyleClass().add("button-blue");
        buttonASDA.getStyleClass().add("button-blue");

        // Button Actions
        allButtons = new Button[]{buttonTORA, buttonTODA, buttonLDA, buttonASDA};

        // Button Actions
        for (Button button : allButtons) {
            button.setOnAction(e -> {
                updateButtonStyles(button, allButtons, displayArea);
            });
        }
        HBox buttonLayout = new HBox(5);
        buttonLayout.setPadding(new Insets(5));
        buttonLayout.getChildren().addAll(buttonTORA, buttonTODA, buttonLDA, buttonASDA);

        VBox vbox = new VBox(buttonLayout, displayArea);
        tpane2.setContent(vbox);

        return tpane2;
    }

    private void updateButtonStyles(Button selectedButton, Button[] allButtons, TextArea displayArea) {
        boolean isSelected = selectedButton.getStyleClass().contains("button-selected");
        for (Button btn : allButtons) {
//            if (currentState.getColourSettting() == "Default (Blue/Green)"){
//                btn.getStyleClass().remove("button-selected-blue");
//            }else if (currentState.getColourSettting() == "Blue/Yellow"){
//                btn.getStyleClass().remove("button-selected-blue");
//            }else if (currentState.getColourSettting() == "Magenta/Lime Green"){
//                btn.getStyleClass().remove("button-selected-green");
//            }else if (currentState.getColourSettting() == "Cyan/Deep Purple"){
//                btn.getStyleClass().remove("button-selected-purple");
//            }
            btn.getStyleClass().remove("button-selected");

        }

        // If the selected button was not already highlighted, highlight it
        if (!isSelected) {

            selectedButton.getStyleClass().add("button-selected");
            try {
                if (selectedButton.getText().equals("TORA")) {
                    if (firstDirectionButton.isSelected()) {
                        displayArea.setText(RunwayCalculator.breakdownTORA(subRunway1, currentObstacle, subRunway1.getObstacleDistance()));
                    } else if (secondDirectionButton.isSelected()) {
                        displayArea.setText(RunwayCalculator.breakdownTORA(subRunway2, currentObstacle, subRunway2.getObstacleDistance()));
                    }

                } else if (selectedButton.getText().equals("TODA")) {
                    if (firstDirectionButton.isSelected()) {
                        displayArea.setText(RunwayCalculator.breakdownTODA(subRunway1, currentObstacle, subRunway1.getObstacleDistance()));
                    } else if (secondDirectionButton.isSelected()) {
                        displayArea.setText(RunwayCalculator.breakdownTODA(subRunway2, currentObstacle, subRunway2.getObstacleDistance()));
                    }

                } else if (selectedButton.getText().equals("ASDA")) {
                    if (firstDirectionButton.isSelected()) {
                        displayArea.setText(RunwayCalculator.breakdownASDA(subRunway1, currentObstacle, subRunway1.getObstacleDistance()));
                    } else if (secondDirectionButton.isSelected()) {
                        displayArea.setText(RunwayCalculator.breakdownASDA(subRunway2, currentObstacle, subRunway2.getObstacleDistance()));
                    }

                } else if (selectedButton.getText().equals("LDA")) {
                    if (firstDirectionButton.isSelected()) {
                        displayArea.setText(RunwayCalculator.breakdownLDA(subRunway1, currentObstacle, subRunway1.getObstacleDistance()));
                    } else if (secondDirectionButton.isSelected()) {
                        displayArea.setText(RunwayCalculator.breakdownLDA(subRunway2, currentObstacle, subRunway2.getObstacleDistance()));
                    }

                }
            } catch(Exception e){}

            if (this instanceof SimultaneousScene){
                return;
            }




            if (selectedButton.getText().equals("TORA")){
                indicatorsSubRunway1.getChildren().clear();
                indicatorsSubRunway2.getChildren().clear();
                indicatorsSubRunway1.getChildren().add(toraBox);
                indicatorsSubRunway2.getChildren().add(toraBox2);

            }
            else if (selectedButton.getText().equals("TODA")){
                indicatorsSubRunway1.getChildren().clear();
                indicatorsSubRunway2.getChildren().clear();
                indicatorsSubRunway1.getChildren().add(todaBox);
                indicatorsSubRunway2.getChildren().add(todaBox2);

            }
            else if (selectedButton.getText().equals("ASDA")){
                indicatorsSubRunway1.getChildren().clear();
                indicatorsSubRunway2.getChildren().clear();
                indicatorsSubRunway1.getChildren().add(asdaBox);
                indicatorsSubRunway2.getChildren().add(asdaBox2);

            }
            else if (selectedButton.getText().equals("LDA")){
                indicatorsSubRunway1.getChildren().clear();
                indicatorsSubRunway2.getChildren().clear();
                indicatorsSubRunway1.getChildren().add(ldaBox);
                indicatorsSubRunway2.getChildren().add(ldaBox2);
            }


        } else {
            displayArea.clear();
            // If it was already selected, clear the text area as the button is deselected

            indicatorsSubRunway1.getChildren().clear();
            indicatorsSubRunway1.getChildren().addAll(toraBox, todaBox, ldaBox, asdaBox, stripEndBox,blastAllowanceBox, resaBox);

            indicatorsSubRunway2.getChildren().clear();
            indicatorsSubRunway2.getChildren().addAll(toraBox2, todaBox2, ldaBox2, asdaBox2,stripEndBox2, blastAllowanceBox2, resaBox2);


        }

    }




    /**
     * Create the airport Titled Pane, This is the top box in the right box
     * @return TitledPane the airport Titled Pane
     */
    public TitledPane makeAirportTPane(){
        GridPane airportGrid = new GridPane();
        airportGrid.getStyleClass().add("full-width-grid");

        airportGrid.setVgap(5);
        airportGrid.setHgap(25);

        TitledPane airportTPane = new TitledPane();
        airportTPane.setText("Airport:");
        airportTPane.setCollapsible(true);

        ComboBox<String> comboRunways = new ComboBox<>();
        for (Runway runway : currentAirport.getRunways()){
            comboRunways.getItems().add(runway.getName());
        }
        comboRunways.setValue(currentRunway.getName());
        // If the runway is changed, update the subRunway indicators
        comboRunways.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                for (Runway runway : currentAirport.getRunways()){

                    if (runway.getName().equals(newValue)){

                        notificationMessage.set("Successfully Changed Runway");
                        FadeTransition ft = new FadeTransition(Duration.millis(3000), notificationLabel);
                        ft.setFromValue(1.0);
                        ft.setToValue(0.0);
                        ft.play();

                        clearAllButtons();
                        planeBox.visibleProperty().set(false);
                        planeBox2.visibleProperty().set(false);

                        displayBorderToObstacle.set(0);

                        obstacleBox.getChildren().clear();

                        currentRunway = runway;


                        subRunway1.update(currentRunway.getSubRunways().get(0));
                        subRunway2.update(currentRunway.getSubRunways().get(1));

                        // Make displaced threshold indicator
                        displacedThresholdBox.getChildren().clear();
                        displacedThresholdBox.setAlignment(Pos.CENTER_LEFT);
                        double ratio1 = (subRunway1.getDisplacedThreshold().get() / subRunway1.getOriginalTORA().get());
                        double displayDisplacedThreshold1 = displayRunwayLength.getValue() * ratio1;

                        double ratio2 = (subRunway2.getDisplacedThreshold().get() / subRunway2.getOriginalTORA().get());
                        double displayDisplacedThreshold2 = displayRunwayLength.getValue() * ratio2;

                        HBox borderToDisplacedThreshold1 = new HBox();
                        borderToDisplacedThreshold1.getStyleClass().add("empty");
                        borderToDisplacedThreshold1.prefWidthProperty().bind(Bindings.add(displayBorderToRunway, displayDisplacedThreshold1));

                        DashedLine thresholdLine1 = new DashedLine(0.1, 500, "red");
                        thresholdLine1.visibleProperty().set(ratio1 != 0);

                        HBox emptyBox1 = new HBox();
                        emptyBox1.getStyleClass().add("empty");
                        HBox.setHgrow(emptyBox1, Priority.ALWAYS);

                        DashedLine thresholdLine2 = new DashedLine(0.1, 500);
                        thresholdLine2.visibleProperty().set(ratio2 != 0);

                        HBox borderToDisplacedThreshold2 = new HBox();
                        borderToDisplacedThreshold2.getStyleClass().add("empty");
                        borderToDisplacedThreshold2.prefWidthProperty().bind(Bindings.add(displayBorderToRunway, displayDisplacedThreshold2));


                        displacedThresholdBox.getChildren().addAll(borderToDisplacedThreshold1, thresholdLine1, emptyBox1, thresholdLine2, borderToDisplacedThreshold2);


                        displayBorderToTORA.set(displayBorderToRunway.get());
                        displayBorderToTODA.set(displayBorderToRunway.get());
                        displayBorderToASDA.set(displayBorderToRunway.get());
                        displayBorderToLDA.set(displayBorderToRunway.get());

                    }
                }
            }
        });

        comboAirports = new ComboBox<>();
        for (Airport airport : airportList){
            comboAirports.getItems().add(airport);
        }

        comboAirports.setValue(currentAirport);
        comboAirports.setOnAction(e -> {
            for (Airport airport : airportList){
                if (airport.getName().equals(comboAirports.getValue().getName())){



                    clearAllButtons();
                    obstacleBox.getChildren().clear();
                    planeBox.visibleProperty().set(false);
                    planeBox2.visibleProperty().set(false);

                    displayBorderToObstacle.set(0);

                    // Hide all indicators
                    indicatorsSubRunway1.getChildren().clear();
                    indicatorsSubRunway2.getChildren().clear();

                    currentAirport = airport;
                    currentRunway = currentAirport.getRunways().get(0);

                    comboRunways.getItems().clear();
                    for (Runway runway : currentAirport.getRunways()){
                        comboRunways.getItems().add(runway.getName());
                    }
                    comboRunways.setValue(currentRunway.getName());

                    subRunway1.update(currentRunway.getSubRunways().get(0));
                    subRunway2.update(currentRunway.getSubRunways().get(1));

                    stopwayLengthDisplay.setText("(Select designator to show)");
                    clearwayLengthDisplay.setText("(Select designator to show)");
                    thresholdLengthDisplay.setText("(Select designator to show)");

                    displayBorderToTORA.set(displayBorderToRunway.get());
                    displayBorderToTODA.set(displayBorderToRunway.get());
                    displayBorderToASDA.set(displayBorderToRunway.get());
                    displayBorderToLDA.set(displayBorderToRunway.get());

                    notificationMessage.set("Successfully Changed Airport");
                    FadeTransition ft = new FadeTransition(Duration.millis(3000), notificationLabel);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.0);
                    ft.play();


                }
            }

        });

//        comboAirports.valueProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
//                for (Airport airport : airportList){
//                    if (airport.getName().equals(newValue)){
//                        clearAllButtons();
//                        obstacleBox.getChildren().clear();
//                        planeBox.visibleProperty().set(false);
//                        planeBox2.visibleProperty().set(false);
//
//                        displayBorderToObstacle.set(0);
//
//                        // Hide all indicators
//                        indicatorsSubRunway1.getChildren().clear();
//                        indicatorsSubRunway2.getChildren().clear();
//
//                        currentAirport = airport;
//                        currentRunway = currentAirport.getRunways().get(0);
//
//                        comboRunways.getItems().clear();
//                        for (Runway runway : currentAirport.getRunways()){
//                            comboRunways.getItems().add(runway.getName());
//                        }
//                        comboRunways.setValue(currentRunway.getName());
//
//                        subRunway1.update(currentRunway.getSubRunways().get(0));
//                        subRunway2.update(currentRunway.getSubRunways().get(1));
//
//                        stopwayLengthDisplay.setText("(Select designator to show)");
//                        clearwayLengthDisplay.setText("(Select designator to show)");
//                        thresholdLengthDisplay.setText("(Select designator to show)");
//
//                        displayBorderToTORA.set(displayBorderToRunway.get());
//                        displayBorderToTODA.set(displayBorderToRunway.get());
//                        displayBorderToASDA.set(displayBorderToRunway.get());
//                        displayBorderToLDA.set(displayBorderToRunway.get());
//
//
//
//
//                    }
//                }
//
//
//            }
//        });




        airportGrid.addRow(0, new Label("Airport:"), comboAirports);
        airportGrid.addRow(1, new Label("Runway:"), comboRunways);

        thresholdLengthDisplay = new Text("Select designator to show");
        airportGrid.addRow(2, new Label("Threshold:"), thresholdLengthDisplay);
        clearwayLengthDisplay = new Text("Select designator to show");
        airportGrid.addRow(3, new Label("Clearway:"), clearwayLengthDisplay);
        stopwayLengthDisplay = new Text("Select designator to show");
        airportGrid.addRow(4, new Label("Stopway:"), stopwayLengthDisplay);

        airportTPane.setContent(airportGrid);


        return airportTPane;
    }

    /**
     * Create the obstacle Titled Pane, This is the bottom box in the right box
     * @return TitledPane the obstacle Titled Pane
     */
    public TitledPane makeObstacleTPane(){
        GridPane obstacleGrid = new GridPane();
        obstacleGrid.setVgap(5);
        obstacleGrid.setHgap(10);

        obstacleGrid.setPrefWidth(300);
        TitledPane obstacleTPane = new TitledPane();
        obstacleTPane.setText("Add Obstacles:");
        obstacleTPane.setCollapsible(true);

        comboObstacles = new ComboBox<>();
        for (Obstacle o : obstacleList){
            comboObstacles.getItems().add(o);
        }

        //Custom obstacle
        comboObstacles.getItems().add(new Obstacle("Custom"));


        VBox obstacleBox = new VBox(5);

        TextField obstacleHeight = new TextField();
        obstacleBox.getChildren().addAll(new Label("Height (metres):"), obstacleHeight);


        TextField obstacleToCentreLine = new TextField();
        obstacleBox.getChildren().addAll(new Label("Distance from centreline (metres):"), obstacleToCentreLine);

        TextField distanceFromThreshold1 = new TextField();
        TextField distanceFromThreshold2 = new TextField();

        Label distanceFromLabel1 = new Label();
        distanceFromLabel1.textProperty().bind(Bindings.concat("Distance from ", subRunway1.getDesignator() , " threshold (meters):"));

        Label distanceFromLabel2 = new Label();
        distanceFromLabel2.textProperty().bind(Bindings.concat("Distance from ", subRunway2.getDesignator() , " threshold (meters):"));

        obstacleBox.getChildren().addAll(distanceFromLabel1, distanceFromThreshold1, distanceFromLabel2, distanceFromThreshold2);

        comboObstacles.setOnAction(e -> {

            obstacleHeight.clear();
            obstacleToCentreLine.clear();
            distanceFromThreshold1.clear();
            distanceFromThreshold2.clear();


            currentObstacle = comboObstacles.getValue();
            if (!currentObstacle.getName().equals("Custom")){
                obstacleHeight.setText(String.valueOf(currentObstacle.getHeight()));
                obstacleToCentreLine.setText(String.valueOf(currentObstacle.getDistanceToCentreLine()));
                distanceFromThreshold1.setText(String.valueOf(currentObstacle.getDistanceToLowerThreshold()));
                distanceFromThreshold2.setText(String.valueOf(currentObstacle.getDistanceToHigherThreshold()));
            }


            subRunway1.setTORA(subRunway1.getOriginalTORA().get());
            subRunway1.setTODA(subRunway1.getOriginalTODA().get());
            subRunway1.setASDA(subRunway1.getOriginalASDA().get());
            subRunway1.setLDA(subRunway1.getOriginalLDA().get());

            subRunway2.setTORA(subRunway2.getOriginalTORA().get());
            subRunway2.setTODA(subRunway2.getOriginalTODA().get());
            subRunway2.setASDA(subRunway2.getOriginalASDA().get());
            subRunway2.setLDA(subRunway2.getOriginalLDA().get());


        });

        // Recalculate Button
        Button recalculateB = new Button("Recalculate");
        recalculateB.setMaxWidth(Double.MAX_VALUE);
        recalculateB.setOnAction(e -> {
            String heightText = obstacleHeight.getText();
            String distanceToCentrelineText = obstacleToCentreLine.getText();
            String distanceTextD1 = distanceFromThreshold1.getText();
            String distanceTextD2 = distanceFromThreshold2.getText();
            for (Button btn : allButtons){
                btn.getStyleClass().remove("button-selected");
            }
            displayArea.clear();


            double height = 0;
            double distanceToCentreline = 0;
            double distance1 = 0;
            double distance2 = 0;

            String errorMessage = "";

            // Clear all fields if parsing fails
            try {
                height = Double.parseDouble(heightText);
            } catch (NumberFormatException f) {
                errorMessage = errorMessage +  "Invalid height.\n";
                obstacleHeight.clear();
            }

            try {
                distanceToCentreline = Double.parseDouble(distanceToCentrelineText);
            } catch (NumberFormatException f) {
                errorMessage = errorMessage + "Invalid distance from centreline.\n";
                obstacleToCentreLine.clear();
            }

            try {
                distance1 = Double.parseDouble(distanceTextD1);
            } catch (NumberFormatException f) {
                errorMessage = errorMessage + "Invalid distance from " + subRunway1.getDesignator().get() + " .\n";
                distanceFromThreshold1.clear();
            }

            try {
                distance2 = Double.parseDouble(distanceTextD2);
            } catch (NumberFormatException f) {
                errorMessage = errorMessage + "Invalid distance from " + subRunway2.getDesignator().get() + " .\n";
                distanceFromThreshold2.clear();
            }

            if (height < 0){
                errorMessage = errorMessage + "Obstacle height cannot be negative.\n";
                obstacleHeight.clear();
            }

            if (height < 0.01){
                errorMessage = errorMessage + "Obstacle height is too small.\n";
                obstacleHeight.clear();
            }

            if (height > 100){
                errorMessage = errorMessage + "Obstacle height is unreasonable.\n";
                obstacleHeight.clear();
            }

            if (distanceToCentreline > 75 || distanceToCentreline < -75){
                errorMessage = errorMessage + "Distance from centreline exceeds consideration zone.\n";
                obstacleToCentreLine.clear();
            }

            if (distance1 + subRunway1.getDisplacedThreshold().get() - subRunway1.getOriginalTORA().get() > 60 || distance1  + subRunway1.getDisplacedThreshold().get() < -60){
                errorMessage = errorMessage + "Distance from " + subRunway1.getDesignator().get() + " threshold exceeds consideration zone.\n";
                distanceFromThreshold1.clear();
            }

            if (distance2 + subRunway2.getDisplacedThreshold().get() - subRunway2.getOriginalTORA().get() > 60 || distance2 + subRunway2.getDisplacedThreshold().get() < -60){
                errorMessage = errorMessage + "Distance from " + subRunway2.getDesignator().get() + " threshold exceeds consideration zone.\n";
                distanceFromThreshold2.clear();
            }


            if (currentObstacle == null){
                errorMessage = errorMessage + "No obstacle is selected.\n";
            }

            double calculateRunwayLength = distance1 + subRunway1.getDisplacedThreshold().get() + distance2 + subRunway2.getDisplacedThreshold().get();

            if (calculateRunwayLength > subRunway1.getOriginalTORA().get() + 120 && calculateRunwayLength > subRunway2.getOriginalTORA().get() + 120){
                errorMessage = errorMessage + "Wrong distances from threshold.\n";
                distanceFromThreshold1.clear();
                distanceFromThreshold2.clear();
            }



            // Output combined error message
            if (!errorMessage.equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText(errorMessage + "Please enter valid values.");
                alert.showAndWait();
                return;
            }

            notificationMessage.set("Successfully Added Obstacle");
            FadeTransition ft = new FadeTransition(Duration.millis(3000), notificationLabel);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.play();

            currentObstacle = new Obstacle(comboObstacles.getValue().getName());
            currentObstacle.setHeight(height);



            subRunway1.setObstacle(currentObstacle, distance1);
            subRunway2.setObstacle(currentObstacle, distance2);


            subRunway1.setTORA(RunwayCalculator.calculateTORA(subRunway1, currentObstacle, distance1));
            subRunway1.setTODA(RunwayCalculator.calculateTODA(subRunway1, currentObstacle, distance1));
            subRunway1.setASDA(RunwayCalculator.calculateASDA(subRunway1, currentObstacle, distance1));
            subRunway1.setLDA(RunwayCalculator.calculateLDA(subRunway1, currentObstacle, distance1));


            subRunway2.setTORA(RunwayCalculator.calculateTORA(subRunway2, currentObstacle, distance2));
            subRunway2.setTODA(RunwayCalculator.calculateTODA(subRunway2, currentObstacle, distance2));
            subRunway2.setASDA(RunwayCalculator.calculateASDA(subRunway2, currentObstacle, distance2));
            subRunway2.setLDA(RunwayCalculator.calculateLDA(subRunway2, currentObstacle, distance2));



            this.obstacleBox.getChildren().clear();

            if (subRunway1.getObstacle() != null){
                double distance = subRunway1.getObstacleDistance();
                double ratio = distance / subRunway1.getOriginalTORA().get();
                double displayObstacleToThreshold;
                if (ratio < 0){// If negative then the obstacle is before the threshold
                    displayObstacleToThreshold = ratio * displayRunwayLength.get() - 30;
                }
                else{
                    displayObstacleToThreshold = ratio * displayRunwayLength.get();
                }

                HBox borderToObstacleBox = new HBox();
                borderToObstacleBox.getStyleClass().add("empty");
                if(subRunway1.getDisplacedThreshold().get() != 0){
                    borderToObstacleBox.prefWidthProperty().bind( Bindings.add(Bindings.add(displayBorderToRunway, displayObstacleToThreshold), displayDisplacedThreshold1));
                }
                else{
                    borderToObstacleBox.prefWidthProperty().bind( Bindings.add(displayBorderToRunway, displayObstacleToThreshold));
                }


                // Obstacle Image
                Image obstacleImage = new Image(getClass().getResource("/images/Obstacle.png").toExternalForm());
                ImageView obstacleImageView = new ImageView(obstacleImage);
                obstacleImageView.setPreserveRatio(true);
                obstacleImageView.setFitWidth(30);

                this.obstacleBox.getChildren().addAll(borderToObstacleBox, obstacleImageView);

            }



            if (firstDirectionButton.selectedProperty().get()){
                System.out.println("First direction selected fired");
                firstDirectionButton.setSelected(false);
                firstDirectionButton.fire();

            }
            else if (secondDirectionButton.selectedProperty().get()) {
                System.out.println("Second direction selected fired");
                secondDirectionButton.setSelected(false);
                secondDirectionButton.fire();
            }


        });


        HBox obstacleLabel = new HBox(30, new Label("Obstacle: "), comboObstacles);

        obstacleGrid.addRow(0, obstacleLabel);
        obstacleGrid.addRow(1, obstacleBox);
        obstacleGrid.addRow(2, recalculateB);


        obstacleGrid.prefWidthProperty().bind(obstacleTPane.widthProperty());
        obstacleTPane.setContent(obstacleGrid);

        return obstacleTPane;
    }







    public TitledPane makeAirplaneTPane() {


        VBox airplaneStuff = new VBox(3);




        RadioButton takeOffRadioButton = new RadioButton("Taking Off");
        takeOffRadioButton.setUserData("takeoff");

        RadioButton landingRadioButton = new RadioButton("Landing");
        landingRadioButton.setUserData("landing");

        //ToggleGroup operationButtons = new ToggleGroup();
        takeOffRadioButton.setToggleGroup(operationButtons);
        landingRadioButton.setToggleGroup(operationButtons);
        operationButtons.selectToggle(null);


        takeOffRadioButton.setOnAction(e -> {
            if (firstDirectionButton.isSelected()){
                planeBox.visibleProperty().set(true);
                planeBox2.visibleProperty().set(false);
            }
            else if (secondDirectionButton.isSelected()){
                planeBox.visibleProperty().set(false);
                planeBox2.visibleProperty().set(true);
            }
            else {
                planeBox.visibleProperty().set(false);
                planeBox2.visibleProperty().set(false);
            }


            if (firstDirectionButton.isSelected()){
                firstDirectionButton.setSelected(false);
                firstDirectionButton.fire();
            }
            else if (secondDirectionButton.isSelected()){
                secondDirectionButton.setSelected(false);
                secondDirectionButton.fire();
            }
        });


        landingRadioButton.setOnAction(e -> {
            if (firstDirectionButton.isSelected()){
                planeBox.visibleProperty().set(true);
                planeBox2.visibleProperty().set(false);
            }
            else if (secondDirectionButton.isSelected()){
                planeBox.visibleProperty().set(false);
                planeBox2.visibleProperty().set(true);
            }
            else {
                planeBox.visibleProperty().set(false);
                planeBox2.visibleProperty().set(false);
            }

            if (firstDirectionButton.isSelected()){
                firstDirectionButton.setSelected(false);
                firstDirectionButton.fire();
            }
            else if (secondDirectionButton.isSelected()){
                secondDirectionButton.setSelected(false);
                secondDirectionButton.fire();
            }

        });

        // Direction buttons
        firstDirectionButton = new RadioButton();
        firstDirectionButton.textProperty().bind(subRunway1.getDesignator());
        firstDirectionButton.setUserData(subRunway1.getDesignator());
        firstDirectionButton.setOnAction(e -> {
            System.out.println("First direction selected");
            clearwayLengthDisplay.setText(subRunway1.getClearwayLength().get() + "m");
            stopwayLengthDisplay.setText(subRunway1.getStopwayLength().get() + "m");
            thresholdLengthDisplay.setText(subRunway1.getDisplacedThreshold().get() + "m");
            currentSubRunway = subRunway1;
            planeBox2.visibleProperty().set(false);

            if (!takeOffRadioButton.isSelected() && !landingRadioButton.isSelected() ){ // if neither operation button is selected
                planeBox.visibleProperty().set(false);

            }
            else { // if one operation is selected
                planeBox.visibleProperty().set(true);
            }


            indicatorsSubRunway1.getChildren().clear();

            indicatorsSubRunway1.getChildren().addAll(toraBox, todaBox, ldaBox, asdaBox, stripEndBox,blastAllowanceBox, resaBox);



            if ( obstacleBox.getChildren().isEmpty() ){ // If there is no obstacle on the runway
                indicatorsSubRunway1.getChildren().remove(resaBox);
                indicatorsSubRunway1.getChildren().remove(stripEndBox);
                indicatorsSubRunway1.getChildren().remove(blastAllowanceBox);

                displayTORA.set(displayRunwayLength.get() - 2);

                if (subRunway1.getDisplacedThreshold().get() != 0){ // If there is a displaced threshold
                    displayBorderToLDA.set(displayBorderToRunway.get() + displayDisplacedThreshold1.get() + 1);
                    displayLDA.set(displayTORA.get() - displayDisplacedThreshold1.get() - 2);

                    if (landingRadioButton.isSelected()){
                        displayBorderToPlane.set(displayBorderToRunway.get() + displayDisplacedThreshold1.get());
                    }
                    else if (takeOffRadioButton.isSelected()){
                        displayBorderToPlane.set(displayBorderToRunway.get() - displayPlaneWidth.get());
                    }

                }
                else {
                    displayBorderToLDA.set(displayBorderToRunway.get());
                    displayLDA.set(displayTORA.get());


                    displayBorderToPlane.set(displayBorderToRunway.get() - displayPlaneWidth.get());

                }


                if (subRunway1.getStopwayLength().get() == 0){
                    displayASDA.set(displayTORA.get());
                }
                else{
                    displayASDA.set(displayTORA.get() - 2 + displayStopWayLength.get());

                }
                if (subRunway1.getClearwayLength().get() == 0){
                    displayTODA.set(displayTORA.get() );
                }
                else{
                    displayTODA.set(displayTORA.get() - 2 + displayClearWayLength.get());
                }


            }
            else{ // If there is an obstacle on the runway


                displayBorderToObstacle.set( ((HBox) obstacleBox.getChildren().get(0)).prefWidthProperty().get() );



                // Check whether the obstacle is close or further away from the threshold
                if (displayBorderToObstacle.get() > (displayBorderToRunway.get() + (displayRunwayLength.get() / 2)) ){
                    // If the obstacle is further away
                    displayBorderToRESA.set(displayBorderToObstacle.get() - displayRESA.get() - 2);
                    resaBox.visibleProperty().set(true);

                    // Check slope calculation and RESA, choose the larger value to display
                    if (currentObstacle.getHeight() * 50 > subRunway1.getRESA().get()){
                        ((Text) ((VBox) resaBox.getChildren().get(2)).getChildren().get(1)).setText("Slope");
                    }
                    else {
                        ((Text) ((VBox) resaBox.getChildren().get(2)).getChildren().get(1)).setText("RESA");
                    }
                    displayBorderToStripEnd.set(displayBorderToRESA.get() - displayStripEnd.get() - 2);

                    stripEndBox.visibleProperty().set(true);

                    displayBorderToTORA.set(displayBorderToRunway.get());
                    displayBorderToTODA.set(displayBorderToTORA.get());
                    displayBorderToASDA.set(displayBorderToTORA.get());


                    displayTORA.set(displayBorderToStripEnd.get() - displayBorderToRunway.get() - 2);
                    displayTODA.set(displayTORA.get());
                    displayASDA.set(displayTORA.get());

                    if (subRunway1.getDisplacedThreshold().get() != 0){
                        displayBorderToLDA.set(displayBorderToRunway.get() + displayDisplacedThreshold1.get());
                        displayLDA.set(displayTORA.get() - displayDisplacedThreshold1.get() - 2);

                        if (landingRadioButton.isSelected()){
                            displayBorderToPlane.set(displayBorderToLDA.get());
                        }
                        else if (takeOffRadioButton.isSelected()){
                            displayBorderToPlane.set(displayBorderToRunway.get() - displayPlaneWidth.get());
                        }
                    }
                    else {
                        displayBorderToLDA.set(displayBorderToRunway.get());
                        displayLDA.set(displayBorderToStripEnd.get() - displayBorderToRunway.get() - 2);

                        displayBorderToPlane.set(displayBorderToRunway.get() - displayPlaneWidth.get());
                    }

                    blastAllowanceBox.visibleProperty().set(false);


                }
                else{
                    // If the obstacle is closer

                    blastAllowanceBox.visibleProperty().set(true);
                    // 30 is the obstacle image view width
                    displayBorderToBlastAllowance.set(displayBorderToObstacle.get() + 30 );

                    displayBorderToTORA.set(displayBorderToBlastAllowance.get() + displayBlastAllowance.get() + 1);
                    displayBorderToTODA.set(displayBorderToTORA.get());
                    displayBorderToASDA.set(displayBorderToTORA.get());



                    displayTORA.set( (displayBorderToRunway.get() + displayRunwayLength.get()) - displayBorderToObstacle.get() - displayBlastAllowance.get() - 30 - 3);
                    if (subRunway1.getStopwayLength().get() == 0){
                        displayASDA.set(displayTORA.get());
                    }
                    else {
                        displayASDA.set(displayTORA.get() + displayStopWayLength.get());
                    }
                    if (subRunway1.getClearwayLength().get() == 0){
                        displayTODA.set(displayTORA.get());
                    }
                    else {
                        displayTODA.set(displayTORA.get() + displayClearWayLength.get());
                    }


                    displayBorderToRESA.set(displayBorderToBlastAllowance.get());
                    // Check slope calculation and RESA, choose the larger value to display
                    if (currentObstacle.getHeight() * 50 > subRunway1.getRESA().get()){
                        ((Text) ((VBox) resaBox.getChildren().get(2)).getChildren().get(1)).setText("Slope");
                    }
                    else {
                        ((Text) ((VBox) resaBox.getChildren().get(2)).getChildren().get(1)).setText("RESA");
                    }
                    displayBorderToStripEnd.set(displayBorderToRESA.get() + displayRESA.get() + 2);

                    displayBorderToLDA.set(displayBorderToStripEnd.get() + displayStripEnd.get() + 2);
                    displayLDA.set(displayRunwayLength.get() + displayBorderToRunway.get() - displayBorderToLDA.get() - 3);

                    if (landingRadioButton.isSelected()){
                        displayBorderToPlane.set(displayBorderToStripEnd.get() + displayStripEnd.get());
                    }
                    else if (takeOffRadioButton.isSelected()){
                        displayBorderToPlane.set(displayBorderToBlastAllowance.get() + displayBlastAllowance.get());
                    }
                }

            }



        });



        secondDirectionButton = new RadioButton();
        secondDirectionButton.textProperty().bind(subRunway2.getDesignator());
        secondDirectionButton.setUserData(subRunway2.getDesignator());
        secondDirectionButton.setOnAction(e -> {
            System.out.println("Second direction selected");
            clearwayLengthDisplay.setText(subRunway2.getClearwayLength().get() + "m");
            stopwayLengthDisplay.setText(subRunway2.getStopwayLength().get() + "m");
            thresholdLengthDisplay.setText(subRunway2.getDisplacedThreshold().get() + "m");
            currentSubRunway = subRunway2;
            planeBox.visibleProperty().set(false);

            if (!takeOffRadioButton.isSelected() && !landingRadioButton.isSelected() ){ // If neither operation button is selected
                planeBox2.visibleProperty().set(false);
            }
            else { // if one operation is selected
                planeBox2.visibleProperty().set(true);
            }

            indicatorsSubRunway2.getChildren().clear();

            indicatorsSubRunway2.getChildren().addAll(toraBox2, todaBox2, ldaBox2, asdaBox2, stripEndBox2, blastAllowanceBox2, resaBox2);


            if ( obstacleBox.getChildren().isEmpty() ){ // If there is no obstacle on the runway
                indicatorsSubRunway2.getChildren().remove(resaBox2);
                indicatorsSubRunway2.getChildren().remove(stripEndBox2);
                indicatorsSubRunway2.getChildren().remove(blastAllowanceBox2);


                displayTORA2.set(displayRunwayLength.get() - 2);

                if (subRunway2.getDisplacedThreshold().get() != 0){ // If there is a displaced threshold
                    displayBorderToLDA2.set(displayBorderToRunway.get() + displayDisplacedThreshold2.get() + 1);
                    displayLDA2.set(displayTORA2.get() - displayDisplacedThreshold2.get() - 2);

                    if (landingRadioButton.isSelected()){
                        displayBorderToPlane2.set(displayBorderToRunway.get() + displayDisplacedThreshold2.get());
                    }
                    else if (takeOffRadioButton.isSelected()){
                        displayBorderToPlane2.set(displayBorderToRunway.get() - displayPlaneWidth.get());
                    }
                }
                else { // If there is no displaced threshold
                    displayBorderToLDA2.set(displayBorderToRunway.get());
                    displayLDA2.set(displayTORA2.get() - 2);

                    displayBorderToPlane2.set(displayBorderToRunway.get() - displayPlaneWidth.get());
                }


                if (subRunway2.getStopwayLength().get() == 0){
                    displayASDA2.set(displayTORA2.get() - 2);
                }
                else{
                    displayASDA2.set(displayTORA2.get() - 2 + displayStopWayLength.get());

                }
                if (subRunway2.getClearwayLength().get() == 0){
                    displayTODA2.set(displayTORA2.get() - 2);
                }
                else{
                    displayTODA2.set(displayTORA2.get() - 2 + displayClearWayLength.get());
                }




            }
            else{ // If there is an obstacle on the runway
                displayBorderToObstacle.set( 800 - 30 - ((HBox) obstacleBox.getChildren().get(0)).prefWidthProperty().get() );


                // Check whether the obstacle is close or further away from the threshold
                if (displayBorderToObstacle.get() > (displayBorderToRunway.get() + (displayRunwayLength.get() / 2)) ){
                    // If the obstacle is further away
                    displayBorderToRESA2.set(displayBorderToObstacle.get() - displayRESA.get() - 2);
                    resaBox2.visibleProperty().set(true);

                    // Check slope calculation and RESA, choose the larger value to display
                    if (currentObstacle.getHeight() * 50 > subRunway2.getRESA().get()){
                        ((Text) ((VBox) resaBox2.getChildren().get(1)).getChildren().get(1)).setText("Slope");
                    }
                    else {
                        ((Text) ((VBox) resaBox2.getChildren().get(1)).getChildren().get(1)).setText("RESA");
                    }
                    displayBorderToStripEnd2.set(displayBorderToRESA2.get() - displayStripEnd.get() - 2);

                    stripEndBox2.visibleProperty().set(true);

                    displayBorderToTORA2.set(displayBorderToRunway.get());
                    displayBorderToTODA2.set(displayBorderToTORA2.get());
                    displayBorderToASDA2.set(displayBorderToTORA2.get());


                    displayTORA2.set(displayBorderToStripEnd2.get() - displayBorderToRunway.get() - 2);
                    displayTODA2.set(displayTORA2.get());
                    displayASDA2.set(displayTORA2.get());

                    if (subRunway2.getDisplacedThreshold().get() != 0){
                        displayBorderToLDA2.set(displayBorderToRunway.get() + displayDisplacedThreshold2.get());
                        displayLDA2.set(displayTORA2.get() - displayDisplacedThreshold2.get() - 2);

                        if (landingRadioButton.isSelected()){
                            displayBorderToPlane2.set(displayBorderToLDA2.get());
                        }
                        else if (takeOffRadioButton.isSelected()){
                            displayBorderToPlane2.set(displayBorderToRunway.get() - displayPlaneWidth.get());
                        }
                    }
                    else {
                        displayBorderToLDA2.set(displayBorderToRunway.get());
                        displayLDA2.set(displayBorderToStripEnd2.get() - displayBorderToRunway.get() - 2);

                        displayBorderToPlane2.set(displayBorderToRunway.get() - displayPlaneWidth.get());
                    }

                    blastAllowanceBox2.visibleProperty().set(false);


                }
                else{
                    // If the obstacle is closer

                    blastAllowanceBox2.visibleProperty().set(true);
                    // 30 is the obstacle image view width
                    displayBorderToBlastAllowance2.set(displayBorderToObstacle.get() + 30 );

                    displayBorderToTORA2.set(displayBorderToBlastAllowance2.get() + displayBlastAllowance.get() + 1);
                    displayBorderToTODA2.set(displayBorderToTORA2.get());
                    displayBorderToASDA2.set(displayBorderToTORA2.get());

                    displayTORA2.set( (displayBorderToRunway.get() + displayRunwayLength.get()) - displayBorderToObstacle.get() - displayBlastAllowance.get() - 30 - 3);

                    if (subRunway2.getStopwayLength().get() == 0){
                        displayASDA2.set(displayTORA2.get());
                    }
                    else {
                        displayASDA2.set(displayTORA2.get() + displayStopWayLength.get());
                    }
                    if (subRunway2.getClearwayLength().get() == 0){
                        displayTODA2.set(displayTORA2.get());
                    }
                    else {
                        displayTODA2.set(displayTORA2.get() + displayClearWayLength.get());
                    }


                    displayBorderToRESA2.set(displayBorderToBlastAllowance2.get());
                    // Check slope calculation and RESA, choose the larger value to display
                    if (currentObstacle.getHeight() * 50 > subRunway2.getRESA().get()){
                        ((Text) ((VBox) resaBox2.getChildren().get(1)).getChildren().get(1)).setText("Slope");
                    }
                    else {
                        ((Text) ((VBox) resaBox2.getChildren().get(1)).getChildren().get(1)).setText("RESA");
                    }
                    displayBorderToStripEnd2.set(displayBorderToRESA2.get() + displayRESA.get() + 2);

                    displayBorderToLDA2.set(displayBorderToStripEnd2.get() + displayStripEnd.get() + 2);
                    displayLDA2.set(displayRunwayLength.get() + displayBorderToRunway.get() - displayBorderToLDA2.get() - 3);


                    if (landingRadioButton.isSelected()){
                        displayBorderToPlane2.set(displayBorderToStripEnd2.get() + displayStripEnd.get());
                    }
                    else if (takeOffRadioButton.isSelected()){
                        displayBorderToPlane2.set(displayBorderToBlastAllowance2.get() + displayBlastAllowance.get());
                    }
                }

            }


        });

        //ToggleGroup directionButtons = new ToggleGroup();
        firstDirectionButton.setToggleGroup(directionButtons);
        secondDirectionButton.setToggleGroup(directionButtons);

        directionButtons.selectToggle(null);

        indicatorsSubRunway1.visibleProperty().bind(firstDirectionButton.selectedProperty());
        indicatorsSubRunway2.visibleProperty().bind(secondDirectionButton.selectedProperty());



        operationButtons.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            // Update the global variable based on the new selection
            if (newValue != null) {
                operationSelected = newValue.getUserData().toString();
            } else {
                // If no RadioButton is selected (e.g., initial state or all selections are cleared)
                operationSelected = null;
            }
            //System.out.println("Current operation: " + operationSelected);
        });

        directionButtons.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            // Update the global variable based on the new selection
            if (newValue != null) {
                directionSelected.set( ((RadioButton) newValue).getText());

            } else {
                // If no RadioButton is selected (e.g., initial state or all selections are cleared)
                directionSelected.set("(...)");
            }

        });




        airplaneStuff.getChildren().add(new Label("Operation:"));
        airplaneStuff.getChildren().add(takeOffRadioButton);
        airplaneStuff.getChildren().add(landingRadioButton);

        airplaneStuff.getChildren().add(new Label("Designator: "));
        airplaneStuff.getChildren().add(firstDirectionButton);
        airplaneStuff.getChildren().add(secondDirectionButton);


        TitledPane airplaneTPane = new TitledPane();
        airplaneTPane.setText("Add Airplane:");
        airplaneTPane.setCollapsible(true);
        airplaneTPane.setContent(airplaneStuff);


        return airplaneTPane;
    }



    public void clearAllButtons(){
        operationButtons.selectToggle(null);
        directionButtons.selectToggle(null);
    }






    /**
     * Create a new JavaFX scene using the root contained within this scene
     * @return JavaFX scene
     */
    public Scene setScene() {
        var previous = homeWindow.getScene();
        Scene scene = new Scene(root, previous.getWidth(), previous.getHeight(), Color.BLACK);
        scene.getStylesheets().add(getClass().getResource("/style/all.css").toExternalForm());
        this.scene = scene;
        return scene;
    }

    /**
     * Get the JavaFX scene contained inside
     * @return JavaFX scene
     */
    public Scene getScene() {
        return this.scene;
    }


    public void changeBaseSceneColours(){
        if (currentState.getColourSettting() == "Default (Blue/Green)"){
            Platform.runLater( () -> {
                lblOriginal.getStyleClass().clear();
                lblRecalculated.getStyleClass().clear();
                lblOriginal.getStyleClass().add("center-label-blue");
                lblRecalculated.getStyleClass().add("center-label-blue");
                //System.out.println("shj;jhskgkjhbsegjhkgjiojhksbdklojslkgbm");

//                buttonASDA.getStyleClass().remove(1);
//                buttonLDA.getStyleClass().remove(1);
//                buttonTODA.getStyleClass().remove(1);
//                buttonTORA.getStyleClass().remove(1);
//                buttonASDA.getStyleClass().add("button-blue");
//                buttonLDA.getStyleClass().add("button-blue");
//                buttonTODA.getStyleClass().add("button-blue");
//                buttonTORA.getStyleClass().add("button-blue");

//                globalSelectedButton.getStyleClass().add("button-selected-blue");
            });
        }else if (currentState.getColourSettting() == "Blue/Yellow"){
            Platform.runLater( () -> {
                lblOriginal.getStyleClass().clear();
                lblRecalculated.getStyleClass().clear();
                lblOriginal.getStyleClass().add("center-label-blue");
                lblRecalculated.getStyleClass().add("center-label-blue");

//                buttonASDA.getStyleClass().remove(1);
//                buttonLDA.getStyleClass().remove(1);
//                buttonTODA.getStyleClass().remove(1);
//                buttonTORA.getStyleClass().remove(1);
//                buttonASDA.getStyleClass().add("button-blue");
//                buttonLDA.getStyleClass().add("button-blue");
//                buttonTODA.getStyleClass().add("button-blue");
//                buttonTORA.getStyleClass().add("button-blue");
            });
        }else if (currentState.getColourSettting() == "Magenta/Lime Green"){
            Platform.runLater( () -> {
                lblOriginal.getStyleClass().clear();
                lblRecalculated.getStyleClass().clear();
                lblOriginal.getStyleClass().add("center-label-green");
                lblRecalculated.getStyleClass().add("center-label-green");


//                buttonASDA.getStyleClass().remove(1);
//                buttonLDA.getStyleClass().remove(1);
//                buttonTODA.getStyleClass().remove(1);
//                buttonTORA.getStyleClass().remove(1);
//                buttonASDA.getStyleClass().add("button-green");
//                buttonLDA.getStyleClass().add("button-green");
//                buttonTODA.getStyleClass().add("button-green");
//                buttonTORA.getStyleClass().add("button-green");
            });

        }else if (currentState.getColourSettting() == "Cyan/Deep Purple"){
            Platform.runLater( () -> {
                lblOriginal.getStyleClass().clear();
                lblRecalculated.getStyleClass().clear();
                lblOriginal.getStyleClass().add("center-label-purple");
                lblRecalculated.getStyleClass().add("center-label-purple");

//                buttonASDA.getStyleClass().remove(1);
//                buttonLDA.getStyleClass().remove(1);
//                buttonTODA.getStyleClass().remove(1);
//                buttonTORA.getStyleClass().remove(1);
//                buttonASDA.getStyleClass().add("button-purple");
//                buttonLDA.getStyleClass().add("button-purple");
//                buttonTODA.getStyleClass().add("button-purple");
//                buttonTORA.getStyleClass().add("button-purple");
            });
        }
    }




}
