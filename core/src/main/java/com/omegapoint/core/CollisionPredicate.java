package com.omegapoint.core;

import com.artemis.Entity;
import com.artemis.World;
import com.omegapoint.core.components.PositionComponent;

/**
 *
 */
public interface CollisionPredicate {
    boolean collides(Entity entity, Entity collidesWith, World world);
    PredicateAction[] actions();
}
