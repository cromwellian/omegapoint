package com.omegapoint.core.state;

import com.artemis.Entity;
import com.artemis.World;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.components.*;
import com.omegapoint.core.events.ChangeStateEvent;
import com.omegapoint.core.systems.*;
import playn.core.CanvasImage;
import playn.core.PlayN;
import playn.core.ResourceCallback;
import playn.core.Sound;
import react.UnitSlot;
import tripleplay.game.UIScreen;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import javax.inject.Inject;

import static playn.core.PlayN.graphics;

/**
 *
 */
public class IntroScreen extends UIScreen {
    private final EntityTemplates templateManager;
    private EventBus eventBus;
    private final World world;
    private final MovementSystem movementSystem;
    private final TextRenderSystem textRenderSystem;
    private final SimpleTweenSystem simpleTweenSystem;
    private CanvasImage title;
    private final StarRenderSystem starSystem;
    private final Playfield introPlayField;
    private Sound sound;
    private Root _root;

    @Inject
    public IntroScreen(EntityTemplates templateManager, EventBus eventBus) {
        //TODO(ray) clean this up, use injection and share more code with GameScreen
        this.templateManager = templateManager;
        this.eventBus = eventBus;
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

        templateManager.lookupAndInstantiate("titleText", world);
        templateManager.lookupAndInstantiate("titleTextCredits", world);
    }

    @Override
    public void wasAdded() {
        super.wasAdded();
        layer.add(introPlayField.layer());
        sound = PlayN.assets().getSound("sounds/cybernoid2");
        sound.addCallback(new ResourceCallback<Sound>() {
            @Override
            public void done(Sound resource) {
                resource.setLooping(true);
                resource.setVolume(1);
                resource.play();
            }

            @Override
            public void error(Throwable err) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
         _root = iface.createRoot(AxisLayout.vertical().gap(10).offEqualize(), SimpleStyles.newSheet(), layer);
        _root.setSize(width(), height());
        Button button = new Button("Start Game");
        button.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                eventBus.fireEvent(new ChangeStateEvent("play"));
            }
        });
        _root.add(button);

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
