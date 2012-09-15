package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.omegapoint.core.Playfield;
import com.omegapoint.core.components.PositionComponent;
import com.omegapoint.core.components.TextComponent;
import playn.core.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static playn.core.PlayN.graphics;

/**
 *
 */
public class TextRenderSystem extends EntityProcessingSystem {

    private ComponentMapper<TextComponent> textMapper;
    private ComponentMapper<PositionComponent> positionMapper;
    private Map<Entity, ImageLayer> entity2ImageLayer = new HashMap<Entity, ImageLayer>();
    private Map<TextComponent, CacheText> text2image = new HashMap<TextComponent, CacheText>();
    private Playfield screen;

    static class CacheText {

        private Image image;
        private TextLayout tl;
        private Entity entity;

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public TextLayout getTl() {
            return tl;
        }

        public void setTl(TextLayout tl) {
            this.tl = tl;
        }

        public Entity getEntity() {
            return entity;
        }

        public CacheText(Image image, TextLayout tl, Entity entity) {

            this.image = image;
            this.tl = tl;
            this.entity = entity;
        }
    }

    @Inject
    public TextRenderSystem(Playfield screen) {
        super(TextComponent.class, PositionComponent.class);
        this.screen = screen;
    }

    @Override
    protected void initialize() {
        textMapper = new ComponentMapper<TextComponent>(TextComponent.class, world);
        positionMapper = new ComponentMapper<PositionComponent>(PositionComponent.class, world);
    }

    @Override
    protected void removed(Entity e) {
        super.removed(e);
        screen.layer().remove(entity2ImageLayer.get(e));
        entity2ImageLayer.remove(e);
        Iterator<Map.Entry<TextComponent, CacheText>> entryIt = text2image.entrySet().iterator();
        while (entryIt.hasNext()) {
            CacheText ct = entryIt.next().getValue();
            if (ct.getEntity() == e) {
                entryIt.remove();
            }
        }
        text2image.remove(textMapper.get(e));
    }

    @Override
    protected void added(Entity e) {
        super.added(e);
    }

    @Override
    protected void process(Entity e) {
        TextComponent textComp = textMapper.get(e);
        PositionComponent posComp = positionMapper.get(e);

        ImageLayer layer = entity2ImageLayer.get(e);

        if (layer == null) {
            layer = graphics().createImageLayer();
            entity2ImageLayer.put(e, layer);
            screen.layer().add(layer);
        }

        // TODO: reuse canvases
        CacheText ct = text2image.get(textComp);
        if (ct == null) {
            TextLayout tl = graphics().layoutText(textComp.getText(),
                    new TextFormat(textComp.getFont(), graphics().width(), textComp.getAlign()));

            CanvasImage cimage = graphics().createImage((int) tl.width(), (int) tl.height());
            cimage.canvas().setFillColor(textComp.getColor());
            cimage.canvas().fillText(tl, 0, 0);
            ct = new CacheText(cimage, tl, e);
            text2image.put(textComp, ct);
        }
        layer.setImage(ct.getImage());

        int x = 0;
        if (textComp.getAlign() == TextFormat.Alignment.CENTER) {
            x = (int) ((graphics().width() - ct.getTl().width()) / 2);
        }
        layer.setTranslation(posComp.getX()/100.0f * graphics().width() + x, posComp.getY()/100.0f * graphics().height());
    }
}
