package com.omegapoint.core.screens;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.Playfield;
import com.omegapoint.core.components.*;
import com.omegapoint.core.data.EntityTemplates;
import com.omegapoint.core.events.ChangeStateEvent;
import com.omegapoint.core.systems.*;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Mouse;
import playn.core.PlayN;
import react.UnitSlot;
import tripleplay.game.UIAnimScreen;
import tripleplay.ui.Button;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.layout.AxisLayout;

import javax.inject.Inject;

import static playn.core.PlayN.graphics;

/**
 *
 */
public class TileEditorScreen extends UIAnimScreen {


    public Root _root;
    private EntityTemplates templateManager;
    private EventBus eventBus;
    private final World world;
    private final Playfield editorPlayField;
    private final StarRenderSystem starSystem;
    private final MovementSystem movementSystem;
    private final TileEditorRenderSystem tileEditorSystem;
    private final Entity tileEntity;
    private final TileComponent tileComponent;

    @Inject
    public TileEditorScreen(EntityTemplates templateManager, EventBus eventBus) {
        this.templateManager = templateManager;
        this.eventBus = eventBus;
        this.eventBus = eventBus;
        this.world = new World();
        editorPlayField = new Playfield();
        this.starSystem = new StarRenderSystem(editorPlayField);
        this.tileEditorSystem = new TileEditorRenderSystem(editorPlayField);
        this.world.getSystemManager().setSystem(this.starSystem);
        this.world.getSystemManager().setSystem(this.tileEditorSystem);
        movementSystem = new MovementSystem();
        this.world.getSystemManager().setSystem(movementSystem);
        this.world.getSystemManager().initializeAll();
        makeStars();
        tileEntity = templateManager.lookupAndInstantiate("level1tiles", world);
        tileComponent = new ComponentMapper<TileComponent>(TileComponent.class, world).get(tileEntity);

    }

    int dir = 0;
    @Override
    public void wasAdded() {
        super.wasAdded();
        PlayN.mouse().setListener(new Mouse.Listener() {
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                if (event.button() == Mouse.BUTTON_LEFT) {
                  tileComponent.clickTile(event.localX(), event.localY());
                } else {
                  tileComponent.eraseTile(event.localX(), event.localY());
                }
            }

            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onMouseMove(Mouse.MotionEvent event) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onMouseWheelScroll(Mouse.WheelEvent event) {
               updateTilePosition((int) (10 * Math.signum(event.velocity())));
            }
        });

        layer.add(editorPlayField.layer());
        _root = iface.createRoot(AxisLayout.vertical().gap(10).offEqualize(), SimpleStyles.newSheet(), layer);
        _root.setSize(width(), height());
        Button button5 = new Button("Back");
        _root.add(button5);
        button5.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                _root.setVisible(false);
                eventBus.fireEvent(new ChangeStateEvent("pause"));
            }
        });
        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyDown(Keyboard.Event event) {
                super.onKeyDown(event);
                if (event.key() == Key.LEFT) {
                    dir = -10;
                    tileComponent.setCurrentScreenPosition(tileComponent.getCurrentScreenPosition() - 10);
                    tileEntity.refresh();
                } else if (event.key() == Key.RIGHT) {
                    dir = +10;
                    tileComponent.setCurrentScreenPosition(tileComponent.getCurrentScreenPosition() + 10);
                    tileEntity.refresh();
                }
            }

            @Override
            public void onKeyUp(Keyboard.Event event) {
                super.onKeyUp(event);
                if (event.key() == Key.LEFT || event.key() == Key.RIGHT) {
                    dir = 0;
                }
            }
        });

    }

    @Override
    public void wasRemoved() {
        super.wasRemoved();
        iface.destroyRoot(_root);
        PlayN.keyboard().setListener(null);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        world.loopStart(); // signals loop start
        world.setDelta((int) delta); // sets delta for systems to use
        updateTilePosition(dir);
        movementSystem.process();
    }

    private void updateTilePosition(int amt) {
        tileComponent.setCurrentScreenPosition(Math.max(0, tileComponent.getCurrentScreenPosition() + amt));
    }

    @Override
    public void paint(float alpha) {
        super.paint(alpha);
        this.starSystem.process();
    }

    @Override
    protected float updateRate() {
        return 15;
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

