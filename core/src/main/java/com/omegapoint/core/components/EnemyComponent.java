package com.omegapoint.core.components;

import com.artemis.Component;
import playn.core.Json;
import playn.core.PlayN;
import playn.core.Sound;

/**
 *
 */
public class EnemyComponent extends BaseComponent {

    private EnemyType type;
    private int health;
    public static final String NAME = "enemyComponent";

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new EnemyComponent(getType(), getHealth());
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
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

    public EnemyComponent(EnemyType type, int health) {
        this.type = type;
        this.health = health;
    }

    public static class Codec implements Jsonable<EnemyComponent> {

        @Override
        public EnemyComponent fromJson(Json.Object object) {
          return new EnemyComponent(EnemyType.valueOf(object.getString("type").toLowerCase()), object.getInt("health"));
        }

        @Override
        public Json.Object toJson(EnemyComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("type", object.getType().name().toLowerCase());
            obj.put("health", object.getHealth());
            return obj;
        }
    }
}
