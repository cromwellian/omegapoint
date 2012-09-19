package com.omegapoint.core.data;


import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.omegapoint.core.components.BaseComponent;
import com.omegapoint.core.data.EntityDatabase;
import com.omegapoint.core.data.EntityTemplate;
import com.omegapoint.core.util.JsonUtil;
import playn.core.AssetWatcher;
import playn.core.Json;
import playn.core.PlayN;
import playn.core.util.Callback;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry of all loaded {@link com.omegapoint.core.data.EntityTemplate}s.
 */
public class EntityTemplates {
    private Map<String, EntityTemplate> templates = new HashMap<String, EntityTemplate>();
    private EntityDatabase persistenceDatabase;
    private EntityDatabase.JsonableRegistry<BaseComponent> jsonableComponentRegistry;

    public void registerByName(String entityName, EntityTemplate entityTemplate) {
       templates.put(entityName, entityTemplate);
    }

    @Inject
    public EntityTemplates(EntityDatabase persistenceDatabase,
                           EntityDatabase.JsonableRegistry<BaseComponent> jsonableComponentRegistry) {
        this.persistenceDatabase = persistenceDatabase;
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

    public void persist(String name, World world, Entity e) {
        EntityTemplate template = templates.get(name);
        template.setGroup(world.getGroupManager().getGroupOf(e));
        template.updateComponents(e.getComponents());
        PlayN.log().debug("Persist " + name + ":" + JsonUtil.toString(template.toJson(template)));
        persistenceDatabase.persist(templates.values());
    }

    public void waitForAllAssets(AssetWatcher watcher) {
        for (EntityTemplate template : templates.values()) {
          for (Component c : template.getComponents()) {
            if (c instanceof BaseComponent) {
                ((BaseComponent) c).addAssetsToWatcher(watcher);
            }
          }
        }
        watcher.start();
    }

    public EntityDatabase.JsonableRegistry<BaseComponent> getJsonableComponentRegistry() {
        return jsonableComponentRegistry;
    }
}
