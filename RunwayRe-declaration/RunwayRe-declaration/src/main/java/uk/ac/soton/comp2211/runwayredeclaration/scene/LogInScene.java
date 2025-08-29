package uk.ac.soton.comp2211.runwayredeclaration.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import uk.ac.soton.comp2211.runwayredeclaration.Calculator.PasswordChecker;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Airport;
import uk.ac.soton.comp2211.runwayredeclaration.Component.User;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomePane;
import uk.ac.soton.comp2211.runwayredeclaration.ui.HomeWindow;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogInScene extends BaseScene{
    public LogInVBox logInVBox;
    public Button logIn;
    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param homeWindow the home window
     */
    public LogInScene(HomeWindow homeWindow) {
        super(homeWindow);
        logIn = new Button();
        logIn.setOnAction(e -> {
            homeWindow.startHome(logInVBox.getUser());
        });
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        root = new HomePane(homeWindow.getWidth(),homeWindow.getHeight());


        var logInPane = new StackPane();
        logInPane.setMaxWidth(homeWindow.getWidth());
        logInPane.setMaxHeight(homeWindow.getHeight());

        root.getChildren().add(logInPane);

        logInVBox = new LogInVBox(logIn);
        logInPane.getChildren().add(logInVBox);



    }

    private class LogInVBox extends VBox {
        private User user;

        private String adminPassword = "123456";
        private PasswordField passwordField;
        private TextField usernameTextField;
        private TextField adminPasswordField = new TextField();
        private TextField visiblePasswordField;
        private Button showHidePasswordButton;
        private Button logInSuccessful;

        private Map<String, User> users = new HashMap<>();


        public LogInVBox(Button logInSuccessful) {
            super();
            this.logInSuccessful = logInSuccessful;
            this.setMaxHeight(450);
            this.setMaxWidth(600);
            this.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));

            build();
            this.setAlignment(Pos.CENTER);
            this.user = new User("Guest", "", "User","");

        }
        /**
         * Initialise this scene. Called after creation
         */
        public void initialise(){};

        /**
         * Build the layout of the scene
         */
        public void build(){
            loadUsers();

            Label usernameLabel = new Label("Enter your Username:");
            Label passwordLabel = new Label("Enter your Password:");

            usernameTextField = new TextField();
            usernameTextField.setPromptText("Username");

            passwordField = new PasswordField();
            passwordField.setPromptText("Password");
            visiblePasswordField = new TextField();
            visiblePasswordField.setManaged(false);
            visiblePasswordField.setVisible(false);

            passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

            showHidePasswordButton = new Button("Show");
            showHidePasswordButton.setOnAction(e -> togglePasswordVisibility());

            Button loginButton = new Button("Log In");
            loginButton.setOnAction(e -> handleLogin());
            loginButton.getStyleClass().add("button-login");
            HBox boxForButton = new HBox(loginButton);
            boxForButton.setAlignment(Pos.CENTER);

            Hyperlink registerLink = new Hyperlink("here");
            registerLink.setOnAction(e -> handleRegister());

            Button guestButton = new Button("Continue as Guest");
            guestButton.setOnAction(e -> handleGuestLogin());
            guestButton.getStyleClass().add("button-guest");
            HBox boxForGuest = new HBox(guestButton);
            boxForGuest.setAlignment(Pos.CENTER);


            HBox userNameFields = new HBox(10, usernameLabel, usernameTextField);
            userNameFields.setAlignment(Pos.CENTER_LEFT);
            userNameFields.setPadding(new Insets(0,0,0,120));
            HBox passwordFields = new HBox(14, passwordLabel, passwordField, visiblePasswordField, showHidePasswordButton);
            passwordFields.setAlignment(Pos.CENTER_LEFT);
            passwordFields.setPadding(new Insets(0,0,0,120));

            VBox layout = new VBox(10); // 10 is the spacing between elements

            HBox noAccountBox = new HBox( new Label("Don't have an account? Register"),registerLink);
            noAccountBox.setAlignment(Pos.CENTER);

            VBox logInStuff = new VBox(15,userNameFields, passwordFields, boxForButton );
            ImageView planeIconView = new ImageView(new Image(getClass().getResourceAsStream("/images/planeIcon.png")));
            planeIconView.setFitHeight(150);
            planeIconView.setFitWidth(150);
            HBox plane = new HBox(planeIconView);
            plane.setAlignment(Pos.CENTER);
            VBox everything = new VBox(20,plane, logInStuff,boxForGuest, noAccountBox);

            layout.getChildren().addAll(everything);

            this.getChildren().add(layout);
        }

        private void togglePasswordVisibility() {
            if (passwordField.isVisible()) {
                passwordField.setManaged(false);
                passwordField.setVisible(false);
                visiblePasswordField.setManaged(true);
                visiblePasswordField.setVisible(true);
                showHidePasswordButton.setText("Hide");
            } else {
                passwordField.setManaged(true);
                passwordField.setVisible(true);
                visiblePasswordField.setManaged(false);
                visiblePasswordField.setVisible(false);
                showHidePasswordButton.setText("Show");
            }
        }

        private void loadUsers() {

            try {
                InputStream inputStream = getClass().getResourceAsStream("/predefined/Users.csv");
                if (inputStream == null) {
                    throw new IOException("Resource not found");
                }
                try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                        String[] parts = line.split(",");
                        if (parts.length == 4) {
                            users.put(parts[0], new User(parts[0], parts[1], parts[2],parts[3]));
                        }
                        else{
                            // Invalid file format
                            throw new IOException("Invalid file format");
                        }
                    }
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to Load User Data");
                alert.setContentText("The application could not parse the user data file: " + e.getMessage());
                alert.showAndWait();
            }
        }

        private void handleLogin() {
            User user = users.get(usernameTextField.getText());
            if (user != null && user.getPassword().equals(passwordField.getText())) {
                System.out.println("Login successful for " + user.getUsername() + " with permission: " + user.getPermissionLevel());
                this.user = user;
                logInSuccessful.fire();
            } else {
                usernameTextField.clear();
                passwordField.clear();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Incorrect username or password");
                alert.showAndWait();
            }
        }



        private void handleRegister() {
            System.out.println("Register link clicked");
            this.getChildren().clear();
            Label usernameLabel = new Label("Create your username:");
            Label passwordLabel = new Label("Enter your Password:");

            TextField newUsernameTextField = new TextField();
            newUsernameTextField.setPromptText("Username");

            TextField newPasswordField = new TextField();
            newPasswordField.setPromptText("Password");
            PasswordChecker passwordChecker = new PasswordChecker();
            passwordChecker.check(newPasswordField.getText());


            ImageView planeIconView = new ImageView(new Image(getClass().getResourceAsStream("/images/planeIcon.png")));
            planeIconView.setFitHeight(150);
            planeIconView.setFitWidth(150);
            HBox plane = new HBox(planeIconView);
            plane.setAlignment(Pos.CENTER);




            HBox usname = new HBox(5, usernameLabel, newUsernameTextField);
            usname.setAlignment(Pos.CENTER_LEFT);
            usname.setPadding(new Insets(0,0,0,120));
            Label checker = new Label(passwordChecker.check(newPasswordField.getText()));
            newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
                checker.setText(passwordChecker.check(newValue));
            });
            HBox pwname = new HBox(15, passwordLabel, newPasswordField, checker);
            pwname.setAlignment(Pos.CENTER_LEFT);
            pwname.setPadding(new Insets(0,0,0,120));




            ChoiceBox<String> roleChoiceBox = new ChoiceBox<>();
            roleChoiceBox.getItems().addAll("Admin", "User");
            roleChoiceBox.setValue("Select your role"); // Default text
            roleChoiceBox.setStyle("-fx-text-fill: black;");
            roleChoiceBox.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

            ChoiceBox<String> airportChoiceBox = new ChoiceBox<>();
            ArrayList<String> airports = new ArrayList<>();
            for (Airport airport : airportList) {
                airports.add(airport.getName());
            }
            airportChoiceBox.getItems().addAll(airports);
            airportChoiceBox.setValue("Select your airport"); // Default text
            airportChoiceBox.setStyle("-fx-text-fill: black;");
            airportChoiceBox.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));


            HBox roleBox = new HBox(5, roleChoiceBox,airportChoiceBox);
            roleBox.setAlignment(Pos.CENTER);



            // Continue as guest button
            Button createAccount = new Button("Create Account");
            createAccount.setAlignment(Pos.CENTER);
            createAccount.setOnAction(e -> {
                try {
                    if (roleChoiceBox.getValue().equals("Admin")){
                        if (!adminPasswordField.getText().equals(adminPassword)){
                            throw new Exception();
                        }
                    }
                    System.out.println("!!!!"+airportChoiceBox.getValue());
                    handleCreatingAccount(newUsernameTextField.getText(), newPasswordField.getText(), roleChoiceBox.getValue(), airportChoiceBox.getValue());
                } catch (Exception ex) {
                    Alert emptyFields = new Alert(Alert.AlertType.ERROR);
                    emptyFields.setTitle("Error");
                    emptyFields.setHeaderText("Unable to create account.");
                    emptyFields.setContentText("Please try again");
                    emptyFields.showAndWait();
                }
                newPasswordField.clear();
                newUsernameTextField.clear();
            });
            createAccount.getStyleClass().add("button-login");
            HBox boxForCreateAccount = new HBox(createAccount);
            boxForCreateAccount.setAlignment(Pos.CENTER);

            Button exit = new Button("Back");

            exit.setOnAction(e -> {
                homeWindow.loadScene(new LogInScene(homeWindow));
            });

            VBox register = new VBox(10,plane, usname, pwname, roleBox, boxForCreateAccount, exit);
            register.setAlignment(Pos.CENTER);

            roleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue != null) {
                    System.out.println("Selected role: " + newValue);
                }


                if (newValue.equals("Admin")){
                    adminPasswordField.clear();
                    adminPasswordField.setPromptText("Please enter admin password");
                    adminPasswordField.setAlignment(Pos.CENTER);
                    int secondToLastIndex = Math.max(0, register.getChildren().size() - 1);
                    register.getChildren().add(secondToLastIndex,adminPasswordField);
                }
                else {
                    try{
                        register.getChildren().remove(adminPasswordField);
                    }catch (Exception e){}
                }
            });
            this.getChildren().add(register);
        }



        private void handleCreatingAccount(String newUsername, String newPassword, String role, String airport) throws Exception{
            if (users.containsKey(newUsername)) {
                throw new Exception("Username already exists.");
            }
            if (newUsername == null || newUsername.isEmpty()){
                throw new Exception("Username cannot be empty.");

            }
            if (newPassword == null || newPassword.isEmpty()) {
                throw new Exception("Password cannot be empty.");
            }

            appendToCSV(newUsername,newPassword,role,airport);

            user = new User(newUsername, newPassword, role, airport);
            users.put(newUsername, user);
            logInSuccessful.fire();
        }


        private void appendToCSV(String username, String password, String role, String airport) {


            String newUserLine = "\n" + username + "," + password + "," + role + "," + airport;
            URL csvFilePath = getClass().getResource("/predefined/Users.csv");


            try {
                File usersFile = new File(csvFilePath.toURI());
                FileWriter fw = new FileWriter(usersFile,true);
                fw.write(newUserLine);
                fw.close();
                System.out.println("User added to file");
            } catch (Exception e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        }


        private void handleGuestLogin() {
            System.out.println("Guest login button clicked");
            logInSuccessful.fire();
            user.setPermissionLevel("Guest");

        }

        public User getUser() {
            return user;
        }
    }
}
