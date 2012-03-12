package com.omegapoint.core;

import com.artemis.Entity;

/**
 *
 */
public class DeleteAction implements PredicateAction {
    @Override
    public void exec(Entity... collisionEntities) {
        for (Entity e : collisionEntities) {
            e.delete();
        }
    }
}
