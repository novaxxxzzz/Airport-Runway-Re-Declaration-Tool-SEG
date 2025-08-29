package uk.ac.soton.comp2211.runwayredeclaration.Component;

public class User {
    String username;
    String password;
    String permissionLevel;
    String workingAirport;

    public User(String username, String password, String permissionLevel, String airport) {
        this.username = username;
        this.password = password;
        this.permissionLevel = permissionLevel;
        this.workingAirport = airport;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPermissionLevel() {
        return permissionLevel;
    }

    public String getWorkingAirport() {
        return workingAirport;
    }


    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}
