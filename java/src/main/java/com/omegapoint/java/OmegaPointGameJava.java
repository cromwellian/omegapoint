package com.omegapoint.java;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.omegapoint.java.inject.JavaModule;
import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.omegapoint.core.OmegaPointGame;

public class OmegaPointGameJava {

    public static void main(String[] args) {
        JavaPlatform platform = JavaPlatform.register();
        platform.assetManager().setPathPrefix("com/omegapoint/resources");
        Injector injector = Guice.createInjector(new JavaModule());
        PlayN.run(injector.getInstance(OmegaPointGame.class));
    }
}
