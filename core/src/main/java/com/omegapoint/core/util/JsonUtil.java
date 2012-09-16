package com.omegapoint.core.util;

import playn.core.Json;

import static playn.core.PlayN.json;

/**
 *
 */
public class JsonUtil {
    public static String toString(Json.Object obj) {
        Json.Writer sink = json().newWriter().object();
        obj.write(sink);
        sink.end();
        return sink.write();
    }
}
