package com.omegapoint.core.predicates;

import com.artemis.Entity;
import com.artemis.World;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.data.EntityTemplates;

/**
 *
 */
public interface PredicateAction {
    void exec(EventBus eventBus, World world, EntityTemplates templateManager, Entity... collisionEntities);
}
