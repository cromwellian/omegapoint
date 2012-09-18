package com.omegapoint.core.predicates;

import com.artemis.Entity;
import com.artemis.World;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.components.BeamComponent;
import com.omegapoint.core.data.EntityTemplates;
import com.omegapoint.core.data.HasName;
import com.omegapoint.core.events.BulletDeleteEvent;
import playn.core.PlayN;

/**
*
*/
public class BulletCollisionPredicate implements CollisionPredicate, HasName {
    public static final String NAME = "bullet";

    public BulletCollisionPredicate() {
    }

    @Override
    public boolean collides(Entity e, Entity collidesWith, World world) {
       return "BOUNDS".equals(world.getGroupManager().getGroupOf(collidesWith)) ||
               "ENEMY".equals(world.getGroupManager().getGroupOf(collidesWith));
    }

    @Override
    public PredicateAction[] actions() {
        PredicateAction actions[] = new PredicateAction[1];
        actions[0] = new PredicateAction() {

            @Override
            public void exec(EventBus eventBus, World world, EntityTemplates templateManager, Entity... collisionEntities) {
                if ("ENEMY".equals(world.getGroupManager().getGroupOf(collisionEntities[1]))) {
                    if (collisionEntities[0].getComponent(BeamComponent.class) != null) {
                        // beams go right through enemies until they hit a wall!
                        return;
                    }
                }
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
