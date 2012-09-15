package com.omegapoint.core.state;

import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import se.hiflyer.fettle.builder.StateMachineBuilder;
import tripleplay.game.Screen;
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
    private final GameState initState;

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
        this.initState = new GameState(){
            @Override
            public Screen getScreen() {
                return null;
            }
        };
    }

    public StateMachine<GameState, String> build() {
        StateMachineBuilder<GameState, String> builder = StateMachineBuilder.create(GameState.class, String.class);
        builder.transition().from(initState).to(loadState).on("load");
        builder.onEntry(loadState).perform(loadState);
        builder.transition().from(loadState).to(introState).on("intro");
        builder.onEntry(introState).perform(introState);

        builder.transition().from(introState).to(playState).on("play");
        builder.onEntry(playState).perform(playState);
        builder.transition().from(playState).to(pauseState).on("pause");
        builder.onEntry(pauseState).perform(pauseState);
        builder.onExit(pauseState).perform(pauseState);
        builder.transition().from(pauseState).to(playState).on("play");

        StateMachine<GameState, String> stateMachine = builder.build(initState);
        return stateMachine;
    }
}
