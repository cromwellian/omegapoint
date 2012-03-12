package com.omegapoint.core.components;

import com.artemis.Component;

/**
 *
 */
public class MovementComponent extends Component {
    public int getVy() {
        return vy;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    private int vx;
    private int vy;
    private MotionType moveType;

    public int getVx() {
        return vx;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public MotionType getMoveType() {
        return moveType;
    }

    public void setMoveType(MotionType moveType) {
        this.moveType = moveType;
    }

    public MovementComponent(int vx, int vy, MotionType moveType) {
        this.vx = vx;
        this.vy = vy;
        this.moveType = moveType;
    }

    public enum MotionType {LINEAR, SINUSOIDAL}
}
