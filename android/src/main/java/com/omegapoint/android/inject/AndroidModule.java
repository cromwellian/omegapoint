package com.omegapoint.android.inject;

import com.google.gwt.inject.rebind.adapter.GinModuleAdapter;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.omegapoint.core.inject.GuiceBindProvider;
import com.omegapoint.core.inject.OmegaPointBaseModule;

/**
 *
 */
public class AndroidModule extends OmegaPointBaseModule implements Module {
    private final GuiceBindProvider bindProvider = new GuiceBindProvider();

    @Override
    public void configure(Binder binder) {
        bindProvider.setBinder(binder);
        bindAll(bindProvider);
    }
}
