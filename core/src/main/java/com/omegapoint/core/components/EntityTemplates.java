package com.omegapoint.core.components;


import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import playn.core.Json;
import playn.core.PlayN;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry of all loaded {@link EntityTemplate}s.
 */
public class EntityTemplates {
    private Map<String, EntityTemplate> templates = new HashMap<String, EntityTemplate>();
    private JsonableRegistry<BaseComponent> jsonableComponentRegistry;

    public void registerByName(String entityName, EntityTemplate entityTemplate) {
       templates.put(entityName, entityTemplate);
    }

    @Inject
    public EntityTemplates(JsonableRegistry<BaseComponent> jsonableComponentRegistry) {
        this.jsonableComponentRegistry = jsonableComponentRegistry;
    }

    public EntityTemplate lookup(String entityName) {
        return templates.get(entityName);
    }

    public void parseAndRegister(String entityTemplateJson) {
        Json.Object obj = PlayN.json().parse(entityTemplateJson);
        EntityTemplate template = new EntityTemplate(jsonableComponentRegistry);
        template = template.fromJson(obj);
        templates.put(template.getEntityName(), template);
    }

    public Entity lookupAndInstantiate(String templateName, World world) {
        Entity entity = world.createEntity();
        EntityTemplate template = lookup(templateName);
        template.addComponentsToEntity(entity);
        entity.refresh();
        return entity;
    }
}
