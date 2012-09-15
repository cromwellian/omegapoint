package com.omegapoint.core.state;

import com.omegapoint.core.inject.OmegaPointBaseModule;
import com.omegapoint.core.screens.GameScreen;
import com.omegapoint.core.screens.TileEditorScreen;
import se.hiflyer.fettle.Action;
import se.hiflyer.fettle.Arguments;
import se.hiflyer.fettle.StateMachine;

import javax.inject.Inject;

/**
 *
 */
public class TileEditorState extends AbstractGameState implements Action<GameState,String> {
    @Inject
    public TileEditorState(TileEditorScreen screen) {
        super(screen);
    }

    @Override
    public void onTransition(GameState from, GameState to, String event, Arguments arguments, StateMachine<GameState, String> gameStateStringStateMachine) {
        OmegaPointBaseModule.ScreenStackImpl screens = (OmegaPointBaseModule.ScreenStackImpl) arguments.getFirst();
        if (to == this && !screens.contains(getScreen())) {
            screens.push(getScreen());
        } else {
            screens.popTo(to.getScreen(), screens.slide().down());
        }
    }
}
