package com.omegapoint.core.events;

import com.google.web.bindery.event.shared.Event;

/**
 * Event fired when a enemy ship has been deleted from the world.
 */
public class EnemyDeleteEvent extends Event<EnemyDeleteHandler> {
    public static Type<EnemyDeleteHandler> TYPE = new Type<EnemyDeleteHandler>();

    @Override
    public Type<EnemyDeleteHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EnemyDeleteHandler handler) {
        handler.onEnemyDelete(this);
    }
}
