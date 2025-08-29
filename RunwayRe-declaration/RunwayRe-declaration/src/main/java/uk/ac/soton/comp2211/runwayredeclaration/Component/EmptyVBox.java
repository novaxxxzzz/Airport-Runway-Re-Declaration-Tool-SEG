package uk.ac.soton.comp2211.runwayredeclaration.Component;

import javafx.scene.layout.VBox;

public class EmptyVBox extends VBox {

    public EmptyVBox(double width, double height) {
        super();

        setPrefSize(width, height);
        getStyleClass().add("empty");

    }
}
