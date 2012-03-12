package com.omegapoint.core;

import com.artemis.Component;
import pythagoras.i.Rectangle;

/**
 *
 */
public class CollisionComponent extends Component {
    private CollisionPredicate[] predicates;
    private Rectangle bounds;

    public CollisionComponent(int x, int y, int w, int h, CollisionPredicate... predicates) {
        this.bounds = new Rectangle(x, y, w, h);
        this.predicates = predicates;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public CollisionPredicate[] getPredicates() {
        return predicates;
    }
}
