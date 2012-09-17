package com.omegapoint.core.state;

import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.data.EntityTemplates;
import com.omegapoint.core.events.ChangeStateEvent;
import com.omegapoint.core.screens.LoadScreen;
import playn.core.AssetWatcher;
import playn.core.PlayN;
import playn.core.ResourceCallback;
import playn.core.Sound;
import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;
import tripleplay.game.ScreenStack;

import javax.inject.Inject;

/**
 *
 */
public class LoadGameState extends AbstractGameState implements Action<GameState, String> {
    private EntityTemplates templateManager;
    private final EventBus eventBus;

    @Inject
    public LoadGameState(LoadScreen screen, EntityTemplates templateManager, EventBus eventBus) {
        super(screen);
        this.templateManager = templateManager;
        this.eventBus = eventBus;
    }

    @Override
    public void onTransition(GameState from, GameState to, String event, Arguments arguments, StateMachine<GameState, String> stateMachine) {
        ScreenStack screens = (ScreenStack) arguments.getFirst();
        screens.push(getScreen());
        AssetWatcher watcher = new AssetWatcher(new AssetWatcher.Listener() {
            @Override
            public void done() {
                eventBus.fireEvent(new ChangeStateEvent("intro"));
            }

            @Override
            public void error(Throwable e) {
                eventBus.fireEvent(new ChangeStateEvent("intro"));
            }
        }) ;
        watcher.add(PlayN.assets().getSound("sounds/cybernoid2"));
        templateManager.waitForAllAssets(watcher);
//        PlayN.mouse().setListener(new Mouse.Adapter() {
//            @Override
//            public void onMouseUp(Mouse.ButtonEvent event) {
//                event.setPreventDefault(true);
//                PlayN.mouse().setListener(null);
//
//            }
//        });
    }
}
