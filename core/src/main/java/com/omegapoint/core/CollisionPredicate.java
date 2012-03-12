package com.omegapoint.core;

import com.artemis.Entity;
import com.omegapoint.core.components.PositionComponent;

/**
 *
 */
public interface CollisionPredicate {
    boolean collides(Entity entity, Entity collidesWith);
    PredicateAction[] actions();
}
