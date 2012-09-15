package com.omegapoint.core.predicates;

import com.artemis.Entity;
import com.artemis.World;

/**
 *
 */
public interface CollisionPredicate {
    boolean collides(Entity entity, Entity collidesWith, World world);
    PredicateAction[] actions();
}
