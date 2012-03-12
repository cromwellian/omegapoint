package com.omegapoint.core.systems;

import com.artemis.*;
import com.omegapoint.core.components.PositionComponent;
import com.omegapoint.core.components.SpriteComponent;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class SpriteRenderSystem extends EntityProcessingSystem {

    private ComponentMapper<SpriteComponent> spriteMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private Map<Entity, ImageLayer> entity2imageLayer = new HashMap<Entity, ImageLayer>();
    private Map<String, Image> imageCache = new HashMap<String, Image>();
    
    public SpriteRenderSystem() {
        super(SpriteComponent.class, PositionComponent.class);
    }

    @Override
    protected void initialize() {
        spriteMapper = new ComponentMapper<SpriteComponent>(SpriteComponent.class, world);
        positionMapper = new ComponentMapper<PositionComponent>(PositionComponent.class, world);
    }

    @Override
    protected void removed(Entity e) {
        super.removed(e);
        PlayN.graphics().rootLayer().remove(entity2imageLayer.get(e));
        entity2imageLayer.remove(e);
    }

    @Override
    protected void added(Entity e) {
        super.added(e);
        String imgName = spriteMapper.get(e).getImg();
        Image image = imageCache.get(imgName);
        if (image == null) {
          image = PlayN.assetManager().getImage(imgName);
          imageCache.put(imgName, image);
        }

        ImageLayer layer = PlayN.graphics().createImageLayer(image);
        entity2imageLayer.put(e, layer);
        layer.setTranslation(positionMapper.get(e).getX(), positionMapper.get(e).getY());
        PlayN.graphics().rootLayer().add(layer);
    }

    @Override
    protected void process(Entity e) {
        PositionComponent pos = positionMapper.get(e);
        SpriteComponent spr = spriteMapper.get(e);

        ImageLayer layer = entity2imageLayer.get(e);
        float cx = (layer.width() / 2);
        float cy = (layer.height() / 2);

        if (spr.getCols() > 0) {
            int frame = spr.getCurFrame();
            if (spr.getMsPerFrame() >= 0) {
                int timeLeft = spr.getTimeToNextFrame() - world.getDelta();
                if (timeLeft < 0) {
                  spr.setCurFrame(frame + 1 % spr.getCols() * spr.getRows());
                  spr.setTimeToNextFrame(spr.getMsPerFrame());
                } else {
                  spr.setTimeToNextFrame(timeLeft);
                }
            }
            int row = frame / spr.getCols();
            int col = frame % spr.getCols();
            layer.setSourceRect(spr.getSw() * col, spr.getSh() * row, spr.getSw(), spr.getSh());
            layer.setWidth(spr.getSw());
            layer.setHeight(spr.getSh());
            if (spr.getMsPerFrame() >= 0 && spr.getCurFrame() == spr.getCols() * spr.getRows() && !spr.isCyclic()) {
                e.delete();
            }
        }

        layer.setOrigin(cx, cy);
        layer.setTranslation(pos.getX(), pos.getY());
        layer.setRotation((float) pos.getAngle());
    }
}
