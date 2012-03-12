package com.omegapoint.core;

import com.artemis.Entity;

/**
 *
 */
public interface PredicateAction {
    void exec(Entity... collisionEntities);
}
