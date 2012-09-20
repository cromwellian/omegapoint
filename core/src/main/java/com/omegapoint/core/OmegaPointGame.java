package com.omegapoint.core;

import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.events.ChangeStateEvent;
import com.omegapoint.core.events.ChangeStateHandler;
import com.omegapoint.core.screens.GameScreen;
import com.omegapoint.core.state.GameState;
import playn.core.Game;
import playn.core.Platform;
import playn.core.PlayN;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import tripleplay.game.ScreenStack;

import javax.inject.Inject;

public class OmegaPointGame implements Game {

    private StateMachine<GameState, String> stateMachine;
    private ScreenStack screens;
    private GameScreen screen;
    private EventBus eventBus;


    @Inject
    public OmegaPointGame(StateMachine<GameState, String> stateMachine,
                          ScreenStack screens, GameScreen screen, EventBus eventBus) {
        this.stateMachine = stateMachine;
        this.screens = screens;
        this.screen = screen;
        this.eventBus = eventBus;
    }

    @Override
    public void init() {

        if (PlayN.platformType() == Platform.Type.ANDROID) {
            PlayN.graphics().setSize(PlayN.graphics().screenWidth(), PlayN.graphics().screenHeight());
        } else {
//            PlayN.graphics().setSize(960, 640);
        }
        eventBus.addHandler(ChangeStateEvent.TYPE, new ChangeStateHandler() {
            @Override
            public void onStateChange(ChangeStateEvent changeStateEvent) {
                stateMachine.fireEvent(changeStateEvent.getNextState(), new Arguments(screens));
            }
        });
        eventBus.fireEvent(new ChangeStateEvent("load"));
    }


    @Override
    public void paint(float alpha) {
        screens.paint(alpha);
    }

    @Override
    public void update(float delta) {
        screens.update(delta);
    }

    @Override
    public int updateRate() {
        return 30;
    }

}
