package com.omegapoint.core.state;

import com.omegapoint.core.GameScreen;
import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.TransitionModel;
import se.hiflyer.fettle.builder.StateMachineBuilder;
import tripleplay.game.ScreenStack;

import javax.inject.Inject;

/**
 *
 */
public class OmegaStateMachineBuilder {
    private ScreenStack screens;
    private final LoadGameState loadState;
    private final IntroGameState introState;
    private final PlayGameState playState;
    private final PauseGameState pauseState;
    private final Arguments arguments;

    @Inject
    public OmegaStateMachineBuilder(ScreenStack screens, LoadGameState loadState,
                                    IntroGameState introState,
                                    PlayGameState playState,
                                    PauseGameState pauseState) {
        this.screens = screens;
        this.loadState = loadState;
        this.introState = introState;
        this.playState = playState;
        this.pauseState = pauseState;
        this.arguments = new Arguments(screens);
    }

    public StateMachine<GameState, String> build() {
        StateMachineBuilder<GameState, String> builder = StateMachineBuilder.create(GameState.class, String.class);
        builder.onEntry(loadState).perform(loadState);
        builder.transition().from(loadState).to(introState).on("intro");
        builder.onEntry(introState).perform(introState);

        builder.transition().from(introState).to(playState).on("play");
        builder.onEntry(playState).perform(playState);
        builder.transition().from(playState).to(pauseState).on("pause");
        builder.onEntry(pauseState).perform(pauseState);
        builder.transition().from(pauseState).to(playState).on("play");
        builder.onExit(pauseState).perform(pauseState);

        StateMachine<GameState, String> stateMachine = builder.build(loadState);
        stateMachine.fireEvent("intro", arguments);
        return stateMachine;
    }
}
