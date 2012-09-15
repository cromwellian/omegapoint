package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.omegapoint.core.Playfield;
import com.omegapoint.core.components.AppearanceComponent;
import com.omegapoint.core.components.PositionComponent;
import com.omegapoint.core.components.StarComponent;
import playn.core.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static playn.core.PlayN.graphics;

/**
 *
 */
public class StarRenderSystem extends EntityProcessingSystem implements ImmediateLayer.Renderer {

    private ComponentMapper<PositionComponent> positionMapper;
    private ComponentMapper<StarComponent> starMapper;
    private ComponentMapper<AppearanceComponent> appearanceMapper;
    private ImmediateLayer bgLayer;
    private static int alphas[] = {0x40000000, 0x80000000, 0xc0000000, 0xff000000};
    private Playfield screen;

    @Inject
    public StarRenderSystem(Playfield screen) {
        super(StarComponent.class, PositionComponent.class, AppearanceComponent.class);
        this.screen = screen;
    }

    @Override
    protected void initialize() {
        positionMapper = new ComponentMapper<PositionComponent>(PositionComponent.class, world);
        starMapper = new ComponentMapper<StarComponent>(StarComponent.class, world);
        appearanceMapper = new ComponentMapper<AppearanceComponent>(AppearanceComponent.class, world);
        bgLayer = graphics().createImmediateLayer(this);
        bgLayer.setDepth(-10);
        screen.layer().add(bgLayer);
    }


    @Override
    protected void begin() {
        super.begin();
        toBeDrawn.clear();
    }

    List<Entity> toBeDrawn = new ArrayList<Entity>();

    @Override
    protected void process(Entity e) {
        toBeDrawn.add(e);
    }

    @Override
    public void render(Surface surface) {
        for (Entity e : toBeDrawn) {
            AppearanceComponent app = appearanceMapper.get(e);
            PositionComponent pos = positionMapper.get(e);
            StarComponent star = starMapper.get(e);
            surface.setFillColor(0xffffff | alphas[star.getStarBrightness()]);
            surface.fillRect(pos.getX(), pos.getY(), 2, 2);
        }
    }
}
