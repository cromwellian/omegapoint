package com.omegapoint.core.components;

import com.omegapoint.core.data.Jsonable;
import playn.core.Json;
import playn.core.PlayN;

/**
 *
 */
public class EnemyComponent extends BaseComponent {

    private EnemyType type;
    private int health;
    private int points;
    private AiType aiType;
    private int shootDelay;
    private double lastShot = -1;
    private String shotEntity;

    public int getShootDelay() {
        return shootDelay;
    }

    public AiType getAiType() {
        return aiType;
    }

    public static final String NAME = "enemyComponent";

    public boolean isTimeToShoot() {
      if (lastShot == -1) {
          lastShot = PlayN.currentTime();
          return false;
      } else {
          return PlayN.currentTime() - lastShot > shootDelay;
      }
    }

    public void shot() {
        lastShot = PlayN.currentTime();
    }

    public String getShotEntity() {
        return shotEntity;
    }

    public enum AiType {
        NONE, SHOOT_LEFT, SHOOT_UP, SHOOT_DOWN, SHOOT_PLAYER;
    }

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new EnemyComponent(getType(), getHealth(), getPoints(), getAiType(), getShootDelay(),  getShotEntity());
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    public int getPoints() {
        return points;
    }

    public static enum EnemyType { BASIC }

    public EnemyType getType() {
        return type;
    }

    public void setType(EnemyType type) {
        this.type = type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public EnemyComponent(EnemyType type, int health, int points, AiType ai, int shootDelay, String shotEntity) {
        this.type = type;
        this.health = health;
        this.points = points;
        aiType = ai;
        this.shootDelay = shootDelay;
        this.shotEntity = shotEntity;
    }

    public static class Codec implements Jsonable<EnemyComponent> {

        @Override
        public EnemyComponent fromJson(Json.Object object) {
          return new EnemyComponent(EnemyType.valueOf(object.getString("type").toUpperCase()), object.getInt("health"),
                  object.getInt("points"),
                  AiType.valueOf(object.getString("ai").toUpperCase()),
                  object.getInt("shootDelay"), object.getString("shotEntity"));
        }

        @Override
        public Json.Object toJson(EnemyComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("type", object.getType().name().toLowerCase());
            obj.put("health", object.getHealth());
            obj.put("points", object.getPoints());
            obj.put("ai", object.getAiType().name().toLowerCase());
            obj.put("shootDelay", object.getShootDelay());
            obj.put("shotEntity", object.getShotEntity());
            return obj;
        }
    }
}
