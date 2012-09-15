package com.omegapoint.core.components;

import com.omegapoint.core.data.Jsonable;
import playn.core.Json;
import playn.core.PlayN;

/**
 *
 */
public class MovementComponent extends BaseComponent  {
    public static final String NAME = "moveComponent";

    public int getVy() {
        return vy;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    private int vx;
    private int vy;
    private MotionType moveType;
    private boolean wrapAround;

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

    public boolean isWrapAround() {
        return wrapAround;
    }

    public MovementComponent(int vx, int vy, MotionType moveType, boolean wrapAround) {
        this.vx = vx;
        this.vy = vy;
        this.moveType = moveType;
        this.wrapAround = wrapAround;
    }

    public static class Codec implements Jsonable<MovementComponent> {
        public MovementComponent fromJson(Json.Object object) {
            return new MovementComponent(object.getInt("vx"), object.getInt("vy"),
                    /*MotionType.valueOf(object.getString("motionType").toUpperCase())*/
                    "LINEAR".equalsIgnoreCase(object.getString("motionType")) ? MotionType.LINEAR : MotionType.SINUSOIDAL,
                    object.getBoolean("wrapAround"));
        }

        public Json.Object toJson(MovementComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("vx", object.getVx());
            obj.put("vy", object.getVy());
            obj.put("motionType", object.getMoveType().name().toLowerCase());
            obj.put("wrapAround", object.isWrapAround());
            return obj;
        }
    }


    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new MovementComponent(this.getVx(), this.getVy(), this.getMoveType(), this.isWrapAround());
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    public enum MotionType {LINEAR, SINUSOIDAL}
}
