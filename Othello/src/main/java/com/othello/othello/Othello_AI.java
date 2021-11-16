package com.othello.othello;


import java.util.ArrayList;

/**
 *
 * Implementation of an Othello AI that determines moves based on a
 * mini-max search with a heuristic function.
 *
 * The heuristic function focuses on mobility i.e. maximizing its number of moves
 * while minimizing the opponents moves while also counting the number of
 * pieces and number of possible moves
 *
 * Author: Ante Zovko
 * Version: November 14, 2021
 *
 *
 */
public class Othello_AI {

    public OthelloGameplay game;

    public Controller controller;

    public int number_of_states_explored = 0;

    /**
     * OthelloAI Constructor
     *
     * @param game game instance
     * @param controller controller instance
     */
    public Othello_AI(OthelloGameplay game, Controller controller) {


        this.game = game;
        this.controller = controller;

    }


    /**
     * Plays a move
     *
     * @param row given row
     * @param col given col
     */
    public void play_move(int row, int col) {

        game.make_move(row, col);

    }


    /**
     * Performs Mini-Max search with no pruning
     *
     * @param root TreeNode given root
     * @param temporary_board temporary board that is used to see into the future
     * @param depth current depth
     * @param maximizing_player max or min player
     *
     * @return evaluation
     */
    public double mini_max_no_pruning(TreeNode root, OthelloGameplay temporary_board, int depth, boolean maximizing_player) {

        // Every time this function is called, a new state is explored
        // Counter
        number_of_states_explored++;


        // Determine if either white or black have no moves
        // In that case another move is played by the same player until they both run out or the other
        // has another one
        if(!temporary_board.white_has_move && temporary_board.black_has_move) {
            if(controller.DEBUG)
                System.out.println("White is out of moves");
            temporary_board.current_player = temporary_board.black_piece_path;
            temporary_board.determine_possible_moves();
        }
        else if(!temporary_board.black_has_move && temporary_board.white_has_move) {
            if(controller.DEBUG)
                System.out.println("Black is out of moves");
            temporary_board.current_player = temporary_board.white_piece_path;
            temporary_board.determine_possible_moves();

        }

        // If depth = 0 or both run out of moves
        if(depth == 0 || !(temporary_board.white_has_move || temporary_board.black_has_move)) {

            if(!(temporary_board.white_has_move || temporary_board.black_has_move)){
                // If there are truly no more moves, then the leaf evaluation will be either
                // the minimum of maximum value
                if(temporary_board.white_piece_count > temporary_board.black_piece_count)
                    root.set_evaluation(Integer.MIN_VALUE);
                else if(temporary_board.black_piece_count > temporary_board.white_piece_count)
                    root.set_evaluation(Integer.MAX_VALUE);
                else
                    root.set_evaluation(0);
            } else {

                // Calculate heuristic
                root.set_evaluation(heuristic(temporary_board));

            }


            // Print information
            if(controller.DEBUG) {

                System.out.println("Final Level");
                System.out.println();


                for (int row = 0; row < 8; row++) {

                    for (int col = 0; col < 8; col++) {

                        if (temporary_board.current_board[row][col].getImage() != null) {


                            if (temporary_board.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.move_piece)) {

                                System.out.print("X" + " ");

                            } else if (temporary_board.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.black_piece_path))
                                System.out.print("B" + " ");
                            else if (temporary_board.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.white_piece_path))
                                System.out.print("W" + " ");

                        } else

                            System.out.print("-" + " ");

                    }
                    System.out.println();

                }


                System.out.println("Heuristic Value of Given Position: " + heuristic(temporary_board));
            }
//            root.set_evaluation(heuristic(temporary_board));

            // Return the evaluation
            return root.get_evaluation();

        } else if (maximizing_player) {


            // Count possible moves
            ArrayList<Integer[]> move_counter =  new ArrayList<>();
            for (int row = 0; row < 8; row++) {

                for (int col = 0; col < 8; col++) {


                    if (temporary_board.current_board[row][col].getImage() != null) {


                        if (temporary_board.current_board[row][col].getImage().getUrl().contentEquals(game.move_piece)) {

                            move_counter.add(new Integer[]{row, col});

                        }

                    }

                }

            }

            double max_evaluation = Integer.MIN_VALUE;
            for (int i = 0; i < move_counter.size(); i++) {

                // Create new child
                root.add_child(new TreeNode(move_counter.get(i)));

                // Save the current state of the board
                OthelloGameplay saved_state = new OthelloGameplay(null, controller, false);
                saved_state = saved_state.load_game_from_position(temporary_board.get_game_state(), temporary_board.current_board);

                // Print info
                if(controller.DEBUG) {


                    System.out.println();
                    System.out.println();
                    System.out.println("LEVEL: _____________" + depth + "_____________");
                    System.out.println();
                    System.out.println("Previous State: ");
                    System.out.println();
                    for (int row = 0; row < 8; row++) {

                        for (int col = 0; col < 8; col++) {

                            if (saved_state.current_board[row][col].getImage() != null) {


                                if (saved_state.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.move_piece)) {

                                    System.out.print("X" + " ");

                                } else if (saved_state.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.black_piece_path))
                                    System.out.print("B" + " ");
                                else if (saved_state.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.white_piece_path))
                                    System.out.print("W" + " ");

                            } else

                                System.out.print("-" + " ");

                        }
                        System.out.println();

                    }

                    System.out.println("Heuristic Value of Given Position: " + heuristic(temporary_board));
                    System.out.println();
                    System.out.println();
                    System.out.println("Move to be made: " + root.get_children().get(i).get_tile()[0] + " " + root.get_children().get(i).get_tile()[1]);
                    System.out.println(temporary_board.current_player.contentEquals(temporary_board.black_piece_path) ? "Black Moved" : "White Moved");
                }

                // Make a move
                saved_state.make_move(root.get_children().get(i).get_tile()[0], root.get_children().get(i).get_tile()[1]);

                // Evaluate after the move
                double evaluation = mini_max_no_pruning(root.get_children().get(i), saved_state, depth - 1, false);
                max_evaluation = Math.max(max_evaluation, evaluation);
                root.set_evaluation(max_evaluation);

            }

            return max_evaluation;

            // Minimizing node
        } else {


            // Find possible moves
            ArrayList<Integer[]> move_counter =  new ArrayList<>();
            for (int row = 0; row < 8; row++) {

                for (int col = 0; col < 8; col++) {


                    if (temporary_board.current_board[row][col].getImage() != null) {


                        if (temporary_board.current_board[row][col].getImage().getUrl().contentEquals(game.move_piece)) {

                            move_counter.add(new Integer[]{row, col});

                        }

                    }

                }

            }

            // For each possible move
            double min_evaluation = Integer.MAX_VALUE;
            for (int i = 0; i < move_counter.size(); i++) {

                // Create new node
                root.add_child(new TreeNode(move_counter.get(i)));

                // Save current state
                OthelloGameplay saved_state = new OthelloGameplay(null, controller, false);
                saved_state = saved_state.load_game_from_position(temporary_board.get_game_state(), temporary_board.current_board);

                // Print info
                if(controller.DEBUG) {

                    System.out.println();
                    System.out.println();
                    System.out.println("LEVEL: _____________" + depth + "_____________");
                    System.out.println();
                    System.out.println("Previous State: ");
                    System.out.println();
                    for (int row = 0; row < 8; row++) {

                        for (int col = 0; col < 8; col++) {

                            if (saved_state.current_board[row][col].getImage() != null) {


                                if (saved_state.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.move_piece)) {

                                    System.out.print("X" + " ");

                                } else if (saved_state.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.black_piece_path))
                                    System.out.print("B" + " ");
                                else if (saved_state.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.white_piece_path))
                                    System.out.print("W" + " ");

                            } else

                                System.out.print("-" + " ");

                        }
                        System.out.println();

                    }

                    System.out.println("Heuristic Value of Given Position: " + heuristic(temporary_board));
                    System.out.println();
                    System.out.println();
                    System.out.println("Move to be made: " + root.get_children().get(i).get_tile()[0] + " " + root.get_children().get(i).get_tile()[1]);
                    System.out.println(temporary_board.current_player.contentEquals(temporary_board.black_piece_path) ? "Black Moved" : "White Moved");

                }

                // Make move
                saved_state.make_move(root.get_children().get(i).get_tile()[0], root.get_children().get(i).get_tile()[1]);

                double evaluation = mini_max_no_pruning(root.get_children().get(i), saved_state, depth - 1, true);
                min_evaluation = Math.min(min_evaluation, evaluation);
                root.set_evaluation(min_evaluation);

            }

            return min_evaluation;

        }


    }

    /**
     * Performs Mini-Max search with no pruning
     *
     * @param root TreeNode given root
     * @param temporary_board temporary board that is used to see into the future
     * @param depth current depth
     * @param maximizing_player max or min player
     *
     * @return evaluation
     */
    public double mini_max_pruning(TreeNode root, OthelloGameplay temporary_board, double alpha, double beta, int depth, boolean maximizing_player) {


        // Every time this function is called, a new state is explored
        // Counter
        number_of_states_explored++;


        // Determine if either white or black have no moves
        // In that case another move is played by the same player until they both run out or the other
        // has another one
        if(!temporary_board.white_has_move && temporary_board.black_has_move) {
            if(controller.DEBUG)
                System.out.println("White is out of moves");
            temporary_board.current_player = temporary_board.black_piece_path;
            temporary_board.determine_possible_moves();
        }
        else if(!temporary_board.black_has_move && temporary_board.white_has_move) {
            if(controller.DEBUG)
                System.out.println("Black is out of moves");
            temporary_board.current_player = temporary_board.white_piece_path;
            temporary_board.determine_possible_moves();

        }

        // If depth is 0 or players have no moves
        if(depth == 0 || !(temporary_board.white_has_move || temporary_board.black_has_move)) {

            if(!(temporary_board.white_has_move || temporary_board.black_has_move)){

                    if(temporary_board.white_piece_count > temporary_board.black_piece_count)
                        root.set_evaluation(Integer.MIN_VALUE);
                    else if(temporary_board.black_piece_count > temporary_board.white_piece_count)
                        root.set_evaluation(Integer.MAX_VALUE);
                    else
                        root.set_evaluation(0);
            } else {

                root.set_evaluation(heuristic(temporary_board));

            }

            // Print info
            if(controller.DEBUG) {
                System.out.println();
                System.out.println("Final Level");
                System.out.println();

                for (int row = 0; row < 8; row++) {

                    for (int col = 0; col < 8; col++) {

                        if (temporary_board.current_board[row][col].getImage() != null) {


                            if (temporary_board.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.move_piece)) {

                                System.out.print("X" + " ");

                            } else if (temporary_board.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.black_piece_path))
                                System.out.print("B" + " ");
                            else if (temporary_board.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.white_piece_path))
                                System.out.print("W" + " ");

                        } else

                            System.out.print("-" + " ");

                    }
                    System.out.println();

                }


                System.out.println("Heuristic Value of Given Position: " + root.get_evaluation());


            }

            return root.get_evaluation();

        } else if (maximizing_player) {


            // Find possible moves
            ArrayList<Integer[]> move_counter =  new ArrayList<>();
            for (int row = 0; row < 8; row++) {

                for (int col = 0; col < 8; col++) {


                    if (temporary_board.current_board[row][col].getImage() != null) {


                        if (temporary_board.current_board[row][col].getImage().getUrl().contentEquals(game.move_piece)) {

                            move_counter.add(new Integer[]{row, col});

                        }

                    }

                }

            }

            // For each possible move
            double max_evaluation = Integer.MIN_VALUE;
            for (int i = 0; i < move_counter.size(); i++) {

                root.add_child(new TreeNode(move_counter.get(i)));

                OthelloGameplay saved_state = new OthelloGameplay(null, controller, false);
                saved_state = saved_state.load_game_from_position(temporary_board.get_game_state(), temporary_board.current_board);

                if(controller.DEBUG) {
                    System.out.println();
                    System.out.println();
                    System.out.println("LEVEL: _____________" + depth + "_____________");
                    System.out.println();
                    System.out.println("Previous State: ");
                    System.out.println();
                    for (int row = 0; row < 8; row++) {

                        for (int col = 0; col < 8; col++) {

                            if (saved_state.current_board[row][col].getImage() != null) {


                                if (saved_state.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.move_piece)) {

                                    System.out.print("X" + " ");

                                } else if (saved_state.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.black_piece_path))
                                    System.out.print("B" + " ");
                                else if (saved_state.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.white_piece_path))
                                    System.out.print("W" + " ");

                            } else

                                System.out.print("-" + " ");

                        }
                        System.out.println();

                    }

                    System.out.println("Heuristic Value of Given Position: " + heuristic(temporary_board));
                    System.out.println();
                    System.out.println();
                    System.out.println("Move to be made: " + root.get_children().get(i).get_tile()[0] + " " + root.get_children().get(i).get_tile()[1]);
                    System.out.println(temporary_board.current_player.contentEquals(temporary_board.black_piece_path) ? "Black Moved" : "White Moved");
                }

                saved_state.make_move(root.get_children().get(i).get_tile()[0], root.get_children().get(i).get_tile()[1]);

                double evaluation = mini_max_pruning(root.get_children().get(i), saved_state, alpha, beta, depth - 1, false);
                max_evaluation = Math.max(max_evaluation, evaluation);
                alpha = Math.max(alpha, evaluation);
                root.set_evaluation(max_evaluation);

                if(beta <= alpha)
                    break;

            }

            return max_evaluation;

            // Minimizing player
        } else {


            // Find moves
            ArrayList<Integer[]> move_counter =  new ArrayList<>();
            for (int row = 0; row < 8; row++) {

                for (int col = 0; col < 8; col++) {


                    if (temporary_board.current_board[row][col].getImage() != null) {


                        if (temporary_board.current_board[row][col].getImage().getUrl().contentEquals(game.move_piece)) {

                            move_counter.add(new Integer[]{row, col});

                        }

                    }

                }

            }

            // For each move
            double min_evaluation = Integer.MAX_VALUE;
            for (int i = 0; i < move_counter.size(); i++) {

                root.add_child(new TreeNode(move_counter.get(i)));

                OthelloGameplay saved_state = new OthelloGameplay(null, controller, false);
                saved_state = saved_state.load_game_from_position(temporary_board.get_game_state(), temporary_board.current_board);

                if(controller.DEBUG) {

                    System.out.println();
                    System.out.println();
                    System.out.println("LEVEL: _____________" + depth + "_____________");
                    System.out.println();
                    System.out.println("Previous State: ");
                    System.out.println();
                    for (int row = 0; row < 8; row++) {

                        for (int col = 0; col < 8; col++) {

                            if (saved_state.current_board[row][col].getImage() != null) {


                                if (saved_state.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.move_piece)) {

                                    System.out.print("X" + " ");

                                } else if (saved_state.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.black_piece_path))
                                    System.out.print("B" + " ");
                                else if (saved_state.current_board[row][col].getImage().getUrl().contentEquals(temporary_board.white_piece_path))
                                    System.out.print("W" + " ");

                            } else

                                System.out.print("-" + " ");

                        }
                        System.out.println();

                    }

                    System.out.println("Heuristic Value of Given Position: " + heuristic(temporary_board));
                    System.out.println();
                    System.out.println();
                    System.out.println("Move to be made: " + root.get_children().get(i).get_tile()[0] + " " + root.get_children().get(i).get_tile()[1]);
                    System.out.println(temporary_board.current_player.contentEquals(temporary_board.black_piece_path) ? "Black Moved" : "White Moved");
                }

                // Make move
                saved_state.make_move(root.get_children().get(i).get_tile()[0], root.get_children().get(i).get_tile()[1]);

                double evaluation = mini_max_pruning(root.get_children().get(i), saved_state, alpha, beta,  depth - 1, true);
                min_evaluation = Math.min(min_evaluation, evaluation);
                beta = Math.min(beta, evaluation);
                root.set_evaluation(min_evaluation);
                if(beta <= alpha)
                    break;


            }

            return min_evaluation;

        }


    }

    /**
     *
     * Heuristic Function
     * Rewards 10 for each possible move, 10 for each empty tile next to opponent piece
     * Rewards 100 for corner tiles
     * Rewards 1/100 for piece balance
     *
     * @param game game instance
     * @return calculated heuristic
     */
    public double heuristic(OthelloGameplay game) {

        // Count number of pieces
        int white_pieces = game.white_piece_count;
        int black_pieces = game.black_piece_count;

        // calculate piece weight
        double heuristic =  ((double) black_pieces - white_pieces) / (black_pieces + white_pieces);

        // calculate move weight
        for (int rows = 0; rows < 8; rows++) {

            for (int cols = 0; cols < 8; cols++) {

                if (game.current_board[rows][cols].getImage() != null) {

                    if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.move_piece)) {

                        if (game.current_player.contentEquals(game.black_piece_path))
                            heuristic += 10;
                        else
                            heuristic -= 10;
                    }

                    // move left one step if possible
                    if (cols - 1 >= 0) {
                        //  move left one step and check if empty
                        if (game.current_board[rows][cols - 1].getImage() == null) {

                            // check if the piece is black or white
                            if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.black_piece_path)) {

                                heuristic -= 10;

                            } else if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.white_piece_path)) {

                                heuristic += 10;

                            }


                        }
                    }

                    // move up if possible
                    if (rows - 1 >= 0) {

                        //  move up one step and check if empty
                        if (game.current_board[rows - 1][cols].getImage() == null) {

                            // check if the piece is black or white
                            if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.black_piece_path)) {

                                heuristic -= 10;

                            } else if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.white_piece_path)) {

                                heuristic += 10;

                            }

                        }

                    }
                    // move right if possible
                    if (cols + 1 < 8) {

                        //  move right one step and check if empty
                        if (game.current_board[rows][cols + 1].getImage() == null) {

                            // check if the piece is black or white
                            if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.black_piece_path)) {

                                heuristic -= 10;

                            } else if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.white_piece_path)) {

                                heuristic += 10;

                            }

                        }

                    }
                    // move down if possible
                    if (rows + 1 < 8) {

                        //  move down one step and check if empty
                        if (game.current_board[rows + 1][cols].getImage() == null) {

                            // check if the piece is black or white
                            if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.black_piece_path)) {

                                heuristic -= 10;

                            } else if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.white_piece_path)) {

                                heuristic += 10;

                            }

                        }

                    }
                    // move up left if possible
                    if (rows - 1 >= 0 && cols - 1 >= 0) {

                        //  move up left one step and check if empty
                        if (game.current_board[rows - 1][cols - 1].getImage() == null) {

                            // check if the piece is black or white
                            if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.black_piece_path)) {

                                heuristic -= 10;

                            } else if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.white_piece_path)) {

                                heuristic += 10;

                            }

                        }

                    }
                    // move up right if possible
                    if (rows - 1 >= 0 && cols + 1 < 8) {

                        //  move up right one step and check if empty
                        if (game.current_board[rows - 1][cols + 1].getImage() == null) {

                            // check if the piece is black or white
                            if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.black_piece_path)) {

                                heuristic -= 10;

                            } else if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.white_piece_path)) {

                                heuristic += 10;

                            }

                        }

                    }
                    // move down left if possible
                    if (rows + 1 < 8 && cols - 1 >= 0) {

                        //  move down left one step and check if empty
                        if (game.current_board[rows + 1][cols - 1].getImage() == null) {

                            // check if the piece is black or white
                            if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.black_piece_path)) {

                                heuristic -= 10;

                            } else if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.white_piece_path)) {

                                heuristic += 10;

                            }

                        }

                    }
                    // move down right if possible
                    if (rows + 1 < 8 && cols + 1 < 8) {

                        //  move down right one step and check if empty
                        if (game.current_board[rows + 1][cols + 1].getImage() == null) {

                            // check if the piece is black or white
                            if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.black_piece_path)) {

                                heuristic -= 10;

                            } else if (game.current_board[rows][cols].getImage().getUrl().contentEquals(game.white_piece_path)) {

                                heuristic += 10;

                            }

                        }


                    }
                }

            }

        }

        // get heuristic value for each step around the corners
        if(game.current_board[0][1].getImage() != null){


            if(game.current_board[0][1].getImage().getUrl().contentEquals(game.move_piece)) {

                // If current player is black
                if (game.current_player.contentEquals(game.black_piece_path)) {

                    heuristic -= 50;

                } else if(game.current_player.contentEquals(game.white_piece_path)) {

                    heuristic += 50;

                }

            }


            if(game.current_board[0][1].getImage().getUrl().contentEquals(game.black_piece_path)){

                heuristic -= 75;

            } else if(game.current_board[0][1].getImage().getUrl().contentEquals(game.white_piece_path)){

                heuristic += 75;

            }



        }

        // get heuristic value for each step around the corners
        if(game.current_board[1][0].getImage() != null){


            if(game.current_board[1][0].getImage().getUrl().contentEquals(game.move_piece)) {

                // If current player is black
                if (game.current_player.contentEquals(game.black_piece_path)) {

                    heuristic -= 50;

                } else if(game.current_player.contentEquals(game.white_piece_path)) {

                    heuristic += 50;

                }

            }


            if(game.current_board[1][0].getImage().getUrl().contentEquals(game.black_piece_path)){

                heuristic -= 75;

            } else if(game.current_board[1][0].getImage().getUrl().contentEquals(game.white_piece_path)){

                heuristic += 75;

            }



        }

        // get heuristic value for each step around the corners
        if(game.current_board[1][1].getImage() != null){


            if(game.current_board[1][1].getImage().getUrl().contentEquals(game.move_piece)) {

                // If current player is black
                if (game.current_player.contentEquals(game.black_piece_path)) {

                    heuristic -= 50;

                } else if(game.current_player.contentEquals(game.white_piece_path)) {

                    heuristic += 50;

                }

            }


            if(game.current_board[1][1].getImage().getUrl().contentEquals(game.black_piece_path)){

                heuristic -= 75;

            } else if(game.current_board[1][1].getImage().getUrl().contentEquals(game.white_piece_path)){

                heuristic += 75;

            }



        }

        // get heuristic value for each step around the corners
        if(game.current_board[0][6].getImage() != null){


            if(game.current_board[0][6].getImage().getUrl().contentEquals(game.move_piece)) {

                // If current player is black
                if (game.current_player.contentEquals(game.black_piece_path)) {

                    heuristic -= 50;

                } else if(game.current_player.contentEquals(game.white_piece_path)) {

                    heuristic += 50;

                }

            }


            if(game.current_board[0][6].getImage().getUrl().contentEquals(game.black_piece_path)){

                heuristic -= 75;

            } else if(game.current_board[0][6].getImage().getUrl().contentEquals(game.white_piece_path)){

                heuristic += 75;

            }

            // get heuristic value for each step around the corners
            if(game.current_board[1][6].getImage() != null){


                if(game.current_board[1][6].getImage().getUrl().contentEquals(game.move_piece)) {

                    // If current player is black
                    if (game.current_player.contentEquals(game.black_piece_path)) {

                        heuristic -= 50;

                    } else if(game.current_player.contentEquals(game.white_piece_path)) {

                        heuristic += 50;

                    }

                }


                if(game.current_board[1][6].getImage().getUrl().contentEquals(game.black_piece_path)){

                    heuristic -= 75;

                } else if(game.current_board[1][6].getImage().getUrl().contentEquals(game.white_piece_path)){

                    heuristic += 75;

                }



            }

            // get heuristic value for each step around the corners
            if(game.current_board[1][7].getImage() != null){


                if(game.current_board[1][7].getImage().getUrl().contentEquals(game.move_piece)) {

                    // If current player is black
                    if (game.current_player.contentEquals(game.black_piece_path)) {

                        heuristic -= 50;

                    } else if(game.current_player.contentEquals(game.white_piece_path)) {

                        heuristic += 50;

                    }

                }


                if(game.current_board[1][7].getImage().getUrl().contentEquals(game.black_piece_path)){

                    heuristic -= 75;

                } else if(game.current_board[1][7].getImage().getUrl().contentEquals(game.white_piece_path)){

                    heuristic += 75;

                }



            }

            // get heuristic value for each step around the corners
            if(game.current_board[1][6].getImage() != null){


                if(game.current_board[1][6].getImage().getUrl().contentEquals(game.move_piece)) {

                    // If current player is black
                    if (game.current_player.contentEquals(game.black_piece_path)) {

                        heuristic -= 50;

                    } else if(game.current_player.contentEquals(game.white_piece_path)) {

                        heuristic += 50;

                    }

                }


                if(game.current_board[1][6].getImage().getUrl().contentEquals(game.black_piece_path)){

                    heuristic -= 75;

                } else if(game.current_board[1][6].getImage().getUrl().contentEquals(game.white_piece_path)){

                    heuristic += 75;

                }



            }
            // get heuristic value for each step around the corners
            if(game.current_board[6][0].getImage() != null){


                if(game.current_board[6][0].getImage().getUrl().contentEquals(game.move_piece)) {

                    // If current player is black
                    if (game.current_player.contentEquals(game.black_piece_path)) {

                        heuristic -= 50;

                    } else if(game.current_player.contentEquals(game.white_piece_path)) {

                        heuristic += 50;

                    }

                }


                if(game.current_board[6][0].getImage().getUrl().contentEquals(game.black_piece_path)){

                    heuristic -= 75;

                } else if(game.current_board[6][0].getImage().getUrl().contentEquals(game.white_piece_path)){

                    heuristic += 75;

                }



            }



        }

        // get heuristic value for each step around the corners
        if(game.current_board[6][1].getImage() != null){


            if(game.current_board[6][1].getImage().getUrl().contentEquals(game.move_piece)) {

                // If current player is black
                if (game.current_player.contentEquals(game.black_piece_path)) {

                    heuristic -= 50;

                } else if(game.current_player.contentEquals(game.white_piece_path)) {

                    heuristic += 50;

                }

            }


            if(game.current_board[6][1].getImage().getUrl().contentEquals(game.black_piece_path)){

                heuristic -= 75;

            } else if(game.current_board[6][1].getImage().getUrl().contentEquals(game.white_piece_path)){

                heuristic += 75;

            }

            // get heuristic value for each step around the corners
            if(game.current_board[7][1].getImage() != null){


                if(game.current_board[7][1].getImage().getUrl().contentEquals(game.move_piece)) {

                    // If current player is black
                    if (game.current_player.contentEquals(game.black_piece_path)) {

                        heuristic -= 50;

                    } else if(game.current_player.contentEquals(game.white_piece_path)) {

                        heuristic += 50;

                    }

                }


                if(game.current_board[7][1].getImage().getUrl().contentEquals(game.black_piece_path)){

                    heuristic -= 75;

                } else if(game.current_board[7][1].getImage().getUrl().contentEquals(game.white_piece_path)){

                    heuristic += 75;

                }



            }



        }

        // get heuristic value for each step around the corners
        if(game.current_board[6][6].getImage() != null){


            if(game.current_board[6][6].getImage().getUrl().contentEquals(game.move_piece)) {

                // If current player is black
                if (game.current_player.contentEquals(game.black_piece_path)) {

                    heuristic -= 50;

                } else if(game.current_player.contentEquals(game.white_piece_path)) {

                    heuristic += 50;

                }

            }


            if(game.current_board[6][6].getImage().getUrl().contentEquals(game.black_piece_path)){

                heuristic -= 75;

            } else if(game.current_board[6][6].getImage().getUrl().contentEquals(game.white_piece_path)){

                heuristic += 75;

            }

            // get heuristic value for each step around the corners
            if(game.current_board[6][7].getImage() != null){


                if(game.current_board[6][7].getImage().getUrl().contentEquals(game.move_piece)) {

                    // If current player is black
                    if (game.current_player.contentEquals(game.black_piece_path)) {

                        heuristic -= 50;

                    } else if(game.current_player.contentEquals(game.white_piece_path)) {

                        heuristic += 50;

                    }

                }


                if(game.current_board[6][7].getImage().getUrl().contentEquals(game.black_piece_path)){

                    heuristic -= 75;

                } else if(game.current_board[6][7].getImage().getUrl().contentEquals(game.white_piece_path)){

                    heuristic += 75;

                }



            }



        }

        // get heuristic value for each step around the corners
        if(game.current_board[7][6].getImage() != null){


            if(game.current_board[7][6].getImage().getUrl().contentEquals(game.move_piece)) {

                // If current player is black
                if (game.current_player.contentEquals(game.black_piece_path)) {

                    heuristic -= 50;

                } else if(game.current_player.contentEquals(game.white_piece_path)) {

                    heuristic += 50;

                }

            }


            if(game.current_board[7][6].getImage().getUrl().contentEquals(game.black_piece_path)){

                heuristic -= 75;

            } else if(game.current_board[7][6].getImage().getUrl().contentEquals(game.white_piece_path)){

                heuristic += 75;

            }



        }

        // For each tile around the sides
        for(int rows = 2; rows < 6; rows++) {

            // get heuristic value for each step around the corners
            if (game.current_board[rows][0].getImage() != null) {


                if (game.current_board[rows][0].getImage().getUrl().contentEquals(game.move_piece)) {

                    // If current player is black
                    if (game.current_player.contentEquals(game.black_piece_path)) {

                        heuristic += 25;

                    } else if (game.current_player.contentEquals(game.white_piece_path)) {

                        heuristic -= 25;

                    }

                }


                if (game.current_board[rows][0].getImage().getUrl().contentEquals(game.black_piece_path)) {

                    heuristic += 30;

                } else if (game.current_board[rows][0].getImage().getUrl().contentEquals(game.white_piece_path)) {

                    heuristic -= 30;

                }

            }
        }

        // For each tile around the sides
        for(int rows = 2; rows < 6; rows++) {

            // get heuristic value for each step around the corners
            if (game.current_board[rows][7].getImage() != null) {


                if (game.current_board[rows][7].getImage().getUrl().contentEquals(game.move_piece)) {

                    // If current player is black
                    if (game.current_player.contentEquals(game.black_piece_path)) {

                        heuristic += 25;

                    } else if (game.current_player.contentEquals(game.white_piece_path)) {

                        heuristic -= 25;

                    }

                }


                if (game.current_board[rows][7].getImage().getUrl().contentEquals(game.black_piece_path)) {

                    heuristic += 30;

                } else if (game.current_board[rows][7].getImage().getUrl().contentEquals(game.white_piece_path)) {

                    heuristic -= 30;

                }

            }
        }

        // For each tile around the sides
        for(int cols = 2; cols < 6; cols++) {

            // get heuristic value for each step around the corners
            if (game.current_board[0][cols].getImage() != null) {


                if (game.current_board[0][cols].getImage().getUrl().contentEquals(game.move_piece)) {

                    // If current player is black
                    if (game.current_player.contentEquals(game.black_piece_path)) {

                        heuristic += 25;

                    } else if (game.current_player.contentEquals(game.white_piece_path)) {

                        heuristic -= 25;

                    }

                }


                if (game.current_board[0][cols].getImage().getUrl().contentEquals(game.black_piece_path)) {

                    heuristic += 30;

                } else if (game.current_board[0][cols].getImage().getUrl().contentEquals(game.white_piece_path)) {

                    heuristic -= 30;

                }

            }
        }

        // For each tile around the sides
        for(int cols = 2; cols < 6; cols++) {

            // get heuristic value for each step around the corners
            if (game.current_board[7][cols].getImage() != null) {


                if (game.current_board[7][cols].getImage().getUrl().contentEquals(game.move_piece)) {

                    // If current player is black
                    if (game.current_player.contentEquals(game.black_piece_path)) {

                        heuristic += 25;

                    } else if (game.current_player.contentEquals(game.white_piece_path)) {

                        heuristic -= 25;

                    }

                }


                if (game.current_board[7][cols].getImage().getUrl().contentEquals(game.black_piece_path)) {

                    heuristic += 30;

                } else if (game.current_board[7][cols].getImage().getUrl().contentEquals(game.white_piece_path)) {

                    heuristic -= 30;

                }

            }
        }




        // calculate corner weight
        if (game.current_board[0][0].getImage() != null) {

            if(game.current_board[0][0].getImage().getUrl().contentEquals(game.move_piece)) {

                // If current player is black
                if (game.current_player.contentEquals(game.black_piece_path)) {

                    heuristic += 100;

                } else if (game.current_player.contentEquals(game.white_piece_path)) {

                    heuristic -= 100;

                }

            }
             if (game.current_board[0][0].getImage().getUrl().contentEquals(game.black_piece_path))
                heuristic += 300;
            else if (game.current_board[0][0].getImage().getUrl().contentEquals(game.white_piece_path))
                heuristic -= 300;
        } else if (game.current_board[0][7].getImage() != null) {

            if(game.current_board[0][7].getImage().getUrl().contentEquals(game.move_piece)) {

                // If current player is black
                if (game.current_player.contentEquals(game.black_piece_path)) {

                    heuristic += 100;

                } else if(game.current_player.contentEquals(game.white_piece_path)) {

                    heuristic -= 100;

                }

            }
             if (game.current_board[0][7].getImage().getUrl().contentEquals(game.black_piece_path))
                heuristic += 300;
            else if (game.current_board[0][7].getImage().getUrl().contentEquals(game.white_piece_path))
                heuristic -= 300;

        } else if (game.current_board[7][0].getImage() != null) {

            if(game.current_board[7][0].getImage().getUrl().contentEquals(game.move_piece)) {

                // If current player is black
                if (game.current_player.contentEquals(game.black_piece_path)) {

                    heuristic += 100;

                } else if(game.current_player.contentEquals(game.white_piece_path)) {

                    heuristic -= 100;

                }

            }  if (game.current_board[7][0].getImage().getUrl().contentEquals(game.black_piece_path))
                heuristic += 300;
            else if (game.current_board[7][0].getImage().getUrl().contentEquals(game.white_piece_path))
                heuristic -= 300;
        } else if (game.current_board[7][7].getImage() != null) {

            if(game.current_board[7][7].getImage().getUrl().contentEquals(game.move_piece)) {

                // If current player is black
                if (game.current_player.contentEquals(game.black_piece_path)) {

                    heuristic += 100;

                } else if(game.current_player.contentEquals(game.white_piece_path)) {

                    heuristic -= 100;

                }

            }  if(game.current_board[7][7].getImage().getUrl().contentEquals(game.black_piece_path))
                heuristic += 300;
            else if (game.current_board[7][7].getImage().getUrl().contentEquals(game.white_piece_path))
                heuristic -= 300;

        }


        return heuristic;

    }
}
