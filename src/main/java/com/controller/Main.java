package com.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Principal.fxml"));

        Scene scene = new Scene(root, 274, 55);

        primaryStage.setTitle("Trabajo Final de Programación Concurrente - UNC");
        primaryStage.getIcons().add(new Image("index.png"));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
