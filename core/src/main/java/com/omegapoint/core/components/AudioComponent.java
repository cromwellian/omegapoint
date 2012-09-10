package com.omegapoint.core.components;

import com.artemis.Component;
import playn.core.Json;
import playn.core.PlayN;
import playn.core.Sound;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class AudioComponent extends BaseComponent {

    private String url;
    private double volume;
    public static final String NAME = "audioComponent";

    public AudioComponent(String url) {
       this(url, 1.0); 
    }

    public Sound getSound() {
        return sound;
    }

    private Sound sound;
    private static Map<String, Sound> soundCache = new HashMap<String, Sound>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public AudioComponent(String url, double volume) {
        this.url = url;
        this.volume = volume;
        sound = soundCache.get(url);

        if (sound == null) {
          sound = PlayN.assets().getSound(url);
          soundCache.put(url, sound);
          sound.setVolume((float) volume);
        }
    }

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new AudioComponent(this.getUrl(), this.getVolume());
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    public static class Codec implements Jsonable<AudioComponent> {

        @Override
        public AudioComponent fromJson(Json.Object object) {
            return new AudioComponent(object.getString("assetPath"), object.getDouble("volume"));
        }

        @Override
        public Json.Object toJson(AudioComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("assetPath", object.getUrl());
            obj.put("volume", object.getVolume());
            return obj;
        }
    }
}
