package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("layout.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Edytor Obrazów");
        stage.setScene(scene);
        stage.show();
        Log.log("INFO", "Uruchomiono aplikację");
    }
    @Override
    public void stop() throws Exception {
        Log.log("INFO", "Zamknięto aplikację");
        super.stop();
    }
    public static void main(String[] args) {
        launch();
    }
}