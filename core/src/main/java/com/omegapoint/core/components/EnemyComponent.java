package com.omegapoint.core.components;

import com.artemis.Component;
import playn.core.PlayN;
import playn.core.Sound;

/**
 *
 */
public class EnemyComponent extends Component {

    private EnemyType type;
    private int health;

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
}
