package com.omegapoint.core.components;

import com.artemis.Component;
import playn.core.Json;
import playn.core.PlayN;

/**
 *
 */
public class StarComponent extends BaseComponent {
    private static final String NAME = "starComponent";
    int starBrightness;

    public StarComponent(int starBrightness) {
        this.starBrightness = starBrightness;
    }

    public int getStarBrightness() {

        return starBrightness;
    }

    public void setStarBrightness(int starBrightness) {
        this.starBrightness = starBrightness;
    }

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new StarComponent(starBrightness);
    }

    @Override
    public Json.Object toJson() {
        return PlayN.json().createObject();
    }
}
