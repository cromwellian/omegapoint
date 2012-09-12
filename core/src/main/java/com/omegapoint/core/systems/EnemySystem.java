package com.omegapoint.core.systems;

import com.artemis.*;
import com.artemis.utils.ImmutableBag;
import com.omegapoint.core.Enemies;
import com.omegapoint.core.EnemyCollisionPredicate;
import com.omegapoint.core.components.CollisionComponent;
import com.omegapoint.core.components.*;
import playn.core.PlayN;

import javax.inject.Inject;

/**
 *
 */
public class EnemySystem extends EntitySystem {

    private ComponentMapper<EnemyComponent> enemyMapper;
    private long lastSpawn = 0;
    private Enemies enemies;
    private EntityTemplates templateManager;

    @Inject
    public EnemySystem(Enemies enemies, EntityTemplates templateManager) {
        super(EnemyComponent.class, PositionComponent.class);
        this.enemies = enemies;
        this.templateManager = templateManager;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        if (System.currentTimeMillis() - lastSpawn > 5000) {
            spawnEnemies();
            lastSpawn = System.currentTimeMillis();
        } else {
            for (int i = 0, s = entities.size(); s > i; i++) {

            }
        }

    }

    int numKilled = 0;

    private void spawnEnemies() {
        for (int i = enemies.currentLiveEnemies(); i < Enemies.MAX_ENEMIES; i++) {
            enemies.incrementLiveEnemies();
            final Entity enemyEntity = templateManager.lookupAndInstantiate("enemy1", world);
            final PositionComponent posComp = new PositionComponent(PlayN.graphics().width() + i * 45, 0, -Math.PI / 2 * 3);
            enemyEntity.addComponent(posComp);
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void initialize() {
        enemyMapper = new ComponentMapper<EnemyComponent>(EnemyComponent.class, world);
    }


}
