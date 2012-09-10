package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.omegapoint.core.components.MovementComponent;
import com.omegapoint.core.components.PositionComponent;
import com.omegapoint.core.components.SimpleTweenComponent;
import com.omegapoint.core.components.TextComponent;
import playn.core.PlayN;

/**
 *
 */
public class SimpleTweenSystem extends EntityProcessingSystem {
    private ComponentMapper<MovementComponent> movementMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<TextComponent> textMapper;
    private ComponentMapper<SimpleTweenComponent> tweenMapper;

    public SimpleTweenSystem() {
        super(SimpleTweenComponent.class);
    }

    public void initialize() {
        positionMapper = new ComponentMapper<PositionComponent>(PositionComponent.class, world);
        textMapper = new ComponentMapper<TextComponent>(TextComponent.class, world);
        tweenMapper = new ComponentMapper<SimpleTweenComponent>(SimpleTweenComponent.class, world);
    }

    @Override
    protected void process(Entity e) {
       SimpleTweenComponent tweenComp = tweenMapper.get(e);
       tweenComp.update(world.getDelta());
       tweenComp.getPropertyChanger().change(world, tweenComp.getValue(), e);
    }
}
