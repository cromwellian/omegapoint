package com.omegapoint.core;

/**
 * Hold current game state variables regarding inventory;
 */
public class Inventory {
    private int score;
    private int resources;

    public int score() {
        return score;
    }

    public int resources() {
        return resources;
    }

    public void incrementScore(int score) {
        this.score += score;
    }

    public void incrementResources(int resources) {
        this.resources += resources;
    }
}
