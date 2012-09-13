package com.omegapoint.core.state;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import tripleplay.game.Screen;

import javax.inject.Inject;

/**
 *
 */
public class LoadGameState extends AbstractGameState implements Action<GameState, String> {
    @Inject
    public LoadGameState(LoadScreen screen) {
        super(screen);
    }

    @Override
    public void onTransition(GameState from, GameState to, String event, Arguments arguments, StateMachine<GameState, String> stateMachine) {

    }
}
