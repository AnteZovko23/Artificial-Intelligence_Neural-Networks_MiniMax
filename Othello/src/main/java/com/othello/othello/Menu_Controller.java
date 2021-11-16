package com.othello.othello;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Menu Controller for the opening menu
 * Author: Ante Zovko
 * Version: November 14th, 2021
 *
 */
public class Menu_Controller implements Initializable {

    @FXML
    ChoiceBox<String> choice_box;

    @FXML
    RadioButton white_color, black_color;

    @FXML
    Label radio_label;


    Controller ctrl;

    private FXMLLoader root;

    private String color = "black";

    /**
     * Switches to game screen one new game has been clicked
     *
     * @param event event
     * @throws IOException IOException
     */
    public void switch_to_game(ActionEvent event) throws IOException {

        // Gets the game window
        FXMLLoader fxmlLoader = new FXMLLoader(OthelloMain.class.getResource("view.fxml"));
        Stage stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();


        Scene scene = new Scene(fxmlLoader.load());


        stage.setScene(scene);
        stage.show();

        // Loads the controller for the game window and sets player choices
        ctrl = fxmlLoader.getController();

        ctrl.set_player_color(color);
        ctrl.set_setting(choice_box.getValue());
        ctrl.start_game();

    }


    /**
     * Gets player color
     *
     * @param event color
     */
    public void get_player_color(ActionEvent event) {

        if (black_color.isSelected()) {
            color = "black";
        } else {
            color = "white";
        }

    }

    /**
     * Adjusts radio buttons based on what the player selects
     *
     * @param event event
     */
    public void adjust_radio_visibility(ActionEvent event) {

        if (choice_box.getValue().contentEquals("Player vs AI")) {
            black_color.setVisible(true);
            white_color.setVisible(true);
            radio_label.setVisible(true);
        } else {
            black_color.setVisible(false);
            white_color.setVisible(false);
            radio_label.setVisible(false);
        }
    }


    /**
     * On Initialize window
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        choice_box.getItems().addAll("Player vs Player", "Player vs AI");
        choice_box.setValue("Player vs Player");
        black_color.setSelected(true);

        black_color.setVisible(false);
        white_color.setVisible(false);

    }


}
