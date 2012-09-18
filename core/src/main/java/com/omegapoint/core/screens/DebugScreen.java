package com.omegapoint.core.screens;

import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.Debug;
import com.omegapoint.core.events.ChangeStateEvent;
import react.UnitSlot;
import tripleplay.game.UIAnimScreen;
import tripleplay.ui.Button;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.ToggleButton;
import tripleplay.ui.layout.AxisLayout;

import javax.inject.Inject;

/**
 *
 */
public class DebugScreen extends UIAnimScreen {


    public Root _root;
    private EventBus eventBus;

    @Inject
    public DebugScreen(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();
        _root = iface.createRoot(AxisLayout.vertical().gap(10).offEqualize(), SimpleStyles.newSheet(), layer);
        _root.setSize(width(), height());
        final ToggleButton button = new ToggleButton("Collision Bounding Boxes?");
        _root.add(button);
        if (Debug.isCollisionBoundingBoxesEnabled()) {
            //button.setSelected(true);
        }
        button.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                //Debug.setCollisionBoundingBoxes(button.isSelected());
            }
        });
        final ToggleButton frameRateButton = new ToggleButton("Show Frame Rate?");
        _root.add(frameRateButton);
        if (Debug.isShowFrameRateEnabled()) {
            //button.setSelected(true);
        }
        frameRateButton.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                //Debug.setShowFrameRate(frameRateButton.isSelected());
            }
        });

        Button button5 = new Button("Return to Menu");
        _root.add(button5);
        button5.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                _root.setVisible(false);
                eventBus.fireEvent(new ChangeStateEvent("pause"));
            }
        });
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
}

