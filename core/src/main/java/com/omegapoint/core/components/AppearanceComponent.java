package com.omegapoint.core.components;

import com.artemis.Component;

/**
 *
 */
public class AppearanceComponent extends Component {
  int color;

    public AppearanceComponent(int color) {
        this.color = color;
    }

    public int getColor() {

        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
