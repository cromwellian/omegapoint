package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.omegapoint.core.components.PositionComponent;
import com.omegapoint.core.components.SpriteComponent;
import com.omegapoint.core.components.TileComponent;
import playn.core.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Render 1 or more Tile layers (e.g. supports parallax)
 */
public class TileRenderSystem extends EntityProcessingSystem {

    private ComponentMapper<TileComponent> tileMapper;
    private Map<Entity, Layer> entity2imageLayer = new HashMap<Entity, Layer>();
    private Map<String, Image> imageCache = new HashMap<String, Image>();
    private Playfield screen;

    @Inject
    public TileRenderSystem(Playfield screen) {
        super(TileComponent.class);
        this.screen = screen;
    }

    @Override
    protected void initialize() {
        tileMapper = new ComponentMapper<TileComponent>(TileComponent.class, world);
    }

    @Override
    protected void removed(Entity e) {
        super.removed(e);
        screen.layer().remove(entity2imageLayer.get(e));
        entity2imageLayer.remove(e);
    }

    @Override
    protected void added(Entity e) {
        super.added(e);
        TileComponent tileComponent = tileMapper.get(e);
        String imgName = tileComponent.getAssetPath();
        Image image = imageCache.get(imgName);
        if (image == null) {
            image = PlayN.assets().getImage(imgName);
            imageCache.put(imgName, image);
        }

        Layer layer = PlayN.graphics().createImmediateLayer(new TileRenderer(image, tileComponent));
        entity2imageLayer.put(e, layer);
        layer.setDepth(tileComponent.getDepth());
        screen.layer().add(layer);
    }

    @Override
    protected void process(Entity e) {
        TileComponent tileComponent = tileMapper.get(e);
        // 2 pixels per frame, at 30fps, move approximation 1 tile per second if tile is 60 pixels across
        tileComponent.setCurrentScreenPosition(tileComponent.getCurrentScreenPosition() + tileComponent.getParallaxSpeed());
    }

    class TileRenderer implements ImmediateLayer.Renderer {

        private Image.Region image;
        private TileComponent tileComponent;

        public TileRenderer(Image image, TileComponent tileComponent) {
            this.image = image.subImage(0, 0, image.width(), image.height());
            this.tileComponent = tileComponent;
        }

        @Override
        public void render(Surface surface) {
            float starty = PlayN.graphics().height() - tileComponent.getTileHeight();
            float startx = 0;
            TileComponent.TileArrangement arr = tileComponent.getArrangement();
            for (TileComponent.TileRow row : arr.getRows()) {
                int[] indices = row.getIndices();
                int startIndice = (int) (tileComponent.getCurrentScreenPosition() / tileComponent.getTileWidth());
                startx = 0 - tileComponent.getCurrentScreenPosition() % tileComponent.getTileWidth();

                for (int i = startIndice; i < indices.length; i++) {
                    int indice = indices[i];
                    if (indice == TileComponent.EMPTY_SPACE) {
                        startx += tileComponent.getTileWidth();
                        if (startx > PlayN.graphics().width()) {
                            break;
                        } else {
                            continue;
                        }
                    }
                    int spriteCol = indice % tileComponent.getTileCols();
                    int spriteRow = indice / tileComponent.getTileCols();
                    int tileOffsetX = spriteCol * tileComponent.getTileWidth();
                    int tileOffsetY = spriteRow * tileComponent.getTileHeight();
                    image.setBounds(tileOffsetX, tileOffsetY, tileComponent.getTileWidth(),
                            tileComponent.getTileHeight());
                    surface.drawImage(image, startx, starty);
                    startx += tileComponent.getTileWidth();
                    if (startx > PlayN.graphics().width()) {
                        break;
                    }
                }
                starty -= tileComponent.getTileHeight();
            }
        }
    }
}
