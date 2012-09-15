package com.omegapoint.core.predicates;

import com.artemis.Entity;
import com.artemis.World;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.data.EntityTemplates;

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
