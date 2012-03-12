package com.artemis;

/**
 * A tag class. All components in the system must extend this class.
 *
 * @author Arni Arent
 */
public abstract class Component {
    private boolean dirty = true;

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
