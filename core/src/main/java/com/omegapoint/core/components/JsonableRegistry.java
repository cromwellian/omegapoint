package com.omegapoint.core.components;

import com.artemis.Component;
import playn.core.Json;
import playn.core.PlayN;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class JsonableRegistry<T> {
    private Map<String, Jsonable<T>> jsonables = new HashMap<String, Jsonable<T>>();
    public T fromJson(String name, String json) {
        Json.Object obj = PlayN.json().parse(json);
        return from(name, obj);
    }

    public T from(String name, Json.Object object) {
        return jsonables.get(name).fromJson(object);
    }

    public void register(String name, Jsonable codec) {
       jsonables.put(name, codec);
    }

    public Json.Object toJson(String componentName, T component) {
        return jsonables.get(componentName).toJson(component);
    }
}
