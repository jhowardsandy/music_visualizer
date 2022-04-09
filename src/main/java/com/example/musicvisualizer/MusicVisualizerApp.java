package com.example.musicvisualizer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;

public class MusicVisualizerApp extends Application {
	// all for the amplitudeProcessor class
//	private Media media;
//	private MediaPlayer mediaPlayer;
//	private File file = new File("src/main/resources/music/Perfumed Eves - Masayoshi Soken.mp3");
//	private String MEDIA_URL = file.toURI().toString();

	@Override
	public void start(Stage mainStage) throws IOException {
		// this works, now I've gotta figure out how to integrate it with the controller.
//		media = new Media(MEDIA_URL);
//		mediaPlayer = new MediaPlayer(media);
//		amplitudeProcessor ampProcessor = new amplitudeProcessor(mediaPlayer);
//		ampProcessor.addListener();
//		mediaPlayer.setAutoPlay(true);

		FXMLLoader fxmlLoader = new FXMLLoader(MusicVisualizerApp.class.getResource("main-ui.fxml"));
		Scene mainScene = new Scene(fxmlLoader.load());
		mainStage.setTitle("Music Visualizer");
		Image musicIcon = new Image(MusicVisualizerApp.class.getResource("/images/musicIcon.png").toString());
		mainStage.getIcons().add(musicIcon);
		mainScene.getStylesheets().add(getClass().getResource("/stylesheets/visualizerViolet.css").toExternalForm());
		mainStage.setScene(mainScene);
		mainStage.setResizable(false);
		mainStage.show();

		mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		launch();
	}
}