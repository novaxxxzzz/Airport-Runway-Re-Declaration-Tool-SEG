package uk.ac.soton.comp2211.runwayredeclaration.Component;

import java.util.ArrayList;

public class Airport {

    private String name;

    private ArrayList<Runway> runways;


    public Airport (String name){
        this.name = name;
        runways = new ArrayList<>();

    }


    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }


    public void addRunway(Runway runway){
        runways.add(runway);
    }

    public ArrayList<Runway> getRunways(){
        return runways;
    }

    public String toString(){
        return name;
    }
}
