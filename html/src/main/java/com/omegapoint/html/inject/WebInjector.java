package com.omegapoint.html.inject;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.omegapoint.core.OmegaPointGame;

@GinModules({HtmlModule.class})
public interface WebInjector extends Ginjector {
    OmegaPointGame getGame();
}