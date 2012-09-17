package com.omegapoint.core.data;

import com.omegapoint.core.util.JsonUtil;
import playn.core.Json;
import playn.core.PlayN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *  A transient implementation fo a mutable {@link EntityDatabase} that uses memory instead of persistency storage.
 */
public class PlayNStorageEntityDatabase implements EntityDatabase {
    @Override
    public Collection<String> getTemplates() {
        String entityDataString = PlayN.storage().getItem("entityDatabase");

        if (false && entityDataString != null) {
          Json.Object jsonTemplatesObj = PlayN.json().parse(entityDataString);
          Json.Array jsonTemplates = jsonTemplatesObj.getArray("templates");

          List<String> templates = new ArrayList<String>();

          for (int i = 0; i < jsonTemplates.length(); i++) {
            templates.add(JsonUtil.toString(jsonTemplates.getObject(i)));
          }
            return templates;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void persist(Collection<EntityTemplate> toBePersisted) {
        Json.Object obj = PlayN.json().createObject();
        Json.Array out = PlayN.json().createArray();
        for (EntityTemplate template : toBePersisted) {
            out.add(template.toJson(template));
        }
        obj.put("templates", out);
        PlayN.storage().setItem("entityDatabase", JsonUtil.toString(obj));
        PlayN.log().debug(toBePersisted.size() + " entities written to " + getClass().getName());
    }
}
