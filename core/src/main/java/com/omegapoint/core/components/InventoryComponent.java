package com.omegapoint.core.components;

import com.omegapoint.core.data.Jsonable;
import playn.core.Json;
import playn.core.PlayN;

/**
 * 
 */
public class InventoryComponent extends BaseComponent  {
    public static final String NAME = "inventoryComponent";

    private int resources;
    private int score;

    public int resources() {
        return resources;
    }

    public void setResources(int resources) {
        this.resources = resources;
    }

    public void addResources(int resources) {
        this.resources += resources;
    }

    public int score() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public InventoryComponent(int resources, int score) {
        this.resources = resources;
        this.score = score;
    }

    public static class Codec implements Jsonable<InventoryComponent> {
        public InventoryComponent fromJson(Json.Object object) {
            return new InventoryComponent(object.getInt("resources"), object.getInt("score"));
        }

        public Json.Object toJson(InventoryComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("resources", object.resources());
            obj.put("score", object.score());
            return obj;
        }
    }


    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new InventoryComponent(this.resources(), this.score());
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }
}
