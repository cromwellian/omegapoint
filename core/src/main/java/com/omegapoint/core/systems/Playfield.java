package com.omegapoint.core.systems;

import playn.core.GroupLayer;
import playn.core.PlayN;

/**
 *
 */
public class Playfield {
    private GroupLayer layer;

    public Playfield() {
        layer = PlayN.graphics().createGroupLayer();
    }

    public GroupLayer layer() {
        return layer;
    }
}
