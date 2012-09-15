package com.omegapoint.ios.inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.omegapoint.core.inject.GuiceBindProvider;
import com.omegapoint.core.inject.OmegaPointBaseModule;

/**
 *
 */
public class IOSModule extends OmegaPointBaseModule implements Module {
    private final GuiceBindProvider bindProvider = new GuiceBindProvider();

    @Override
    public void configure(Binder binder) {
        bindProvider.setBinder(binder);
        bindAll(bindProvider);
    }
}
