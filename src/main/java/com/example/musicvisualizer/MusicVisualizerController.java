package com.example.musicvisualizer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
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
	private File songDirectory;
	private File[] songFiles;
	private ArrayList<File> songList;

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
	private Circle circle;
	int CIRCLE_MAX_RADIUS = 120;
	int CIRCLE_MIN_RADIUS = 40;
	
	@FXML
	private Line[] lines = new Line[256];
	int amplitude;
	
	@FXML
	private MediaView visualizerMediaViewer;
	
	//avg_magnitude runs a rolling average 
	double avg_magnitude = 0;
	int magnitude_measure_count = 0;
	
	float smoothingFactor = 0.6f;

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
		songMediaPlayer.setAudioSpectrumThreshold(-100);
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
		
		InitializeLines();

	}

	public class SpektrumListener implements AudioSpectrumListener {
		@Override
		public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
			
			//magnitude length is by default 128
			int magntiude_arr_length = magnitudes.length;
			double[] correctedMag = new double[magntiude_arr_length];
			double correctedMagSum = 0;
			
			for (int i = 0; i < magntiude_arr_length; i ++) {

				if (magnitudes[i] - songMediaPlayer.getAudioSpectrumThreshold() > 0) {

					correctedMag[i] = Math.floor(magnitudes[i] - songMediaPlayer.getAudioSpectrumThreshold());
					correctedMagSum += correctedMag[i];
				}

			}
	
			magnitude_measure_count++;
			double current_mag  = correctedMagSum / magntiude_arr_length;
			avg_magnitude = (magnitude_measure_count * avg_magnitude + current_mag)/(magnitude_measure_count + 1);
			
			double smoothed_magnitude = (current_mag) * (1-smoothingFactor) + avg_magnitude * smoothingFactor;
			double scale_factor = smoothed_magnitude - avg_magnitude;
			
			
			UpdateCircleRadius(scale_factor);			
						
			DrawLines(scale_factor);

				
			
			
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
		
		resetSongParams();
		
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
		
		resetSongParams();
		
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
		
		resetSongParams();

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
		// TODO WANTED ADDITIONAL FUNCTION
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
	
	private void resetSongParams() {
		
		avg_magnitude = 0;
		magnitude_measure_count = 0;
	}
	
	private void UpdateCircleRadius(double scale_factor) {
		
		double new_radius = scale_factor * (CIRCLE_MAX_RADIUS-CIRCLE_MIN_RADIUS) + CIRCLE_MIN_RADIUS;
	
		if(new_radius > CIRCLE_MAX_RADIUS) new_radius = CIRCLE_MAX_RADIUS;
		if (new_radius < CIRCLE_MIN_RADIUS) new_radius = CIRCLE_MIN_RADIUS;
		circle.setRadius(new_radius);
	}
	
	private void DrawLines(double scale_factor) {
		
		double circle_radius = circle.getRadius();
//		Rectangle2D window = visualizerMediaViewer.getViewport();
//		double window_boundary_x = window.getMaxX();
//		double window_boundary_y = window.getMaxY();
		
		double window_boundary_x = 500 - circle_radius;
		double window_boundary_y = 500 - circle_radius;
				
		for (int i = 0; i < 128; i ++){
			double angle = i * (Math.PI / 127);
			Line l = lines[i];
			double length_x = l.getEndX() - l.getStartX();
			double length_y = l.getEndY() - l.getStartY();
			// need to convert angle to radians for actual cos and sin functions)
			if(angle >= 0 && angle <= Math.toRadians(60)){

				double x_start = circle_radius * Math.cos(angle - (Math.PI / 2));
				double y_start = circle_radius * Math.sin(angle - (Math.PI / 2));
				
				double x_end = Math.max(length_x * scale_factor * 1, window_boundary_x);
				double y_end = Math.max(length_y * scale_factor * 1, window_boundary_y);

				l.setStartX(x_start);
				l.setStartY(y_start);
				l.setEndX(x_end);
				l.setEndY(y_end);
				// we also want to do the mirror side, so we want to draw the same line
				// but with the x coordinate multiplied by negative 1
//				line_end = (x_end * - 1, y_end);
//				line_start = (x_start * - 1, y_start);
//				draw_line(line_start, line_end);

			} else if(angle > Math.toRadians(60) && angle <= Math.toRadians(120)){

				double x_start = circle_radius * Math.cos(angle - (Math.PI / 2));
				double y_start = circle_radius * Math.sin(angle - (Math.PI / 2));
				
				double x_end = Math.max(x_start + scale_factor * 1.5, window_boundary_x);
				double y_end = Math.max(y_start + scale_factor * 1.5, window_boundary_y);
				
				l.setStartX(x_start);
				l.setStartY(y_start);
				l.setEndX(x_end);
				l.setEndY(y_end);
				
			} else if(angle > Math.toRadians(120) && angle <= Math.toRadians(180)){

				double x_start = circle_radius * Math.cos(angle - (Math.PI / 2));
				double y_start = circle_radius * Math.sin(angle - (Math.PI / 2));
				
				double x_end = Math.max(x_start + scale_factor * 2, window_boundary_x);
				double y_end = Math.max(y_start + scale_factor * 2, window_boundary_y);

				l.setStartX(x_start);
				l.setStartY(y_start);
				l.setEndX(x_end);
				l.setEndY(y_end);
			}

		}
	}
	
	private void InitializeLines() {
		
		for(int i =0; i < lines.length; i++) {
			lines[i] = new Line(0,0,0,0);
		}
	}
}