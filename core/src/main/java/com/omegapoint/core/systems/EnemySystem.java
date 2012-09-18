package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.artemis.World;
import com.omegapoint.core.components.EnemyComponent;
import com.omegapoint.core.components.MovementComponent;
import com.omegapoint.core.components.PositionComponent;
import com.omegapoint.core.data.EntityTemplates;
import playn.core.PlayN;

import javax.inject.Inject;

/**
 *
 */
public class EnemySystem extends EntityProcessingSystem {
    private final EntityTemplates templateManager;
    private final World world;
    private ComponentMapper<MovementComponent> movementMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<EnemyComponent> enemyMapper;

    @Inject
    public EnemySystem(EntityTemplates templateManager, World world) {
        super(EnemyComponent.class, MovementComponent.class, PositionComponent.class);
        this.templateManager = templateManager;
        this.world = world;
    }

    public void initialize() {
        enemyMapper = new ComponentMapper<EnemyComponent>(EnemyComponent.class, world);
        movementMapper = new ComponentMapper<MovementComponent>(MovementComponent.class, world);
        positionMapper = new ComponentMapper<PositionComponent>(PositionComponent.class, world);
    }

    @Override
    protected void process(Entity e) {
        MovementComponent move = movementMapper.get(e);
        PositionComponent pos = positionMapper.get(e);
        EnemyComponent enemy = enemyMapper.get(e);
        enemy.setDirty(true);
        if (enemy.getAiType() != EnemyComponent.AiType.NONE && enemy.getShootDelay() != 0 &&
                enemy.isTimeToShoot()) {
            Entity shot = templateManager.lookupAndInstantiate(enemy.getShotEntity(), world);
            shot.addComponent(new MovementComponent(-10, 0, MovementComponent.MotionType.LINEAR, false));
            shot.addComponent(new PositionComponent(pos.getX(), pos.getY(), 0));
            shot.refresh();
            enemy.shot();
        }
    }
}
