package com.omegapoint.core.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.omegapoint.core.components.AudioComponent;
import playn.core.PlayN;
import playn.core.ResourceCallback;
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
        sound.addCallback(new ResourceCallback<Sound>() {
            @Override
            public void done(Sound resource) {
                resource.play();
            }

            @Override
            public void error(Throwable err) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        e.removeComponent(audioComp);
        e.refresh();
    }
}
