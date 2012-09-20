//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package tripleplay.ui;

import react.Slot;
import react.Value;

/**
 * Extends the {@link ClickableTextWidget} with toggling behavior.
 */
public abstract class TogglableTextWidget<T extends TogglableTextWidget<T>>
    extends ClickableTextWidget<T>
    implements Togglable<T>
{
    /** Indicates whether this widget is selected. It may be listened to, and updated. */
    public final Value<Boolean> selected = Value.create(false);

    @Override // from Togglable
    public Value<Boolean> selected () {
        return selected;
    }

    protected TogglableTextWidget () {
        selected.connect(new Slot<Boolean>() {
            public void onEmit (Boolean selected) {
                if (selected != isSelected()) {
                    set(Flag.SELECTED, selected);
                    invalidate();
                }
            }
        });
    }

    /** Called when the mouse is clicked on this widget. */
    protected void onPress () {
        _anchorState = isSelected();
        selected.update(!_anchorState);
    }

    /** Called as the user drags the pointer around with the widget depressed. */
    protected void onHover (boolean inBounds) {
        selected.update(inBounds ? !_anchorState : _anchorState);
    }

    @Override protected void onRelease () {
        // we explicitly don't call super here
        if (_anchorState != isSelected()) {
            onClick();
        }
    }

    @Override protected void onCancel () {
        // we explicitly don't call super here
        selected.update(_anchorState);
    }

    protected boolean _anchorState;
}
