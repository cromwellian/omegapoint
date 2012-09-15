package com.omegapoint.core.components;

import com.omegapoint.core.data.Jsonable;
import playn.core.Json;
import playn.core.PlayN;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a wave of spawnable enemies.
 */
public class WaveComponent extends BaseComponent {

    public static final String NAME = "waveComponent";

    private List<SpawnComponent> spawnEntities = new ArrayList<SpawnComponent>();
    private String nextWave;
    private String nextState;

    private int curSpawn = 0;
    private double lastSpawned = -1;

    public int getCurSpawn() {
        return curSpawn;
    }

    public void setCurSpawn(int curSpawn) {
        this.curSpawn = curSpawn;
    }

    public double getLastSpawned() {
        return lastSpawned;
    }

    public void setLastSpawned(double lastSpawned) {
        this.lastSpawned = lastSpawned;
    }

    public List<SpawnComponent> getSpawnEntities() {
        return spawnEntities;
    }

    public String getNextWave() {
        return nextWave;
    }

    public String getNextState() {
        return nextState;
    }

    public WaveComponent(List<SpawnComponent> spawnEntities, String nextWave, String nextState) {
        this.spawnEntities = spawnEntities;
        this.nextWave = nextWave;
        this.nextState = nextState;
    }

    public boolean done() {
        return curSpawn >= spawnEntities.size();
    }

    public long getNextDelay() {
        return spawnEntities.get(curSpawn).getDelay();
    }

    public SpawnComponent dequeueNextSpawn() {
        SpawnComponent comp = spawnEntities.get(curSpawn++);
        lastSpawned = PlayN.currentTime();
        return comp;
    }

    public static class SpawnComponent extends BaseComponent {
        private String entityName;
        private int delay;

        public SpawnComponent(String entityName, int delay) {
            this.entityName = entityName;
            this.delay = delay;
        }

        public String getEntityName() {
            return entityName;
        }

        public int getDelay() {
            return delay;
        }

        @Override
        public String getComponentName() {
            return "spawnComponent";
        }

        @Override
        public SpawnComponent duplicate() {
            return new SpawnComponent(entityName, delay);
        }

        @Override
        public Json.Object toJson() {
            return new SpawnComponent.Codec().toJson(this);
        }

        public static class Codec implements Jsonable<SpawnComponent> {

            @Override
            public SpawnComponent fromJson(Json.Object object) {
                return new SpawnComponent(object.getString("spawnEntity"), object.getInt("delay"));
            }

            @Override
            public Json.Object toJson(SpawnComponent object) {
                Json.Object obj = PlayN.json().createObject();
                obj.put("spawnEntity", object.getEntityName());
                obj.put("delay", object.getDelay());
                return obj;
            }
        }
    }
    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new WaveComponent(spawnEntities, getNextWave(), getNextState());
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    public static class Codec implements Jsonable<WaveComponent> {

        @Override
        public WaveComponent fromJson(Json.Object object) {
          return new WaveComponent(decodeSpawnEntities(object.getArray("spawnEntities")),
                  object.getString("nextWave"), object.getString("nextstate"));
        }

        private List<SpawnComponent> decodeSpawnEntities(Json.Array spawnEntities) {
          List<SpawnComponent> spawns = new ArrayList<SpawnComponent>();
          for (int i = 0; i < spawnEntities.length(); i++) {
              spawns.add(new SpawnComponent.Codec().fromJson(spawnEntities.getObject(i)));
          }
            return spawns;
        }

        @Override
        public Json.Object toJson(WaveComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("spawnEntities", encodeSpawnEntities(object.getSpawnEntities()));
            obj.put("nextWave", object.getNextWave());
            obj.put("nextState", object.getNextState());
            return obj;
        }

        private Json.Array encodeSpawnEntities(List<SpawnComponent> spawnEntities) {
            Json.Array spawns = PlayN.json().createArray();
            for (SpawnComponent spawn : spawnEntities) {
                spawns.add(spawn.toJson());
            }
            return spawns;
        }
    }
}
