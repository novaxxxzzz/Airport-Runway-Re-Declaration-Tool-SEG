package uk.ac.soton.comp2211.runwayredeclaration.Component;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DashedLine extends Rectangle {
    public DashedLine(double width, double height) {
        super(width, height);
        setFill(null);
        setStroke(Color.web("#4472C4"));
        setStrokeWidth(1);
        getStrokeDashArray().addAll(2d, 10d);
//        setStroke(Color.web("transparent"));
    }

    public DashedLine(double width, double height, boolean isDashed) {
        super(width, height);
        setFill(null);
        setStrokeWidth(1);
        setStroke(Color.web("#4472C4"));
        if (isDashed) {
            getStrokeDashArray().addAll(10d, 5d);

        }



    }

    public DashedLine(double width, double height, String color) {
        super(width, height);
        setFill(null);
        setStrokeWidth(1);
        setStroke(Color.web(color));
        getStrokeDashArray().addAll(10d, 5d);

    }

    public DashedLine(double width, double height, String color, boolean isDashed) {
        super(width, height);
        setFill(null);
        setStrokeWidth(1);
        setStroke(Color.web(color));
        if (isDashed) {
            getStrokeDashArray().addAll(10d, 5d);
        }

    }

}
