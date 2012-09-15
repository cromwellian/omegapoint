package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.omegapoint.core.Playfield;
import com.omegapoint.core.components.TileComponent;
import playn.core.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Render 1 or more Tile layers (e.g. supports parallax)
 */
public class TileEditorRenderSystem extends TileRenderSystem {


    @Inject
    public TileEditorRenderSystem(Playfield screen) {
        super(screen);
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void removed(Entity e) {
        super.removed(e);
    }

    @Override
    protected void added(Entity e) {
        super.added(e);
    }

    @Override
    protected void process(Entity e) {
        super.process(e);
    }

    @Override
    protected ImmediateLayer getTileRenderer(TileComponent tileComponent, Image image) {
        return PlayN.graphics().createImmediateLayer(new TileEditorRenderer(image, tileComponent));
    }

    class TileEditorRenderer extends TileRenderer {

        public TileEditorRenderer(Image image, TileComponent tileComponent) {
            super(image, tileComponent);
        }

        @Override
        public void render(Surface surface) {
            super.render(surface);
            TileComponent.TileArrangement arr = tileComponent.getArrangement();

            float starty = PlayN.graphics().height() - tileComponent.getTileHeight() * arr.getRows().length;
            float startx = 0 - tileComponent.getCurrentScreenPosition() % tileComponent.getTileWidth();

            surface.setFillColor(0xffffffff);
            float numCols = (PlayN.graphics().width() - startx) / tileComponent.getTileWidth() + 1;
            for (int i = 0; i < numCols; i++) {
                surface.drawLine(startx, starty, startx, PlayN.graphics().height(), 1);
                startx += tileComponent.getTileWidth();
            }

            for (int j = 0; j < arr.getRows().length; j++) {
                surface.drawLine(0, starty, PlayN.graphics().width(), starty, 1);
                starty += tileComponent.getTileHeight();
            }
        }
    }
}
