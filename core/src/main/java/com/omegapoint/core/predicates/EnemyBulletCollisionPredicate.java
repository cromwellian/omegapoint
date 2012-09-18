package com.omegapoint.core.predicates;

import com.artemis.Entity;
import com.artemis.World;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.components.BeamComponent;
import com.omegapoint.core.data.EntityTemplates;
import com.omegapoint.core.data.HasName;
import com.omegapoint.core.events.BulletDeleteEvent;

/**
*
*/
public class EnemyBulletCollisionPredicate implements CollisionPredicate, HasName {
    public static final String NAME = "enemybullet";

    public EnemyBulletCollisionPredicate() {
    }

    @Override
    public boolean collides(Entity e, Entity collidesWith, World world) {
       return "BOUNDS".equals(world.getGroupManager().getGroupOf(collidesWith)) ||
               "PLAYER".equals(world.getGroupManager().getGroupOf(collidesWith));
    }

    @Override
    public PredicateAction[] actions() {
        PredicateAction actions[] = new PredicateAction[1];
        actions[0] = new PredicateAction() {

            @Override
            public void exec(EventBus eventBus, World world, EntityTemplates templateManager, Entity... collisionEntities) {
                collisionEntities[0].delete();
                eventBus.fireEvent(new BulletDeleteEvent());
            }
        };
        return actions;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
