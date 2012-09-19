package com.omegapoint.core.components;

import com.artemis.Component;
import playn.core.Json;
import playn.core.PlayN;

/**
 *
 */
public class AppearanceComponent extends BaseComponent {
    private static final String NAME = "appearanceComponent";
    int color;

    public AppearanceComponent(int color) {
        this.color = color;
    }

    public int getColor() {

        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new AppearanceComponent(color);
    }

    @Override
    public Json.Object toJson() {
        return PlayN.json().createObject();
    }
}
