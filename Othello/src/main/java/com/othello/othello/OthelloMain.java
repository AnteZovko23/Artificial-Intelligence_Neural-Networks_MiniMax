package com.othello.othello;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Author: Ante Zovko
 * Student Number: 103-55-122
 * Date: November 14th, 2021
 * Assignment 2
 *
 * Othello Program that implements the Mini-Max algorithm with Alpha-Beta Pruning
 *
 */

public class OthelloMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Loads opening menu from opening-menu.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(OthelloMain.class.getResource("opening-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Othello");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}