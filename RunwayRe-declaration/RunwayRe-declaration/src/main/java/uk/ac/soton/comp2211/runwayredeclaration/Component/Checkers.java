package uk.ac.soton.comp2211.runwayredeclaration.Component;

public class Checkers {

    private double height;
    private double width;
    private double distance;

    public Checkers(double height, double width, double distance){
        this.height = height;
        this.width = width;
        this.distance = distance;
    }


    public String obstacleChecker(double height, double width, double distance1, double distance2, SubRunway runway) {
        if (height <= 0 && width <= 0 && distance < 0) {
            return "Invalid height, width and distance(values are negative)";
        } else if (height <= 0 && width <= 0) {
            return "Invalid height and width(values are negative)";
        } else if (height <= 0) {
            return "Invalid height(values are negative)";
        } else if (width <= 0) {
            return "Invalid width(values are negative)";
        } else if (height > 100) {
            return "Your obstacle height is unreasonable for a runway (" + height + ") <100m";
        } else if ((width > 75) || (width < -75)){
            return "Distance is too far away from the centerline";
        }
        return null; // No error

    }
}



