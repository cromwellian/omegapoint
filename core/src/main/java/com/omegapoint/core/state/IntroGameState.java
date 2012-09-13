package com.omegapoint.core.state;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import tripleplay.game.Screen;

import javax.inject.Inject;

/**
 *
 */
public class IntroGameState extends AbstractGameState implements Action<GameState, String> {
    private IntroScreen screen;

    @Inject
    public IntroGameState(IntroScreen screen) {
        super(screen);
    }

    @Override
    public void onTransition(GameState gameState, GameState gameState1, String s, Arguments arguments, StateMachine<GameState, String> gameStateStringStateMachine) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
