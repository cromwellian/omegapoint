package com.omegapoint.core.components;

import java.util.Collection;
import java.util.Collections;

/**
 *  A transient implementation fo a mutable {@link EntityDatabase} that uses memory instead of persistency storage.
 */
public class MemoryEntityDatabase implements EntityDatabase {
    @Override
    public Collection<String> getTemplates() {
        return Collections.EMPTY_LIST;
    }
}
