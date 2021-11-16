package com.othello.othello;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * Othello Gameplay logic implementation
 *
 * Author: Ante Zovko
 * Date: November 14th, 2021
 *
 */
public class OthelloGameplay  {

    public Controller controller;

    boolean white_has_move = true;
    boolean black_has_move = true;


    public final String white_piece_path = "file:src/main/resources/Images/white_piece.png";
    public final String black_piece_path = "file:src/main/resources/Images/black_piece.png";
    public final String move_piece = "file:src/main/resources/Images/move.png";

    public final Image white_piece = new Image(white_piece_path);
    public final Image black_piece = new Image(black_piece_path);
    public final Image move_piece_img = new Image(move_piece);

    GridPane grid;

    public ModifiedImageView[][] current_board = new ModifiedImageView[8][8];
    public String current_player = black_piece_path;

    public String human_control;

    public int white_piece_count = 2;
    public int black_piece_count = 2;

    public int last_move_played_row;
    public int last_move_played_col;

    public boolean visible;


    /**
     * Gameplay constructor
     *
     * @param grid a grid
     * @param controller a controller
     * @param visible visible or not
     */
    public OthelloGameplay(GridPane grid, Controller controller, boolean visible) {

        this.grid = grid;
        this.controller = controller;
        this.visible = visible;

    }



    /**
     * Sets up the first 4 pieces on the board
     */
    public void setup_board() {

        for(int i = 0; i < 8; i++) {

            for(int j = 0; j < 8; j++) {

                ModifiedImageView imageView = new ModifiedImageView();

                grid.add(imageView, i, j);

                if(!visible) {

                    current_board[i][j] = imageView;

                }

            }

        }
        // Remove some null object
        grid.getChildren().remove(0);

        if(visible) {
            for(Node n : grid.getChildren()) {

                current_board[GridPane.getRowIndex(n)][GridPane.getColumnIndex(n)] = (ModifiedImageView) n;

            }
        }

        // Set the first 4 pieces
        current_board[3][3].setImage(white_piece);
        current_board[3][3].setFitWidth(70);
        current_board[3][3].setFitHeight(70);

        current_board[4][3].setImage(black_piece);
        current_board[4][3].setFitWidth(70);
        current_board[4][3].setFitHeight(70);

        current_board[3][4].setImage(black_piece);
        current_board[3][4].setFitWidth(70);
        current_board[3][4].setFitHeight(70);

        current_board[4][4].setImage(white_piece);
        current_board[4][4].setFitWidth(70);
        current_board[4][4].setFitHeight(70);

        if(visible) {
            human_control = controller.get_player_color().contentEquals("black") ? this.black_piece_path : this.white_piece_path;


        }
        controller.play_game(this);

    }


    /**
     * Turns pieces into the opposite color in all directions
     *
     * @param row given row of possible move
     * @param col given column of possible move
     */
    private void turn_pieces_each_direction(int row, int col) {
        for(String direction : current_board[row][col].getOrigins()) {


            // Given a direction from origin
            // It switches all pieces in that direction
            // Origin tells where potential move came from
            switch(direction) {
                case "left" -> {

                    for (int i = col - 1; i >= 0; i--) {

                        try {

                            if (current_player.contentEquals(black_piece_path) && current_board[row][i].getImage().getUrl().contentEquals(white_piece_path)) {
                                current_board[row][i].setImage(black_piece);
                                black_piece_count++;
                                white_piece_count--;

                            } else if (current_player.contentEquals(white_piece_path) && current_board[row][i].getImage().getUrl().contentEquals(black_piece_path)) {
                                current_board[row][i].setImage(white_piece);
                                white_piece_count++;
                                black_piece_count--;
                            } else
                                break;

                            current_board[row][i].setFitWidth(70);
                            current_board[row][i].setFitHeight(70);
                            current_board[row][i].setOpacity(1);

                        } catch (NullPointerException e) {

                            break;
                        }

                    }
                }

                case "right" -> {

                    // Check the right side
                    for (int i = col + 1; i < 8; i++) {

                        try {

                            if (current_player.contentEquals(black_piece_path) && current_board[row][i].getImage().getUrl().contentEquals(white_piece_path)) {
                                current_board[row][i].setImage(black_piece);
                                black_piece_count++;
                                white_piece_count--;

                            } else if (current_player.contentEquals(white_piece_path) && current_board[row][i].getImage().getUrl().contentEquals(black_piece_path)) {
                                current_board[row][i].setImage(white_piece);
                                white_piece_count++;
                                black_piece_count--;

                            } else
                                break;

                            current_board[row][i].setFitWidth(70);
                            current_board[row][i].setFitHeight(70);
                            current_board[row][i].setOpacity(1);
                        } catch (NullPointerException e) {

                            break;

                        }

                    }
                }
                case "up" -> {

                    // Check the top side
                    for (int i = row - 1; i >= 0; i--) {

                        try {

                            if (current_player.contentEquals(black_piece_path) && current_board[i][col].getImage().getUrl().contentEquals(white_piece_path)) {
//                                System.out.println("herehere");
                                current_board[i][col].setImage(black_piece);
                                black_piece_count++;
                                white_piece_count--;

                            } else if (current_player.contentEquals(white_piece_path) && current_board[i][col].getImage().getUrl().contentEquals(black_piece_path)) {
                                current_board[i][col].setImage(white_piece);
                                white_piece_count++;
                                black_piece_count--;

                            } else
                                break;

                            current_board[i][col].setFitWidth(70);
                            current_board[i][col].setFitHeight(70);
                            current_board[i][col].setOpacity(1);
                        } catch (NullPointerException e) {

                            break;

                        }

                    }
                }
                case "down" -> {
                    // Check the bottom side
                    for (int i = row + 1; i < 8; i++) {

                        try {

                            if (current_player.contentEquals(black_piece_path) && current_board[i][col].getImage().getUrl().contentEquals(white_piece_path)) {
                                current_board[i][col].setImage(black_piece);
                                black_piece_count++;
                                white_piece_count--;
                            } else if (current_player.contentEquals(white_piece_path) && current_board[i][col].getImage().getUrl().contentEquals(black_piece_path)) {
                                current_board[i][col].setImage(white_piece);
                                white_piece_count++;
                                black_piece_count--;

                            } else
                                break;

                            current_board[i][col].setFitWidth(70);
                            current_board[i][col].setFitHeight(70);
                            current_board[i][col].setOpacity(1);
                        } catch (NullPointerException e) {

                            break;

                        }

                    }
                }
                case "up_left" -> {

                    // Check the top left side diagonal
                    for (int i = 1; i <= Math.min(row, col); i++) {

                        try {

                            if (current_player.contentEquals(black_piece_path) && current_board[row - i][col - i].getImage().getUrl().contentEquals(white_piece_path)) {
                                current_board[row - i][col - i].setImage(black_piece);
                                black_piece_count++;
                                white_piece_count--;
                            } else if (current_player.contentEquals(white_piece_path) && current_board[row - i][col - i].getImage().getUrl().contentEquals(black_piece_path)) {
                                current_board[row - i][col - i].setImage(white_piece);
                                white_piece_count++;
                                black_piece_count--;
                            } else
                                break;

                            current_board[row - i][col - i].setFitWidth(70);
                            current_board[row - i][col - i].setFitHeight(70);
                            current_board[row - i][col - i].setOpacity(1);
                        } catch (NullPointerException e) {

                            break;

                        }

                    }
                }
                case "up_right" -> {

                    // Check the top right side diagonal
                    for (int i = 1; i <= Math.min(row, 7 - col); i++) {

                        try {

                            if (current_player.contentEquals(black_piece_path) && current_board[row - i][col + i].getImage().getUrl().contentEquals(white_piece_path)) {
                                current_board[row - i][col + i].setImage(black_piece);
                                black_piece_count++;
                                white_piece_count--;

                            } else if (current_player.contentEquals(white_piece_path) && current_board[row - i][col + i].getImage().getUrl().contentEquals(black_piece_path)) {
                                current_board[row - i][col + i].setImage(white_piece);
                                white_piece_count++;
                                black_piece_count--;

                            } else
                                break;

                            current_board[row - i][col + i].setFitWidth(70);
                            current_board[row - i][col + i].setFitHeight(70);
                            current_board[row - i][col + i].setOpacity(1);
                        } catch (NullPointerException e) {

                            break;

                        }

                    }
                }

                case "down_left" -> {


                    // Check the bottom left side diagonal
                    for (int i = 1; i <= Math.min(7 - row, col); i++) {

                        try {

                            if (current_player.contentEquals(black_piece_path) && current_board[row + i][col - i].getImage().getUrl().contentEquals(white_piece_path)) {
                                current_board[row + i][col - i].setImage(black_piece);
                                black_piece_count++;
                                white_piece_count--;

                            } else if (current_player.contentEquals(white_piece_path) && current_board[row + i][col - i].getImage().getUrl().contentEquals(black_piece_path)) {
                                current_board[row + i][col - i].setImage(white_piece);
                                white_piece_count++;
                                black_piece_count--;

                            } else
                                break;

                            current_board[row + i][col - i].setFitWidth(70);
                            current_board[row + i][col - i].setFitHeight(70);
                            current_board[row + i][col - i].setOpacity(1);
                        } catch (NullPointerException e) {

                            break;

                        }

                    }
                }

                case "down_right" -> {

                    // Check the bottom right side diagonal
                    for (int i = 1; i <= Math.min(7 - row, 7 - col); i++) {

                        try {

                            if (current_player.contentEquals(black_piece_path) && current_board[row + i][col + i].getImage().getUrl().contentEquals(white_piece_path)) {
                                current_board[row + i][col + i].setImage(black_piece);
                                black_piece_count++;
                                white_piece_count--;

                            } else if (current_player.contentEquals(white_piece_path) && current_board[row + i][col + i].getImage().getUrl().contentEquals(black_piece_path)) {
                                current_board[row + i][col + i].setImage(white_piece);
                                white_piece_count++;
                                black_piece_count--;

                            } else
                                break;

                            current_board[row + i][col + i].setFitWidth(70);
                            current_board[row + i][col + i].setFitHeight(70);
                            current_board[row + i][col + i].setOpacity(1);

                        } catch (NullPointerException e) {

                            break;

                        }

                    }
                }
            }
        }

    }

    /**
     * Determines possible moves on the entire board
     *
     */
    public void determine_possible_moves() {

        // First it steps one step into each direction
        // Eliminates possibilities
        for(int rows = 0; rows < 8; rows++) {

            for(int cols = 0; cols < 8; cols++) {
                String[][] directions_to_go = new String[3][3];

                // If the current tile is not empty
                if((current_board[rows][cols].getImage() != null)) {

                    /* The 2x2 direction matrix will have a one if there is a piece of opposite color in that
                     * direction, and a zero if there is not.
                     */
                    // Step matrix


                    // Left
                    if(cols - 1 >= 0) {

                        // Check the tile
                        // If the tile is empty or the same as the current player's color ignore
                        if(current_board[rows][cols - 1].getImage() == null) {

                        } else if(current_board[rows][cols - 1].getImage().getUrl().contentEquals(current_player) || current_board[rows][cols - 1].getImage().getUrl().contentEquals(move_piece)) {

                            // If the tile is the same color as the current player's color, ignore
                            // Can't go in above if as it will lead to a null pointer exception


                        }

                        // If it is the opposite color and a given piece is the color of the current player update matrix
                        else if(current_board[rows][cols].getImage().getUrl().contentEquals(current_player) && !current_board[rows][cols - 1].getImage().getUrl().contentEquals(current_player)) {

                            directions_to_go[1][0] = "left";

                        }

                    }


                    // Right
                    if(cols + 1 < 8) {

                        // Check the tile
                        // If the tile is empty or the same as the current player's color ignore
                        if(current_board[rows][cols + 1].getImage() == null ) {

                        } else if(current_board[rows][cols + 1].getImage().getUrl().contentEquals(current_player) || current_board[rows][cols + 1].getImage().getUrl().contentEquals(move_piece)) {

                            // If the tile is the same color as the current player's color, ignore
                            // Can't go in above if as it will lead to a null pointer exception


                        }
                        else if(current_board[rows][cols].getImage().getUrl().contentEquals(current_player) &&!current_board[rows][cols + 1].getImage().getUrl().contentEquals(current_player)) {

                            // If it is the opposite color update direction matrix
                            directions_to_go[1][2] = "right";

                        }

                    }

                    // Up
                    if(rows - 1 >= 0) {

                        // Check the tile
                        // If the tile is empty or the same as the current player's color ignore
                        if(current_board[rows - 1][cols].getImage() == null) {



                        } else if(current_board[rows - 1][cols].getImage().getUrl().contentEquals(current_player) || current_board[rows - 1][cols].getImage().getUrl().contentEquals(move_piece)) {

                            // If the tile is the same color as the current player's color, ignore
                            // Can't go in above if as it will lead to a null pointer exception


                        } else if(current_board[rows][cols].getImage().getUrl().contentEquals(current_player) && !current_board[rows - 1][cols].getImage().getUrl().contentEquals(current_player)) {

                            // If it is the opposite color update direction matrix
                            directions_to_go[0][1] = "up";

                        }

                    }

                    // Down
                    if(rows + 1 < 8) {

                        // Check the tile
                        // If the tile is empty or the same as the current player's color ignore
                        if(current_board[rows + 1][cols].getImage() == null) {



                        } else if(current_board[rows + 1][cols].getImage().getUrl().contentEquals(current_player) || current_board[rows + 1][cols].getImage().getUrl().contentEquals(move_piece)) {

                            // If the tile is the same color as the current player's color, ignore
                            // Can't go in above if as it will lead to a null pointer exception


                        } else if(current_board[rows][cols].getImage().getUrl().contentEquals(current_player) && !current_board[rows + 1][cols].getImage().getUrl().contentEquals(current_player)) {

                            // If it is the opposite color update direction matrix
                            directions_to_go[2][1] = "down";

                        }

                    }


                    // Left Up
                    if(cols - 1 >= 0 && rows - 1 >= 0) {

                        // Check the tile
                        // If the tile is empty or the same as the current player's color ignore
                        if(current_board[rows - 1][cols - 1].getImage() == null) {



                        } else if(current_board[rows - 1][cols - 1].getImage().getUrl().contentEquals(current_player) || current_board[rows - 1][cols - 1].getImage().getUrl().contentEquals(move_piece)) {

                            // If the tile is the same color as the current player's color, ignore
                            // Can't go in above if as it will lead to a null pointer exception

                        } else if(current_board[rows][cols].getImage().getUrl().contentEquals(current_player) && !current_board[rows - 1][cols - 1].getImage().getUrl().contentEquals(current_player)) {

                            // If it is the opposite color update direction matrix
                            directions_to_go[0][0] = "up_left";

                        }

                    }

                    // Left Down
                    if(cols - 1 >= 0 && rows + 1 < 8) {

                        // Check the tile
                        // If the tile is empty or the same as the current player's color ignore
                        if(current_board[rows + 1][cols - 1].getImage() == null) {


                        } else if(current_board[rows + 1][cols - 1].getImage().getUrl().contentEquals(current_player) || current_board[rows + 1][cols - 1].getImage().getUrl().contentEquals(move_piece)) {

                            // If the tile is the same color as the current player's color, ignore
                            // Can't go in above if as it will lead to a null pointer exception

                        } else if(current_board[rows][cols].getImage().getUrl().contentEquals(current_player) && !current_board[rows + 1][cols - 1].getImage().getUrl().contentEquals(current_player)) {

                            // If it is the opposite color update direction matrix
                            directions_to_go[2][0] = "down_left";

                        }

                    }

                    // Right Up
                    if(cols + 1 < 8 && rows - 1 >= 0) {

                        // Check the tile
                        // If the tile is empty or the same as the current player's color ignore
                        if(current_board[rows - 1][cols + 1].getImage() == null) {


                        } else if(current_board[rows - 1][cols + 1].getImage().getUrl().contentEquals(current_player) || current_board[rows - 1][cols + 1].getImage().getUrl().contentEquals(move_piece)) {

                            // If the tile is the same color as the current player's color, ignore
                            // Can't go in above if as it will lead to a null pointer exception

                        } else if(current_board[rows][cols].getImage().getUrl().contentEquals(current_player) && !current_board[rows - 1][cols + 1].getImage().getUrl().contentEquals(current_player)) {

                            // If it is the opposite color update direction matrix
                            directions_to_go[0][2] = "up_right";

                        }

                    }

                    // Right Down
                    if(cols + 1 < 8 && rows + 1 < 8) {

                        // Check the tile
                        // If the tile is empty or the same as the current player's color ignore
                        if(current_board[rows + 1][cols + 1].getImage() == null) {


                        } else if(current_board[rows + 1][cols + 1].getImage().getUrl().contentEquals(current_player) || current_board[rows + 1][cols + 1].getImage().getUrl().contentEquals(move_piece)) {

                            // If the tile is the same color as the current player's color, ignore
                            // Can't go in above if as it will lead to a null pointer exception

                        } else if(current_board[rows][cols].getImage().getUrl().contentEquals(current_player) && !current_board[rows + 1][cols + 1].getImage().getUrl().contentEquals(current_player)) {

                            // If it is the opposite color update direction matrix
                            directions_to_go[2][2] = "down_right";

                        }

                    }


                }

                // determine directions from directions_to_go
                // if direction is not null, add to directions
                // Using arraylist because I don't know how many directions will be needed
                ArrayList<String> directions = new ArrayList<>();

                for (String[] directions_row : directions_to_go) {

                    for (String direction : directions_row) {

                        if (direction != null) {

                            directions.add(direction);

                        }

                    }

                }


                if(directions.size() > 0) {

                    for (String direction : directions) {

                        draw_possible_moves(rows, cols, direction);

                    }

                }

            }

        }

        // Check if current player has any moves
        int counter = 0;
        for(int i = 0; i < 8; i++) {

            for(int j = 0; j < 8; j++) {

                if(current_board[i][j].getImage() != null) {

                    if(current_board[i][j].getImage().getUrl().contentEquals(move_piece)) {

                        counter++;

                    }

                }

            }

        }



        // If the player doesn't have any moves, switch players
        if (counter == 0) {

            if (current_player.contentEquals(black_piece_path)) {

                black_has_move = false;

            } else if (current_player.contentEquals(white_piece_path)) {

                white_has_move = false;

            }

            // Propositional Logic :)
            if (!(black_has_move || white_has_move)) {

                String winner = white_piece_count > black_piece_count ? "White" : "Black";
                if (white_piece_count == black_piece_count) {

                    winner = "Tie";

                }
                if (visible){
                    controller.set_current_player_label("Game Over!");
                controller.game_over(winner);

            }
            } else {

                current_player = get_next_player(current_player);
                assert current_player != null;
                if(visible){
                    controller.set_current_player_label(current_player.contentEquals(black_piece_path) ? "Black to move" : "White to move");
                    controller.play_game(this);

                }

            }
        } else {

            // If the player has moves set true
            if(current_player.contentEquals(black_piece_path)) {

                black_has_move = true;

            } else if(current_player.contentEquals(white_piece_path)) {

                white_has_move = true;

            }

        }


    }

    /**
     * Draw possible moves for each piece
     *
     * @param row given row
     * @param col given col
     * @param direction direction to step
     */
    private void draw_possible_moves(int row, int col, String direction) {

        switch(direction) {

            case "up" -> {
                // Check the top side
                for(int i = row - 1; i >= 0; i--) {

                    // Check if tile is empty
                    if(current_board[i][col].getImage() == null || current_board[i][col].getImage().getUrl().contentEquals(move_piece)) {

                        // If empty, draw a possible move
                        current_board[i][col].addOrigin(reverse_direction(direction));

                        draw_move(i, col);
                        break;

                        // If tile is the same color as the current player's color it's a dead path
                    } else if(current_board[i][col].getImage().getUrl().contentEquals(current_player)) {

                        break;

                    }
                }
            }
            case "down" -> {

                // Check the bottom side
                for(int i = row + 1; i < 8; i++) {

                    // Check if tile is empty
                    if(current_board[i][col].getImage() == null || current_board[i][col].getImage().getUrl().contentEquals(move_piece)) {

                        // If empty, draw a possible move
                        current_board[i][col].addOrigin(reverse_direction(direction));

                        draw_move(i, col);
                        break;

                        // If tile is the same color as the current player's color it's a dead path
                    } else if(current_board[i][col].getImage().getUrl().contentEquals(current_player)) {

                        break;

                    }

                }

            }
            case "left" -> {

                // Check the left side
                for(int i = col - 1; i >= 0; i--) {

                    // Check if tile is empty
                    if(current_board[row][i].getImage() == null || current_board[row][i].getImage().getUrl().contentEquals(move_piece)) {

                        // If empty, draw a possible move
                        current_board[row][i].addOrigin(reverse_direction(direction));

                        draw_move(row, i);
                        break;


                        // If tile is the same color as the current player's color it's a dead path
                    } else if(current_board[row][i].getImage().getUrl().contentEquals(current_player)) {

                        break;

                    }

                }

            }
            case "right" -> {

                // Check the right side
                for(int i = col + 1; i < 8; i++) {

                    // Check if tile is empty
                    if(current_board[row][i].getImage() == null || current_board[row][i].getImage().getUrl().contentEquals(move_piece)) {

                        // If empty, draw a possible move
                        current_board[row][i].addOrigin(reverse_direction(direction));
                        draw_move(row, i);
                        break;

                        // If tile is the same color as the current player's color it's a dead path
                    } else if(current_board[row][i].getImage().getUrl().contentEquals(current_player)) {

                        break;

                    }

                }

            }
            case "up_left" -> {

                // Check the top left side
                for(int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {

                    // Check if tile is empty
                    if(current_board[i][j].getImage() == null || current_board[i][j].getImage().getUrl().contentEquals(move_piece)) {

                        current_board[i][j].addOrigin(reverse_direction(direction));


                        // If empty, draw a possible move
                        draw_move(i, j);
                        break;

                        // If tile is the same color as the current player's color it's a dead path
                    } else if(current_board[i][j].getImage().getUrl().contentEquals(current_player)) {

                        break;

                    }

                }

            }
            case "up_right" -> {

                // Check the top right side
                for(int i = row - 1, j = col + 1; i >= 0 && j < 8; i--, j++) {

                    // Check if tile is empty
                    if(current_board[i][j].getImage() == null || current_board[i][j].getImage().getUrl().contentEquals(move_piece)) {

                        // If empty, draw a possible move
                        current_board[i][j].addOrigin(reverse_direction(direction));

                        draw_move(i, j);
                        break;

                        // If tile is the same color as the current player's color it's a dead path
                    } else if(current_board[i][j].getImage().getUrl().contentEquals(current_player)) {

                        break;

                    }

                }

            }
            case "down_left" -> {

                // Check the bottom left side
                for(int i = row + 1, j = col - 1; i < 8 && j >= 0; i++, j--) {

                    // Check if tile is empty
                    if(current_board[i][j].getImage() == null || current_board[i][j].getImage().getUrl().contentEquals(move_piece)) {

                        current_board[i][j].addOrigin(reverse_direction(direction));

                        // If empty, draw a possible move
                        draw_move(i, j);
                        break;

                        // If tile is the same color as the current player's color it's a dead path
                    } else if(current_board[i][j].getImage().getUrl().contentEquals(current_player)) {

                        break;

                    }

                }

            }
            case "down_right" -> {

                // Check the bottom right side
                for(int i = row + 1, j = col + 1; i < 8 && j < 8; i++, j++) {

                    // Check if tile is empty
                    if(current_board[i][j].getImage() == null || current_board[i][j].getImage().getUrl().contentEquals(move_piece)) {

                        current_board[i][j].addOrigin(reverse_direction(direction));
                        // If empty, draw a possible move
                        draw_move(i, j);
                        break;

                        // If tile is the same color as the current player's color it's a dead path
                    } else if(current_board[i][j].getImage().getUrl().contentEquals(current_player)) {

                        break;

                    }

                }

            }


        }


    }

    /**
     * Draws move
     *
     * @param row given row
     * @param col given col
     */
    private void draw_move(int row, int col) {


        current_board[row][col].setImage(move_piece_img);
        current_board[row][col].setFitWidth(70);
        current_board[row][col].setFitHeight(70);
        current_board[row][col].setOpacity(0.5);

        // Adds Mouse Listener
        current_board[row][col].setOnMouseClicked(e -> make_move(row, col));

    }

    /**
     * Makes move
     *
     * @param row given row
     * @param col given col
     */
    public void make_move(int row, int col) {


        // If current player is white increment white counter, otherwise black
        if(current_player.contentEquals(white_piece_path)) {

            white_piece_count++;

        } else if(current_player.contentEquals(black_piece_path)) {

            black_piece_count++;

        }
        last_move_played_row = row;
        last_move_played_col = col;

        // Flip appropriate pieces
        turn_pieces_each_direction(row, col);
        if(visible) {
            controller.set_last_move_played(row, col);
            controller.update_counts();

        }
        clear_moves();
        current_board[row][col].setImage(get_current_image(current_player));
        current_board[row][col].setFitWidth(70);
        current_board[row][col].setFitHeight(70);
        current_board[row][col].setOpacity(1);




        // Switch player
        current_player = get_next_player(current_player);
        assert current_player != null;
        if(visible) {
            controller.set_current_player_label(current_player.contentEquals(black_piece_path) ? "Black to move" : "White to move");


        }

        // Remove listener
        current_board[row][col].setOnMouseClicked(null);
        controller.play_game(this);
        // Switch maximizing and minimizing
        if(visible && controller.setting.contentEquals("Player vs Player") && current_player.contentEquals(black_piece_path))
            controller.maximizing = true;
        else if(visible && controller.setting.contentEquals("Player vs Player") && current_player.contentEquals(white_piece_path))
            controller.maximizing = false;

        if(visible && !this.current_player.contentEquals(human_control) && controller.setting.contentEquals("Player vs AI")) {
            // Start a new thread to determine the AI's move
            new Thread( () -> {

                for(int i = 0; i < 8; i++) {

                    for(int j = 0; j < 8; j++) {

                        if(current_board[i][j].getImage() != null) {

                            if(current_board[i][j].getImage().getUrl().contentEquals(move_piece))
                                current_board[i][j].setOnMouseClicked(null);

                        }

                    }

                }
                // Don't let player mess with things while AI is making a move
                controller.new_game_btn.setDisable(true);
                controller.search_depth.setDisable(true);
                controller.DEBUG_MODE.setDisable(true);
                controller.minimax_on_off.setDisable(true);
                controller.alpha_beta_pruning_on_off.setDisable(true);
                controller.start_minimax();
                determine_possible_moves();

                controller.DEBUG_MODE.setDisable(false);

                controller.new_game_btn.setDisable(false);
                controller.search_depth.setDisable(false);
                controller.minimax_on_off.setDisable(false);
                controller.alpha_beta_pruning_on_off.setDisable(false);


            }).start();
        }
    }

    /**
     * Returns current game info
     *
      * @return current game info
     */
    public HashMap<String, String> get_game_state() {

        HashMap<String, String> current_state = new HashMap<String, String>();

        current_state.put("current player", current_player);
        current_state.put("black piece count", String.valueOf(black_piece_count));
        current_state.put("white piece count", String.valueOf(white_piece_count));
        current_state.put("human control", String.valueOf(human_control));

        return current_state;

    }

    /**
     * Loads game from a position
     *
     * @param game_state given game state
     * @param current_board given board
     * @return game instance
     */
    public OthelloGameplay load_game_from_position(HashMap<String, String> game_state, ModifiedImageView[][] current_board) {

        this.current_board = new ModifiedImageView[8][8];

        for(int i = 0; i < 8; i++) {

            for(int j = 0; j < 8; j++) {

                ModifiedImageView imageView = new ModifiedImageView();

                if(current_board[i][j].getImage() == null){

                    this.current_board[i][j] = imageView;


                } else if(current_board[i][j].getImage().getUrl().contentEquals(white_piece_path)) {

                    imageView.setImage(white_piece);
                    this.current_board[i][j] = imageView;

                } else if(current_board[i][j].getImage().getUrl().contentEquals(black_piece_path)) {

                    imageView.setImage(black_piece);
                    this.current_board[i][j] = imageView;

                } else if(current_board[i][j].getImage().getUrl().contentEquals(move_piece)) {

                    imageView.setImage(move_piece_img);
                    this.current_board[i][j] = imageView;
                }

            }

        }

        this.current_player = game_state.get("current player");
        this.black_piece_count = Integer.parseInt(game_state.get("black piece count"));
        this.white_piece_count = Integer.parseInt(game_state.get("white piece count"));
        this.human_control = game_state.get("human control");

        determine_possible_moves();

        return this;

    }

    /**
     * Gets current image based on player
     *
     * @param current_player current player
     * @return image
     */
    private Image get_current_image(String current_player) {


        switch (current_player) {

            case white_piece_path -> {
                return white_piece;
            }
            case black_piece_path -> {

                return black_piece;

            }
            default -> {
                return null;
            }

        }
    }

    /**
     * Gets next player based on current player
     *
     * @param current_player given player
     * @return next player
     */
    private String get_next_player(String current_player) {

        switch (current_player) {

            case white_piece_path -> {
                return black_piece_path;
            }
            case black_piece_path -> {
                return white_piece_path;
            }
            default -> {
                return null;
            }

        }

    }

    /**
     * Clears moves
     *
     */
    private void clear_moves() {

        for(int i = 0; i < 8; i++) {

            for(int j = 0; j < 8; j++) {

                if(current_board[i][j].getImage() == move_piece_img) {

                    current_board[i][j].clearOrigins();
                    current_board[i][j].setImage(null);

                }

            }

        }

    }

    /**
     * Clears board
     *
     */
    public void clear_board() {

        for(int i = 0; i < 8; i++) {

            for(int j = 0; j < 8; j++) {

                current_board[i][j].clearOrigins();
                current_board[i][j].setImage(null);

            }

        }

        current_player = black_piece_path;
        assert current_player != null;
        if(visible)
            controller.set_current_player_label(current_player.contentEquals(black_piece_path) ? "Black to move" : "White to move");
        white_piece_count = 2;
        black_piece_count = 2;
        black_has_move = true;
        white_has_move = true;

    }

    /**
     * Gets reverse direction of a given origin
     * Example top -> bottom
     *
     * @param origin origin
     * @return opposite direction
     */
    private String reverse_direction(String origin) {

        switch (origin) {

            case "up" -> {
                return "down";
            }
            case "down" -> {
                return "up";
            }
            case "left" -> {
                return "right";
            }
            case "right" -> {
                return "left";
            }
            case "up_left" -> {
                return "down_right";
            }
            case "up_right" -> {
                return "down_left";
            }
            case "down_left" -> {
                return "up_right";
            }
            case "down_right" -> {
                return "up_left";
            }
            default -> {
                return null;
            }

        }

    }


}
