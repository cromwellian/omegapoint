package com.omegapoint.core.data;


import com.artemis.Entity;
import com.artemis.World;
import com.omegapoint.core.components.BaseComponent;
import com.omegapoint.core.data.EntityDatabase;
import com.omegapoint.core.data.EntityTemplate;
import playn.core.Json;
import playn.core.PlayN;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry of all loaded {@link com.omegapoint.core.data.EntityTemplate}s.
 */
public class EntityTemplates {
    private Map<String, EntityTemplate> templates = new HashMap<String, EntityTemplate>();
    private EntityDatabase.JsonableRegistry<BaseComponent> jsonableComponentRegistry;

    public void registerByName(String entityName, EntityTemplate entityTemplate) {
       templates.put(entityName, entityTemplate);
    }

    @Inject
    public EntityTemplates(EntityDatabase.JsonableRegistry<BaseComponent> jsonableComponentRegistry) {
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
