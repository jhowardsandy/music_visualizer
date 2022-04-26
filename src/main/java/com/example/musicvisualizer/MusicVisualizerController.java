package com.example.musicvisualizer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller for all the music player's button functions. Built with a lot of
 * help from (<a href="https://www.youtube.com/watch?v=-D2OIekCKes">BroCode's
 * JavaFX Music Player Tutorial</a>)
 * 
 * @author Rianna M, all methods unless otherwise stated.
 */
public class MusicVisualizerController implements Initializable {
	@FXML
	private Pane pane;

	@FXML
	private Label songLabel;

	@FXML
	private Button restartBtn, previousBtn, playPauseBtn, nextBtn, fileBtn;

	// Paths for SVG shapes for toggling play and pause button.
	@FXML
	SVGPath playPauseSVG = new SVGPath();
	String playPath = "M361 215C375.3 223.8 384 239.3 384 256C384 272.7 375.3 288.2 361 296.1L73.03 472.1C58.21 482 39.66 482.4 24.52 473.9C9.377 465.4 0 449.4 0 432V80C0 62.64 9.377 46.63 24.52 38.13C39.66 29.64 58.21 29.99 73.03 39.04L361 215z";
	String pausePath = "M272 63.1l-32 0c-26.51 0-48 21.49-48 47.1v288c0 26.51 21.49 48 48 48L272 448c26.51 0 48-21.49 48-48v-288C320 85.49 298.5 63.1 272 63.1zM80 63.1l-32 0c-26.51 0-48 21.49-48 48v288C0 426.5 21.49 448 48 448l32 0c26.51 0 48-21.49 48-48v-288C128 85.49 106.5 63.1 80 63.1z";

	@FXML
	private Slider volumeSlider;

	@FXML
	private ProgressBar songProgressBar;

	// Filepath Variables
	@FXML
	private File songDirectory;
	private File[] songFiles;
	private ArrayList<File> songList;
	private Stage stage;

	private int songNumber;

	// Timers
	private Timer progBarTimer;
	private TimerTask progBarTask;

	// Logic check for status of song.
	private boolean isPlaying; // if music is playing AT ALL (vs. being stopped)
	private boolean isPaused = true; // if music is in a paused state. used to toggle the play/pause button

	// MediaPlayer
	private Media songMedia;
	private MediaPlayer songMediaPlayer;

	@FXML
	private Rectangle rec, rec1, rec2, rec3, rec4, rec5, rec6, rec7, rec8, rec9, rec10, rec11;
	int amplitude;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		songList = new ArrayList<File>();
		songDirectory = new File("src/main/resources/music");
		songFiles = songDirectory.listFiles();
		if (songFiles != null) {
			for (File oneFile : songFiles) {
				songList.add(oneFile);
			}
		} else {
			System.err.print("ERROR: Song directory is incorrect or empty!");
		}
		// the songList.get(songNumber).toURI().toString()) bit is the MEDIA_URL in the
		// amplitude Processor.
		songMedia = new Media(songList.get(songNumber).toURI().toString());

		songMediaPlayer = new MediaPlayer(songMedia);
		songLabel.setText(songList.get(songNumber).getName().replaceFirst("[.][^.]+$", "")); // Filename is included,
		// extension is
		// start of amplitude audio data processing. // truncated.

		// Testing

		// Testing
//		amplitudeProcessor ampProcessor = new amplitudeProcessor(songMediaPlayer);
//		ampProcessor.addListener();
		songMediaPlayer.setAudioSpectrumListener(new SpektrumListener());

		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				songMediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
			}
		});

	}

	public class SpektrumListener implements AudioSpectrumListener {
		@Override
		public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
			int[] correctedMag = new int[magnitudes.length];
			for (int i = 0; i < magnitudes.length; i += 10) {
//				magnitudes[i] = magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold();
//				System.out.println(magnitudes[i]);
				if (magnitudes[i] - songMediaPlayer.getAudioSpectrumThreshold() > 0) {
//					System.out.println(Math.floor(magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold()));
					correctedMag[i] = (int) Math.floor(magnitudes[i] - songMediaPlayer.getAudioSpectrumThreshold());

				}
//				rec.setHeight(Math.floor(magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold()));
//				System.out.println("will this print also?");
//				System.out.println(mediaPlayer.getAudioSpectrumThreshold());
//				rec.setHeight(correctedMag[0]);
			}
//			System.out.println(correctedMag[0]);
			
			// start of setting the rectangle's heights to the song.
//			amplitude = correctedMag[0] * 15;
			
			rec.setHeight(correctedMag[0] * 15);
			rec1.setHeight(correctedMag[0] * 13);
			rec2.setHeight(correctedMag[0] * 11);
			rec3.setHeight(correctedMag[0] * 9);
			rec4.setHeight(correctedMag[0] * 8);
			rec5.setHeight(correctedMag[0] * 7);
			rec6.setHeight(correctedMag[0] * 6);
			rec7.setHeight(correctedMag[0] * 5);
			rec8.setHeight(correctedMag[0] * 4);
			rec9.setHeight(correctedMag[0] * 3);
			rec10.setHeight(correctedMag[0] * 2);
			rec11.setHeight(correctedMag[0] * 1);
		}
	}

	/**
	 * Plays and pauses the music.
	 */
	public void playPauseMedia() {
		if (isPaused == true) {
			beginTimer();
			songMediaPlayer.play();
			songMediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
			isPaused = false;
			playPauseSVG.setContent(pausePath);
		}

		else {
			isPaused = true;
			playPauseSVG.setContent(playPath);
			cancelTimer();
			songMediaPlayer.pause();
		}
	}

	/**
	 * Restarts the music.
	 */
	public void restartMedia() {
		songProgressBar.setProgress(0);
		songMediaPlayer.seek(Duration.seconds(0));

		if (!isPlaying) {
			beginTimer();
		}
	}

	/**
	 * Stops the current song and then plays the previous song on the playlist.
	 */
	public void previousMedia() {
		if (songNumber > 0) {
			songNumber -= 1;
			songMediaPlayer.stop();
		}

		else {
			songNumber = songList.size() - 1;
		} // returns to start of the playlist if the end of the playlist is reached.

		if (isPlaying) {
			cancelTimer();
		}

		songMedia = new Media(songList.get(songNumber).toURI().toString());
		songMediaPlayer = new MediaPlayer(songMedia);
		songLabel.setText(songList.get(songNumber).getName().replaceFirst("[.][^.]+$", "")); // Filename is included,
																								// extension is
																								// truncated.
		// Allows raw data to be displayed when choosing the next song.
//		amplitudeProcessor ampProcessor = new amplitudeProcessor(songMediaPlayer);
//		ampProcessor.addListener();
		songMediaPlayer.setAudioSpectrumListener(new SpektrumListener());

		isPaused = true;
		playPauseMedia(); // autoplays next song
	}

	/**
	 * Stops the current song and then plays the next song on the playlist.
	 */
	public void nextMedia() {

		if (songNumber < songList.size() - 1) {
			songNumber += 1;
			songMediaPlayer.stop();
		}

		else {
			songNumber = 0;
		} // returns to start of the playlist if the end of the playlist is reached.

		if (isPlaying) {
			cancelTimer();
		}

		songMedia = new Media(songList.get(songNumber).toURI().toString());
		songMediaPlayer = new MediaPlayer(songMedia);
		songLabel.setText(songList.get(songNumber).getName().replaceFirst("[.][^.]+$", "")); // Filename is included,
																								// truncated.
		// Allows raw data to be displayed when choosing the previous song.
//		amplitudeProcessor ampProcessor = new amplitudeProcessor(songMediaPlayer);
//		ampProcessor.addListener();
		songMediaPlayer.setAudioSpectrumListener(new SpektrumListener());

		isPaused = true;
		playPauseMedia(); // autoplays next song
	}

	public void fileChooseMedia() {
		
		songList = new ArrayList<File>();
		
		//listView to display at least names of songs
		//playList = new ListView<>();
		
		FileChooser fileChooser = new FileChooser();
		 
		//Set extension filter for MP3 files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("mp3 files (*.mp3)", "*.mp3");
		fileChooser.getExtensionFilters().add(extFilter);

		//Show open file dialog
		File selectedFile = fileChooser.showOpenDialog(stage);
			
		songMedia = new Media(selectedFile.toURI().toString());

		songMediaPlayer = new MediaPlayer(songMedia);
		songLabel.setText(selectedFile.getName().replaceFirst("[.][^.]+$", ""));
		
		songMediaPlayer.setAudioSpectrumListener(new SpektrumListener());

		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				songMediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
			}
		});
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
				// System.out.println((currentTime / endTime) * 100); testing bar progression
				// values.
				if (currentTime / endTime == 1) {
					cancelTimer();
					isPaused = true; // test for debug
				}
			}
		};

		progBarTimer.scheduleAtFixedRate(progBarTask, 0, 1000);
	}

	/**
	 * Cancels timer for progression bar visuals.
	 */
	public void cancelTimer() {
		isPlaying = false;
		progBarTimer.cancel();
	}
}