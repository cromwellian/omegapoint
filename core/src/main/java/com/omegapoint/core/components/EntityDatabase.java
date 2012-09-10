package com.omegapoint.core.components;

import java.util.Collection;

/**
 * A source of {@link EntityTemplate}s to loaded into the registry, or persisted to.
 */
public interface EntityDatabase {
    Collection<String> getTemplates();
}
