package com.example.musicvisualizer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MusicVisualizerApp extends Application {
    @Override
    public void start(Stage mainStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MusicVisualizerApp.class.getResource("main-ui.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load());
        mainStage.setTitle("Music Visualizer");
        Image musicIcon = new Image(MusicVisualizerApp.class.getResource("/images/musicIcon.png").toString());
        mainStage.getIcons().add(musicIcon);
        mainStage.setScene(mainScene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}