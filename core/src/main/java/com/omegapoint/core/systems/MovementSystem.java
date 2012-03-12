package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.omegapoint.core.components.MovementComponent;
import com.omegapoint.core.components.PositionComponent;
import playn.core.PlayN;

/**
 *
 */
public class MovementSystem extends EntityProcessingSystem {
    private ComponentMapper<MovementComponent> movementMapper;
    private ComponentMapper<PositionComponent> positionMapper;

    public MovementSystem() {
        super(MovementComponent.class, PositionComponent.class);
    }

    public void initialize() {
        movementMapper = new ComponentMapper<MovementComponent>(MovementComponent.class, world);
        positionMapper = new ComponentMapper<PositionComponent>(PositionComponent.class, world);
    }

    @Override
    protected void process(Entity e) {
       MovementComponent move = movementMapper.get(e);
       PositionComponent pos = positionMapper.get(e);
       pos.setX(pos.getX() + move.getVx());
       if (move.getMoveType() == MovementComponent.MotionType.LINEAR) {
         pos.setY(pos.getY() + move.getVy());
       }  else {
         pos.setY((int) (Math.sin(pos.getX()/(double)PlayN.graphics().width() * 2*Math.PI) * PlayN.graphics().height()/2 + PlayN.graphics().height()/2));
       }
       pos.setDirty(true);
    }
}
