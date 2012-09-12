package com.omegapoint.core.events;

/**
 * Handler for {@link com.omegapoint.core.events.BulletDeleteEvent} has been fired.
 */
public interface EnemyDeleteHandler {
    void onEnemyDelete(EnemyDeleteEvent event);
}
