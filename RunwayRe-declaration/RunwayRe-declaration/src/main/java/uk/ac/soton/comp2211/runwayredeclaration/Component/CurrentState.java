package uk.ac.soton.comp2211.runwayredeclaration.Component;

public class CurrentState {

    private String colourSettting;
    private String musicSetting = "Off"; // default value


    public CurrentState(){
        this.colourSettting = "Default (Blue/Green)";
    }
    public String getColourSettting() {
        return colourSettting;
    }

    public void setColourSettting(String colourSettting) {
        this.colourSettting = colourSettting;
    }

    public String getMusicSetting() {
        return musicSetting;
    }

    public void setMusicSetting(String musicSetting) {
        this.musicSetting = musicSetting;
    }
}