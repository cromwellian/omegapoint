package com.omegapoint.core;

/**
 * Counter of number of bullets in flight.
 */
public class Bullets {
    private static int bullets;
    private static int MAX_BULLETS = 5;

    public static void dec() {
        bullets--;
    }

    public static void inc() {
        bullets++;
    }

    public static boolean maxedOut() {
        return bullets >= MAX_BULLETS;
    }

    public static void reset() {
        bullets = 0;
    }
}
