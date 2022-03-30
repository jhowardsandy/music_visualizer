package com.example.musicvisualizer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller for all the music player's button functions.
 * Built with a lot of help from (<a href="https://www.youtube.com/watch?v=-D2OIekCKes">BroCode's JavaFX Music Player Tutorial</a>)
 * @author Rianna M, all methods unless otherwise stated.
 */
public class MusicVisualizerController implements Initializable {

    //Button Declaration

    @FXML
    private Pane pane;

    @FXML
    private Label songLabel;

    @FXML
    private Button restartBtn, previousBtn, playPauseBtn, nextBtn, fileBtn;

    //Paths for SVG shapes for toggling play and pause button.

    @FXML
    SVGPath playPauseSVG = new SVGPath();
    String playPath = "M361 215C375.3 223.8 384 239.3 384 256C384 272.7 375.3 288.2 361 296.1L73.03 472.1C58.21 482 39.66 482.4 24.52 473.9C9.377 465.4 0 449.4 0 432V80C0 62.64 9.377 46.63 24.52 38.13C39.66 29.64 58.21 29.99 73.03 39.04L361 215z";
    String pausePath = "M272 63.1l-32 0c-26.51 0-48 21.49-48 47.1v288c0 26.51 21.49 48 48 48L272 448c26.51 0 48-21.49 48-48v-288C320 85.49 298.5 63.1 272 63.1zM80 63.1l-32 0c-26.51 0-48 21.49-48 48v288C0 426.5 21.49 448 48 448l32 0c26.51 0 48-21.49 48-48v-288C128 85.49 106.5 63.1 80 63.1z";

    @FXML
    private Slider volumeSlider;

    @FXML
    private ProgressBar songProgressBar;

    //Filepath Variables

    private File songDirectory;

    private File[] songFiles;

    private ArrayList<File> songList;

    private int songNumber;

    //Timers

    private Timer progBarTimer;

    private TimerTask progBarTask;

    //Logic check for status of song.

    private boolean isPlaying; //if music is playing AT ALL (vs. being stopped)
    private boolean isPaused = true; //if music is in a paused state. used to toggle the play/pause button

    //MediaPlayer

    private Media songMedia;
    private MediaPlayer songMediaPlayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songList = new ArrayList<File>();
        songDirectory = new File("src/main/resources/music");
        songFiles = songDirectory.listFiles();
        if(songFiles != null) {
            for (File oneFile : songFiles) {
                songList.add(oneFile);
            }
        }

        else{
                System.err.print("ERROR: Song directory is incorrect or empty!");
            }

        songMedia = new Media(songList.get(songNumber).toURI().toString());
        songMediaPlayer = new MediaPlayer(songMedia);
        songLabel.setText(songList.get(songNumber).getName().replaceFirst("[.][^.]+$", "")); //Filename is included, extension is truncated.

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                songMediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });
    }


    /**
     * Plays and pauses the music.
     */
    public void playPauseMedia(){
        if(isPaused == true){
        beginTimer();
        songMediaPlayer.setAudioSpectrumListener(new SpektrumListener());
        songMediaPlayer.setAudioSpectrumInterval(.5);
        songMediaPlayer.play();
        songMediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        isPaused= false;
        playPauseSVG.setContent(pausePath);
        }

        else{
            isPaused = true;
            playPauseSVG.setContent(playPath);
            cancelTimer();
            songMediaPlayer.pause();
        }
    }


    /**
     *  Restarts the music.
     */
    public void restartMedia(){
        songProgressBar.setProgress(0);
        songMediaPlayer.seek(Duration.seconds(0));

        if(!isPlaying){
            beginTimer();
        }
    }

    /**
     *  Stops the current song and then plays the previous song on the playlist.
     */
    public void previousMedia(){
        if(songNumber > 0){
            songNumber -=1;
            songMediaPlayer.stop();
        }

        else{songNumber = songList.size() -1;} //returns to start of the playlist if the end of the playlist is reached.

        if(isPlaying){
            cancelTimer();
        }

        songMedia = new Media(songList.get(songNumber).toURI().toString());
        songMediaPlayer = new MediaPlayer(songMedia);
        songLabel.setText(songList.get(songNumber).getName().replaceFirst("[.][^.]+$", "")); //Filename is included, extension is truncated.
        isPaused = true;
        playPauseMedia(); //autoplays next song
    }


    /**
     *  Stops the current song and then plays the next song on the playlist.
     */
    public void nextMedia(){

        if(songNumber < songList.size() -1){
            songNumber +=1;
            songMediaPlayer.stop();
        }

        else{songNumber = 0;} //returns to start of the playlist if the end of the playlist is reached.

        if(isPlaying){
            cancelTimer();
        }

        songMedia = new Media(songList.get(songNumber).toURI().toString());
        songMediaPlayer = new MediaPlayer(songMedia);
        songLabel.setText(songList.get(songNumber).getName().replaceFirst("[.][^.]+$", "")); //Filename is included, extension is truncated.

        isPaused = true;
        playPauseMedia(); //autoplays next song
    }


    public void fileChooseMedia(){
        //TODO WANTED ADDITIONAL FUNCTION
    }

    /**
     * Starts timer for progression bar visuals.
     */
    public void beginTimer() {
        progBarTimer = new Timer();

        progBarTask = new TimerTask() {
            public void run() {
                isPlaying = true;
                double currentTime = songMediaPlayer.getCurrentTime().toSeconds();
                double endTime = songMedia.getDuration().toSeconds();
                songProgressBar.setProgress(currentTime / endTime);
                //System.out.println((currentTime / endTime) * 100); testing bar progression values.
                if (currentTime / endTime == 1) {
                    cancelTimer();
                    isPaused = true; //test for debug
                }
            }
        };

        progBarTimer.scheduleAtFixedRate(progBarTask, 0, 1000);
    }

    /**
     * Cancels timer for progression bar visuals.
     */
    public void cancelTimer(){
        isPlaying = false;
        progBarTimer.cancel();
    }

    public class SpektrumListener implements AudioSpectrumListener {

        @Override
        public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {

        }
    }
}