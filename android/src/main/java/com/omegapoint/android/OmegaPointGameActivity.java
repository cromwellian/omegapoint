package com.omegapoint.android;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.omegapoint.android.inject.AndroidModule;
import playn.android.GameActivity;
import playn.core.PlayN;

import com.omegapoint.core.OmegaPointGame;

public class OmegaPointGameActivity extends GameActivity {

  @Override
  public void main(){
    platform().assetManager().setPathPrefix("com/omegapoint/resources");
    Injector injector = Guice.createInjector(new AndroidModule());
    PlayN.run(injector.getInstance(OmegaPointGame.class));
  }
}
