package com.omegapoint.core.tween;

import com.artemis.Entity;
import com.artemis.World;
import com.omegapoint.core.components.Jsonable;
import com.omegapoint.core.components.TextComponent;
import playn.core.Json;
import playn.core.PlayN;

/**
 *
 */
public class TextColorChanger {
    private int start;
    private int end;
    private int a1;
    private int r1;
    private int g1;
    private int b1;
    private int a2;
    private int r2;
    private int g2;
    private int b2;

    public TextColorChanger(int start, int end) {
        this.start = start;
        a1 = start >> 24 & 0xff;
        r1 = start >> 16 & 0xff;
        g1 = start >> 8 & 0xff;
        b1 = start & 0xff;
        this.end = end;
        a2 = end >> 24 & 0xff;
        r2 = end >> 16 & 0xff;
        g2 = end >> 8 & 0xff;
        b2 = end & 0xff;
    }

    public void change(World world, float value, Entity e) {
        TextComponent comp = e.getComponent(TextComponent.class);
        int a = (int) (a1 + (a2 - a1) * value);
        int r = (int) (r1 + (r2 - r1) * value);
        int g = (int) (g1 + (g2 - g1) * value);
        int b = (int) (b1 + (b2 - b1) * value);
        comp.setColor(a << 24 | r << 16 | g << 8 | b);
        comp.setDirty(true);
    }

    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    public static class Codec implements Jsonable<TextColorChanger> {
        @Override
        public TextColorChanger fromJson(Json.Object object) {
            return new TextColorChanger(object.getInt("startColor"), object.getInt("endColor"));
        }

        @Override
        public Json.Object toJson(TextColorChanger object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("name", "colorChanger");
            obj.put("startColor", object.start);
            obj.put("endColor", object.end);
            return obj;
        }
    }
}
