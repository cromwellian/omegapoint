package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.omegapoint.core.CollisionComponent;
import com.omegapoint.core.CollisionPredicate;
import com.omegapoint.core.PredicateAction;
import com.omegapoint.core.components.AudioComponent;
import com.omegapoint.core.components.PositionComponent;
import com.omegapoint.core.components.SpriteComponent;
import playn.core.Sound;
import pythagoras.i.Rectangle;

/**
 *
 */
public class CollisionSystem extends EntitySystem {
    private ComponentMapper<CollisionComponent> collisionMapper;
    private ComponentMapper<PositionComponent> positionMapper;

    public CollisionSystem() {
        super(CollisionComponent.class, PositionComponent.class);
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
      for (int i = 0; i < entities.size(); i++) {
          Entity e = entities.get(i);
          CollisionComponent colComp = collisionMapper.get(e);
          PositionComponent pos = positionMapper.get(e);
          Rectangle rect1 = colComp.getBounds().clone();
          rect1.translate(pos.getX(), pos.getY());
          
          for (int j = 0; j < entities.size(); j++) {
              if (i != j) {
                  Entity e2 = entities.get(j);
                  CollisionComponent colComp2 = collisionMapper.get(e2);
                  PositionComponent pos2 = positionMapper.get(e2);
                  Rectangle rect2 = colComp2.getBounds().clone();
                  rect2.translate(pos2.getX(), pos2.getY());

                  if (rect1.intersects(rect2)) {
                      for (CollisionPredicate cp : colComp.getPredicates()) {
                          if (cp.collides(e, e2)) {
                              for (PredicateAction action : cp.actions()) {
                                  action.exec(e, e2);
                              }
                          }
                      }
                      for (CollisionPredicate cp : colComp2.getPredicates()) {
                          if (cp.collides(e, e2)) {
                              for (PredicateAction action : cp.actions()) {
                                  action.exec(e2, e);
                              }
                          }
                      }
                  }
              }
          }
      }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    public void initialize() {
        collisionMapper = new ComponentMapper<CollisionComponent>(CollisionComponent.class, world);
        positionMapper = new ComponentMapper<PositionComponent>(PositionComponent.class, world);

    }
}
