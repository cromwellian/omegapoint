//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011-2012, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package tripleplay.demo.ui;

import playn.core.Image;
import playn.core.PlayN;

import react.Function;

import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.CheckBox;
import tripleplay.ui.Constraints;
import tripleplay.ui.Field;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Shim;
import tripleplay.ui.Style;
import tripleplay.ui.Styles;
import tripleplay.ui.ToggleButton;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.TableLayout;

import tripleplay.demo.DemoScreen;

/**
 * Displays various UI stuff.
 */
public class MiscDemo extends DemoScreen
{
    @Override protected String name () {
        return "General";
    }
    @Override protected String title () {
        return "UI: General";
    }

    @Override protected Group createIface () {
        Image smiley = PlayN.assets().getImage("images/smiley.png");
        final Image squares = PlayN.assets().getImage("images/squares.png");

        Styles wrapped = Styles.make(Style.TEXT_WRAP.is(true));
        Styles greenBg = Styles.make(Style.BACKGROUND.is(Background.solid(0xFF99CC66).inset(5)));
        Styles redBg = Styles.make(Style.BACKGROUND.is(Background.solid(0xFFCC6666).inset(5)));

        CheckBox toggle, toggle2;
        ToggleButton toggle3;
        Button disabled;
        Label label2;
        Group iface = new Group(new TableLayout(2).gaps(10, 10)).add(
            new Label("Toggling visibility"),
            new Label("Buttons"),
            // labels, visibility and icon toggling
            new Group(AxisLayout.horizontal().gap(15), greenBg).add(
                new Group(AxisLayout.vertical()).add(
                    new Group(AxisLayout.horizontal()).add(
                        toggle = new CheckBox(),
                        new Label("Toggle Viz")),
                    new Group(AxisLayout.horizontal()).add(
                        toggle2 = new CheckBox(),
                        new Label("Toggle Icon"))),
                new Group(AxisLayout.vertical()).add(
                    new Label("Label 1").addStyles(redBg),
                    label2 = new Label("Label 2"),
                    new Label("Label 3", smiley))),
            // buttons, toggle buttons, wirey uppey
            new Group(AxisLayout.horizontal().gap(15), greenBg).add(
                toggle3 = new ToggleButton("Toggle Enabled"),
                disabled = new Button("Disabled")),
            new Label("Icon positioning"),
            new Label("Text editing"),
            // labels with varying icon alignment
            new Group(AxisLayout.horizontal().gap(10), greenBg).add(
                new Label("Left", tile(squares, 0)).setStyles(Style.ICON_POS.left),
                new Label("Right", tile(squares, 1)).setStyles(Style.ICON_POS.right),
                new Label("Above", tile(squares, 2)).setStyles(Style.ICON_POS.above,
                                                               Style.HALIGN.center),
                new Label("Below", tile(squares, 3)).setStyles(Style.ICON_POS.below,
                                                               Style.HALIGN.center)),
            // an editable text field
            new Group(AxisLayout.horizontal().gap(10)).add(
                new Field("Editable text").setConstraint(Constraints.fixedWidth(150)),
                new Field("Disabled text").setEnabled(false)));

        toggle.checked.update(true);
        toggle.checked.connect(label2.visibleSlot());
        toggle2.checked.map(new Function<Boolean,Image>() {
            public Image apply (Boolean checked) {
                return checked ? tile(squares, 0) : null;
            }
        }).connect(label2.icon.slot());

        toggle3.selected.connectNotify(disabled.enabledSlot());
        toggle3.selected.map(new Function<Boolean,String>() {
            public String apply (Boolean selected) { return selected ? "Enabled" : "Disabled"; }
        }).connectNotify(disabled.text.slot());

        return iface;
    }

    protected Image tile (Image image, int index) {
        final float iwidth = 16, iheight = 16;
        return image.subImage(index*iwidth, 0, iwidth, iheight);
    }
}
