package com.omegapoint.core.data;

import playn.core.Json;
import playn.core.PlayN;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A source of {@link EntityTemplate}s to loaded into the registry, or persisted to.
 */
public interface EntityDatabase {
    Collection<String> getTemplates();
    void persist(Collection<EntityTemplate> toBePersisted);

    /**
     *
     */
    class JsonableRegistry<T> {
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
}
