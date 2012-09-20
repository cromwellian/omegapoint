/**
 * Copyright 2011 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package playn.tests.core;

import java.util.HashSet;
import java.util.Set;

import playn.core.*;
import static playn.core.PlayN.*;

public class TestsGame implements Game {

  public static Image makeButtonImage(String label) {
    TextLayout layout = graphics().layoutText(label, BUTTON_FMT);
    CanvasImage image = graphics().createImage(layout.width()+10, layout.height()+10);
    image.canvas().setFillColor(0xFFCCCCCC);
    image.canvas().fillRect(0, 0, image.width(), image.height());
    image.canvas().setFillColor(0xFF000000);
    image.canvas().fillText(layout, 5, 5);
    image.canvas().setStrokeColor(0xFF000000);
    image.canvas().strokeRect(0, 0, image.width()-1, image.height()-1);
    return image;
  }

  private Test[] tests = new Test[] {
    new CanvasTest(),
    new SurfaceTest(),
    new TextTest(),
    new SubImageTest(),
    new ClippedGroupTest(),
    new PauseResumeTest(),
    new ImmediateTest(),
    new ImageTypeTest(),
    new AlphaLayerTest(),
    new DepthTest(),
    new ClearBackgroundTest(),
    new LayerClickTest(),
    new GetTextTest(),
    new PointerMouseTouchTest(),
    new MouseWheelTest(),
    new ShaderTest(),
    /*new YourTest(),*/
  };
  private Test currentTest;

  @Override
  public void init() {
    // display basic instructions
    log().info("Right click, touch with two fingers, or type ESC to return to test menu.");

    // add a listener for mouse and touch inputs
    mouse().setListener(new Mouse.Adapter() {
      @Override
      public void onMouseDown(Mouse.ButtonEvent event) {
        if (currentTest != null && currentTest.usesPositionalInputs())
          return;
        if (event.button() == Mouse.BUTTON_RIGHT)
          displayMenuLater();
      }
    });
    touch().setListener(new Touch.Adapter() {
      @Override
      public void onTouchStart(Touch.Event[] touches) {
        if (currentTest != null && currentTest.usesPositionalInputs())
          return;
        // Android and iOS handle touch events rather differently, so we need to do this finagling
        // to determine whether there is an active two or three finger touch
        for (Touch.Event event : touches)
          _active.add(event.id());
        if (_active.size() > 1)
          displayMenuLater();
      }
      @Override
      public void onTouchEnd(Touch.Event[] touches) {
        if (currentTest != null && currentTest.usesPositionalInputs())
          return;
        for (Touch.Event event : touches)
          _active.remove(event.id());
      }
      protected Set<Integer> _active = new HashSet<Integer>();
    });
    keyboard().setListener(new Keyboard.Adapter() {
      @Override
      public void onKeyDown(Keyboard.Event event) {
        if (event.key() == Key.ESCAPE)
          displayMenu();
      }
    });

    displayMenu();
    // startTest(tests[3]);
  }

  // defers display of menu by one frame to avoid the right click or touch being processed by the
  // menu when it is displayed
  void displayMenuLater() {
    invokeLater(new Runnable() {
      public void run() {
        displayMenu();
      }
    });
  }

  void displayMenu() {
    GroupLayer root = graphics().rootLayer();
    root.clear();
    root.add(createWhiteBackground());

    float gap = 20, x = gap, y = gap, maxHeight = 0;
    for (Test test : tests) {
      ImageLayer button = createButton(test);
      if (x + button.width() > graphics().width() - gap) {
        x = gap;
        y += maxHeight + gap;
        maxHeight = 0;
      }
      maxHeight = Math.max(maxHeight, button.height());
      root.addAt(button, x, y);
      x += button.width() + gap;
    }
  }

  ImageLayer createButton (final Test test) {
    ImageLayer layer = graphics().createImageLayer(makeButtonImage(test.getName()));
    layer.addListener(new Pointer.Adapter() {
      public void onPointerStart(Pointer.Event event) {
        startTest(test);
      }
    });
    return layer;
  }

  void startTest (Test test) {
    if (currentTest != null)
      currentTest.dispose();
    currentTest = test;

    // setup root layer for next test
    graphics().rootLayer().clear();
    graphics().rootLayer().add(createWhiteBackground());

    log().info("Starting " + currentTest.getName());
    log().info(" Description: " + currentTest.getDescription());
    currentTest.init();
  }

  @Override
  public void paint(float alpha) {
    if (currentTest != null)
      currentTest.paint(alpha);
  }

  @Override
  public void update(float delta) {
    if (currentTest != null)
      currentTest.update(delta);
  }

  @Override
  public int updateRate() {
    return (currentTest == null) ? 25 : currentTest.updateRate();
  }

  protected ImmediateLayer createWhiteBackground() {
    ImmediateLayer bg = graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
      public void render(Surface surf) {
        surf.setFillColor(0xFFFFFFFF).fillRect(0, 0, graphics().width(), graphics().height());
      }
    });
    bg.setDepth(Float.NEGATIVE_INFINITY); // render behind everything
    return bg;
  }

  protected static TextFormat BUTTON_FMT = new TextFormat().withFont(
    graphics().createFont("Helvetica", Font.Style.PLAIN, 24));
}
