package com.omegapoint.core;

/**
 * Hold current game state variables regarding enemies;
 */
public class Enemies {
    private static int currentLive;
    public static int MAX_ENEMIES = 5;

    public int currentLiveEnemies() {
        return currentLive;
    }

    public void incrementLiveEnemies() {
        currentLive++;
    }

    public void decrementCurrentLive() {
        currentLive--;
    }
}
