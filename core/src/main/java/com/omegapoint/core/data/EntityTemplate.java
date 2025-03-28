package com.omegapoint.core.data;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.ImmutableBag;
import com.omegapoint.core.components.BaseComponent;
import playn.core.Json;
import playn.core.PlayN;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a named entity template to be used to spawn a new
 * entity from a JSON template.
 */
public class EntityTemplate implements Jsonable<EntityTemplate> {
    public static final String NAME = "name";
    public static final String GROUP = "group";
    private Collection<BaseComponent> components = new ArrayList<BaseComponent>();
    private EntityDatabase.JsonableRegistry<BaseComponent> jsonableComponentRegistry;
    private String group;

    public EntityTemplate(EntityDatabase.JsonableRegistry<BaseComponent> jsonableComponentRegistry) {
        this.jsonableComponentRegistry = jsonableComponentRegistry;
    }

    public String getEntityName() {
        return entityName;
    }

    private String entityName;

    @Override
    public EntityTemplate fromJson(Json.Object object) {
        EntityTemplate template = new EntityTemplate(jsonableComponentRegistry);
        template.entityName = object.getString("name");
        template.group = object.getString("group");

        for (String key : object.keys()) {
            if (key.endsWith("Component")) {
                if (object.containsKey(key)) {
                    BaseComponent c = jsonableComponentRegistry.from(key, object.getObject(key));
                    addComponentIfNotNull(template, c);
                }
            }
        }
        return template;
    }

    private static void addComponentIfNotNull(EntityTemplate template, BaseComponent c) {
        if (c != null) {
            template.components.add(c);
        }
    }

    @Override
    public Json.Object toJson(EntityTemplate object) {
        Json.Object obj = PlayN.json().createObject();
        obj.put("name", entityName);
        for (BaseComponent c : components) {
            Json.Object json = jsonableComponentRegistry.toJson(c.getComponentName(), c);
            obj.put(c.getComponentName(), json);

        }
        if (object.getGroup() != null) {
            obj.put("group", object.getGroup());
        }
        return obj;
    }

    public void addComponentsToEntity(Entity entity) {
        for (BaseComponent c : components) {
            entity.addComponent(c.duplicate());
        }
        if (getGroup() != null) {
            entity.setGroup(getGroup());
        }
    }

    public String getGroup() {
        return group;
    }

    public void updateComponents(ImmutableBag<Component> replacements) {
        components.clear();
        for (int i = 0; i < replacements.size(); i++) {
            Component c = replacements.get(i);
            if (c instanceof BaseComponent) {
                components.add((BaseComponent) c);
            }
        }
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Collection<BaseComponent> getComponents() {
        return components;
    }
}
