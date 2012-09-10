package com.omegapoint.core.state;

import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.builder.StateMachineBuilder;

/**
 *
 */
public class OmegaStateMachineBuilder {
    public enum GameStates {
        INTRO, PLAY
    }

    public static void build() {
        StateMachineBuilder<GameStates, String> builder = StateMachineBuilder.create(GameStates.class, String.class);
        builder.transition().from(GameStates.INTRO).to(GameStates.PLAY).on("play").perform(new Action<GameStates, String>() {
            @Override
            public void onTransition(GameStates fromState, GameStates toState, String name, Arguments arguments, StateMachine<GameStates, String> gameStatesStringStateMachine) {

            }
        });
    }
}
