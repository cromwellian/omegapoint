package com.omegapoint.core.state;

import playn.core.PlayN;
import tripleplay.game.Screen;

/**
 *
 */
public class LoadScreen extends Screen {
    @Override
    public void wasAdded() {
        super.wasAdded();
        layer.add(PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/skycrane.jpeg")));
    }
}
