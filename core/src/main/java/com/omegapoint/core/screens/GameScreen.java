package com.omegapoint.core.screens;

import com.artemis.*;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.Bullets;
import com.omegapoint.core.Debug;
import com.omegapoint.core.Enemies;
import com.omegapoint.core.components.*;
import com.omegapoint.core.data.EntityTemplates;
import com.omegapoint.core.events.*;
import com.omegapoint.core.Playfield;
import com.omegapoint.core.predicates.CollisionPredicate;
import com.omegapoint.core.predicates.PredicateAction;
import com.omegapoint.core.util.Scheduler;
import playn.core.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

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
    private Enemies enemies;
    private Scheduler scheduler;
    private Entity shipEntity;

    private PositionComponent shipPosition;
    private Entity tileEntity;
    private boolean inited = false;
    private TileComponent tileComponent;
    private Image shipImage;
    private Entity frameRateEntity;
    private TextComponent fpsComponent;
    private double fps;
    private final ScreenStack screens;
    private Entity waveEntity;
    private int lastShipX;
    private int lastShipY;
    private Entity shield;

    @Inject
    public GameScreen(World world,
                      SystemManager systemManager,
                      Playfield playfield,
                      ScreenStack screens,
                      @Named("updateSystems") List<EntitySystem> updateSystems,
                      @Named("renderSystems") List<EntitySystem> renderSystems,
                      EntityTemplates templateManager,
                      EventBus eventBus,
                      Enemies enemies,
                      Scheduler scheduler) {
        this.world = world;
        this.systemManager = systemManager;
        this.playfield = playfield;
        this.screens = screens;
        this.updateSystems = updateSystems;
        this.renderSystems = renderSystems;
        this.templateManager = templateManager;
        this.eventBus = eventBus;
        this.enemies = enemies;
        this.scheduler = scheduler;
    }

    public GroupLayer layer() {
        return layer;
    }

    public void init() {

        if (inited) {
            return;
        }
        inited = true;

        layer().add(playfield.layer());
        eventBus.addHandler(BulletDeleteEvent.TYPE, new BulletDeleteHandler() {
            @Override
            public void onBulletDelete(BulletDeleteEvent bulletDeleteEvent) {
                Bullets.dec();
            }
        });

        eventBus.addHandler(EnemyDeleteEvent.TYPE, new EnemyDeleteHandler() {
            @Override
            public void onEnemyDelete(EnemyDeleteEvent event) {
                enemies.decrementCurrentLive();
            }
        });

        eventBus.addHandler(EnemyKilledEvent.TYPE, new EnemyKilledHandler() {
            int numKilled = 0;

            @Override
            public void onEnemyKilled(EnemyKilledEvent event) {
                numKilled++;
                if (numKilled % 5 == 0) {
                    GameScreen.useBeam = !GameScreen.useBeam;
                }
            }
        });
        initEntities();
    }

    private void initEntities() {
        systemManager.initializeAll();
        shipEntity = null;
        tileEntity = templateManager.lookupAndInstantiate("level1tiles", world);
        waveEntity = templateManager.lookupAndInstantiate("wave1", world);
        shipPosition = makeShip();
        makeGameBounds();
        makeStars();
        makeBackgroundMusic();
        if (Debug.isShowFrameRateEnabled()) {
            makeFrameRate();
        } else {
            frameRateEntity = null;
            fpsComponent = null;
        }
    }

    private Entity makeFrameRate() {
        frameRateEntity = world.createEntity();
        fpsComponent = new TextComponent(getFps() + " fps",
                graphics().createFont("Space Age", Font.Style.PLAIN, 30), TextFormat.Alignment.LEFT, 0xffffffff);
        frameRateEntity.addComponent(fpsComponent);
        frameRateEntity.addComponent(new PositionComponent(0, 90, 0));
        frameRateEntity.refresh();
        return frameRateEntity;
    }

    private String getFps() {
        return String.valueOf(fps);
    }


    public void reset() {
        world = new World();
        systemManager = world.getSystemManager();
        for (EntitySystem sys : updateSystems) {
            systemManager.setSystem(sys);
        }

        for (EntitySystem sys : renderSystems) {
            systemManager.setSystem(sys);
        }
        playfield.layer().clear();
        initEntities();
        Enemies.reset();
        Bullets.reset();
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
        PlayN.keyboard().setListener(null);
    }

    @Override
    public void wasShown() {
        super.wasShown();
        setupControls();
    }

    private void setupControls() {
        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyTyped(Keyboard.TypedEvent event) {
                if (event.typedChar() == 'e') {
                    eventBus.fireEvent(new ChangeStateEvent("pause"));
                }
                if (event.typedChar() == 'r') {
                    reset();
                }
            }
        });
        listenMouse();
    }

    private void listenMouse() {
        if (PlayN.mouse() != null) {
            PlayN.mouse().setListener(new Mouse.Adapter() {
                @Override
                public void onMouseUp(Mouse.ButtonEvent event) {
                    fireBullet((int) event.x(), (int) event.y());
                }

                @Override
                public void onMouseMove(Mouse.MotionEvent event) {
                    lastShipX = (int) event.x();
                    shipPosition.setX(lastShipX);
                    lastShipY = (int) event.y();
                    shipPosition.setY(lastShipY);
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
        if (shipEntity != null) {
            return shipPosition;
        }
        shipEntity = templateManager.lookupAndInstantiate("playerShip", world);
        shipPosition = shipEntity.getComponent(PositionComponent.class);
        shipPosition.setX(lastShipX);
        shipPosition.setY(lastShipY);

        SpriteComponent ship = new ComponentMapper<SpriteComponent>(SpriteComponent.class, world).get(shipEntity);
        final Image image = PlayN.assets().getImage(ship.getImg());
        tileComponent = new ComponentMapper<TileComponent>(TileComponent.class, world).get(tileEntity);
        image.addCallback(new ResourceCallback<Image>() {
            @Override
            public void done(final Image resource) {
                shipImage = resource;
                // sprite has a lot of whitespace, this is a tighter bounding box
                CollisionComponent colComp = new CollisionComponent(16, 6, 60, 40,
                        new CollisionPredicate() {
                            @Override
                            public boolean collides(Entity entity, Entity collidesWith, World world) {
                                if (shipEntity == null) {
                                    return false;
                                }
                                String group = world.getGroupManager().getGroupOf(collidesWith);
                                if ("ENEMY".equals(group) || "BULLET".equals(group)) {
                                    return true;
                                }

                                return false;
                            }

                            @Override
                            public PredicateAction[] actions() {

                                return new PredicateAction[]  {
                                  new PredicateAction() {

                                      @Override
                                      public void exec(EventBus eventBus, World world, EntityTemplates templateManager, Entity... collisionEntities) {
                                          blowUpShip();
                                      }
                                  }
                                };
                            }
                        });
                shipEntity.addComponent(colComp);

            }

            @Override
            public void error(Throwable err) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        shield = templateManager.lookupAndInstantiate("shield", world);
        shield.addComponent(shipPosition);
        shield.refresh();
        return shipPosition;
    }

    private void blowUpShip() {
        Entity explosionEntity = templateManager.lookupAndInstantiate("explosion", world);
        // explosion starts with same speed and position of the object that blew up

        explosionEntity.addComponent(new PositionComponent(shipPosition.getX(), shipPosition.getY(), 0));
        explosionEntity.refresh();

        shipEntity.delete();
        shipEntity = null;
        shield.delete();
        shield = null;
        eventBus.fireEvent(new PlayerKilledEvent());
        scheduler.schedule(2000, new Runnable() {
            @Override
            public void run() {
                makeShip();
            }
        });
    }

    private void makeGameBounds() {
        templateManager.lookupAndInstantiate("topBounds", world);
        templateManager.lookupAndInstantiate("leftBounds", world);
        templateManager.lookupAndInstantiate("rightBounds", world);
        templateManager.lookupAndInstantiate("bottomBounds", world);
    }

    private int lastFire = 0;

    private void fireBullet(int x, int y) {
        if (Bullets.maxedOut() || isShipDead()) {
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

    private boolean isShipDead() {
        return shipEntity == null;
    }


    double fpsCountStart = -1;
    int fpsNumFrames = 0;

    @Override
    public void paint(float alpha) {
        super.paint(alpha);
        updateFps();
        for (EntitySystem es : renderSystems) {
            es.process();
        }

        maybeDisplayFps();
    }

    private void maybeDisplayFps() {
        if (Debug.isShowFrameRateEnabled()) {
            if (fpsNumFrames >= 60) {
                fps = (int) (1000 * fpsNumFrames / (PlayN.currentTime() - fpsCountStart));
                fpsNumFrames = 0;
                if (frameRateEntity != null) {
                    frameRateEntity.delete();
                    makeFrameRate();
                }
            }
        }
    }

    private void updateFps() {
        if (Debug.isShowFrameRateEnabled()) {
            if (fpsNumFrames == 0) {
                fpsCountStart = PlayN.currentTime();
            }
            fpsNumFrames++;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        world.loopStart(); // signals loop start
        world.setDelta((int) delta); // sets delta for systems to use

        scheduler.update(delta);

        for (EntitySystem es : updateSystems) {
            es.process();
        }

        if (shipEntity != null && shipImage != null && tileComponent != null && tileComponent.collides(shipPosition, shipImage.height(),
                shipImage.width())) {
            blowUpShip();
        }

    }

    public static boolean useBeam = true;
}
