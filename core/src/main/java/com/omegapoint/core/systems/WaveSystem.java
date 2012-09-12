package com.omegapoint.core.systems;

import com.artemis.*;
import com.artemis.utils.ImmutableBag;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.Enemies;
import com.omegapoint.core.EnemyCollisionPredicate;
import com.omegapoint.core.components.CollisionComponent;
import com.omegapoint.core.components.*;
import com.omegapoint.core.events.ChangeStateEvent;
import playn.core.PlayN;

import javax.inject.Inject;

/**
 *
 */
public class WaveSystem extends EntityProcessingSystem {

    private ComponentMapper<WaveComponent> waveMapper;
    private ComponentMapper<PositionComponent> posMapper;

    private long lastSpawn = 0;
    private Enemies enemies;
    private EntityTemplates templateManager;
    private EventBus eventBus;

    @Inject
    public WaveSystem(Enemies enemies, EntityTemplates templateManager, EventBus eventBus) {
        super(WaveComponent.class);
        this.enemies = enemies;
        this.templateManager = templateManager;
        this.eventBus = eventBus;
    }

//    @Override
//    protected void processEntities(ImmutableBag<Entity> entities) {
//        if (System.currentTimeMillis() - lastSpawn > 5000) {
//            spawnEnemies();
//            lastSpawn = System.currentTimeMillis();
//        } else {
//            for (int i = 0, s = entities.size(); s > i; i++) {
//
//            }
//        }
//
//    }

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
    protected void process(Entity e) {
      WaveComponent wave = waveMapper.get(e);
      if (wave != null) {
          if (!wave.done()) {
            if (PlayN.currentTime() - wave.getLastSpawned() > wave.getNextDelay()) {
               WaveComponent.SpawnComponent spawnComponent = wave.dequeueNextSpawn();
               Entity spawned = templateManager.lookupAndInstantiate(spawnComponent.getEntityName(), world);
               PositionComponent pos = posMapper.get(spawned);
               // relative coordinates 0 .. 100%
               pos.setX(pos.getX() * PlayN.graphics().width() / 100);
               pos.setY(pos.getY() * PlayN.graphics().height() / 100);
            }
            wave.setDirty(true);
          } else {
              if (wave.getNextWave() != null) {
                  templateManager.lookupAndInstantiate(wave.getNextWave(), world);
              }
              if (wave.getNextState() != null) {
                 eventBus.fireEvent(new ChangeStateEvent(wave.getNextState()));
              }
              e.delete();
          }

      }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void initialize() {
        waveMapper = new ComponentMapper<WaveComponent>(WaveComponent.class, world);
        posMapper = new ComponentMapper<PositionComponent>(PositionComponent.class, world);
    }


}
