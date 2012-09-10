package com.omegapoint.core.components;

import com.artemis.Component;

/**
 *
 */
public class StarComponent extends Component {
    int starBrightness;

    public StarComponent(int starBrightness) {
        this.starBrightness = starBrightness;
    }

    public int getStarBrightness() {

        return starBrightness;
    }

    public void setStarBrightness(int starBrightness) {
        this.starBrightness = starBrightness;
    }
}
