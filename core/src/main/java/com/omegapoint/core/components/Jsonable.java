package com.omegapoint.core.components;

import playn.core.Json;

/**
 * Supports serialization to/from Json.
 */
public interface Jsonable<T> {
    <T> T fromJson(Json.Object object);
    Json.Object toJson(T object);
}
