package com.example.musicvisualizer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MusicVisualizerApp extends Application {
    @Override
    public void start(Stage mainStage) throws IOException {
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