package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.omegapoint.core.components.AudioComponent;
import playn.core.Sound;

/**
 *
 */
public class AudioSystem extends EntityProcessingSystem {
    private ComponentMapper<AudioComponent> audioMapper;

    public AudioSystem() {
        super(AudioComponent.class);
    }

    public void initialize() {
        audioMapper = new ComponentMapper<AudioComponent>(AudioComponent.class, world);
    }

    @Override
    protected void process(Entity e) {
        AudioComponent audioComp = audioMapper.get(e);
        if (audioComp == null) {
            return;
        }
        Sound sound = audioComp.getSound();
        sound.play();
        e.removeComponent(audioComp);
        e.refresh();
    }
}
