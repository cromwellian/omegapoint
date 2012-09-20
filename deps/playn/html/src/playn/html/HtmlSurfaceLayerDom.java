/**
 * Copyright 2010 The PlayN Authors
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
package playn.html;

import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;

import pythagoras.f.MathUtil;

import playn.core.Asserts;
import playn.core.CanvasSurface;
import playn.core.Surface;
import playn.core.SurfaceLayer;

class HtmlSurfaceLayerDom extends HtmlLayerDom implements SurfaceLayer {

  private Surface surface;

  HtmlSurfaceLayerDom(float width, float height) {
    super(Document.get().createCanvasElement());
    canvas().setWidth(MathUtil.iceil(width));
    canvas().setHeight(MathUtil.iceil(height));
    surface = new CanvasSurface(new HtmlCanvas(canvas(), width, height));
  }

  @Override
  public void destroy() {
    super.destroy();
    surface = null;
  }

  @Override
  public Surface surface() {
    return surface;
  }

  private CanvasElement canvas()  {
    return element().cast();
  }

  @Override
  public float width() {
    Asserts.checkNotNull(surface, "Surface must not be null");
    return surface.width();
  }

  @Override
  public float height() {
    Asserts.checkNotNull(surface, "Surface must not be null");
    return surface.height();
  }

  @Override
  public float scaledWidth() {
    return transform().scaleX() * width();
  }

  @Override
  public float scaledHeight() {
    return transform().scaleY() * height();
  }
}
