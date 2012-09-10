package com.omegapoint.core;

import com.artemis.Entity;
import com.google.web.bindery.event.shared.EventBus;

/**
 *
 */
public interface PredicateAction {
    void exec(EventBus eventBus, Entity... collisionEntities);
}
