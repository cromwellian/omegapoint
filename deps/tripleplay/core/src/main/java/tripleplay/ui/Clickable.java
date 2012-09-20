//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package tripleplay.ui;

import react.SignalView;

/**
 * Implemented by {@link Element}s that can be clicked.
 */
public interface Clickable<T extends Element<T>>
{
    /** A signal that is emitted when this element is clicked. */
    SignalView<T> clicked();
}
