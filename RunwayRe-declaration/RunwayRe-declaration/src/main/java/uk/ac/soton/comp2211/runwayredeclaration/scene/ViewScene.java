package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import uk.ac.soton.comp2211.runwayredeclaration.Calculator.RunwayCalculator;
import uk.ac.soton.comp2211.runwayredeclaration.Component.*;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;
import uk.ac.soton.comp2211.runwayredeclaration.XMLHandler.xmlFileLoader;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import javafx.scene.control.*;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ViewScene extends BaseScene{
    private User currentUser;

    private VBox left_box;
    private VBox right_box;

    private HBox menuBox;

    private StackPane middleDisplayBox;




    private BorderPane mainPane = new BorderPane();

    private BorderPane bluePane = new BorderPane();

    private BorderPane groundPane = new BorderPane();

    private StackPane displayStackPaneTop = new StackPane();

    private StackPane topViewPane = new StackPane();
    private StackPane directionPane;
    private StackPane compassPane;



    /**
     * Create a new side view scene
     * @param homeWindow the home Window this will be displayed in
     */
    public ViewScene(HomeWindow homeWindow) {
        super(homeWindow);
    }

    public ViewScene(HomeWindow homeWindow, User user) {
        super(homeWindow);
        this.currentUser = user;
        for (Airport airport : airportList){
            if (airport.getName().equals(currentUser.getWorkingAirport())){
                currentAirport = airport;
            }
        }

        subRunway1 = new SubRunway(currentRunway.getSubRunways().get(0));
        subRunway2 = new SubRunway(currentRunway.getSubRunways().get(1));


        stopWayLength1.bind(subRunway1.getStopwayLength());
        stopWayLength2.bind(subRunway2.getStopwayLength());

        clearWayLength1.bind(subRunway1.getClearwayLength());
        clearWayLength2.bind(subRunway2.getClearwayLength());
    }


    @Override
    public void initialise() {

    }

    /**
     * Build the layout of the scene
     */
    @Override
    public void build() {

        root = new HomePane(homeWindow.getWidth(),homeWindow.getHeight());

        currentView = "Side";

        var sideViewPane = new StackPane();
        sideViewPane.setMaxWidth(homeWindow.getWidth());
        sideViewPane.setMaxHeight(homeWindow.getHeight());

        root.getChildren().add(sideViewPane);


        mainPane.getStyleClass().add("sideView-background");
        sideViewPane.getChildren().add(mainPane);


        // Left Box Set-up
        left_box = new VBox(2, makeResultsTPane(), makeCalcBreakTPane());
        left_box.getStyleClass().add("left-box");
        //left_box.toFront();
        mainPane.setLeft(left_box);
        BorderPane.setAlignment(left_box, Pos.CENTER);


        // Right Box Set-up
        right_box = new VBox(2, makeAirportTPane(), makeAirplaneTPane(), makeObstacleTPane());
        right_box.getStyleClass().add("right-box");
        //right_box.toFront();
        mainPane.setRight(right_box);
        BorderPane.setAlignment(right_box, Pos.CENTER);

        // Menu Set-up
        menuBox = new HBox(makeMenuBox());
        menuBox.getStyleClass().add("menu-box");
        BorderPane.setAlignment(menuBox, Pos.TOP_CENTER);
        mainPane.setTop(menuBox);

        // Middle Display Box Set-up
        middleDisplayBox = new StackPane(makeSideViewMiddleDisplayBox(), makeDirectionPane());
        middleDisplayBox.toBack();
        middleDisplayBox.getStyleClass().add("sideView-background");
        BorderPane.setAlignment(middleDisplayBox, Pos.CENTER);
        mainPane.setCenter(middleDisplayBox);


        displacedThresholdBox.getChildren().clear();
        displacedThresholdBox.setAlignment(Pos.CENTER_LEFT);
        double ratio1 = (subRunway1.getDisplacedThreshold().get() / subRunway1.getOriginalTORA().get());
        displayDisplacedThreshold1.set(displayRunwayLength.getValue() * ratio1);

        double ratio2 = (subRunway2.getDisplacedThreshold().get() / subRunway2.getOriginalTORA().get());
        displayDisplacedThreshold2.set(displayRunwayLength.getValue() * ratio2);

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

        this.initialise();
    }

    /**
     * Add the direction arrows
     * @return borderPane the direction pane
     */
    public StackPane makeDirectionPane(){
        directionPane = new StackPane();
        directionPane.setMaxHeight(300);
        StackPane.setAlignment(directionPane, Pos.TOP_CENTER);

        BorderPane directionPane = new BorderPane();

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
        landingArrowBox.getChildren().addAll(landingArrow);
        landingArrowBox.setAlignment(Pos.TOP_RIGHT);
        directionPane.setRight(landingArrowBox);

        // Takeoff Direction
        Image arrow2 = new Image(getClass().getResource("/images/Arrow1.png").toExternalForm());
        ImageView takeoffArrow = new ImageView(arrow2);
        takeoffArrow.setPreserveRatio(true);
        takeoffArrow.setFitWidth(100);
        VBox takeoffArrowBox = new VBox();
        takeoffArrowBox.setAlignment(Pos.TOP_LEFT);
        VBox arrowEmpty2 = new VBox();
        arrowEmpty2.getStyleClass().add("empty");
        VBox.setVgrow(arrowEmpty2, Priority.ALWAYS);
        Text takeoffText = new Text("Take off");
        takeoffText.getStyleClass().add("arrow-text");
        takeoffArrowBox.getChildren().addAll(takeoffArrow);
        directionPane.setLeft(takeoffArrowBox);


        //Notification Message

        notificationMessage.addListener((observable, oldValue, newValue) -> {
            notificationLabel.setText(newValue);
        });

        VBox notificationBox = new VBox();
        notificationBox.getStyleClass().add("empty");
        notificationBox.setAlignment(Pos.TOP_CENTER);
        notificationBox.getChildren().add(notificationLabel);

        directionPane.setCenter(notificationBox);

        this.directionPane.getChildren().add(directionPane);

        return this.directionPane;
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
        MenuItem exportReport = new MenuItem("Export Report");
        MenuItem importXML = new MenuItem("Import XML");
        MenuItem exportXML = new MenuItem("Export XML");
        MenuItem exportPDF = new MenuItem("Export PDF");
        fileMenu.getItems().addAll(importXML, exportXML, exportReport, exportPDF);

        // Import XML
        importXML.setOnAction(e -> {

            // Check if the user has permission
            if (currentUser.getPermissionLevel().equals("Guest")){
                Alert noImport = new Alert(Alert.AlertType.ERROR);
                noImport.setTitle("Error");
                noImport.setHeaderText("Client doesn't have permission for this operation");
                noImport.setContentText("Please log in");
                noImport.showAndWait();

            }
            else {


                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open XML File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    try {
                        System.out.println("File selected: " + file.getName());
                        FileInputStream inputStream = new FileInputStream(file);
                        List<Airport> airports = xmlFileLoader.loadAirports(inputStream);
                        inputStream = new FileInputStream(file);
                        List<Obstacle> obstacles = xmlFileLoader.loadObstacles(inputStream);
                        List<Airport> airportsToAdd = new ArrayList<>();
                        List<Obstacle> obstaclesToAdd = new ArrayList<>();
                        for (Airport airport : airports) {
                            airportsToAdd.add(airport);
                            for (Airport airport1 : airportList) {
                                if (airport1.getName().equals(airport.getName())) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Warning");
                                    alert.setHeaderText("Airport " + airport1.getName() + " already exists.");
                                    alert.setContentText("The new imported airport is discarded.");
                                    alert.showAndWait();
                                    airportsToAdd.remove(airport);
                                    break;
                                }
                            }
                        }

                        for (Obstacle obstacle : obstacles) {
                            obstaclesToAdd.add(obstacle);
                            for (Obstacle obstacle1 : obstacleList) {
                                if (obstacle1.getName().equals(obstacle.getName())) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Warning");
                                    alert.setHeaderText("Obstacle " + obstacle1.getName() + " already exists.");
                                    alert.setContentText("Please use the pre-defined obstacle");
                                    alert.showAndWait();
                                    obstaclesToAdd.remove(obstacle);
                                    break;
                                }
                            }
                        }

                        // Add the new airports and obstacles
                        this.airportList.addAll(airportsToAdd);
                        this.obstacleList.addAll(obstaclesToAdd);

                        notificationMessage.set("Successfully Imported");
                        FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(3000), notificationLabel);
                        ft.setFromValue(1.0);
                        ft.setToValue(0.0);
                        ft.play();


                    } catch (FileNotFoundException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("An error occurred while reading the file.");
                        alert.setContentText("Please try again.");
                        alert.showAndWait();
                        ex.printStackTrace();
                    }

                } else {
                    System.out.println("No file selected.");
                }
            }
        });

        // Export XML
        exportXML.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Airports XML File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    System.out.println("File saved: " + file.getName());
                    FileWriter writer = new FileWriter(file);
                    writer.write("<data>\n");
                    writer.write("\t<airports>\n");
                    for (Airport airport : airportList) {
                        writer.write("\t\t<airport>\n");
                        writer.write("\t\t\t<name>" + airport.getName() + "</name>\n");
                        for (Runway runway : airport.getRunways()){
                            writer.write("\t\t\t<runway>\n");
                            writer.write("\t\t\t\t<name>" + runway.getName() + "</name>\n");
                            for (SubRunway subRunway : runway.getSubRunways()){
                                writer.write("\t\t\t\t<subRunway>\n");
                                writer.write("\t\t\t\t\t<tora>" + subRunway.getTORA().get() + "</tora>\n");
                                writer.write("\t\t\t\t\t<toda>" + subRunway.getTODA().get() + "</toda>\n");
                                writer.write("\t\t\t\t\t<asda>" + subRunway.getASDA().get() + "</asda>\n");
                                writer.write("\t\t\t\t\t<lda>" + subRunway.getLDA().get() + "</lda>\n");
                                writer.write("\t\t\t\t\t<designator>" + subRunway.getDesignator().get() + "</designator>\n");
                                writer.write("\t\t\t\t\t<displacedThreshold>" + subRunway.getDisplacedThreshold().get() + "</displacedThreshold>\n");
                                writer.write("\t\t\t\t\t<RESA>" + subRunway.getRESA().get() + "</RESA>\n");
                                writer.write("\t\t\t\t\t<stripEndLength>" + subRunway.getStripEndLength().get() + "</stripEndLength>\n");
                                writer.write("\t\t\t\t\t<blastProtection>" + subRunway.getBlastProtection().get() + "</blastProtection>\n");
                                writer.write("\t\t\t\t</subRunway>\n");
                            }

                            writer.write("\t\t\t</runway>\n");
                        }
                        writer.write("\t\t</airport>\n");

                    }
                    writer.write("\t</airports>\n");

                    writer.write("\t<obstacles>\n");
                    for (Obstacle obstacle : obstacleList) {
                        writer.write("\t\t<obstacle>\n");
                        writer.write("\t\t\t<name>" + obstacle.getName() + "</name>\n");
                        writer.write("\t\t\t<height>" + obstacle.getHeight() + "</height>\n");
                        writer.write("\t\t\t<distanceToCenterline>" + obstacle.getDistanceToCentreLine() + "</distanceToCenterline>\n");
                        writer.write("\t\t\t<distanceToLowerThreshold>" + obstacle.getDistanceToLowerThreshold() + "</distanceToLowerThreshold>\n");
                        writer.write("\t\t\t<distanceToHigherThreshold>" + obstacle.getDistanceToHigherThreshold() + "</distanceToHigherThreshold>\n");
                        writer.write("\t\t</obstacle>\n");
                    }
                    writer.write("\t</obstacles>\n");
                    writer.write("</data>");
                    writer.close();
                    notificationMessage.set("Successfully Exported");
                    FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(3000), notificationLabel);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.0);
                    ft.play();
                } catch (IOException ex) {
                    System.out.println("An error occurred while writing to the file.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("An error occurred while writing to the file.");
                    alert.setContentText("Please try again.");
                    alert.showAndWait();
                    ex.printStackTrace();
                }
            } else {
                System.out.println("XML file saving cancelled.");
            }


        });

        // Export Report
        exportReport.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Report");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    FileWriter writer = new FileWriter(file);
                    if (firstDirectionButton.isSelected()) {
                        writer.write(RunwayCalculator.breakdownTORA(subRunway1, currentObstacle, subRunway1.getObstacleDistance()));
                    } else if (secondDirectionButton.isSelected()) {
                        writer.write(RunwayCalculator.breakdownTORA(subRunway2, currentObstacle, subRunway2.getObstacleDistance()));
                    }
                    writer.write("\n\n");
                    if (firstDirectionButton.isSelected()) {
                        writer.write(RunwayCalculator.breakdownTODA(subRunway1, currentObstacle, subRunway1.getObstacleDistance()));
                    } else if (secondDirectionButton.isSelected()) {
                        writer.write(RunwayCalculator.breakdownTODA(subRunway2, currentObstacle, subRunway2.getObstacleDistance()));
                    }
                    writer.write("\n\n");
                    if (firstDirectionButton.isSelected()) {
                        writer.write(RunwayCalculator.breakdownASDA(subRunway1, currentObstacle, subRunway1.getObstacleDistance()));
                    } else if (secondDirectionButton.isSelected()) {
                        writer.write(RunwayCalculator.breakdownASDA(subRunway2, currentObstacle, subRunway2.getObstacleDistance()));
                    }
                    writer.write("\n\n");
                    if (firstDirectionButton.isSelected()) {
                        writer.write(RunwayCalculator.breakdownLDA(subRunway1, currentObstacle, subRunway1.getObstacleDistance()));
                    } else if (secondDirectionButton.isSelected()) {
                        writer.write(RunwayCalculator.breakdownLDA(subRunway2, currentObstacle, subRunway2.getObstacleDistance()));
                    }
                    writer.close();

                    notificationMessage.set("Successfully Exported.");
                    FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(3000), notificationLabel);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.0);
                    ft.play();

                } catch (IOException ex) {
                    System.out.println("An error occurred while writing to the file.");
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Report saving cancelled.");
            }


        });

        exportPDF.setOnAction(e -> {
            try{
                exportPDF();;
            }catch (Exception exportPDFfailed){
                Alert failedExportPDF = new Alert(Alert.AlertType.ERROR);
                failedExportPDF.setTitle("Error");
                failedExportPDF.setHeaderText("An error occurred while exporting the PDF");
                failedExportPDF.setContentText("Please try again");
                failedExportPDF.showAndWait();
            }
        });

        // View Menu
        Menu viewMenu = new Menu("View");

        // Switch to Side View
        MenuItem sideView = new MenuItem("Side View");

        // Switch to Top view
        MenuItem topView = new MenuItem("Top View");

        // Switch to simultaneous view
        MenuItem simultaneous = new MenuItem("Simultaneous");


        sideView.setOnAction(e -> {
            notificationMessage.set("Successfully switched to Side View");
            FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(3000), notificationLabel);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.play();

            currentView = "Side";
            bluePane.setTop(null);
            middleDisplayBox.getChildren().clear();
            obstacleBox.setAlignment(Pos.BOTTOM_LEFT);
            middleDisplayBox.getChildren().addAll(makeSideViewMiddleDisplayBox(), makeDirectionPane());
            for (Node n : middleDisplayBox.getChildren()){
                System.out.println(n);
            }
            viewMenu.getItems().clear();
            viewMenu.getItems().addAll(topView, simultaneous);

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

        topView.setOnAction(e -> {
            notificationMessage.set("Successfully switched to Top View");
            FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(3000), notificationLabel);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.play();

            currentView = "Top";
            middleDisplayBox.getChildren().clear();
            obstacleBox.setAlignment(Pos.CENTER_LEFT);
            middleDisplayBox.setMaxWidth(homeWindow.getWidth() - 600);
            middleDisplayBox.getChildren().addAll(makeTopViewMiddleDisplayBox(), makeDirectionPane(), makeCompassPane());


            viewMenu.getItems().clear();
            viewMenu.getItems().addAll(sideView, simultaneous);

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

        simultaneous.setOnAction(e -> {
            notificationMessage.set("Successfully switched to Simultaneous");
            FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(3000), notificationLabel);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.play();

            currentView = "Simultaneous";
            middleDisplayBox.getChildren().clear();
            middleDisplayBox.getChildren().addAll(makeSimultaneousMiddleDisplayBox(), makeDirectionPane());
            viewMenu.getItems().clear();
            viewMenu.getItems().addAll(sideView, topView);
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

        // Add menu items
        viewMenu.getItems().addAll(topView, simultaneous);


        // Help Menu
        Menu helpMenu = new Menu("Help");

        // Only for admin user
        if (currentUser.getPermissionLevel().equals("Admin")){
            MenuItem airportInformation = new MenuItem("Airport Information");
            airportInformation.setOnAction(e -> makeAirportInformation());
            helpMenu.getItems().add(airportInformation);
        }




        MenuItem colourSettings = new MenuItem("Colour Schemes");
        colourSettings.setOnAction(e -> makeColourSettingPage());

        MenuItem musicSettings = new MenuItem("Music Settings");
        musicSettings.setOnAction(e -> makeMusicSettingPage());

        helpMenu.getItems().addAll(colourSettings, musicSettings);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);

        // Create Log out button
        Button logoutButton = new Button("Log out");
        logoutButton.setOnAction(e -> homeWindow.startLogIn());
        logoutButton.getStyleClass().add("logout-button");


        // Empty box to push the logout button to the right
        HBox empty = new HBox();
        empty.getStyleClass().add("empty");


        HBox.setHgrow(empty, Priority.ALWAYS); // This will push the logout button to the right

        HBox everything = new HBox(menuBar, empty, logoutButton);
        HBox.setHgrow(everything, Priority.ALWAYS);
        return (everything);
    }


    public StackPane makeDisplacedThreshold (){
        StackPane displacedThresholdStackPane = new StackPane();
        displacedThresholdStackPane.getStyleClass().add("empty");
        displacedThresholdStackPane.setAlignment(Pos.CENTER_LEFT);
        displacedThresholdStackPane.getChildren().add(displacedThresholdBox);

        return displacedThresholdStackPane;

    }

    /**
     * StackPane for all the indicators for subRunway1
     * @return StackPane the indicators
     */
    public StackPane makeIndicators1(){


        // TORA HBox
        toraBox = new HBox();
        //toraBox.setMinWidth(125 * 2 + 550);
        toraBox.getStyleClass().add("empty");
        toraBox.setAlignment(Pos.CENTER_LEFT);
        HBox borderToTORA = new HBox();
        borderToTORA.getStyleClass().add("empty");
        displayBorderToTORA.set(displayBorderToRunway.get());
        borderToTORA.prefWidthProperty().bind(displayBorderToTORA);
        DashedLine toraStart = new DashedLine(0.1, 500);
        DashedLine toraEnd = new DashedLine(0.1, 500);

        // TORA Distance Box (Line + Text)
        VBox toraDistanceBox = new VBox();
        toraDistanceBox.getStyleClass().add("empty");
        toraDistanceBox.prefWidthProperty().bind(displayTORA);
        toraDistanceBox.setMaxHeight(500);
        toraDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine toraArrow = new DashedLine(displayTORA.get(), 0.1, false);
        toraArrow.widthProperty().bind(displayTORA);
        Text toraText = new Text("TORA");
        toraText.getStyleClass().add("arrow-text");
        toraDistanceBox.getChildren().addAll(toraArrow, toraText, new EmptyVBox(0.1, 60));

        toraBox.getChildren().addAll(borderToTORA, toraStart, toraDistanceBox, toraEnd);



        // TODA HBox
        todaBox = new HBox();
        //todaBox.setMinWidth(125 * 2 + 550);
        todaBox.getStyleClass().add("empty");
        todaBox.setAlignment(Pos.CENTER_LEFT);
        DashedLine todaStart = new DashedLine(0.1, 500);
        DashedLine todaEnd = new DashedLine(0.1, 500);

        VBox todaDistanceBox = new VBox();
        todaDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        todaDistanceBox.setMaxHeight(500);
        todaDistanceBox.getStyleClass().add("empty");

        if (subRunway1.getClearwayLength().get() == 0){
            displayTODA.set(displayTORA.get());
        }else{
            displayTODA.set(displayTORA.get() + displayClearWayLength.get());
        }



        todaDistanceBox.prefWidthProperty().bind(displayTODA);
        DashedLine todaArrow = new DashedLine(displayTODA.get(), 0.1, false);
        todaArrow.widthProperty().bind(displayTODA);
        Text todaText = new Text("TODA");
        todaText.getStyleClass().add("arrow-text");
        todaDistanceBox.getChildren().addAll(todaArrow, todaText, new EmptyVBox(0.1, 105));

        HBox borderToTODA = new HBox();
        borderToTODA.getStyleClass().add("empty");
        displayBorderToTODA.set(displayBorderToRunway.get());
        borderToTODA.prefWidthProperty().bind(displayBorderToTODA);


        todaBox.getChildren().addAll(borderToTODA, todaStart, todaDistanceBox, todaEnd);


        // ASDA HBox
        asdaBox = new HBox();
        //asdaBox.setMinWidth(125 * 2 + 550);
        asdaBox.getStyleClass().add("empty");
        asdaBox.setAlignment(Pos.CENTER_LEFT);

        DashedLine asdaStart = new DashedLine(0.1, 500);
        DashedLine asdaEnd = new DashedLine(0.1, 500);

        VBox asdaDistanceBox = new VBox();
        asdaDistanceBox.getStyleClass().add("empty");

        if (subRunway1.getStopwayLength().get() != 0){
            displayASDA.set(displayTORA.get() + displayStopWayLength.get());
        }
        else{
            displayASDA.set(displayTORA.get());
        }

        asdaDistanceBox.prefWidthProperty().bind(displayASDA);
        asdaDistanceBox.setMaxHeight(500);
        asdaDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine asdaArrow = new DashedLine(displayASDA.get(), 0.1, false);
        asdaArrow.widthProperty().bind(displayASDA);
        Text asdaText = new Text("ASDA");
        asdaText.getStyleClass().add("arrow-text");
        asdaDistanceBox.getChildren().addAll(asdaArrow, asdaText, new EmptyVBox(0.1, 150));


        HBox borderToASDA = new HBox();
        borderToASDA.getStyleClass().add("empty");
        displayBorderToASDA.set(displayBorderToRunway.get());
        borderToASDA.prefWidthProperty().bind(displayBorderToASDA);

        asdaBox.getChildren().addAll(borderToASDA, asdaStart, asdaDistanceBox, asdaEnd);

        // RESA HBox
        resaBox = new HBox();
        resaBox.getStyleClass().add("empty");
        resaBox.setAlignment(Pos.CENTER_LEFT);


        double height = 150;
        if (currentView.equals("Simultaneous")){
            height = 500;
        }

        DashedLine resaStart = new DashedLine(0.1, height);
        DashedLine resaEnd = new DashedLine(0.1, height);

        VBox resaDistanceBox = new VBox();
        resaDistanceBox.getStyleClass().add("empty");
        resaDistanceBox.prefWidthProperty().bind(displayRESA);
        resaDistanceBox.setMaxHeight(500);
        resaDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine resaArrow = new DashedLine(displayRESA.get(), 0.1, false);
        Text resaText = new Text("RESA");
        resaText.getStyleClass().add("arrow-text");
        resaText.setStyle("-fx-font-size: 10");
        resaDistanceBox.getChildren().addAll(resaArrow, resaText, new EmptyVBox(0.1, 280));

        HBox displayBorderToRESABox = new HBox();
        displayBorderToRESABox.getStyleClass().add("empty");
        displayBorderToRESABox.prefWidthProperty().bind(displayBorderToRESA);
        resaBox.getChildren().addAll(displayBorderToRESABox, resaStart, resaDistanceBox, resaEnd);

        // Strip End HBox
        stripEndBox = new HBox();
        stripEndBox.getStyleClass().add("empty");
        stripEndBox.setAlignment(Pos.CENTER_LEFT);

        DashedLine stripEndStart = new DashedLine(0.1, height);
        DashedLine stripEndEnd = new DashedLine(0.1, height);

        VBox stripEndDistanceBox = new VBox();
        stripEndDistanceBox.getStyleClass().add("empty");
        stripEndDistanceBox.prefWidthProperty().bind(displayStripEnd);
        stripEndDistanceBox.setMaxHeight(500);
        stripEndDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine stripEndArrow = new DashedLine(displayStripEnd.get(), 0.1, false);
        Text stripEndText = new Text("Strip End");
        stripEndText.getStyleClass().add("arrow-text");
        stripEndText.setStyle("-fx-font-size: 10");
        stripEndDistanceBox.getChildren().addAll(stripEndArrow, stripEndText, new EmptyVBox(0.1, 280));

        HBox borderToStripEnd = new HBox();
        borderToStripEnd.getStyleClass().add("empty");
        borderToStripEnd.prefWidthProperty().bind(displayBorderToStripEnd);
        stripEndBox.getChildren().addAll(borderToStripEnd, stripEndStart, stripEndDistanceBox, stripEndEnd);




        // Blast Allowance HBox
        blastAllowanceBox = new HBox();
        blastAllowanceBox.getStyleClass().add("empty");
        blastAllowanceBox.setAlignment(Pos.CENTER_LEFT);

        DashedLine blastAllowanceStart = new DashedLine(0.1, height);
        DashedLine blastAllowanceEnd = new DashedLine(0.1, height);

        VBox blastAllowanceDistanceBox = new VBox();
        blastAllowanceDistanceBox.getStyleClass().add("empty");
        blastAllowanceDistanceBox.prefWidthProperty().bind(displayBlastAllowance);
        blastAllowanceDistanceBox.setMaxHeight(500);
        blastAllowanceDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine blastAllowanceArrow = new DashedLine(displayBlastAllowance.get(), 0.1, false);
        Text blastAllowanceText = new Text("Blast\nAllowance");
        blastAllowanceText.getStyleClass().add("arrow-text");
        blastAllowanceText.setStyle("-fx-font-size: 10");
        blastAllowanceDistanceBox.getChildren().addAll(blastAllowanceArrow, blastAllowanceText, new EmptyVBox(0.1, 180));

        HBox borderToBlastAllowance = new HBox();
        borderToBlastAllowance.getStyleClass().add("empty");
        borderToBlastAllowance.prefWidthProperty().bind(displayBorderToBlastAllowance);
        blastAllowanceBox.getChildren().addAll(borderToBlastAllowance, blastAllowanceStart, blastAllowanceDistanceBox, blastAllowanceEnd);


        // LDA HBox
        ldaBox = new HBox();
        ldaBox.getStyleClass().add("empty");
        ldaBox.setAlignment(Pos.CENTER_LEFT);
        //ldaBox.setMinWidth(125 * 2 + 550);
        DashedLine ldaStart = new DashedLine(0.1, 500);
        DashedLine ldaEnd = new DashedLine(0.1, 500);

        VBox ldaDistanceBox = new VBox();
        ldaDistanceBox.getStyleClass().add("empty");
        ldaDistanceBox.prefWidthProperty().bind(displayLDA);
        ldaDistanceBox.setMaxHeight(500);
        ldaDistanceBox.setAlignment(Pos.TOP_CENTER);
        DashedLine ldaArrow = new DashedLine(displayLDA.get(), 0.1, "yellow",false);
        ldaArrow.widthProperty().bind(displayLDA);
        Text ldaText = new Text("LDA");
        ldaText.getStyleClass().add("arrow-text");
        ldaDistanceBox.getChildren().addAll(new EmptyVBox(0.1, 150), ldaArrow, ldaText);

        HBox borderToLDA = new HBox();
        borderToLDA.getStyleClass().add("empty");
        displayBorderToLDA.set(displayBorderToRunway.get());
        borderToLDA.prefWidthProperty().bind(displayBorderToLDA);
        ldaBox.getChildren().addAll(borderToLDA, ldaStart, ldaDistanceBox, ldaEnd);


        indicatorsSubRunway1.getChildren().addAll(toraBox, todaBox, asdaBox, ldaBox);
        
        return indicatorsSubRunway1;
    }

    /**
     * StackPane for all the indicators for subRunway2
     * @return StackPane the indicators
     */
    public StackPane makeIndicators2(){

        // TORA HBox
        toraBox2 = new HBox();
        //toraBox2.setMinWidth(125 * 2 + 550);
        toraBox2.getStyleClass().add("empty");
        toraBox2.setAlignment(Pos.CENTER_RIGHT);
        HBox borderToTORA = new HBox();
        borderToTORA.getStyleClass().add("empty");
        displayBorderToTORA2.set(displayBorderToRunway.get());
        borderToTORA.prefWidthProperty().bind(displayBorderToTORA2);
        DashedLine toraStart = new DashedLine(0.1, 500);
        DashedLine toraEnd = new DashedLine(0.1, 500);

        // TORA Distance Box (Line + Text)
        VBox toraDistanceBox = new VBox();
        toraDistanceBox.getStyleClass().add("empty");
        toraDistanceBox.prefWidthProperty().bind(displayTORA2);
        toraDistanceBox.setMaxHeight(500);
        toraDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine toraArrow = new DashedLine(displayTORA2.get(), 0.1, false);
        toraArrow.widthProperty().bind(displayTORA2);
        Text toraText = new Text("TORA");
        toraText.getStyleClass().add("arrow-text");
        toraDistanceBox.getChildren().addAll(toraArrow, toraText, new EmptyVBox(0.1, 60));


        toraBox2.getChildren().addAll(toraEnd, toraDistanceBox, toraStart, borderToTORA);



        // TODA HBox
        todaBox2 = new HBox();
        //todaBox2.setMinWidth(125 * 2 + 550);
        todaBox2.getStyleClass().add("empty");
        todaBox2.setAlignment(Pos.CENTER_RIGHT);
        DashedLine todaStart = new DashedLine(0.1, 500);
        DashedLine todaEnd = new DashedLine(0.1, 500);

        VBox todaDistanceBox = new VBox();
        todaDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        todaDistanceBox.setMaxHeight(500);
        todaDistanceBox.getStyleClass().add("empty");

        if (subRunway1.getClearwayLength().get() == 0){
            displayTODA2.set(displayTORA2.get());
        }else{
            displayTODA2.set(displayTORA2.get() + displayClearWayLength.get());
        }



        todaDistanceBox.prefWidthProperty().bind(displayTODA2);
        DashedLine todaArrow = new DashedLine(displayTODA2.get(), 0.1, false);
        todaArrow.widthProperty().bind(displayTODA2);
        Text todaText = new Text("TODA");
        todaText.getStyleClass().add("arrow-text");
        todaDistanceBox.getChildren().addAll(todaArrow, todaText, new EmptyVBox(0.1, 105));

        HBox borderToTODA = new HBox();
        borderToTODA.getStyleClass().add("empty");
        displayBorderToTODA2.set(displayBorderToRunway.get());
        borderToTODA.prefWidthProperty().bind(displayBorderToTODA2);


        todaBox2.getChildren().addAll(todaEnd, todaDistanceBox, todaStart, borderToTODA);


        // ASDA HBox
        asdaBox2 = new HBox();
        //asdaBox2.setMinWidth(125 * 2 + 550);
        asdaBox2.getStyleClass().add("empty");
        asdaBox2.setAlignment(Pos.CENTER_RIGHT);

        DashedLine asdaStart = new DashedLine(0.1, 500);
        DashedLine asdaEnd = new DashedLine(0.1, 500);

        VBox asdaDistanceBox = new VBox();
        asdaDistanceBox.getStyleClass().add("empty");

        if (subRunway2.getStopwayLength().get() != 0){
            displayASDA2.set(displayTORA2.get() + displayStopWayLength.get());
        }
        else{
            displayASDA2.set(displayTORA2.get());
        }

        asdaDistanceBox.prefWidthProperty().bind(displayASDA2);
        asdaDistanceBox.setMaxHeight(500);
        asdaDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine asdaArrow = new DashedLine(displayASDA2.get(), 0.1, false);
        asdaArrow.widthProperty().bind(displayASDA2);
        Text asdaText = new Text("ASDA");
        asdaText.getStyleClass().add("arrow-text");
        asdaDistanceBox.getChildren().addAll(asdaArrow, asdaText, new EmptyVBox(0.1, 150));


        HBox borderToASDA = new HBox();
        borderToASDA.getStyleClass().add("empty");
        displayBorderToASDA2.set(displayBorderToRunway.get());
        borderToASDA.prefWidthProperty().bind(displayBorderToASDA2);

        asdaBox2.getChildren().addAll(asdaEnd, asdaDistanceBox, asdaStart, borderToASDA);

        double height = 150;
        if (currentView.equals("Simultaneous")){
            height = 500;
        }

        // RESA HBox
        resaBox2 = new HBox();
        resaBox2.getStyleClass().add("empty");
        resaBox2.setAlignment(Pos.CENTER_RIGHT);

        DashedLine resaStart = new DashedLine(0.1, height);
        DashedLine resaEnd = new DashedLine(0.1, height);

        VBox resaDistanceBox = new VBox();
        resaDistanceBox.getStyleClass().add("empty");
        resaDistanceBox.prefWidthProperty().bind(displayRESA);
        resaDistanceBox.setMaxHeight(500);
        resaDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine resaArrow = new DashedLine(displayRESA.get(), 0.1, false);
        Text resaText = new Text("RESA");
        resaText.getStyleClass().add("arrow-text");
        resaText.setStyle("-fx-font-size: 10");
        resaDistanceBox.getChildren().addAll(resaArrow, resaText, new EmptyVBox(0.1, 280));

        HBox displayBorderToRESABox = new HBox();
        displayBorderToRESABox.getStyleClass().add("empty");
        displayBorderToRESABox.prefWidthProperty().bind(displayBorderToRESA2);
        resaBox2.getChildren().addAll(resaEnd, resaDistanceBox, resaStart, displayBorderToRESABox);

        // Strip End HBox
        stripEndBox2 = new HBox();
        stripEndBox2.getStyleClass().add("empty");
        stripEndBox2.setAlignment(Pos.CENTER_RIGHT);

        DashedLine stripEndStart = new DashedLine(0.1, height);
        DashedLine stripEndEnd = new DashedLine(0.1, height);

        VBox stripEndDistanceBox = new VBox();
        stripEndDistanceBox.getStyleClass().add("empty");
        stripEndDistanceBox.prefWidthProperty().bind(displayStripEnd);
        stripEndDistanceBox.setMaxHeight(500);
        stripEndDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine stripEndArrow = new DashedLine(displayStripEnd.get(), 0.1, false);
        Text stripEndText = new Text("Strip End");
        stripEndText.getStyleClass().add("arrow-text");
        stripEndText.setStyle("-fx-font-size: 10");
        stripEndDistanceBox.getChildren().addAll(stripEndArrow, stripEndText, new EmptyVBox(0.1, 280));

        HBox borderToStripEnd = new HBox();
        borderToStripEnd.getStyleClass().add("empty");
        borderToStripEnd.prefWidthProperty().bind(displayBorderToStripEnd2);

        stripEndBox2.getChildren().addAll(stripEndEnd, stripEndDistanceBox, stripEndStart, borderToStripEnd);




        // Blast Allowance HBox
        blastAllowanceBox2 = new HBox();
        blastAllowanceBox2.getStyleClass().add("empty");
        blastAllowanceBox2.setAlignment(Pos.CENTER_RIGHT);

        DashedLine blastAllowanceStart = new DashedLine(0.1, 150);
        DashedLine blastAllowanceEnd = new DashedLine(0.1, 150);

        VBox blastAllowanceDistanceBox = new VBox();
        blastAllowanceDistanceBox.getStyleClass().add("empty");
        blastAllowanceDistanceBox.prefWidthProperty().bind(displayBlastAllowance);
        blastAllowanceDistanceBox.setMaxHeight(500);
        blastAllowanceDistanceBox.setAlignment(Pos.BOTTOM_CENTER);
        DashedLine blastAllowanceArrow = new DashedLine(displayBlastAllowance.get(), 0.1, false);
        Text blastAllowanceText = new Text("Blast\nAllowance");
        blastAllowanceText.getStyleClass().add("arrow-text");
        blastAllowanceText.setStyle("-fx-font-size: 10");
        blastAllowanceDistanceBox.getChildren().addAll(blastAllowanceArrow, blastAllowanceText, new EmptyVBox(0.1,180));

        HBox borderToBlastAllowance = new HBox();
        borderToBlastAllowance.getStyleClass().add("empty");
        borderToBlastAllowance.prefWidthProperty().bind(displayBorderToBlastAllowance2);


        blastAllowanceBox2.getChildren().addAll(blastAllowanceEnd, blastAllowanceDistanceBox, blastAllowanceStart, borderToBlastAllowance);

        // LDA HBox
        ldaBox2 = new HBox();
        ldaBox2.getStyleClass().add("empty");
        ldaBox2.setAlignment(Pos.CENTER_RIGHT);
        //ldaBox2.setMinWidth(125 * 2 + 550);
        DashedLine ldaStart = new DashedLine(0.1, 500);
        DashedLine ldaEnd = new DashedLine(0.1, 500);

        VBox ldaDistanceBox = new VBox();
        ldaDistanceBox.getStyleClass().add("empty");
        ldaDistanceBox.prefWidthProperty().bind(displayLDA2);
        ldaDistanceBox.setMaxHeight(500);
        ldaDistanceBox.setAlignment(Pos.TOP_CENTER);
        DashedLine ldaArrow = new DashedLine(displayLDA2.get(), 0.1, "yellow",false);
        ldaArrow.widthProperty().bind(displayLDA2);
        Text ldaText = new Text("LDA");
        ldaText.getStyleClass().add("arrow-text");
        ldaDistanceBox.getChildren().addAll(new EmptyVBox(0.1, 150), ldaArrow, ldaText);

        HBox borderToLDA = new HBox();
        borderToLDA.getStyleClass().add("empty");
        displayBorderToLDA2.set(displayBorderToRunway.get());
        borderToLDA.prefWidthProperty().bind(displayBorderToLDA2);

        ldaBox2.getChildren().addAll(ldaEnd, ldaDistanceBox, ldaStart, borderToLDA);



        indicatorsSubRunway2.getChildren().addAll(toraBox2, todaBox2, asdaBox2, ldaBox2);

        return indicatorsSubRunway2;

    }


    /**
     * Create the compass pane
     * @return BorderPane Compass Pane
     */
    public StackPane makeCompassPane(){
        compassPane = new StackPane();
        BorderPane compassPane = new BorderPane();
        compassPane.getStyleClass().add("empty");

        Image compassImage = new Image(getClass().getResource("/images/Compass.png").toExternalForm());
        ImageView compassImageView = new ImageView(compassImage);
        compassImageView.setPreserveRatio(true);
        compassImageView.setRotate(45);
        compassImageView.setFitWidth(100);
        compassImageView.setFitHeight(100);


        compassImageView.setOnMouseClicked(e -> {
            compassImageView.setRotate(compassImageView.getRotate() + 45);
            displayStackPaneTop.setRotate(displayStackPaneTop.getRotate() + 45);


//            menuBox.toFront();
            left_box.toFront();
            right_box.toFront();
            displayStackPaneTop.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
//            compassPane.toFront();


        });

        VBox compassBox = new VBox();
        compassBox.getStyleClass().add("empty");
        compassBox.setAlignment(Pos.BOTTOM_RIGHT);
        compassBox.getChildren().add(compassImageView);

        compassPane.setRight(compassBox);

        this.compassPane.getChildren().add(compassPane);
        return this.compassPane;
    }




    /**
     * Create the middle display box
     * @return StackPane Middle display box
     */
    public StackPane makeSideViewMiddleDisplayBox(){
        StackPane displayStackPane = new StackPane();
        BorderPane displayBorderPane = new BorderPane();

        displayStackPane.getChildren().add(displayBorderPane);


        // Sky Part

        bluePane.getStyleClass().add("sideView-background");
        bluePane.prefHeightProperty().bind(displayStackPane.heightProperty().divide(2));
        displayBorderPane.setTop(bluePane);


        // Plane and Obstacle Pane
        StackPane planePane = new StackPane();


        obstacleBox.setAlignment(Pos.BOTTOM_LEFT);
        planeBox.setAlignment(Pos.BOTTOM_LEFT);

        // Plane Image
        Image planeImage = new Image(getClass().getResource("/images/Plane1.png").toExternalForm());
        ImageView planeImageView = new ImageView(planeImage);
        planeImageView.setPreserveRatio(true);
        planeImageView.setFitWidth(displayPlaneWidth.getValue());

        // Reverse Plane Image
        Image planeImage2 = new Image(getClass().getResource("/images/Plane2.png").toExternalForm());
        ImageView planeImageView2 = new ImageView(planeImage2);
        planeImageView2.setPreserveRatio(true);
        planeImageView2.setScaleX(-1);
        planeImageView2.setFitWidth(displayPlaneWidth.getValue());



        HBox borderToPlane = new HBox();
        borderToPlane.getStyleClass().add("empty");
        borderToPlane.prefWidthProperty().bind(displayBorderToPlane);


        planeBox.getChildren().clear();
        planeBox.getStyleClass().add("empty");
        planeBox.setAlignment(Pos.CENTER_LEFT);
        planeBox.getChildren().addAll(borderToPlane, planeImageView);

        HBox borderToPlane2 = new HBox();
        borderToPlane2.getStyleClass().add("empty");
        borderToPlane2.prefWidthProperty().bind(displayBorderToPlane2);


        planeBox2.getChildren().clear();
        planeBox2.getStyleClass().add("empty");
        planeBox2.setAlignment(Pos.CENTER_RIGHT);
        planeBox2.getChildren().addAll( planeImageView2, borderToPlane2);


        planePane.getChildren().addAll(planeBox, obstacleBox, planeBox2);

        bluePane.setBottom(planePane);


        // Ground Part

        groundPane.setBackground(new Background(new BackgroundFill(Color.web("#A9D18E"), CornerRadii.EMPTY, Insets.EMPTY)));
        groundPane.prefHeightProperty().bind(displayStackPane.heightProperty().divide(2));

        displayBorderPane.setBottom(groundPane);


        // Runway Image
        Image runwayImage = new Image(getClass().getResource("/images/Runway2.png").toExternalForm());
        ImageView runwayImageView = new ImageView(runwayImage);
        runwayImageView.setPreserveRatio(true);
        runwayImageView.setFitWidth(displayRunwayLength.getValue());
        runwayImageView.setFitHeight(100);


        // Runway Pane
        HBox runwayBox = new HBox();
        runwayBox.getStyleClass().add("empty");
        runwayBox.setAlignment(Pos.CENTER_LEFT);


        // Stop Ways
        HBox stopWay1 = new HBox();
        stopWay1.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay1.prefWidthProperty().bind(displayStopWayLength);
        stopWay1.visibleProperty().bind(stopWayLength2.greaterThan(0));
        stopWay1.setAlignment(Pos.CENTER_LEFT);
        HBox stopWay2 = new HBox();
        stopWay2.setBackground(new Background(new BackgroundFill(Color.web("#4472C4"), CornerRadii.EMPTY, Insets.EMPTY)));
        stopWay2.prefWidthProperty().bind(displayStopWayLength);
        stopWay2.setPrefHeight(runwayImageView.getFitHeight());
        stopWay2.visibleProperty().bind(stopWayLength1.greaterThan(0));
        stopWay2.setAlignment(Pos.CENTER_LEFT);

        HBox stopWayBox = new HBox();
        HBox emptyBoxBetweenStopWays = new HBox();
        emptyBoxBetweenStopWays.getStyleClass().add("empty");
        emptyBoxBetweenStopWays.setPrefHeight(displayStackPane.getHeight());
        emptyBoxBetweenStopWays.setPrefWidth(displayRunwayLength.get());
        stopWayBox.getChildren().addAll(stopWay1, emptyBoxBetweenStopWays, stopWay2);
        stopWayBox.setAlignment(Pos.CENTER);

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


        StackPane runwayPane = new StackPane();
        runwayPane.getStyleClass().add("empty");
        runwayPane.setAlignment(Pos.CENTER_LEFT);
        runwayPane.getChildren().add(runwayPaneBox);
        runwayPane.getChildren().add(stopWayBox);
        runwayPane.setPrefHeight(Region.USE_PREF_SIZE);

        groundPane.setTop(runwayPane);

        // Clearways
        HBox clearWayBox = new HBox();
        clearWayBox.getStyleClass().add("empty");
        clearWayBox.setAlignment(Pos.CENTER);

        HBox clearWay1 = new HBox();
        clearWay1.getStyleClass().add("clearway-box");
        clearWay1.visibleProperty().bind(clearWayLength2.greaterThan(0));
        clearWay1.prefWidthProperty().bind(displayClearWayLength);

        HBox emptyBoxBetweenClearWay = new HBox();
        emptyBoxBetweenClearWay.getStyleClass().add("empty");
        emptyBoxBetweenClearWay.setPrefWidth(displayRunwayLength.get());

        HBox clearWay2 = new HBox();
        clearWay2.getStyleClass().add("clearway-box");
        clearWay2.visibleProperty().bind(clearWayLength1.greaterThan(0));
        clearWay2.prefWidthProperty().bind(displayClearWayLength);



        clearWayBox.getChildren().addAll(clearWay1, emptyBoxBetweenClearWay, clearWay2);
        displayStackPane.getChildren().add(clearWayBox);

        // Designator Display
        Text designator1 = new Text();
        designator1.getStyleClass().add("display-designator-text");
        designator1.textProperty().bind(subRunway1.getDesignator());
        Text designator2 = new Text();
        designator2.getStyleClass().add("display-designator-text");
        designator2.textProperty().bind(subRunway2.getDesignator());
        HBox emptyHbox = new HBox();
        emptyHbox.getStyleClass().add("empty");
        HBox.setHgrow(emptyHbox, Priority.ALWAYS);


        HBox designatorBox = new HBox();
        designatorBox.getStyleClass().add("empty");
        designatorBox.setAlignment(Pos.CENTER);
        designatorBox.getChildren().addAll(designator1, emptyHbox, designator2);

       displayStackPane.getChildren().add(designatorBox);

       displayStackPane.getChildren().add(makeDisplacedThreshold());

       displayStackPane.getChildren().add(makeIndicators1());
       displayStackPane.getChildren().add(makeIndicators2());



        return displayStackPane;
    }

    /**
     * Create the top view middle display box
     * @return StackPane Middle display box
     */
    public StackPane makeTopViewMiddleDisplayBox(){
        displayStackPaneTop.setRotate(0);

        changeColourSchemeTop();

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
        //emptyBoxBetweenStopWays.setPrefHeight(displayStackPaneTop.getHeight());
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
        planeImageView.setFitWidth(displayPlaneWidth.get());

        Image planeImage2 = new Image(getClass().getResource("/images/Plane-TopView2.png").toExternalForm());
        ImageView planeImageView2 = new ImageView(planeImage2);
        planeImageView2.setPreserveRatio(true);
        planeImageView2.setScaleX(-1);
        planeImageView2.setFitWidth(displayPlaneWidth.get());




        if (planeBox.getChildren().size() != 0){
            planeBox.getChildren().remove(1);
            planeBox.getChildren().add(planeImageView);
        }

        if (planeBox2.getChildren().size() != 0){
            planeBox2.getChildren().remove(0);
            planeBox2.getChildren().add(0, planeImageView2);
        }



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


        obstacleBox.setAlignment(Pos.CENTER_LEFT);


        planeBox.setAlignment(Pos.CENTER_LEFT);
        planeBox2.setAlignment(Pos.CENTER_RIGHT);

        displayStackPaneTop.getChildren().clear();
        displayStackPaneTop.getChildren().addAll(gradeAreaImageView, runwayPaneBox, stopWayBox, clearWayBox, obstacleBox, planeBox, planeBox2);

        // Designator Display
        Text designator1 = new Text();
        designator1.getStyleClass().add("display-designator-text");
        designator1.textProperty().bind(subRunway1.getDesignator());
        designator1.setRotate(90);
        Text designator2 = new Text();
        designator2.getStyleClass().add("display-designator-text");
        designator2.textProperty().bind(subRunway2.getDesignator());
        designator2.setRotate(-90);
        HBox emptyHbox = new HBox();
        emptyHbox.getStyleClass().add("empty");
        emptyHbox.setPrefWidth(600);


        HBox designatorBox = new HBox();
        designatorBox.setId("designatorBox");
        designatorBox.getStyleClass().add("empty");
        designatorBox.setAlignment(Pos.CENTER);
        designatorBox.getChildren().addAll(designator1, emptyHbox, designator2);

        displayStackPaneTop.getChildren().add(designatorBox);
        displayStackPaneTop.getChildren().add(makeDisplacedThreshold());


        displayStackPaneTop.getChildren().add(makeIndicators1()); // TORA, TODA ... for left sub runway
        displayStackPaneTop.getChildren().add(makeIndicators2()); // TORA, TODA ... for right sub runway




        changeColourScheme();



        return displayStackPaneTop;



    }

    /**
     * Create the simultaneous middle display box
     * @return StackPane Middle display box
     */
    public StackPane makeSimultaneousMiddleDisplayBox(){
        StackPane displayStackPane = new StackPane();
        BorderPane displayBorderPane = new BorderPane();
        displayStackPane.getChildren().add(displayBorderPane);
        displayStackPane.getChildren().addAll(makeDisplacedThreshold(), makeIndicators1(), makeIndicators2());

        currentView = "Simultaneous";


        // Top-View Part
        //StackPane topViewPane = new StackPane();

        changeColourSchemeTopSim();

        displayBorderPane.setTop(topViewPane);
        topViewPane.prefHeightProperty().bind(displayBorderPane.heightProperty().divide(2));


        // Top-View Runway Image
        Image toprunwayImage = new Image(getClass().getResource("/images/Runway1.png").toExternalForm());
        ImageView toprunwayImageView = new ImageView(toprunwayImage);
        toprunwayImageView.setFitHeight(50);
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

        stopWayTop2.visibleProperty().bind(stopWayLength1.greaterThan(0));
        stopWayTop1.visibleProperty().bind(stopWayLength2.greaterThan(0));

        // Clearways
        HBox topClearWayBox = new HBox();
        topClearWayBox.getStyleClass().add("empty");
        topClearWayBox.setAlignment(Pos.CENTER);

        HBox topclearWay1 = new HBox();
        topclearWay1.getStyleClass().add("clearway-box");
        topclearWay1.visibleProperty().bind(clearWayLength2.greaterThan(0));
        topclearWay1.prefWidthProperty().bind(displayClearWayLength);

        HBox distanceBetweenStopways1 = new HBox();
        distanceBetweenStopways1.getStyleClass().add("empty");
        distanceBetweenStopways1.setPrefWidth(displayRunwayLength.get());

        HBox topclearWay2 = new HBox();
        topclearWay2.getStyleClass().add("clearway-box");
        topclearWay2.visibleProperty().bind(clearWayLength1.greaterThan(0));
        topclearWay2.prefWidthProperty().bind(displayClearWayLength);


        topClearWayBox.getChildren().addAll(topclearWay1, distanceBetweenStopways1, topclearWay2);




        // Add stopways and runway to the top view pane
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
        planeImageViewTop.setFitWidth(displayPlaneWidth.get());
        planeImageViewTop.setPreserveRatio(true);

        Image planeImageTop2 = new Image(getClass().getResource("/images/Plane-TopView2.png").toExternalForm());
        ImageView planeImageViewTop2 = new ImageView(planeImageTop2);
        planeImageViewTop2.setFitWidth(displayPlaneWidth.get());
        planeImageViewTop2.setPreserveRatio(true);


        // Empty boxes to push the plane to the center
        HBox frontPlaneTopEmpty = new HBox();
        frontPlaneTopEmpty.getStyleClass().add("empty");
        frontPlaneTopEmpty.prefWidthProperty().bind(Bindings.add(displayBorderToRunway,displayRunwayToPlane));




        // Obstacle Image
        Image obstacleImageTop = new Image(getClass().getResource("/images/Obstacle.png").toExternalForm());
        ImageView obstacleImageViewTop = new ImageView(obstacleImageTop);
        obstacleImageViewTop.setFitWidth(30);
        obstacleImageViewTop.setPreserveRatio(true);



        // Obstacle
        HBox obstacleTopBox = new HBox();
        obstacleTopBox.getStyleClass().add("empty");
        obstacleTopBox.setAlignment(Pos.CENTER_LEFT);
        obstacleTopBox.visibleProperty().bind(displayBorderToObstacle.greaterThan(0));

        HBox borderToObstacle = new HBox();
        borderToObstacle.getStyleClass().add("empty");
        borderToObstacle.prefWidthProperty().bind( ( displayBorderToObstacle ));
        obstacleTopBox.getChildren().addAll(borderToObstacle, obstacleImageViewTop);



        // Graded Area
        Image gradeArea = new Image(getClass().getResource("/images/GradedArea.png").toExternalForm());
        ImageView gradeAreaImageView = new ImageView(gradeArea);
        gradeAreaImageView.setFitHeight(200);
        gradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);
        // Border for the views
        topViewPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 2, 0))));

        //Top view label
        VBox topViewLabelBox = new VBox();
        topViewLabelBox.setAlignment(Pos.BOTTOM_CENTER);
        VBox topViewEmpty = new VBox();
        topViewEmpty.getStyleClass().add("empty");
        VBox.setVgrow(topViewEmpty, Priority.ALWAYS);
        Label topViewLabel = new Label("Top View");
        topViewLabel.getStyleClass().add("view-label");
        topViewLabelBox.getChildren().addAll(topViewLabel, topViewEmpty);


        // Add the runway, plane, obstacle and graded area to the top view pane
        topViewPane.getChildren().addAll(gradeAreaImageView, runwayTopPaneBox, obstacleTopBox, topViewLabelBox, topClearWayBox);

        // Designator Display
        Text designator1 = new Text();
        designator1.getStyleClass().add("display-designator-text");
        designator1.textProperty().bind(subRunway1.getDesignator());
        Text designator2 = new Text();
        designator2.getStyleClass().add("display-designator-text");
        designator2.textProperty().bind(subRunway2.getDesignator());

        HBox emptyHbox = new HBox();
        emptyHbox.getStyleClass().add("empty");
        HBox.setHgrow(emptyHbox, Priority.ALWAYS);


        HBox designatorBox = new HBox();
        designatorBox.getStyleClass().add("empty");
        designatorBox.setAlignment(Pos.CENTER);
        designatorBox.getChildren().addAll(designator1, emptyHbox, designator2);

        topViewPane.getChildren().add(designatorBox);


        // Side-View Part
        StackPane sideViewStackPane = new StackPane();
        BorderPane sideViewPane = new BorderPane();
        sideViewStackPane.getChildren().add(sideViewPane);
        displayBorderPane.setBottom(sideViewStackPane);

        // Sky Part
        //BorderPane bluePane = new BorderPane();
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



        // Obstacle Image
        Image obstacleImageSide = new Image(getClass().getResource("/images/Obstacle.png").toExternalForm());
        ImageView obstacleImageViewSide = new ImageView(obstacleImageSide);
        obstacleImageViewSide.setPreserveRatio(true);
        obstacleImageViewSide.setFitWidth(30);



        // Obstacle
        HBox obstacleSideBox = new HBox();
        obstacleSideBox.getStyleClass().add("empty");
        obstacleSideBox.setAlignment(Pos.CENTER_LEFT);
        obstacleSideBox.visibleProperty().bind(displayBorderToObstacle.greaterThan(0));

        HBox borderToObstacleSide = new HBox();
        borderToObstacleSide.getStyleClass().add("empty");
        borderToObstacleSide.prefWidthProperty().bind( ( displayBorderToObstacle ));
        obstacleSideBox.getChildren().addAll(borderToObstacleSide, obstacleImageViewSide);


        bluePane.setBottom(obstacleSideBox);


        // Ground Part
        //BorderPane groundPane = new BorderPane();
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

        stopWay2.visibleProperty().bind(stopWayLength1.greaterThan(0));
        stopWay1.visibleProperty().bind(stopWayLength2.greaterThan(0));


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

        sideViewStackPane.getChildren().add(clearWayBox);

        groundPane.setTop(runwayPaneBox);

        // Designator Display
        Text designator3 = new Text();
        designator3.getStyleClass().add("display-designator-text");
        designator3.textProperty().bind(subRunway1.getDesignator());
        Text designator4 = new Text();
        designator4.getStyleClass().add("display-designator-text");
        designator4.textProperty().bind(subRunway2.getDesignator());

        HBox emptyHbox2 = new HBox();
        emptyHbox2.getStyleClass().add("empty");
        HBox.setHgrow(emptyHbox2, Priority.ALWAYS);


        HBox designatorBox2 = new HBox();
        designatorBox2.getStyleClass().add("empty");
        designatorBox2.setAlignment(Pos.CENTER);
        designatorBox2.getChildren().addAll(designator3, emptyHbox2, designator4);

        sideViewStackPane.getChildren().add(designatorBox2);

        // check box for showing indicators
        StackPane cheboxPane = new StackPane();
        CheckBox indicatorsCheckBox = new CheckBox("Enable Indicators");
        for (Node node : indicatorsSubRunway1.getChildren()){
            node.setVisible(false);
        }
        for (Node node : indicatorsSubRunway2.getChildren()){
            node.setVisible(false);
        }

        indicatorsCheckBox.setOnAction( e -> {
            if (indicatorsCheckBox.isSelected()) {
                for (Node node : indicatorsSubRunway1.getChildren()){
                    node.setVisible(true);
                }
                for (Node node : indicatorsSubRunway2.getChildren()){
                    node.setVisible(true);
                }

                notificationMessage.set("Enable Indicators");
            }
            else {
                for (Node node : indicatorsSubRunway1.getChildren()){
                    node.setVisible(false);
                }
                for (Node node : indicatorsSubRunway2.getChildren()){
                    node.setVisible(false);
                }
                notificationMessage.set("Disable Indicators");
            }
            FadeTransition fd = new FadeTransition(Duration.millis(3000), notificationLabel);
            fd.setFromValue(1.0);
            fd.setToValue(0.0);
            fd.play();

        });


        cheboxPane.toFront();

        cheboxPane.getChildren().add(indicatorsCheckBox);
        displayStackPane.getChildren().add(cheboxPane);
        StackPane.setAlignment(indicatorsCheckBox, Pos.BOTTOM_RIGHT);




        changeColourSchemeTopSim();
//        changeBaseSceneColours();
        return displayStackPane;
    }

    public void changeColourScheme(){
        //System.out.println(currentState.getColourSettting());
        if (currentState.getColourSettting() == "Default (Blue/Green)"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box");
                bluePane.getStyleClass().clear();
                bluePane.getStyleClass().add("sideView-background");
                groundPane.getStyleClass().clear();
                groundPane.getStyleClass().add("sideView-ground");

            });



        }else if (currentState.getColourSettting() == "Blue/Yellow"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box");
                bluePane.getStyleClass().clear();
                bluePane.getStyleClass().add("sideView-background-Deuteranopia");
                groundPane.getStyleClass().clear();
                groundPane.getStyleClass().add("sideView-ground-Deuteranopia");

            });
        }else if (currentState.getColourSettting() == "Magenta/Lime Green"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box-limegreen");
                bluePane.getStyleClass().clear();
                bluePane.getStyleClass().add("sideView-background-Tritanopia");
                groundPane.getStyleClass().clear();
                groundPane.getStyleClass().add("sideView-ground-Tritanopia");

            });

        }else if (currentState.getColourSettting() == "Cyan/Deep Purple"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box-purple");
                bluePane.getStyleClass().clear();
                bluePane.getStyleClass().add("sideView-background-Protanopia");
                groundPane.getStyleClass().clear();
                groundPane.getStyleClass().add("sideView-ground-Protanopia");

            });
        }
    }

    public void makeMusicSettingPage() {
        Stage musicSetting = new Stage();
        musicSetting.initModality(Modality.WINDOW_MODAL);

        MusicPlayer player = new MusicPlayer(getClass().getResource("/Music/today-is-the-day.mp3").toExternalForm());

        ToggleGroup group = new ToggleGroup();
        RadioButton onOption = new RadioButton("On");
        onOption.setToggleGroup(group);
        RadioButton offOption = new RadioButton("Off");
        offOption.setToggleGroup(group);

        if (currentState.getMusicSetting().equals("On")) {
            onOption.setSelected(true);
            player.play();
        } else if (currentState.getMusicSetting().equals("Off")) {
            offOption.setSelected(true);
            player.stop();
        }

        RadioButton initiallySelected = (RadioButton) group.getSelectedToggle();

        Button applyButton = new Button("Exit");
        applyButton.setOnAction(e -> musicSetting.close());

        applyButton.setOnAction(e -> {
            currentState.setMusicSetting(((RadioButton) group.getSelectedToggle()).getText());
            if (currentState.getMusicSetting().equals("On")) {
                player.play();
            } else if (currentState.getMusicSetting().equals("Off")) {
                player.stop();
            }
            musicSetting.close();
        });

        // Listen for changes in selection
        group.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != initiallySelected) {
                applyButton.setText("Apply");
            } else {
                applyButton.setText("Exit");
            }
        });

        VBox layout = new VBox(10, new Label("Music:"), onOption, offOption, applyButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 200);
        musicSetting.setScene(scene);
        musicSetting.show();
    }

    public void makeAirportInformation(){
        Stage airportInforStage = new Stage();
        airportInforStage.initModality(Modality.WINDOW_MODAL);


        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);



        Label airportName = new Label("Airport Name: " + currentAirport.getName());
        layout.getChildren().add(airportName);

        VBox subRunwayBox1 = new VBox(10);
        subRunwayBox1.setAlignment(Pos.CENTER);
        Label subRunway1Name = new Label("Runway 1: " + subRunway1.getDesignator().getValue());
        Label subRunway1DisplacedThreshold = new Label("Displaced Threshold: " + subRunway1.getDisplacedThreshold().getValue() + "m");
        Label subRunway1RESA = new Label("RESA: " + subRunway1.getRESA().getValue() + "m");
        Label subRunway1StripEnd = new Label("Strip End: " + subRunway1.getStripEndLength().getValue() + "m");
        Label subRunway1BlastProtection = new Label("Blast Protection: " + subRunway1.getBlastProtection().getValue() + "m");
        subRunwayBox1.getChildren().addAll(subRunway1Name, subRunway1DisplacedThreshold, subRunway1RESA, subRunway1StripEnd, subRunway1BlastProtection);

        VBox subRunwayBox2 = new VBox(10);
        subRunwayBox2.setAlignment(Pos.CENTER);
        Label subRunway2Name = new Label("Runway 2: " + subRunway2.getDesignator().getValue());
        Label subRunway2DisplacedThreshold = new Label("Displaced Threshold: " + subRunway2.getDisplacedThreshold().getValue() + "m");
        Label subRunway2RESA = new Label("RESA: " + subRunway2.getRESA().getValue() + "m");
        Label subRunway2StripEnd = new Label("Strip End: " + subRunway2.getStripEndLength().getValue() + "m");
        Label subRunway2BlastProtection = new Label("Blast Protection: " + subRunway2.getBlastProtection().getValue() + "m");
        subRunwayBox2.getChildren().addAll(subRunway2Name, subRunway2DisplacedThreshold, subRunway2RESA, subRunway2StripEnd, subRunway2BlastProtection);

        HBox subRunwayHBox = new HBox(10, subRunwayBox1, subRunwayBox2);
        subRunwayHBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(subRunwayHBox);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> airportInforStage.close());
        layout.getChildren().add(closeButton);

        Scene scene = new Scene(layout, 500, 300);
        airportInforStage.setScene(scene);
        airportInforStage.show();



    }




    public void makeColourSettingPage(){
        Stage colourSetting = new Stage();
        colourSetting.initModality(Modality.WINDOW_MODAL);


       ToggleGroup group = new ToggleGroup();
//        RadioButton defaultOption = new RadioButton("Default");
        defaultOption.setToggleGroup(group);
//        RadioButton option1 = new RadioButton("Option 1");
        option1.setToggleGroup(group);
//        RadioButton option2 = new RadioButton("Option 2");
        option2.setToggleGroup(group);
//        RadioButton option3 = new RadioButton("Option 3");
        option3.setToggleGroup(group);

        if (currentState.getColourSettting() == "Default (Blue/Green)"){
            defaultOption.setSelected(true);
        } else if (currentState.getColourSettting() == "Blue/Yellow"){
            option1.setSelected(true);
        } else if (currentState.getColourSettting() == "Magenta/Lime Green"){
            option2.setSelected(true);
        } else if (currentState.getColourSettting() == "Cyan/Deep Purple"){
            option3.setSelected(true);
        }

        RadioButton initiallySelected = (RadioButton) group.getSelectedToggle();

        Button applyButton = new Button("Exit");
        applyButton.setOnAction(e -> colourSetting.close());

        applyButton.setOnAction(e -> {
                    currentState.setColourSettting(((RadioButton) group.getSelectedToggle()).getText());
                    colourSetting.close();
                    changeColourScheme();
                    changeColourSchemeTop();
                    changeColourSchemeTopSim();
                    changeBaseSceneColours();

                    //here there needs to be an update style section. not really sure how to do it

                }


        );

        // Listen for changes in selection
        group.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal != initiallySelected) {
                applyButton.setText("Apply");
            } else {
                applyButton.setText("Exit");
            }
        });

        VBox layout = new VBox(10, new Label("Please select the colour scheme you would like:"), defaultOption, option1, option2, option3, applyButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 200);
        colourSetting.setScene(scene);
        colourSetting.show();
    }

    public void changeColourSchemeTop(){
        if (displayStackPaneTop.getChildren().isEmpty()){
            return;
        }
        //System.out.println(currentState.getColourSettting());
        if (currentState.getColourSettting() == "Default (Blue/Green)"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box");

                middleDisplayBox.getStyleClass().clear();
                displayStackPaneTop.getStyleClass().clear();
                displayStackPaneTop.getStyleClass().add("topView-background");

                Image defaultGradeArea = new Image(getClass().getResource("/images/GradedArea.png").toExternalForm());
                ImageView defaultGradeAreaImageView = new ImageView(defaultGradeArea);
                defaultGradeAreaImageView.setPreserveRatio(true);
                defaultGradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);

                displayStackPaneTop.getChildren().set(0,defaultGradeAreaImageView);
            });



        }else if (currentState.getColourSettting() == "Blue/Yellow"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box");


                displayStackPaneTop.getStyleClass().clear();
                middleDisplayBox.getStyleClass().clear();
                middleDisplayBox.getStyleClass().add("topView-background-Deuteranopia");

                Image yellowGradeArea = new Image(getClass().getResource("/images/yellowGraded.png").toExternalForm());
                ImageView yellowGradeAreaImageView = new ImageView(yellowGradeArea);
                yellowGradeAreaImageView.setPreserveRatio(true);
                yellowGradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);
                displayStackPaneTop.getChildren().set(0,yellowGradeAreaImageView);
            });
        }else if (currentState.getColourSettting() == "Magenta/Lime Green"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box-limegreen");

                displayStackPaneTop.getStyleClass().clear();
                middleDisplayBox.getStyleClass().clear();
                middleDisplayBox.getStyleClass().add("topView-background-Tritanopia");

                Image greenGradeArea = new Image(getClass().getResource("/images/greenGraded.png").toExternalForm());
                ImageView greenGradeAreaImageView = new ImageView(greenGradeArea);
                greenGradeAreaImageView.setPreserveRatio(true);
                greenGradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);
                displayStackPaneTop.getChildren().set(0,greenGradeAreaImageView);
            });

        }else if (currentState.getColourSettting() == "Cyan/Deep Purple"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box-purple");

                displayStackPaneTop.getStyleClass().clear();
                middleDisplayBox.getStyleClass().clear();
                middleDisplayBox.getStyleClass().add("topView-background-Protanopia");

                Image purpleGradeArea = new Image(getClass().getResource("/images/purpleGraded.png").toExternalForm());
                ImageView purpleGradeAreaImageView = new ImageView(purpleGradeArea);
                purpleGradeAreaImageView.setPreserveRatio(true);
                purpleGradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);
                displayStackPaneTop.getChildren().set(0,purpleGradeAreaImageView);
            });
        }
    }

    public void changeColourSchemeTopSim(){
        if (topViewPane.getChildren().isEmpty()){
            return;
        }
        //System.out.println(currentState.getColourSettting());
        if (currentState.getColourSettting() == "Default (Blue/Green)"){
            Platform.runLater( () -> {


                topViewPane.getStyleClass().clear();
                topViewPane.getStyleClass().add("topView-background");

                Image gradeArea = new Image(getClass().getResource("/images/GradedArea.png").toExternalForm());
                ImageView gradeAreaImageView = new ImageView(gradeArea);
                gradeAreaImageView.setFitHeight(200);
                gradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);
                topViewPane.getChildren().set(0, gradeAreaImageView);
            });



        }else if (currentState.getColourSettting() == "Blue/Yellow"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box");

                topViewPane.getStyleClass().clear();
                topViewPane.getStyleClass().add("topView-background-Deuteranopia");

                Image gradeArea = new Image(getClass().getResource("/images/yellowGraded.png").toExternalForm());
                ImageView gradeAreaImageView = new ImageView(gradeArea);
                gradeAreaImageView.setFitHeight(200);
                gradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);
                topViewPane.getChildren().set(0, gradeAreaImageView);
            });
        }else if (currentState.getColourSettting() == "Magenta/Lime Green"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box-limegreen");

                topViewPane.getStyleClass().clear();
                topViewPane.getStyleClass().add("topView-background-Tritanopia");

                Image gradeArea = new Image(getClass().getResource("/images/greenGraded.png").toExternalForm());
                ImageView gradeAreaImageView = new ImageView(gradeArea);
                gradeAreaImageView.setFitHeight(200);
                gradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);
                topViewPane.getChildren().set(0, gradeAreaImageView);
            });

        }else if (currentState.getColourSettting() == "Cyan/Deep Purple"){
            Platform.runLater( () -> {
                menuBox.getStyleClass().clear();
                menuBox.getStyleClass().add("menu-box-purple");

                topViewPane.getStyleClass().clear();
                topViewPane.getStyleClass().add("topView-background-Protanopia");

                Image gradeArea = new Image(getClass().getResource("/images/purpleGraded.png").toExternalForm());
                ImageView gradeAreaImageView = new ImageView(gradeArea);
                gradeAreaImageView.setFitHeight(200);
                gradeAreaImageView.setFitWidth(displayRunwayLength.getValue() + 2 * displayStopWayLength.getValue() + 50);
                topViewPane.getChildren().set(0, gradeAreaImageView);
            });
        }
    }

    private void exportPDF() throws IOException {

        // Temporary directory
        String tempDir = System.getProperty("java.io.tmpdir");

        // Define the file path for the image in the temporary directory
        String imagePath = tempDir + "tempChart.png";

        WritableImage image = mainPane.snapshot(null, null);

        File imageFile = new File(imagePath);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", imageFile);

        notificationMessage.set("Image is Saved");
        FadeTransition fd = new FadeTransition(Duration.millis(3000), notificationLabel);
        fd.setFromValue(1.0);
        fd.setToValue(0.0);
        fd.play();


        //PDF creating
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report as PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        //TODO checks
        if (file != null) {

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            PDImageXObject pdImage = PDImageXObject.createFromFileByContent(imageFile, document);

            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float imageWidth = pdImage.getWidth() * 0.38f; // Adjust the scale factor as needed
            float imageHeight = pdImage.getHeight() * 0.38f; // Adjust the scale factor as needed
            contentStream.drawImage(pdImage, 50f, 450f, imageWidth, imageHeight);


            contentStream.beginText();
            contentStream.newLineAtOffset(25, 700);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(0, -270); // Move the text further down on the page
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 9);

            if (firstDirectionButton.isSelected()) {
                String[] TORA = (RunwayCalculator.breakdownTORA(subRunway1, currentObstacle, subRunway1.getObstacleDistance())).split("\n");
                for (String line : TORA) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }
            } else if (secondDirectionButton.isSelected()) {
                String[] TORA = (RunwayCalculator.breakdownTORA(subRunway2, currentObstacle, subRunway2.getObstacleDistance())).split("\n");
                for (String line : TORA) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }
            }

            contentStream.newLineAtOffset(0, -40); // Move the text further down on the page

            if (firstDirectionButton.isSelected()) {
                String[] TODA = (RunwayCalculator.breakdownTODA(subRunway1, currentObstacle, subRunway1.getObstacleDistance())).split("\n");
                for (String line : TODA) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }
            } else if (secondDirectionButton.isSelected()) {
                String[] TODA = (RunwayCalculator.breakdownTODA(subRunway2, currentObstacle, subRunway2.getObstacleDistance())).split("\n");
                for (String line : TODA) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }
            }

            contentStream.newLineAtOffset(310, 242); // Move the text further down on the page

            if (firstDirectionButton.isSelected()) {
                String[] ASDA = (RunwayCalculator.breakdownASDA(subRunway1, currentObstacle, subRunway1.getObstacleDistance())).split("\n");
                for (String line : ASDA) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }
            } else if (secondDirectionButton.isSelected()) {
                String[] ASDA = (RunwayCalculator.breakdownASDA(subRunway2, currentObstacle, subRunway2.getObstacleDistance())).split("\n");
                for (String line : ASDA) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }
            }

            contentStream.newLineAtOffset(0, -35); // Move the text further down on the page

            if (firstDirectionButton.isSelected()) {
                String[] LDA = (RunwayCalculator.breakdownLDA(subRunway1, currentObstacle, subRunway1.getObstacleDistance())).split("\n");
                for (String line : LDA) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }
            } else if (secondDirectionButton.isSelected()) {
                String[] LDA = (RunwayCalculator.breakdownLDA(subRunway2, currentObstacle, subRunway2.getObstacleDistance())).split("\n");
                for (String line : LDA) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }
            }


            contentStream.endText();
            contentStream.close();

            document.save(file);
            document.close();

            notificationMessage.set("Pdf has been created");
            FadeTransition fd2 = new FadeTransition(Duration.millis(3000), notificationLabel);
            fd2.setFromValue(1.0);
            fd2.setToValue(0.0);
            fd2.play();
        }
    }











}
