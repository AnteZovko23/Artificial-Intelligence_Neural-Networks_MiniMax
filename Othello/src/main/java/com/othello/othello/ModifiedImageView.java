package com.othello.othello;

import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Extends JavaFX ImageView class to allow for origin tracking
 * Origin is where did the move determination come from so pieces can be flipped in the
 * opposite direction
 *
 * Author: Ante Zovko
 * Version: November 14th 2021
 *
 */
public class ModifiedImageView extends ImageView  {

    ArrayList<String> origins;

    public ModifiedImageView() {
        super();
        origins = new ArrayList<>();
    }


    /**
     * Adds origin
     *
     * @param origin origin
     */
    public void addOrigin(String origin) {
        origins.add(origin);
    }

    /**
     * Get origin
     *
     * @return origin
     */
    public ArrayList<String> getOrigins() {
        return origins;
    }

    /**
     *
     * Clears the origin list
     *
     */
    public void clearOrigins() {
        origins.clear();
    }
}




