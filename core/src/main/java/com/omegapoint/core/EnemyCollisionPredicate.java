package com.omegapoint.core;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.components.*;
import com.omegapoint.core.events.EnemyDeleteEvent;
import com.omegapoint.core.events.EnemyKilledEvent;

/**
 *
 */
public class EnemyCollisionPredicate implements CollisionPredicate, HasName {


    public static final String NAME = "enemy";

    @Override
    public boolean collides(Entity entity, Entity collidesWith, World world) {
        boolean xx = collidesWith.getComponent(DamageComponent.class) != null || "BOUNDS".equals(world.getGroupManager().getGroupOf(collidesWith));
        return xx;
    }

    @Override
    public PredicateAction[] actions() {
        return new PredicateAction[]{new PredicateAction() {
            @Override
            public void exec(EventBus eventBus, World world, EntityTemplates templateManager, Entity... collisionEntities) {
                if ("BOUNDS".equals(world.getGroupManager().getGroupOf(collisionEntities[1]))) {
                    PositionComponent pos = new ComponentMapper<PositionComponent>(PositionComponent.class, world).get(collisionEntities[0]);
                    if (pos.getX() <= -100) {
                        collisionEntities[0].delete();
                        eventBus.fireEvent(new EnemyDeleteEvent());
                    }
                } else {
                    collisionEntities[0].delete();
                    Entity explosionEntity = templateManager.lookupAndInstantiate("explosion", world);
                    // explosion starts with same speed and position of the object that blew up
                    PositionComponent pos = new ComponentMapper<PositionComponent>(PositionComponent.class, world).get(collisionEntities[0]);
                    MovementComponent mov = new ComponentMapper<MovementComponent>(MovementComponent.class, world).get(collisionEntities[0]);

                    explosionEntity.addComponent(pos);
                    explosionEntity.addComponent(mov);
                    explosionEntity.refresh();
                    collisionEntities[1].delete();
                    // let game compute score or other actions
                    eventBus.fireEvent(new EnemyKilledEvent());
                    // let game update state of # of enemies
                    eventBus.fireEvent(new EnemyDeleteEvent());
                }
            }
        }};
    }

    @Override
    public String getName() {
        return NAME;
    }
}
