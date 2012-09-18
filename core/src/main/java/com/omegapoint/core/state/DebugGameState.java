package com.omegapoint.core.state;

import com.omegapoint.core.inject.OmegaPointBaseModule;
import com.omegapoint.core.screens.DebugScreen;
import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;

import javax.inject.Inject;

/**
 *
 */
public class DebugGameState extends AbstractGameState implements Action<GameState, String> {
    @Inject
    public DebugGameState(DebugScreen screen) {
        super(screen);
    }

    @Override
    public void onTransition(GameState from, GameState to, String event, Arguments arguments, StateMachine<GameState, String> gameStateStringStateMachine) {
        OmegaPointBaseModule.ScreenStackImpl screens = (OmegaPointBaseModule.ScreenStackImpl) arguments.getFirst();
        // entry
        if (to == this && !screens.contains(getScreen())) {
          screens.push(getScreen(), screens.slide().up());
        } else if (screens.contains(to.getScreen())) {
          screens.popTo(to.getScreen(), screens.slide().down());
        }
    }
}
