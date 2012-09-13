package com.omegapoint.core.state;

import com.google.web.bindery.event.shared.EventBus;
import com.omegapoint.core.events.ChangeStateEvent;
import react.UnitSlot;
import tripleplay.game.UIAnimScreen;
import tripleplay.ui.Button;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.layout.AxisLayout;

import javax.inject.Inject;

/**
 *
 */
public class PauseMenuScreen extends UIAnimScreen {


    public Root _root;
    private EventBus eventBus;

    @Inject
    public PauseMenuScreen(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();
        _root = iface.createRoot(AxisLayout.vertical().gap(10).offEqualize(), SimpleStyles.newSheet(), layer);
        _root.setSize(width(), height());
        Button button = new Button("Inventory");
        _root.add(button);
        Button button2 = new Button("Entity Browser");
        _root.add(button2);
        Button button3 = new Button("Level Editor");
        _root.add(button3);
        Button button4 = new Button("Debug Console");
        _root.add(button4);
        Button button5 = new Button("Return to Game");
        _root.add(button5);
        button5.clicked().connect(new UnitSlot() {
            @Override
            public void onEmit() {
                _root.setVisible(false);
                eventBus.fireEvent(new ChangeStateEvent("play"));
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

