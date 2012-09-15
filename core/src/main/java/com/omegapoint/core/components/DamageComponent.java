package com.omegapoint.core.components;

import com.omegapoint.core.data.Jsonable;
import playn.core.Json;
import playn.core.PlayN;

/**
 *
 */
public class DamageComponent extends BaseComponent {
    public static final String NAME = "damageComponent";

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new DamageComponent();
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    public static class Codec implements Jsonable<DamageComponent> {

        @Override
        public DamageComponent fromJson(Json.Object object) {
            return new DamageComponent();
        }

        @Override
        public Json.Object toJson(DamageComponent object) {
            return PlayN.json().createObject();
        }
    }
}
