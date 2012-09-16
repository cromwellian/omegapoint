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
import playn.core.*;
import react.UnitSlot;
import tripleplay.game.UIAnimScreen;
import tripleplay.ui.Button;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.layout.AxisLayout;

import javax.inject.Inject;

import java.util.Arrays;

import static playn.core.PlayN.graphics;

/**
 *
 */
public class TileEditorScreen extends UIAnimScreen {


    private final TileEditorScreen.TileSelector tileSelector;
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
    private int selDir;
    private boolean erasing;
    private boolean drawing;
    private final String tileEntityName;

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
        tileEntityName = "level1tiles";
        tileEntity = templateManager.lookupAndInstantiate(tileEntityName, world);
        tileComponent = new ComponentMapper<TileComponent>(TileComponent.class, world).get(tileEntity);
        tileSelector = new TileSelector();
        editorPlayField.layer().add(graphics().createImmediateLayer(tileSelector));

    }

    int dir = 0;

    @Override
    public void wasAdded() {
        super.wasAdded();
        PlayN.mouse().setListener(new Mouse.Listener() {
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                if (event.button() == Mouse.BUTTON_LEFT) {

                    TileComponent.TilePos pos = tileComponent.setTile(event.localX(), event.localY(), tileSelector.getCurrentTile());
                    if (pos != null) {
                        drawing = true;
                    }
                } else {
                    TileComponent.TilePos pos = tileComponent.eraseTile(event.localX(), event.localY());
                    if (pos != null) {
                        erasing = true;
                    }
                }
                tileSelector.select(event.localX(), event.localY());
            }

            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                drawing = erasing = false;
            }

            @Override
            public void onMouseMove(Mouse.MotionEvent event) {
                if (drawing) {
                    tileComponent.setTile(event.localX(), event.localY(), tileSelector.getCurrentTile());
                } else if (erasing) {
                    tileComponent.eraseTile(event.localX(), event.localY());
                }
            }

            @Override
            public void onMouseWheelScroll(Mouse.WheelEvent event) {
                updateTilePosition((int) (10 * Math.signum(event.velocity())));
            }
        });

        layer.add(editorPlayField.layer());
        _root = iface.createRoot(AxisLayout.vertical().gap(10).offEqualize(), SimpleStyles.newSheet(), layer);
        _root.setSize(width(), height());
        Button nowEditing = new Button("Editing: " + tileEntityName);
        _root.add(nowEditing);
        Button backButton = new Button("Back");
        _root.add(backButton);
        backButton.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                _root.setVisible(false);
                templateManager.persist(tileEntityName, world, tileEntity);
                eventBus.fireEvent(new ChangeStateEvent("pause"));
            }
        });
        final Button clearAll = new Button("Clear All");
        _root.add(clearAll);
        clearAll.clicked().connect(new UnitSlot() {
            private void removeConfirm(Button b1, Button b2) {
                _root.remove(b1);
                _root.remove(b2);
                clearAll.setVisible(true);
            }

            @Override
            public void onEmit() {
               final Button confirmButton = new Button("Sure?");
               final Button cancelButton = new Button("Cancel");
               clearAll.setVisible(false);
                _root.add(confirmButton);
                confirmButton.clicked().connect(new UnitSlot() {
                    @Override
                    public void onEmit() {
                        TileComponent.TileRow[] rows = tileComponent.getArrangement().getRows();
                        for (TileComponent.TileRow row : rows) {
                            Arrays.fill(row.getIndices(), TileComponent.EMPTY_SPACE);
                        }
                        tileEntity.refresh();
                        removeConfirm(confirmButton, cancelButton);
                    }
                });
                cancelButton.clicked().connect(new UnitSlot() {
                    @Override
                    public void onEmit() {
                        removeConfirm(confirmButton, cancelButton);
                    }
                });
                _root.add(cancelButton);

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
                } else if (event.key() == Key.UP) {
                    selDir = 1;
                } else if (event.key() == Key.DOWN) {
                    selDir = -1;
                }
            }

            @Override
            public void onKeyUp(Keyboard.Event event) {
                super.onKeyUp(event);
                if (event.key() == Key.LEFT || event.key() == Key.RIGHT) {
                    dir = 0;
                } else if (event.key() == Key.UP || event.key() == Key.DOWN) {
                    selDir = 0;
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
        tileSelector.increment(selDir);
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

    private class TileSelector implements ImmediateLayer.Renderer {
        public static final int STARTLINE = 20;
        private final Image image;
        private final Image.Region tileRegion;
        private final int selectorBottomY;

        private TileSelector() {
            image = PlayN.assets().getImage(tileComponent.getAssetPath());
            tileRegion = image.subImage(0, 0, tileComponent.getTileWidth(), tileComponent.getTileHeight());
            selectorBottomY = tileComponent.getTileHeight()*2+STARTLINE;
        }

        int currentTile = 0;
        @Override
        public void render(Surface surface) {

            float startx = 1;
            float starty = STARTLINE;
            int maxIndice = tileComponent.getTileRows() * tileComponent.getTileCols();
            int iconWidth = tileComponent.getTileWidth() * 2;
            int iconHeight = tileComponent.getTileHeight() * 2;
            for (int i = currentTile; i < maxIndice; i++) {
                int spriteCol = i % tileComponent.getTileCols();
                int spriteRow = i / tileComponent.getTileCols();
                int tileOffsetX = spriteCol * tileComponent.getTileWidth();
                int tileOffsetY = spriteRow * tileComponent.getTileHeight();
                tileRegion.setBounds(tileOffsetX, tileOffsetY, tileComponent.getTileWidth(),
                        tileComponent.getTileHeight());
                surface.drawImage(tileRegion, startx, starty, iconWidth,
                        iconHeight);
                startx += iconWidth + 2;
                if (startx > graphics().width() - 20 - iconWidth) {
                    break;
                }
            }
            surface.setFillColor(0xffffffff);
            surface.drawLine(1, STARTLINE, iconWidth + 1, STARTLINE, 2);
            surface.drawLine(1, STARTLINE, 1, iconHeight + STARTLINE, 2);
            surface.drawLine(1, selectorBottomY, iconWidth + 1, selectorBottomY, 2);
            surface.drawLine(iconWidth + 1, STARTLINE, iconWidth + 1, selectorBottomY, 2);

        }

        public int getCurrentTile() {
            return currentTile;
        }

        public void increment(int selDir) {
            currentTile = Math.max(Math.min(currentTile + selDir, tileComponent.getTileCols() * tileComponent.getTileRows()), 0);
        }

        public void select(float x, float y) {
            if (y >= STARTLINE && y <=selectorBottomY) {
               x = x - 1;
               x = x / (tileComponent.getTileWidth() * 2 + 2);
               currentTile += x;
            }
        }
    }
}

