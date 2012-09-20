//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011-2012, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package tripleplay.game.trans;

import playn.core.Asserts;

import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

/**
 * A base class for transitions that handles duration and the PITA machinery to case return values
 * to the right type.
 */
public abstract class AbstractTransition<T extends AbstractTransition<T>>
    implements ScreenStack.Transition
{
    /** Configures the duration of the transition. */
    public T duration (float duration) {
        _duration = duration;
        return asT();
    }

    /** Configures an action to be executed when this transition starts. */
    public T onStart (Runnable action) {
        Asserts.checkState(_onStart == null, "onStart action already configured.");
        _onStart = action;
        return asT();
    }

    /** Configures an action to be executed when this transition completes. */
    public T onComplete (Runnable action) {
        Asserts.checkState(_onComplete == null, "onComplete action already configured.");
        _onComplete = action;
        return asT();
    }

    @Override public void init (Screen oscreen, Screen nscreen) {
        if (_onStart != null) {
            _onStart.run();
        }
    }

    @Override public void complete (Screen oscreen, Screen nscreen) {
        if (_onComplete != null) {
            _onComplete.run();
        }
    }

    /**
     * Returns <code>this</code> cast to <code>T</code>.
     */
    @SuppressWarnings({"unchecked", "cast"}) protected T asT () {
        return (T)this;
    }

    protected float defaultDuration () {
        return 1000;
    }

    protected float _duration = defaultDuration();
    protected Runnable _onStart, _onComplete;
}
