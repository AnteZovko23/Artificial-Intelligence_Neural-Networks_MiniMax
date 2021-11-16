package com.othello.othello;


import java.util.ArrayList;

/**
 * Tree Node Implementation
 *
 * Author: Ante Zovko
 * Version: November 14th, 2021
 *
 */
public class TreeNode {

    private int level;
    private double evaluation;
    Integer[] row_col;
    private ArrayList<TreeNode> children = new ArrayList<>();

    /**
     * Constructor
     *
     * @param row_col row and column
     */
    public TreeNode(Integer[] row_col) {

        this.row_col = row_col;

    }

    /**
     * Adds child of the node
     *
     * @param child given child
     */
    public void add_child(TreeNode child) {

        children.add(child);

    }

    /**
     * Gets children of node
     *
     * @return children
     */
    public ArrayList<TreeNode> get_children() {

        return this.children;

    }

    /**
     * Clears children
     *
     */
    public void clear() {

        children.clear();

    }

    /**
     * Gets evaluation
     *
     * @return evaluation
     */
    public double get_evaluation() {

        return evaluation;

    }

    /**
     * Sets evaluation
     *
     * @param evaluation evaluation
     */
    public void set_evaluation(double evaluation) {

        this.evaluation = evaluation;

    }

    /**
     * Sets tile
     *
     * @param row_col row and column
     */
    public void set_tile(Integer[] row_col) {

        this.row_col = row_col;

    }

    /**
     * Gets Tile
     *
     * @return tile
     */
    public Integer[] get_tile() {

        return this.row_col;

    }



}
