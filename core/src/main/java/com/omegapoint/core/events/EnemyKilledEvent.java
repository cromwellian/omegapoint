package com.omegapoint.core.events;

import com.google.web.bindery.event.shared.Event;

/**
 * Event fired when a player bullet has been deleted from the world.
 */
public class EnemyKilledEvent extends Event<EnemyKilledHandler> {
    public static Type<EnemyKilledHandler> TYPE = new Type<EnemyKilledHandler>();

    @Override
    public Type<EnemyKilledHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EnemyKilledHandler handler) {
        handler.onEnemyKilled(this);
    }
}
