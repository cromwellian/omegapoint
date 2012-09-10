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

        @Override
        public CollisionComponent fromJson(Json.Object object) {
            return new CollisionComponent(object.getInt("x"), object.getInt("y"),
                    object.getInt("width"), object.getInt("height"), decodePredicates(object.getArray("predicates")));
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
            obj.put("x", object.getBounds().x);
            obj.put("y", object.getBounds().y);
            obj.put("width", object.getBounds().width);
            obj.put("height", object.getBounds().height);
            obj.put("predicates", encodePredicates(object.getPredicates()));
            return obj;
        }
    }
}
