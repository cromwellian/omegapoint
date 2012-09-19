package com.omegapoint.core.util;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.ImmutableBag;
import com.omegapoint.core.components.BaseComponent;
import com.omegapoint.core.data.EntityTemplate;
import com.omegapoint.core.data.EntityTemplates;
import playn.core.Json;
import playn.core.PlayN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class WorldUtil {
    public static String serialize(World world, Class<?>... includedComponents) {
        StringBuffer json = new StringBuffer();
        Set<Class<?>> includes = new HashSet<Class<?>>(Arrays.asList(includedComponents));

        json.append("{\"world\": [");
        boolean first = true;
        for (Entity e : world.getEntityManager().getAllActiveEntities()) {
            Json.Object entity = PlayN.json().createObject();
            entity.put("id", e.getId());
            entity.put(EntityTemplate.GROUP, world.getGroupManager().getGroupOf(e));
            ImmutableBag<Component> components = e.getComponents();
            for (int i = 0; i < components.size(); i++) {

                Component component = components.get(i);
                if (includes.contains(component) && component instanceof BaseComponent) {
                    BaseComponent bc = (BaseComponent) component;
                    if (bc != null && !includes.contains(bc.getClass())) {
                        entity.put(bc.getComponentName(), bc.toJson());
                    }
                }
            }
            if (first) {
                first = false;
            } else {
                json.append(",");
            }
            json.append(JsonUtil.toString(entity));

        }
        json.append("]}");
        PlayN.log().debug("WORLD: " + json);
        return json.toString();
    }

    public static World deserialize(String json, EntityTemplates templateManager) {
       World world = new World();
       Json.Object worldObject = PlayN.json().parse(json);
       Json.Array entities = worldObject.getArray("world");
       for (int i = 0; i < entities.length(); i++) {
           Json.Object entityObj = entities.getObject(i);
           Entity entity = world.createEntity();
           String group = entityObj.getString(EntityTemplate.GROUP);
           if (group != null && !"".equals(group)) {
               entity.setGroup(group);
           }
           for (String key : entityObj.keys()) {
               if (key.endsWith("Component")) {
                   if (entityObj.containsKey(key)) {
                       BaseComponent c = templateManager.getJsonableComponentRegistry().from(key, entityObj.getObject(key));
                       entity.addComponent(c);
                   }
               }
           }
           entity.refresh();
       }
       return world;
    }
}
