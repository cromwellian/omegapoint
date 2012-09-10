package com.omegapoint.core;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.SystemManager;
import com.artemis.World;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.components.*;
import com.omegapoint.core.events.BulletDeleteEvent;
import com.omegapoint.core.events.BulletDeleteHandler;
import com.omegapoint.core.systems.Playfield;
import playn.core.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIAnimScreen;
import tripleplay.ui.Button;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.layout.AxisLayout;

import javax.inject.Inject;
import java.util.List;

import static playn.core.PlayN.graphics;

/**
 *
 */
public class GameScreen extends Screen {
    private World world;
    private SystemManager systemManager;
    private Playfield playfield;
    private List<EntitySystem> updateSystems;
    private List<EntitySystem> renderSystems;
    private EntityTemplates templateManager;
    private EventBus eventBus;
    private Entity shipEntity;

    private Entity titleTextEntity;
    private PositionComponent shipPosition;
    private ScreenStack screens;

    @Inject
    public GameScreen(World world,
                      SystemManager systemManager,
                      Playfield playfield,
                      ScreenStack screens,
                      @Named("updateSystems") List<EntitySystem> updateSystems,
                      @Named("renderSystems") List<EntitySystem> renderSystems,
                      EntityTemplates templateManager,
                      EventBus eventBus) {
        this.world = world;
        this.systemManager = systemManager;
        this.playfield = playfield;
        this.screens = screens;
        this.updateSystems = updateSystems;
        this.renderSystems = renderSystems;
        this.templateManager = templateManager;
        this.eventBus = eventBus;
    }

    public GroupLayer layer() {
        return layer;
    }

    public void init() {
        systemManager.initializeAll();
        makeTitle();
        shipPosition = makeShip();
        makeGameBounds();
        makeStars();
        makeBackgroundMusic();
        layer().add(playfield.layer());
        eventBus.addHandler(BulletDeleteEvent.TYPE, new BulletDeleteHandler() {
            @Override
            public void onBulletDelete() {
                Bullets.dec();
            }
        });
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

    @Override
    public void wasHidden() {
        super.wasHidden();
        PlayN.mouse().setListener(null);
    }

    @Override
    public void wasShown() {
        super.wasShown();
        setupControls(shipPosition);
    }

    private void setupControls(final PositionComponent shipPosition) {
        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyTyped(Keyboard.TypedEvent event) {
                if (event.typedChar() == 'e') {

                    screens.push(new UIAnimScreen() {
                        public Root _root;

                        @Override
                        public void wasAdded() {
                            super.wasAdded();
                            _root = iface.createRoot(AxisLayout.vertical().gap(0).offStretch(), SimpleStyles.newSheet(), layer);
                            _root.setSize(width(), height());
                            Button button = new Button("Hello");
                            _root.add(button);
                        }

                        @Override
                        public void wasRemoved() {
                            super.wasRemoved();
                            iface.destroyRoot(_root);
                        }

                        @Override
                        protected float updateRate() {
                            return 15;
                        }
                    });
                }
                if (event.typedChar() == 'z') {
                    screens.popTo(GameScreen.this);
                }
            }
        });
        listenMouse(shipPosition);
    }

    private void listenMouse(final PositionComponent shipPosition) {
        if (PlayN.mouse() != null) {
            PlayN.mouse().setListener(new Mouse.Adapter() {
                @Override
                public void onMouseUp(Mouse.ButtonEvent event) {
                    fireBullet((int) event.x(), (int) event.y());
                }

                @Override
                public void onMouseMove(Mouse.MotionEvent event) {
                    shipPosition.setX((int) event.x());
                    shipPosition.setY((int) event.y());
                }
            });
        }
        try {
            if (PlayN.touch() != null) {
                PlayN.touch().setListener(new Touch.Adapter() {
                    @Override
                    public void onTouchMove(Touch.Event[] touches) {
                        Touch.Event event = touches[0];
                        shipPosition.setX((int) (event.x() + 120));
                        shipPosition.setY((int) event.y());
                        lastFire += world.getDelta();
                        if (lastFire >= 500) {
                            lastFire = 0;
                            fireBullet((int) event.x(), (int) event.y());
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeBackgroundMusic() {
        templateManager.lookupAndInstantiate("backgroundMusic", world);
    }

    private PositionComponent makeShip() {
        shipEntity = templateManager.lookupAndInstantiate("playerShip", world);
        shipPosition = shipEntity.getComponent(PositionComponent.class);
        Entity shield = world.createEntity();
        shield.addComponent(shipPosition);
        shield.addComponent(new SpriteComponent("images/shieldAlphaGreen.png", 48, 48, 10, 4, 0, 16, true));
        shield.refresh();
        return shipPosition;
    }

    private void makeTitle() {
        templateManager.lookupAndInstantiate("titleText", world);
    }

    private void makeGameBounds() {
        templateManager.lookupAndInstantiate("topBounds", world);
        templateManager.lookupAndInstantiate("leftBounds", world);
        templateManager.lookupAndInstantiate("rightBounds", world);
        templateManager.lookupAndInstantiate("bottomBounds", world);
    }

    private int lastFire = 0;

    private void fireBullet(int x, int y) {
        if (Bullets.maxedOut()) {
            return;
        }

        Entity bulletEntity = null;
        if (useBeam) {
            bulletEntity = templateManager.lookupAndInstantiate("laserShot", world);
        } else {
            bulletEntity = templateManager.lookupAndInstantiate("beamShot", world);
        }
        bulletEntity.addComponent(new PositionComponent(x, y, 0));
        Bullets.inc();
    }


    int logoFrame = 0;

    @Override
    public void paint(float alpha) {
        super.paint(alpha);
        logoFrame++;
        if (logoFrame > 60 * 7 && titleTextEntity != null) {
            titleTextEntity.delete();
            titleTextEntity = null;
        }
        for (EntitySystem es : renderSystems) {
            es.process();
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        world.loopStart(); // signals loop start
        world.setDelta((int) delta); // sets delta for systems to use

        for (EntitySystem es : updateSystems) {
            es.process();
        }
    }

    public static boolean useBeam = true;
}
