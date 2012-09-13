package com.omegapoint.core.state;

import tripleplay.game.Screen;

/**
 *
 */
public class AbstractGameState implements GameState {
    private Screen screen;

    public AbstractGameState(Screen screen) {
        this.screen = screen;
    }

    @Override
    public Screen getScreen() {
        return screen;
    }
}
