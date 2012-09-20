package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.omegapoint.core.Playfield;
import com.omegapoint.core.components.BeamComponent;
import com.omegapoint.core.components.PositionComponent;
import playn.core.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class BeamRenderSystem extends EntityProcessingSystem implements ImmediateLayer.Renderer {

    private ComponentMapper<BeamComponent> beamMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private Map<BeamComponent, Image> beams = new HashMap<BeamComponent, Image>();
    private ImmediateLayer beamLayer;
    private Playfield screen;

    @Inject
    public BeamRenderSystem(Playfield screen) {
        super(BeamComponent.class, PositionComponent.class);
        this.screen = screen;
    }

    @Override
    protected void initialize() {
        beamMapper = new ComponentMapper<BeamComponent>(BeamComponent.class, world);
        positionMapper = new ComponentMapper<PositionComponent>(PositionComponent.class, world);
        beamLayer = PlayN.graphics().createImmediateLayer(this);
        screen.layer().add(beamLayer);
    }

    @Override
    protected void begin() {
        super.begin();
        toBeDrawn.clear();
    }

    @Override
    protected void added(Entity e) {
        super.added(e);
        BeamComponent beam = beamMapper.get(e);
        CanvasImage canvasImg = PlayN.graphics().createImage(beam.getFinalLength(), 16);
        Canvas canvas = canvasImg.canvas();
        canvas.setFillColor(0x10ff0000);
        canvas.setStrokeWidth(50);


        for (int i = 0; i < 16; i++) {
            canvas.fillRect(i * 16, 8-i/2, beam.getFinalLength(), 2*Math.max(8-i/2, 2));
        }
        beams.put(beam, canvasImg);
    }

    private int alpha(int color, double alpha) {
        int a = (color >> 24) & 0xff;
        return (color & 0x00ffffff) | ((int) (a * alpha)) << 24;
    }

    @Override
    protected void removed(Entity e) {
        super.removed(e);
    }

    List<Entity> toBeDrawn = new ArrayList<Entity>();

    @Override
    protected void process(Entity e) {
        toBeDrawn.add(e);

    }

    @Override
    public void render(Surface surface) {
        for (Entity e : toBeDrawn) {
            PositionComponent pos = positionMapper.get(e);
            BeamComponent beam = beamMapper.get(e);
            surface.drawImage(beams.get(beam), pos.getX(), pos.getY());
//            surface.setFillColor(alpha(0xffffffff, 1.0 / beam.strobe()));
//            surface.fillRect(pos.getX(), pos.getY(), beam.getFinalLength(), 2);
        }
    }
}
