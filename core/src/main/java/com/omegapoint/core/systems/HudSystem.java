package com.omegapoint.core.systems;

import com.omegapoint.core.Inventory;

import playn.core.*;
import playn.core.Font.Style;
import playn.core.TextFormat.Alignment;

import static playn.core.PlayN.graphics;

// TODO(pdr): this is not an Entity System
// TODO(pdr): re-write with tripleplay

public class HudSystem {
    private CanvasImage hud;
    private ImageLayer layer;
    private Inventory inventory;
    private TextFormat scoreFormat;
    private TextFormat resourcesFormat;
    private Font font;

    public HudSystem(Inventory inventory, GroupLayer parent) {
        this.inventory = inventory;
        hud = graphics().createImage(graphics().width(), 25);
        layer = graphics().createImageLayer(hud);
        layer.setTranslation(0, 0);
        layer.setInteractive(false);
        parent.add(layer);

        font = graphics().createFont("Space Age", Style.PLAIN, 25);
        scoreFormat = new TextFormat(font, graphics().width(), Alignment.LEFT);
        resourcesFormat = new TextFormat(font, graphics().width(), Alignment.LEFT);
    }

    public void update() {
        hud.canvas().clear();
        hud.canvas().setFillColor(Color.argb(200, 0, 0, 0));
        hud.canvas().fillRect(0, 0, hud.width(), hud.height());
        hud.canvas().setFillColor(Color.rgb(0, 0, 255));
        TextLayout tl = graphics().layoutText("SCORE " + inventory.score(), scoreFormat);
        hud.canvas().fillText(tl, 0, 0);
        hud.canvas().setFillColor(Color.rgb(0, 0, 255));
        tl = graphics().layoutText("RESOURCES " + inventory.resources(), resourcesFormat);
        hud.canvas().fillText(tl, hud.width() / 1.7f, 0);
    }
}
