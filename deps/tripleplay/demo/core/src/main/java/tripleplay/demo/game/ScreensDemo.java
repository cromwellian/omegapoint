//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011-2012, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package tripleplay.demo.game;

import react.UnitSlot;

import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Button;
import tripleplay.ui.Elements;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.layout.AxisLayout;

import tripleplay.demo.DemoScreen;

/**
 * Tests/demonstrates screen-related things.
 */
public class ScreensDemo extends DemoScreen
{
    public ScreensDemo (ScreenStack stack) {
        _stack = stack;
    }

    @Override protected String name () {
        return "Screens";
    }
    @Override protected String title () {
        return "Screen Stack and Transitions";
    }

    @Override protected Group createIface () {
        Group root = new Group(AxisLayout.vertical());
        addUI(this, root, 0);
        return root;
    }

    protected void addUI (final Screen screen, Elements<?> root, final int depth) {
        root.add(new Label("Screen " + depth));

        addButton(root, "Slide", new UnitSlot() { public void onEmit () {
            _stack.push(createScreen(depth+1), _stack.slide());
        }});
        addButton(root, "Turn", new UnitSlot() { public void onEmit () {
            _stack.push(createScreen(depth+1), _stack.pageTurn());
        }});
        addButton(root, "Flip", new UnitSlot() { public void onEmit () {
            _stack.push(createScreen(depth+1), _stack.flip());
        }});

        if (depth > 0) {
            addButton(root, "Replace", new UnitSlot() { public void onEmit () {
                _stack.replace(createScreen(depth+1), _stack.slide().left());
            }});
            addButton(root, "Back", new UnitSlot() { public void onEmit () {
                _stack.remove(screen, _stack.flip().unflip());
            }});
            addButton(root, "Top", new UnitSlot() { public void onEmit () {
                _stack.popTo(ScreensDemo.this, _stack.slide().right());
            }});
        }
    }

    protected void addButton (Elements<?> root, String label, UnitSlot onClick) {
        Button button = new Button(label);
        button.clicked().connect(onClick);
        root.add(button);
    }

    protected Screen createScreen (final int depth) {
        return new TestScreen(depth) {
            @Override public String toString () {
                return "Screen" + depth;
            }
            @Override protected void createIface () {
                addUI(this, _root, depth);
            }
        };
    }

    protected final ScreenStack _stack;
}
