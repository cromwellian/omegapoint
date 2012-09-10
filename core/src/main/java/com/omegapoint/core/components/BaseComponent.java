package com.omegapoint.core.components;

import com.artemis.Component;
import playn.core.Json;

/**
 *
 */
public abstract class BaseComponent extends Component {

    private String group;

    public abstract String getComponentName();
    public abstract BaseComponent duplicate();
    public abstract Json.Object toJson();

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }
}
