package com.omegapoint.core.components;

import com.omegapoint.core.data.Jsonable;
import playn.core.Json;
import playn.core.PlayN;

/**
 *
 */
public class PositionComponent extends BaseComponent {
    private int x;
    private int y;
    private double angle;
    public static final String NAME = "positionComponent";

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

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new PositionComponent(this.getX(), this.getY(), this.getAngle());
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public static class Codec implements Jsonable<PositionComponent> {

        @Override
        public PositionComponent fromJson(Json.Object object) {
            return new PositionComponent(object.getInt("x"), object.getInt("y"), object.getDouble("angle"));
        }

        @Override
        public Json.Object toJson(PositionComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("x", object.getX());
            obj.put("y", object.getY());
            obj.put("angle", object.getAngle());
            return obj;
        }
    }
}
