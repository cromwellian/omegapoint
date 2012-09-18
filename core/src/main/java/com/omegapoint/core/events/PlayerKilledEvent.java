package com.omegapoint.core.events;

import com.google.web.bindery.event.shared.Event;

/**
 * Event fired when a playerhas been killed.
 */
public class PlayerKilledEvent extends Event<PlayerKilledHandler> {
    public static Type<PlayerKilledHandler> TYPE = new Type<PlayerKilledHandler>();

    @Override
    public Type<PlayerKilledHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PlayerKilledHandler handler) {
        handler.onPlayerKilled(this);
    }
}
