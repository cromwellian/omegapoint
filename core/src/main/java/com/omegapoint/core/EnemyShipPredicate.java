package com.omegapoint.core;

import com.artemis.Entity;
import com.omegapoint.core.components.EnemyComponent;
import com.omegapoint.core.components.PositionComponent;

/**
 *
 */
public class EnemyShipPredicate implements CollisionPredicate {

    @Override
    public boolean collides(Entity entity, Entity collidesWith) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PredicateAction[] actions() {
        return new PredicateAction[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
