package com.omegapoint.core.events;

/**
 * Handler for {@link com.omegapoint.core.events.BulletDeleteEvent} has been fired.
 */
public interface ChangeStateHandler {
    void onStateChange(ChangeStateEvent changeStateEvent);
}
