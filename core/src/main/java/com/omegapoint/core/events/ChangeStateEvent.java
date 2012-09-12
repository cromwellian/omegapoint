package com.omegapoint.core.events;

import com.google.web.bindery.event.shared.Event;

/**
 * Event fired when the Fettle state should change.
 */
public class ChangeStateEvent extends Event<ChangeStateHandler> {
    public static Type<ChangeStateHandler> TYPE = new Type<ChangeStateHandler>();
    private String nextState;

    public String getNextState() {
        return nextState;
    }

    public ChangeStateEvent(String nextState) {
        this.nextState = nextState;
    }

    @Override
    public Type<ChangeStateHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ChangeStateHandler handler) {
        handler.onStateChange(this);
    }
}
