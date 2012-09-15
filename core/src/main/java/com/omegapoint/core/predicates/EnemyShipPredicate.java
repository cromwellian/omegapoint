package com.omegapoint.core.predicates;

import com.artemis.Entity;
import com.artemis.World;

/**
 *
 */
public class EnemyShipPredicate implements CollisionPredicate {

    @Override
    public boolean collides(Entity entity, Entity collidesWith, World world) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PredicateAction[] actions() {
        return new PredicateAction[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
