package com.omegapoint.core;

import com.google.inject.Inject;
import playn.core.Game;
import playn.core.Platform;
import playn.core.PlayN;
import tripleplay.game.ScreenStack;

public class OmegaPointGame implements Game {

    private ScreenStack screens;
    private GameScreen screen;


    @Inject
    public OmegaPointGame(ScreenStack screens, GameScreen screen) {
        this.screens = screens;
        this.screen = screen;
    }

    @Override
    public void init() {

        if (PlayN.platformType() == Platform.Type.ANDROID) {
            PlayN.graphics().setSize(PlayN.graphics().screenWidth(), PlayN.graphics().screenHeight());
        } else {
//            PlayN.graphics().setSize(960, 640);
        }
        screen.init();
        screens.push(screen);
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
