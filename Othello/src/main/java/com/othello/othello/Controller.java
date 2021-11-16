package com.othello.othello;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    public GridPane grid;

    @FXML
    public AnchorPane pane_2;

    @FXML
    public Label white_piece_count;

    @FXML
    public Label black_piece_count;

    @FXML
    public Button new_game_btn;

    @FXML
    public Label current_player_label;

    @FXML
    public Label last_move_played;

    @FXML
    public CheckBox minimax_on_off, alpha_beta_pruning_on_off, DEBUG_MODE;

    @FXML
    public Slider search_depth;

    public String setting;
    public String player_color;
    public OthelloGameplay game;
    public Othello_AI AI;

    public boolean maximizing;

    public boolean DEBUG;

    Stage stage;
    Scene scene;


    /**
     * Starts an Othello game
     *
     */
    public void start_game() {

        // Creates a gameplay instance
        this.game = new OthelloGameplay(grid, this, true);

        // If the player is playing the computer
        if(this.get_setting().contentEquals("Player vs AI")){

            this.maximizing = !this.player_color.contentEquals("black");
            alpha_beta_pruning_on_off.setSelected(true);
            minimax_on_off.setSelected(true);


        }
        else
            this.maximizing = true;



        // Creates an Othello AI
        this.AI = new Othello_AI(game, this);

        // Sets the board up in the default starting position
        game.setup_board();

        // If the computer plays as black it starts finding the first move
        if(this.get_setting().contentEquals("Player vs AI") && this.player_color.contentEquals("white")){

            this.start_minimax();;

        }

    }

    /**
     * Starts the game by determining possible moves
     *
     * @param game the game instance
     */
    public void play_game(OthelloGameplay game){


        game.determine_possible_moves();



    }


    /**
     * When new game button is clicked it goes back to the opening menu
     *
     * @param event event
     * @throws IOException IOException
     */
    public void new_game_btn_clicked(ActionEvent event) throws IOException {


        // Opening menu
        FXMLLoader fxmlLoader = new FXMLLoader(OthelloMain.class.getResource("opening-menu.fxml"));
        stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();

        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

        // Clear and reset board
        game.clear_board();
        game.setup_board();
        white_piece_count.setText(2 + "");
        black_piece_count.setText(2 + "");
        set_current_player_label("Black to move");


    }

    /**
     * Get current game instance
     *
     * @return game instance
     */
    public OthelloGameplay get_current_game() {

        return game;

    }

    /**
     * Starts minimax if slider is changed
     *
     * @param event drag event
     */
    public void drag_start_minimax(MouseEvent event) {

        start_minimax();

    }

    /**
     *
     * Starts the minimax algorithm
     *
     */
    public void start_minimax() {

        // If the minimax checkbox is selected
        if(minimax_on_off.isSelected()) {


            // Creates a temporary board instance in memory hidden from view
            OthelloGameplay temporary_board = new OthelloGameplay(new GridPane(), this, false);
            // Loads game from position
            temporary_board = temporary_board.load_game_from_position(game.get_game_state(), game.current_board);

            // Define root of the tree
            TreeNode root = new TreeNode(null);

            // Counter for number of states explored
            this.AI.number_of_states_explored = 0;
            System.out.println();
            System.out.println((maximizing) ? "Currently Maximizing" : "Currently Minimizing");
            System.out.println();

            // If alpha-beta pruning is selected
            if(alpha_beta_pruning_on_off.isSelected())
                this.AI.mini_max_pruning(root, temporary_board, Integer.MIN_VALUE, Integer.MAX_VALUE, (int) search_depth.getValue(), maximizing);
            else
                this.AI.mini_max_no_pruning(root, temporary_board, (int) search_depth.getValue(), maximizing);

            System.out.println();
            System.out.println();
            System.out.println("Number of states searched: " + this.AI.number_of_states_explored);

            // Best move node
            // Checks if root has any children
            TreeNode best_move;
            if(root.get_children().size() != 0)
                best_move = root.get_children().get(0);
            else
                return;


            // For each child of root it does a search to find the best move location
            for(TreeNode child : root.get_children()){

                System.out.println("Row: " + child.get_tile()[0] + " Col: " + child.get_tile()[1]);
                System.out.println("Evaluation: " + child.get_evaluation());

                if(!maximizing && child.get_evaluation() <= best_move.get_evaluation())
                    best_move = child;
                else if(maximizing && child.get_evaluation () >= best_move.get_evaluation())
                    best_move = child;

            }

            // If the player is against the computer
            if(this.get_setting().contentEquals("Player vs AI") ) {

                // Checks if the computer has a move by inline determining if the human controls black or white
                boolean has_move = game.human_control.contentEquals(game.black_piece_path) ? game.white_has_move : game.black_has_move;
                if(!game.current_player.contentEquals(game.human_control) && has_move) {
                    TreeNode finalBest_move = best_move;
                    // In OthelloGameplay minimax is running on a separate thread
                    // Platform.runLater makes the gui operate on the JavaFX GUI thread
                    Platform.runLater(() -> AI.play_move(finalBest_move.get_tile()[0], finalBest_move.get_tile()[1])

                    );
                }
            }
        }



    }

    /**
     * Updates the score counts
     *
     */
    public void update_counts() {

        white_piece_count.setText(game.white_piece_count + "");
        black_piece_count.setText(game.black_piece_count + "");
    }


    /**
     * Opens game over screen
     *
     * @param winner the winner
     */
    public void game_over(String winner) {

        // Returns to JavaFX GUI Thread
        Platform.runLater(() -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("GAME OVER");
            alert.setHeaderText("The game has finished");
            if (winner.contentEquals("Tie")) {

                alert.setContentText("The game ended in a tie");

            } else {

                alert.setContentText(winner + " has won the game!");

            }
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    alert.close();
                }
            });
        });




    }


    /**
     * Sets player label
     *
     * @param player given player
     */
    public void set_current_player_label(String player) {

        current_player_label.setText(player);

    }

    /**
     * Shows last move played
     *
     * @param row move row
     * @param col move col
     */
    public void set_last_move_played(int row, int col) {

    	last_move_played.setText("COL: " + col + " ROW: " + row);

    }

    /**
     * Sets the play setting
     *
     * @param setting play setting
     */
    public void set_setting(String setting) {
    	this.setting = setting;

    }

    /**
     * Gets the play setting
     *
     * @return setting
     */
    public String get_setting() {
    	return setting;
    }

    /**
     * Sets player color
     *
     * @param player_color color
     */
    public void set_player_color(String player_color) {
        this.player_color = player_color;

    }

    /**
     * Gets player color
     *
     * @return color
     */
    public String get_player_color() {
        return player_color;
    }

    /**
     * Sets debug mode
     *
     * @param event event
     */
    public void set_debug_mode(ActionEvent event) {

        this.DEBUG = DEBUG_MODE.isSelected();

    }



}
