package com.omegapoint.core.components;

import com.artemis.Component;
import com.omegapoint.core.CollisionPredicate;
import playn.core.Json;
import playn.core.PlayN;
import pythagoras.i.Rectangle;

import java.util.ArrayList;

/**
 *
 */
public class CollisionComponent extends BaseComponent {
    public static final String X = "x";
    private CollisionPredicate[] predicates;
    private Rectangle bounds;
    public static final String NAME = "collisionComponent";

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

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new CollisionComponent(bounds.x, bounds.y, bounds.width,  bounds.height, predicates);
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    public static class Codec implements Jsonable<CollisionComponent> {

        public static final String Y = "y";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
        public static final String PREDICATES = "predicates";

        @Override
        public CollisionComponent fromJson(Json.Object object) {
            return new CollisionComponent(object.getInt(X), object.getInt(Y),
                    object.getInt(WIDTH), object.getInt(HEIGHT), decodePredicates(object.getArray(PREDICATES)));
        }

        private CollisionPredicate[] decodePredicates(Json.Array predicates) {
            ArrayList<CollisionPredicate> preds = new ArrayList<CollisionPredicate>();
            for (int i = 0; i < predicates.length(); i++) {
               preds.add(CollisionPredicates.lookup(predicates.getString(i)));
            }
            return preds.toArray(new CollisionPredicate[preds.size()]);
        }

        private Json.Array encodePredicates(CollisionPredicate[] predicates) {
            Json.Array array = PlayN.json().createArray();
            for (CollisionPredicate p : predicates) {
                if (p instanceof HasName) {
                  array.add(((HasName) p).getName());
                }
            }
            return array;
        }

        @Override
        public Json.Object toJson(CollisionComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put(X, object.getBounds().x);
            obj.put(Y, object.getBounds().y);
            obj.put(WIDTH, object.getBounds().width);
            obj.put(HEIGHT, object.getBounds().height);
            obj.put(PREDICATES, encodePredicates(object.getPredicates()));
            return obj;
        }
    }
}
