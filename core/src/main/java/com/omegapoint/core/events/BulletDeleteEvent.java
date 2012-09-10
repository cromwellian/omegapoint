package com.omegapoint.core.events;

import com.google.web.bindery.event.shared.Event;

/**
 * Event fired when a player bullet has been deleted from the world.
 */
public class BulletDeleteEvent extends Event<BulletDeleteHandler> {
    public static Type<BulletDeleteHandler> TYPE = new Type<BulletDeleteHandler>();

    @Override
    public Type<BulletDeleteHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(BulletDeleteHandler handler) {
        handler.onBulletDelete();
    }
}
