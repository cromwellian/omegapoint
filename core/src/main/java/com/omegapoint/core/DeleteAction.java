package com.omegapoint.core;

import com.artemis.Entity;
import com.google.web.bindery.event.shared.EventBus;

/**
 *
 */
public class DeleteAction implements PredicateAction {
    @Override
    public void exec(EventBus eventBus, Entity... collisionEntities) {
        for (Entity e : collisionEntities) {
            e.delete();
        }
    }
}
