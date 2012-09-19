package com.omegapoint.core.achievements;

/**
*
*/
public interface Achievement {
    boolean conditionSatisfied();
    String getDisplayMessage();
    String achievementId();
}
