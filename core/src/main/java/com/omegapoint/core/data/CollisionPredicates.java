package com.omegapoint.core.data;

import com.omegapoint.core.predicates.CollisionPredicate;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CollisionPredicates {

    public static Map<String, CollisionPredicate> predicates = new HashMap<String, CollisionPredicate>();
    public static CollisionPredicate lookup(String name) {
      return predicates.get(name);
    }

    public static void register(String name, CollisionPredicate predicate) {
        predicates.put(name, predicate);
    }
}
