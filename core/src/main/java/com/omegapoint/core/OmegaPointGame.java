package com.omegapoint.core;

import static playn.core.PlayN.*;

import com.artemis.Entity;
import com.artemis.SystemManager;
import com.artemis.World;
import com.omegapoint.core.components.*;
import com.omegapoint.core.systems.*;
import playn.core.*;

public class OmegaPointGame implements Game {
    private World world;
    private Entity shipEntity;
    private final SpriteRenderSystem renderSystem = new SpriteRenderSystem();
    private final MovementSystem movementSystem = new MovementSystem();
    private final AudioSystem audioSystem = new AudioSystem();
    private final CollisionSystem collisionSystem = new CollisionSystem();
    private final EnemySystem enemySystem = new EnemySystem();
    private final BeamRenderSystem beamRenderSystem = new BeamRenderSystem();

    private SurfaceLayer bgLayer;
    private Star[] stars = new Star[50];
    private Entity topBounds;
    private Entity leftBounds;
    private Entity rightBounds;
    private Entity bottomBounds;
    private int bullets = 0;
    private static int MAX_BULLETS = 5;

    static class Star {
        public int x, y;
        private int speed;
        private int color;
        private static int alphas[] = {0x40000000, 0x80000000, 0xc0000000, 0xff000000};

        Star(int x, int y) {
            this.x = x;
            this.y = y;
            this.speed = ((int) (Math.random() * 5)) * 2 + 1;
            this.color = Math.min((int) (Math.random() * alphas.length), alphas.length);
        }

        public void update(float delta) {
            this.x -= (this.speed * delta / 60);
            if (this.x <= 0) {
                this.x = this.x + graphics().width();
            }
        }

        public void draw(Surface canvas) {
            canvas.setFillColor(0xffffff | alphas[this.color]);
            canvas.fillRect(x, y, 3, 3);
        }
    }

    @Override
    public void init() {
        if (PlayN.platformType() == Platform.Type.ANDROID) {
            PlayN.graphics().setSize(PlayN.graphics().screenWidth(), PlayN.graphics().screenHeight());
        } else {
            PlayN.graphics().setSize(960, 640);
        }
        // create and add background image layer
        bgLayer = graphics().createSurfaceLayer(graphics().width(), graphics().height());
        graphics().rootLayer().add(bgLayer);
        world = new World();
        SystemManager systemManager = world.getSystemManager();
        systemManager.setSystem(renderSystem);
        systemManager.setSystem(movementSystem);
        systemManager.setSystem(audioSystem);
        systemManager.setSystem(collisionSystem);
        systemManager.setSystem(enemySystem);
        systemManager.setSystem(beamRenderSystem);

        systemManager.initializeAll();
        shipEntity = world.createEntity();
        shipEntity.addComponent(new SpriteComponent("images/ship.png"));
        final PositionComponent shipPosition = new PositionComponent(0, 0, -Math.PI / 2);
        shipEntity.addComponent(shipPosition);
        shipEntity.refresh();

        topBounds = world.createEntity();
        topBounds.addComponent(new CollisionComponent(0, 0, graphics().width() + 200, 100));
        topBounds.addComponent(new PositionComponent(-100, -100, 0));
        topBounds.setGroup("BOUNDS");
        topBounds.refresh();

        leftBounds = world.createEntity();
        leftBounds.addComponent(new CollisionComponent(0, 0, 100, graphics().height() + 200));
        leftBounds.addComponent(new PositionComponent(-100, -100, 0));
        leftBounds.setGroup("BOUNDS");

        leftBounds.refresh();

        rightBounds = world.createEntity();
        rightBounds.addComponent(new CollisionComponent(0, 0, 100, graphics().height() + 200));
        rightBounds.addComponent(new PositionComponent(graphics().width(), -100, 0));
        rightBounds.setGroup("BOUNDS");

        rightBounds.refresh();

        bottomBounds = world.createEntity();
        bottomBounds.addComponent(new CollisionComponent(0, 0, graphics().width() + 200, 100));
        bottomBounds.addComponent(new PositionComponent(-100, graphics().height(), 0));
        bottomBounds.setGroup("BOUNDS");

        bottomBounds.refresh();

        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyTyped(Keyboard.TypedEvent event) {
            }
        });
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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star((int) (Math.random() * graphics().width()), (int) (Math.random() * graphics().height()));
        }
    }

    private int lastFire = 0;

    private void fireBullet(int x, int y) {
        if (bullets >= MAX_BULLETS) {
            return;
        }

        final Entity bulletEntity = world.createEntity();
        CollisionPredicate boundsPredicate = new CollisionPredicate() {
            @Override
            public boolean collides(Entity e, Entity collidesWith) {
                if (e == bulletEntity) {
                    return collidesWith == topBounds || collidesWith == bottomBounds || collidesWith == leftBounds
                            || collidesWith == rightBounds || "ENEMY".equals(world.getGroupManager().getGroupOf(collidesWith));
                }
                return false;
            }

            @Override
            public PredicateAction[] actions() {
                PredicateAction actions[] = new PredicateAction[1];
                actions[0] = new PredicateAction() {

                    @Override
                    public void exec(Entity... collisionEntities) {
                        bulletEntity.delete();
                        bullets--;
                    }
                };
                return actions;
            }
        };
        buildLaser(x, y, bulletEntity, boundsPredicate);
//        buildBeam(x, y, bulletEntity, boundsPredicate);

        bulletEntity.refresh();
        bullets++;
    }

    private void buildLaser(int x, int y, Entity bulletEntity, CollisionPredicate boundsPredicate) {
        bulletEntity.addComponent(new PositionComponent(x, y, 0));
        bulletEntity.addComponent(new MovementComponent(20, 0, MovementComponent.MotionType.LINEAR));
        bulletEntity.addComponent(new AudioComponent("sounds/laser"));
        bulletEntity.addComponent(new SpriteComponent("images/lasershot.png"));
        bulletEntity.addComponent(new CollisionComponent(0, 0, 32, 8, boundsPredicate));
        bulletEntity.addComponent(new DamageComponent());
    }

    private void buildBeam(int x, int y, Entity bulletEntity, CollisionPredicate boundsPredicate) {
        bulletEntity.addComponent(new PositionComponent(x, y, 0));
        bulletEntity.addComponent(new MovementComponent(20, 0, MovementComponent.MotionType.LINEAR));
        bulletEntity.addComponent(new AudioComponent("sounds/laserheavy"));
        bulletEntity.addComponent(new BeamComponent(128, 0xffff0000));
        bulletEntity.addComponent(new CollisionComponent(0, 0, 32, 8, boundsPredicate));
        bulletEntity.addComponent(new DamageComponent());
    }

    @Override
    public void paint(float alpha) {
        bgLayer.surface().setFillColor(0xff000000);
        bgLayer.surface().fillRect(0, 0, graphics().width(), graphics().height());
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(bgLayer.surface());
        }
        beamRenderSystem.process();
        renderSystem.process();
        audioSystem.process();
    }

    @Override
    public void update(float delta) {
        world.loopStart(); // signals loop start
        world.setDelta((int) delta); // sets delta for systems to use
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }
        enemySystem.process();
        movementSystem.process();
        collisionSystem.process();
    }

    @Override
    public int updateRate() {
        return 25;
    }
}
