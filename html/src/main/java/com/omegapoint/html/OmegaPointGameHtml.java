package com.omegapoint.html;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.omegapoint.html.inject.WebInjector;
import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

public class OmegaPointGameHtml extends HtmlGame {

    @Override
    public void start() {
        registerFont("omegapoint/fonts/spaceage.ttf", "Space Age", "plain");

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                HtmlPlatform platform = HtmlPlatform.register();
                WebInjector injector = GWT.create(WebInjector.class);
                platform.assets().setPathPrefix("omegapoint/");
                PlayN.run(injector.getGame());
            }
        });


    }

    private void registerFont(String file, String familyName, String plain) {
        StyleElement style = Document.get().createStyleElement();
        style.setInnerText("@font-face {\n" +
                "  font-family: '" + familyName + "';\n" +
                "  font-style: normal;\n" +
                "  font-weight: 400;\n" +
                "  src: url(" + file + ");\n" +
                "}");
        Document.get().getElementsByTagName("head").getItem(0).appendChild(style);
    }
}
