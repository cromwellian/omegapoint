package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.components.CollisionComponent;
import com.omegapoint.core.predicates.CollisionPredicate;
import com.omegapoint.core.predicates.PredicateAction;
import com.omegapoint.core.data.EntityTemplates;
import com.omegapoint.core.components.PositionComponent;
import pythagoras.i.Rectangle;

import javax.inject.Inject;

/**
 *
 */
public class CollisionSystem extends EntitySystem {
    private ComponentMapper<CollisionComponent> collisionMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private EventBus eventBus;
    private EntityTemplates templateManager;

    @Inject
    public CollisionSystem(EventBus eventBus, EntityTemplates templateManager) {
        super(CollisionComponent.class, PositionComponent.class);
        this.eventBus = eventBus;
        this.templateManager = templateManager;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
      for (int i = 0; i < entities.size(); i++) {
          Entity e = entities.get(i);
          CollisionComponent colComp = collisionMapper.get(e);
          PositionComponent pos = positionMapper.get(e);
          Rectangle rect1 = colComp.getBounds().clone();
          rect1.translate(pos.getX() - colComp.getBounds().width/2 , pos.getY()  - colComp.getBounds().height/2);
          
          for (int j = 0; j < entities.size(); j++) {
              if (i != j) {
                  Entity e2 = entities.get(j);
                  CollisionComponent colComp2 = collisionMapper.get(e2);
                  PositionComponent pos2 = positionMapper.get(e2);
                  Rectangle rect2 = colComp2.getBounds().clone();
                  rect2.translate(pos2.getX() - colComp2.getBounds().width/2, pos2.getY() - colComp2.getBounds().height/2);

                  if (rect1.intersects(rect2)) {
                      for (CollisionPredicate cp : colComp.getPredicates()) {
                          if (cp.collides(e, e2, world)) {
                              for (PredicateAction action : cp.actions()) {
                                  action.exec(eventBus, world, templateManager, e, e2);
                              }
                          }
                      }
                      for (CollisionPredicate cp : colComp2.getPredicates()) {
                          if (cp.collides(e, e2, world)) {
                              for (PredicateAction action : cp.actions()) {
                                  action.exec(eventBus, world, templateManager, e2, e);
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
