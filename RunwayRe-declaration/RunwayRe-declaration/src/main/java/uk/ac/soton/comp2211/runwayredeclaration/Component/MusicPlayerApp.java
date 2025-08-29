package uk.ac.soton.comp2211.runwayredeclaration.Component;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class MusicPlayerApp extends Application {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Media sound = new Media(new File("resources/game.wav").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        Button toggleButton = new Button("Play");
        toggleButton.setOnAction(event -> {
            if (isPlaying) {
                mediaPlayer.pause();
                toggleButton.setText("Play");
            } else {
                mediaPlayer.play();
                toggleButton.setText("Pause");
            }
            isPlaying = !isPlaying;
        });

        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> mediaPlayer.setVolume(newValue.doubleValue()));

        VBox vbox = new VBox(toggleButton, volumeSlider);
        Scene scene = new Scene(vbox, 200, 100);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
