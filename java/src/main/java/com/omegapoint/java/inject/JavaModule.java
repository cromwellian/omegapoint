package com.omegapoint.java.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.omegapoint.core.OmegaPointGame;
import com.omegapoint.core.inject.GuiceBindProvider;
import com.omegapoint.core.inject.OmegaPointBaseModule;
import com.omegapoint.java.OmegaPointGameJava;
import playn.core.Game;

/**
 *
 */
public class JavaModule extends OmegaPointBaseModule implements Module {
    private final GuiceBindProvider bindProvider = new GuiceBindProvider();

    @Override
    public void configure(Binder binder) {
       bindProvider.setBinder(binder);
       bindAll(bindProvider);
    }
}
