package com.omegapoint.core.state;

import com.omegapoint.core.screens.PauseMenuScreen;
import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import tripleplay.game.ScreenStack;

import javax.inject.Inject;

/**
 *
 */
public class PauseGameState extends AbstractGameState implements Action<GameState, String> {
    @Inject
    public PauseGameState(PauseMenuScreen screen) {
        super(screen);
    }

    @Override
    public void onTransition(GameState from, GameState to, String event, Arguments arguments, StateMachine<GameState, String> gameStateStringStateMachine) {
        ScreenStack screens = (ScreenStack) arguments.getFirst();
        // entry
        if (to == this) {
          screens.push(getScreen(), screens.slide().up());
        } else {
          screens.popTo(to.getScreen(), screens.slide().down());
        }
    }
}
