package uk.ac.soton.comp2211.runwayredeclaration.Component;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    public MusicPlayer(String filePath) {
        Media sound = new Media(filePath);
        mediaPlayer = new MediaPlayer(sound);
    }

    public void play() {
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void loop() {
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public boolean isPlaying() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}

