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
    public static final String NAME = "enemyComponent";

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new EnemyComponent(getType(), getHealth(), getPoints());
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

    public EnemyComponent(EnemyType type, int health, int points) {
        this.type = type;
        this.health = health;
        this.points = points;
    }

    public static class Codec implements Jsonable<EnemyComponent> {

        @Override
        public EnemyComponent fromJson(Json.Object object) {
          return new EnemyComponent(EnemyType.valueOf(object.getString("type").toUpperCase()), object.getInt("health"),
                  object.getInt("points"));
        }

        @Override
        public Json.Object toJson(EnemyComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("type", object.getType().name().toLowerCase());
            obj.put("health", object.getHealth());
            obj.put("points", object.getPoints());
            return obj;
        }
    }
}
