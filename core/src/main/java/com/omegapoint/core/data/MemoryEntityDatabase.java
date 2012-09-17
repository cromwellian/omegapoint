package com.omegapoint.core.data;

import com.omegapoint.core.data.EntityDatabase;

import java.util.Collection;
import java.util.Collections;

/**
 *  A transient implementation fo a mutable {@link com.omegapoint.core.data.EntityDatabase} that uses memory instead of persistency storage.
 */
public class MemoryEntityDatabase implements EntityDatabase {
    @Override
    public Collection<String> getTemplates() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void persist(Collection<EntityTemplate> toBePersisted) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
