package com.omegapoint.core.events;

/**
 * Handler for {@link com.omegapoint.core.events.PlayerKilledEvent} has been fired.
 */
public interface PlayerKilledHandler {
    void onPlayerKilled(PlayerKilledEvent event);
}
