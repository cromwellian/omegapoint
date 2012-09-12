package com.omegapoint.core;

import com.artemis.Entity;
import com.artemis.World;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.components.EntityTemplates;

/**
 *
 */
public class DeleteAction implements PredicateAction {
    @Override
    public void exec(EventBus eventBus, World world, EntityTemplates templateManager, Entity... collisionEntities) {
        for (Entity e : collisionEntities) {
            e.delete();
        }
    }
}
