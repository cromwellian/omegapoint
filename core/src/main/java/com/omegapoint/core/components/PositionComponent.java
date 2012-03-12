package com.omegapoint.core.components;

import com.artemis.Component;

/**
 *
 */
public class PositionComponent extends Component {
    private int x;
    private int y;
    private double angle;

    public double getAngle() {
        return angle;
    }

    public PositionComponent(int x, int y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void rotateLeft() {
        angle = angle + Math.PI/50;
    }

    public void moveDiagonal() {
        x += 2;
        y += 2;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "PositionComponent{" +
                "x=" + x +
                ", y=" + y +
                ", angle=" + angle +
                '}';
    }

    public void setY(int y) {
        this.y = y;
    }
}
