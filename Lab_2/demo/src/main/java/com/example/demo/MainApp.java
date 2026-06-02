package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Zmień "hello-view.fxml" na nazwę swojego pliku FXML, jeśli nazywa się inaczej!
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("layout.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Edytor Obrazów - Lab 6");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}