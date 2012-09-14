package com.omegapoint.core.state;

import com.artemis.Entity;
import com.artemis.World;
import com.omegapoint.core.components.*;
import com.omegapoint.core.systems.*;
import playn.core.*;
import tripleplay.game.Screen;

import javax.inject.Inject;

import static playn.core.PlayN.graphics;

/**
 *
 */
public class IntroScreen extends Screen  {
    private final EntityTemplates templateManager;
    private final World world;
    private final MovementSystem movementSystem;
    private final TextRenderSystem textRenderSystem;
    private final SimpleTweenSystem simpleTweenSystem;
    private CanvasImage title;
    private final StarRenderSystem starSystem;
    private final Playfield introPlayField;
    private final Sound sound;

    @Inject
    public IntroScreen(EntityTemplates templateManager) {
        this.templateManager = templateManager;
        this.world = new World();
        introPlayField = new Playfield();
        this.starSystem = new StarRenderSystem(introPlayField);
        this.world.getSystemManager().setSystem(this.starSystem);
        movementSystem = new MovementSystem();
        this.world.getSystemManager().setSystem(movementSystem);
        textRenderSystem = new TextRenderSystem(introPlayField);
        this.world.getSystemManager().setSystem(textRenderSystem);
        simpleTweenSystem = new SimpleTweenSystem();
        this.world.getSystemManager().setSystem(simpleTweenSystem);
        this.world.getSystemManager().initializeAll();
        makeStars();
        sound = PlayN.assets().getSound("sounds/cybernoid2");
        sound.play();
        sound.setLooping(true);
        templateManager.lookupAndInstantiate("titleText", world);
    }

    @Override
    public void wasAdded() {
        super.wasAdded();
        layer.add(introPlayField.layer());
    }

    @Override
    public void wasHidden() {
        super.wasHidden();
        sound.stop();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        world.loopStart(); // signals loop start
        world.setDelta((int) delta); // sets delta for systems to use
        movementSystem.process();
        simpleTweenSystem.process();
    }

    @Override
    public void paint(float alpha) {
        super.paint(alpha);
        this.starSystem.process();
        this.textRenderSystem.process();
    }

    private void makeStars() {
        for (int i = 0; i < 50; i++) {
            Entity star = world.createEntity();
            PositionComponent pos = new PositionComponent((int) (Math.random() * graphics().width()),
                    (int) (Math.random() * graphics().height()), 0);
            MovementComponent mov = new MovementComponent(-(((int) (Math.random() * 5)) * 2 + 1), 0,
                    MovementComponent.MotionType.LINEAR, true);
            AppearanceComponent app = new AppearanceComponent(0xffffffff);
            StarComponent starComp = new StarComponent((int) (Math.random() * 4));
            star.addComponent(pos);
            star.addComponent(mov);
            star.addComponent(app);
            star.addComponent(starComp);
            star.refresh();
        }
    }

}
