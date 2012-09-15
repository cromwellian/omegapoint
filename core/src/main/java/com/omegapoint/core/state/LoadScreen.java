package com.omegapoint.core.state;

import playn.core.*;
import tripleplay.game.Screen;

import static playn.core.PlayN.graphics;

/**
 *
 */
public class LoadScreen extends Screen implements ImmediateLayer.Renderer {


    private Image image;
    private CanvasImage title;

    @Override
    public void wasAdded() {
        super.wasAdded();
        image = PlayN.assets().getImage("images/horsehead.jpg");
        layer.add(graphics().createImmediateLayer(this));
        TextLayout tl = graphics().layoutText("Loading Omega Point",
                new TextFormat(graphics().createFont("Space Age", Font.Style.PLAIN, 80),
                        graphics().width() - 40, TextFormat.Alignment.CENTER));
        title = graphics().createImage(tl.width(), tl.height());
        title.canvas().setFillColor(0xffffffff);
        title.canvas().fillText(tl, 0, 0);
    }

    @Override
    public void render(Surface surface) {
        surface.drawImage(image, 0, 0, graphics().width(), graphics().height(), 0, 0, image.width(),
                image.height());
        surface.drawImage(title, (graphics().width() - title.width())/2,
                (graphics().height() - title.height())/2);
    }
}
