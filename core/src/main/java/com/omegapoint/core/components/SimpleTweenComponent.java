package com.omegapoint.core.components;

import com.omegapoint.core.data.Jsonable;
import com.omegapoint.core.tween.TextColorChanger;
import playn.core.Json;
import playn.core.PlayN;

/**
 *
 */
public class SimpleTweenComponent extends BaseComponent {
    public static final String NAME = "tweenComponent";
    private float startVal;
    private float endVal;
    private int duration;
    private TextColorChanger propertyChanger;
    private boolean repeat;
    private boolean mirror;
    private int progress = 0;

    public float getEndVal() {
        return endVal;
    }

    public void setEndVal(float endVal) {
        this.endVal = endVal;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public TextColorChanger getPropertyChanger() {
        return propertyChanger;
    }

    public void setPropertyChanger(TextColorChanger propertyChanger) {
        this.propertyChanger = propertyChanger;
    }

    public float getStartVal() {

        return startVal;
    }

    public void setStartVal(float startVal) {
        this.startVal = startVal;
    }

    public SimpleTweenComponent(float startVal, float endVal, int duration, TextColorChanger propertyChanger,
                                boolean repeat,
                                boolean mirror) {
        this.startVal = startVal;
        this.endVal = endVal;
        this.duration = duration;
        this.propertyChanger = propertyChanger;
        this.repeat = repeat;
        this.mirror = mirror;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void update(int delta) {
        progress += delta;
        if (progress > duration) {
            progress = 0;
        }
        setDirty(true);

    }

    public float getValue() {
        float t = (float) progress / (float) duration;
        if (!mirror || t <= 0.5) {
          if (mirror) { t *= 2; }
          return Math.min(startVal + t * (endVal - startVal), endVal);
        } else {
          t = 0.5f - t;
          t *= 2;
          return Math.max(startVal + t * (endVal - startVal), startVal);
        }
    }

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new SimpleTweenComponent(startVal, endVal, duration, propertyChanger, repeat, mirror);
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    public static class Codec implements Jsonable<SimpleTweenComponent> {

        @Override
        public SimpleTweenComponent fromJson(Json.Object object) {
           return new SimpleTweenComponent(object.getNumber("start"), object.getNumber("end"),
                   object.getInt("duration"), new TextColorChanger.Codec().fromJson(object.getObject("operation")),
                   object.getBoolean("repeat"), object.getBoolean("mirror"));
        }

        @Override
        public Json.Object toJson(SimpleTweenComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("start", object.getStartVal());
            obj.put("end", object.getEndVal());
            obj.put("duration", object.getDuration());
            obj.put("operation", object.getPropertyChanger().toJson());
            obj.put("repeat", object.repeat);
            obj.put("mirror", object.mirror);
            return obj;
        }
    }
}
