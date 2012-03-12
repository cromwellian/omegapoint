package com.omegapoint.core.components;

import com.artemis.Component;
import playn.core.PlayN;
import playn.core.Sound;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class AudioComponent extends Component {
    public Sound getSound() {
        return sound;
    }

    private Sound sound;
    private static Map<String, Sound> soundCache = new HashMap<String, Sound>();
    public AudioComponent(String url) {
        sound = soundCache.get(url);
        if (sound == null) {
          sound = PlayN.assetManager().getSound(url);
          soundCache.put(url, sound);
        }
    }
}
