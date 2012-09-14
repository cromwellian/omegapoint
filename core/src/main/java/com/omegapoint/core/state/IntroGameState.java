package com.omegapoint.core.state;

import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.events.ChangeStateEvent;
import playn.core.Mouse;
import playn.core.PlayN;
import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import javax.inject.Inject;

/**
 *
 */
public class IntroGameState extends AbstractGameState implements Action<GameState, String> {
    private EventBus eventBus;

    @Inject
    public IntroGameState(IntroScreen screen, EventBus eventBus) {
        super(screen);
        this.eventBus = eventBus;
    }

    @Override
    public void onTransition(GameState gameState, GameState gameState1, String s, Arguments arguments, StateMachine<GameState, String> gameStateStringStateMachine) {
        ScreenStack screens = (ScreenStack) arguments.getFirst();
        screens.push(getScreen());
        PlayN.mouse().setListener(new Mouse.Adapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                event.setPreventDefault(true);
                PlayN.mouse().setListener(null);
                eventBus.fireEvent(new ChangeStateEvent("play"));
            }
        });
    }
}
