package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.omegapoint.core.Playfield;
import com.omegapoint.core.components.InventoryComponent;

import playn.core.*;
import playn.core.Font.Style;
import playn.core.TextFormat.Alignment;

import javax.inject.Inject;

import static playn.core.PlayN.graphics;

/**
 *
 */
public class HudRenderSystem extends EntityProcessingSystem {
    private ComponentMapper<InventoryComponent> inventoryMapper;
    private Playfield screen;
    private CanvasImage hud;
    private ImageLayer layer;
    private TextFormat scoreFormat;
    private Font font;

    // For caching
    private int prevScore;
    private int prevResources;

    @Inject
    public HudRenderSystem(Playfield screen) {
        super(InventoryComponent.class);
        this.screen = screen;
    }

    @Override
    protected void initialize() {
        inventoryMapper = new ComponentMapper<InventoryComponent>(InventoryComponent.class, world);

        hud = graphics().createImage(graphics().width(), 24);
        layer = graphics().createImageLayer(hud);
        layer.setTranslation(0, 0);
        layer.setInteractive(false);
        screen.layer().add(layer);

        font = graphics().createFont("Space Age", Style.PLAIN, 22);
        scoreFormat = new TextFormat(font, graphics().width(), Alignment.LEFT);

        prevScore = -1;
        prevResources = -1;
    }

    @Override
    protected void removed(Entity e) {
        super.removed(e);
        screen.layer().remove(layer);
    }

    @Override
    protected void added(Entity e) {
        super.added(e);
    }

    @Override
    protected void process(Entity e) {
        InventoryComponent inventory = inventoryMapper.get(e);
        if (inventory == null || (inventory.score() == prevScore && inventory.resources() == prevResources))
            return;

        hud.canvas().clear();
        hud.canvas().setFillColor(Color.argb(20, 255, 255, 255));
        hud.canvas().fillRect(0, 0, hud.width(), hud.height());
        hud.canvas().setFillColor(Color.argb(30, 255, 255, 255));
        hud.canvas().fillRect(0, 23, hud.width(), 1);
        hud.canvas().setFillColor(Color.rgb(0, 0, 255));
        TextLayout tl = graphics().layoutText("SCORE " + inventory.score() /*+ "   RESOURCES " + inventory.resources()*/, scoreFormat);
        hud.canvas().fillText(tl, 7, 0);

        prevScore = inventory.score();
        prevResources = inventory.resources();
    }
}
