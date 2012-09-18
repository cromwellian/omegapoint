package com.omegapoint.core.systems;

import com.artemis.*;
import com.omegapoint.core.Debug;
import com.omegapoint.core.Playfield;
import com.omegapoint.core.components.CollisionComponent;
import com.omegapoint.core.components.PositionComponent;
import com.omegapoint.core.components.SpriteComponent;
import playn.core.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class SpriteRenderSystem extends EntityProcessingSystem implements ImmediateLayer.Renderer {

    private ComponentMapper<SpriteComponent> spriteMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private Map<String, Image.Region> imageCache = new HashMap<String, Image.Region>();
    private Playfield screen;
    private List<Entity> toBeDrawn = new ArrayList<Entity>();
    @Inject
    public SpriteRenderSystem(Playfield screen) {
        super(SpriteComponent.class, PositionComponent.class);
        this.screen = screen;
    }

    @Override
    protected void initialize() {
        spriteMapper = new ComponentMapper<SpriteComponent>(SpriteComponent.class, world);
        positionMapper = new ComponentMapper<PositionComponent>(PositionComponent.class, world);
        screen.layer().add(PlayN.graphics().createImmediateLayer(this));
    }

    @Override
    protected void removed(Entity e) {
        super.removed(e);
    }

    @Override
    protected void added(Entity e) {
        super.added(e);
        String imgName = spriteMapper.get(e).getImg();
        Image image = imageCache.get(imgName);
        if (image == null) {
          image = PlayN.assets().getImage(imgName);
          imageCache.put(imgName, image.subImage(0, 0, image.width(), image.height()));
        }
    }

    @Override
    protected void begin() {
        toBeDrawn.clear();
    }

    @Override
    protected void process(Entity e) {
        toBeDrawn.add(e);
    }

    @Override
    public void render(Surface surface) {
      for(Entity e : toBeDrawn) {
          surface.save();
          PositionComponent pos = positionMapper.get(e);
          SpriteComponent spr = spriteMapper.get(e);

          Image.Region image = imageCache.get(spr.getImg());

          int width = spr.getSw();
          int height = spr.getSh();
          if (width == 0) {
              width = (int) image.width();
              height = (int) image.height();
          }
          if (width == 0) {
              width = height = 64;
          }
          float cx = (width / 2);
          float cy = (height/ 2);

          if (spr.getCols() > 0) {
              int frame = spr.getCurFrame();
              int prev = frame;
              if (spr.getMsPerFrame() >= 0) {
                  int timeLeft = spr.getTimeToNextFrame() - world.getDelta();
                  if (timeLeft < 0) {
                      frame += spr.getTimeToNextFrame() > 0 ? world.getDelta() / spr.getTimeToNextFrame() : 1;
                      spr.setCurFrame(frame % (spr.getCols() * spr.getRows()));
                      spr.setTimeToNextFrame(spr.getMsPerFrame());
                  } else {
                      spr.setTimeToNextFrame(timeLeft);
                  }
              }

              int row = prev / spr.getCols();
              int col = prev % spr.getCols();

              image.setBounds(spr.getSw() * col, spr.getSh() * row, spr.getSw(), spr.getSh());
              if (spr.getMsPerFrame() >= 0 && frame >= spr.getCols() * spr.getRows() && !spr.isCyclic()) {
                  e.delete();
              }
          }

          surface.translate(pos.getX(), pos.getY());
          surface.rotate((float) pos.getAngle());
          if (spr.isCyclic()) {
//                  surface.scale(2, 2);
          }
          surface.translate(-cx, -cy);
          if (Debug.isCollisionBoundingBoxesEnabled()) {
            CollisionComponent colComp = new ComponentMapper<CollisionComponent>(CollisionComponent.class, world).get(e);
            if (colComp != null) {
              surface.save();
              surface.setFillColor(0xffffffff);
              surface.translate(cx, cy);
              surface.rotate((float) -pos.getAngle());
              surface.translate(-colComp.getBounds().width()/2, -colComp.getBounds().height()/2);
              surface.fillRect(0, 0, colComp.getBounds().width(), colComp.getBounds().height());
              surface.restore();
            }
          }
          surface.drawImage(image, 0, 0);
          surface.restore();
      }
    }
}
