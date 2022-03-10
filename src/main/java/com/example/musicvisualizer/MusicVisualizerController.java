package com.example.musicvisualizer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MusicVisualizerController implements Initializable {

    //Button Declaration

    @FXML
    private Pane pane;

    @FXML
    private Label songLabel;

    @FXML
    private Button restartBtn, previousBtn, playBtn, pauseBtn, nextBtn, fileBtn;

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
    private boolean isPlaying;

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
                //System.out.println(oneFile);
            }
        }

        else{
                System.err.println("Song files were not found in specified directory!");
            }
        //System.out.println(songList.get(songNumber).toURI().toString()); ((TESTING URI))
        //songMedia = new Media(songList.get(songNumber).toURI().toString()); ((BREAKS CODE))
        //songMediaPlayer = new MediaPlayer(songMedia); ((BREAKS CODE))
        songLabel.setText(songList.get(songNumber).getName());
    }




    public void playMedia(){

    }

    public void pauseMedia(){

    }

    public void restartMedia(){

    }

    public void previousMedia(){

    }

    public void nextMedia(){

    }

    public void fileChooseMedia(){

    }

    public void beginTimer(){

    }

    public void cancelTimer(){

    }
}