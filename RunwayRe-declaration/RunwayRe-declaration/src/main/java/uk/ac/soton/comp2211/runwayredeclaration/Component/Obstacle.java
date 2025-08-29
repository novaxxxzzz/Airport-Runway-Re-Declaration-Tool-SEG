package uk.ac.soton.comp2211.runwayredeclaration.Component;
import javafx.scene.control.Alert;
import java.sql.SQLException;

public class Obstacle {

    private String name;

    private double height;
    private double distance_to_centreline;
    private double distance_to_lower_threshold;
    private double distance_to_higher_threshold;


    public Obstacle(String name){
        this.name = name;
    }
    public Obstacle(String name, double height){
        this.name = name;
        this.height = height;

    }

    public Obstacle(String name, double height, double distance_to_centreline, double distance_to_lower_threshold, double distance_to_higher_threshold){
        this.name = name;
        this.height = height;
        this.distance_to_centreline = distance_to_centreline;
        this.distance_to_lower_threshold = distance_to_lower_threshold;
        this.distance_to_higher_threshold = distance_to_higher_threshold;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }



    public void setHeight(double height){
        this.height = height;
    }
    public double getHeight(){
        return height;
    }
    public void setDistanceToCentreLine(double distance_to_centreline){
        this.distance_to_centreline = distance_to_centreline;
    }
    public double getDistanceToCentreLine(){
        return distance_to_centreline;
    }
    public void setDistanceToLowerThreshold(double distance_to_lower_threshold){
        this.distance_to_lower_threshold = distance_to_lower_threshold;
    }
    public double getDistanceToLowerThreshold(){
        return distance_to_lower_threshold;
    }
    public void setDistanceToHigherThreshold(double distance_to_higher_threshold){
        this.distance_to_higher_threshold = distance_to_higher_threshold;
    }
    public double getDistanceToHigherThreshold(){
        return distance_to_higher_threshold;
    }


    public String toString(){
        return name;
    }


}
