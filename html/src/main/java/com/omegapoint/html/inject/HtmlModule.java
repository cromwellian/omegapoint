package com.omegapoint.html.inject;

import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.client.binder.GinBinder;
import com.omegapoint.core.inject.GinBindProvider;
import com.omegapoint.core.inject.OmegaPointBaseModule;

/**
 *
 */
public class HtmlModule extends OmegaPointBaseModule implements GinModule {

    private final GinBindProvider bindProvider = new GinBindProvider();

    @Override
    public void configure(GinBinder ginBinder) {
       bindProvider.setBinder(ginBinder);
       bindAll(bindProvider);
    }
}
