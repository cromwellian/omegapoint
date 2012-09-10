package com.omegapoint.core.components;

import com.artemis.Component;
import playn.core.Color;
import playn.core.Json;
import playn.core.PlayN;

/**
 *
 */
public class BeamComponent extends BaseComponent {
    private int finalLength;
    private int strobe = 0;
    public static final String NAME = "beamComponent";

    public int strobe() {
        int s = strobe;
        strobe = (strobe + 1) % 10;
        return s;
    }

    public int getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(int currentLength) {
        this.currentLength = currentLength;
    }

    private int color;

    private int currentLength;

    public int getFinalLength() {
        return finalLength;
    }

    public void setFinalLength(int finalLength) {
        this.finalLength = finalLength;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public BeamComponent(int finalLength, int color) {
        this.finalLength = finalLength;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeamComponent that = (BeamComponent) o;

        if (color != that.color) return false;
        if (finalLength != that.finalLength) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = finalLength;
        result = 31 * result + color;
        return result;
    }

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new BeamComponent(this.finalLength, this.color);
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    public static class Codec implements Jsonable<BeamComponent> {

        @Override
        public BeamComponent fromJson(Json.Object object) {
            return new BeamComponent(object.getInt("finalLength"),
                    (int) Long.parseLong(object.getString("color").substring(2), 16));
        }

        @Override
        public Json.Object toJson(BeamComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("finalLength", object.getFinalLength());
            obj.put("color", "0x" + Integer.toHexString(object.getColor()));
            return obj;
        }
    }
}
