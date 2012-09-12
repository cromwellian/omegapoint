package com.omegapoint.core.events;

/**
 * Handler for {@link BulletDeleteEvent} has been fired.
 */
public interface EnemyKilledHandler {
    void onEnemyKilled(EnemyKilledEvent event);
}
