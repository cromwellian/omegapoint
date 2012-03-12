package com.omegapoint.html;

import com.google.gwt.core.client.GWT;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.omegapoint.html.inject.WebInjector;
import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.omegapoint.core.OmegaPointGame;

public class OmegaPointGameHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    WebInjector injector = GWT.create(WebInjector.class);
    platform.assetManager().setPathPrefix("omegapoint/");
    PlayN.run(injector.getGame());

  }
}
