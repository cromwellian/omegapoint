package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.omegapoint.core.components.BeamComponent;
import com.omegapoint.core.components.PositionComponent;
import com.omegapoint.core.components.SpriteComponent;
import playn.core.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class BeamRenderSystem extends EntityProcessingSystem {

    private ComponentMapper<BeamComponent> beamMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private Map<BeamComponent, Image> beams = new HashMap<BeamComponent, Image>();
    private SurfaceLayer beamLayer;

    public BeamRenderSystem() {
        super(BeamComponent.class, PositionComponent.class);
    }

    @Override
    protected void initialize() {
        beamMapper = new ComponentMapper<BeamComponent>(BeamComponent.class, world);
        positionMapper = new ComponentMapper<PositionComponent>(PositionComponent.class, world);
        beamLayer = PlayN.graphics().createSurfaceLayer(PlayN.graphics().width(), PlayN.graphics().height());
        PlayN.graphics().rootLayer().add(beamLayer);
    }

    @Override
    protected void begin() {
        super.begin();
        beamLayer.surface().clear();
    }

    @Override
    protected void added(Entity e) {
        super.added(e);
        BeamComponent beam = beamMapper.get(e);
        CanvasImage canvasImg = PlayN.graphics().createImage(beam.getFinalLength(), 10);
        Canvas canvas = canvasImg.canvas();
        canvas.setFillColor(alpha(beam.getColor(), 0.10));

        for (int i = 0; i < 16; i++) {
            canvas.fillRect(i*4, 0, beam.getFinalLength(), 2);
        }
        beams.put(beam, canvasImg);
    }

    private int alpha(int color, double alpha) {
        int a = (color >> 24) & 0xff;
        return (color & 0x00ffffff) | ((int)(a * alpha)) << 24;
    }

    @Override
    protected void removed(Entity e) {
        super.removed(e);
    }

    @Override
    protected void process(Entity e) {
        PositionComponent pos = positionMapper.get(e);
        BeamComponent beam = beamMapper.get(e);
        beamLayer.surface().drawImage(beams.get(beam), pos.getX(), pos.getY());
        beamLayer.surface().setFillColor(alpha(0xffffffff, 1.0/beam.strobe()));
        beamLayer.surface().fillRect(pos.getX(), pos.getY(), beam.getFinalLength(), 2);
    }
}
