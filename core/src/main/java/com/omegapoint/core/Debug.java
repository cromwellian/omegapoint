package com.omegapoint.core;

/**
 *
 */
public class Debug {
    // Allow VMs and GWT to compile out
    public static final boolean DEBUG = true;

    public static boolean collisionBoundingBoxes = false;
    private static boolean frameRateEnabled;
    private static boolean showFrameRate;

    public static boolean isCollisionBoundingBoxesEnabled() {
        if (DEBUG) {
            return collisionBoundingBoxes;
        }
        return false;
    }

    public static void setCollisionBoundingBoxes(boolean collisionBoundingBoxes) {
        if (DEBUG) {
            Debug.collisionBoundingBoxes = collisionBoundingBoxes;
        }
    }

    public static boolean isDebugEnabled() {
        return DEBUG;
    }

    public static boolean isShowFrameRateEnabled() {
        if (DEBUG) {
            return showFrameRate;
        }
        return false;
    }

    public static void setShowFrameRate(boolean showFrameRate) {
        if (DEBUG) {
            Debug.showFrameRate = showFrameRate;
        }
    }
}
