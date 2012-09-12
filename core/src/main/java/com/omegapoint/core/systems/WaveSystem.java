package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.utils.ImmutableBag;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.CollisionPredicate;
import com.omegapoint.core.GameScreen;
import com.omegapoint.core.PredicateAction;
import com.omegapoint.core.components.*;
import playn.core.PlayN;

/**
 *
 */
public class WaveSystem extends EntitySystem {

    private ComponentMapper<EnemyComponent> enemyMapper;
    private long lastSpawn = 0;
    private int totalEnemies = 0;
    private static int MAX_ENEMIES = 5;

    public WaveSystem() {
        super(EnemyComponent.class, PositionComponent.class);
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
        for (int i = totalEnemies; i < MAX_ENEMIES; i++) {
            totalEnemies++;
            final Entity enemyEntity = world.createEntity();
            final PositionComponent posComp = new PositionComponent(PlayN.graphics().width() + i * 45, 0, -Math.PI / 2 * 3);
            enemyEntity.addComponent(posComp);
            enemyEntity.addComponent(new SpriteComponent("images/tarentula.png", 60, 60, 10, 4, 0, -1, false));
            final MovementComponent movementComponent = new MovementComponent(-10, 0, MovementComponent.MotionType.SINUSOIDAL, false);
            enemyEntity.addComponent(movementComponent);
            enemyEntity.setGroup("ENEMY");

            enemyEntity.addComponent(new CollisionComponent(0, 0, 72, 72, new CollisionPredicate() {

                @Override
                public boolean collides(Entity entity, Entity collidesWith, World world) {
                    boolean xx = collidesWith.getComponent(DamageComponent.class) != null || "BOUNDS".equals(WaveSystem.this.world.getGroupManager().getGroupOf(collidesWith));
                    return xx;
                }

                @Override
                public PredicateAction[] actions() {
                    return new PredicateAction[]{new PredicateAction() {
                        @Override
                        public void exec(EventBus eventBus, World world, EntityTemplates templateManager, Entity... collisionEntities) {
                            if ("BOUNDS".equals(WaveSystem.this.world.getGroupManager().getGroupOf(collisionEntities[1]))) {
                                if (posComp.getX() <= -100) {
                                    enemyEntity.delete();
                                    totalEnemies--;
                                }
                            } else {
                                enemyEntity.delete();
                                Entity explosionEntity = WaveSystem.this.world.createEntity();
                                explosionEntity.addComponent(posComp);
                                explosionEntity.addComponent(new SpriteComponent("images/explode2.png", 80, 80, 4, 11, 0, 120, false));
                                explosionEntity.addComponent(movementComponent);
                                explosionEntity.addComponent(new AudioComponent("sounds/bomb"));
                                explosionEntity.refresh();
                                collisionEntities[1].delete();
                                totalEnemies--;
                                numKilled++;
                                if (numKilled % 5 == 0) {
                                    GameScreen.useBeam = !GameScreen.useBeam;
                                }
                            }
                        }
                    }};
                }
            }));
            enemyEntity.refresh();
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
