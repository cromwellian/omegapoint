package com.omegapoint.core.state;

import tripleplay.game.Screen;

/**
 *  Each Game state is associated with a tripleplay screen.
 */
public interface GameState {
    Screen getScreen();
}
