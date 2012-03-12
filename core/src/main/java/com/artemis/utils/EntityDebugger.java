package com.artemis.utils;

import com.artemis.Component;
import com.artemis.Entity;

public class EntityDebugger {

    public static String getEntityString(final Entity entity) {
        final ImmutableBag<Component> components = entity.getComponents();
        StringBuilder stringBuilder = new StringBuilder(entity.toString());

        return stringBuilder.append(getComponentsString(components)).toString();
    }

    public static String getComponentsString(ImmutableBag<Component> components) {
        if (components == null) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder().append(", Components: ");

        for (int i = 0; i < components.size(); i++) {
            final Component component = components.get(i);
            stringBuilder.append(component.toString()).append(", ");
        }

        return stringBuilder.toString();
    }

}
