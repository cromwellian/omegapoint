/**
 * Copyright 2010 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package playn.flash;

import java.util.List;

import flash.display.BitmapData;
import flash.gwt.FlashImport;

import playn.core.Image;
import playn.core.Pattern;
import playn.core.util.Callback;
import playn.core.util.Callbacks;

@FlashImport({
  "flash.display.Loader", "flash.events.Event", "flash.net.URLRequest", "flash.system.LoaderContext"
})
class FlashImage implements Image {

  private List<Callback<? super Image>> callbacks;

  protected BitmapData imageData = null;
  private final String url;

  FlashImage(String url) {
    this.url = url;
    scheduleLoad(url);
  }

  FlashImage(BitmapData data) {
    this.url = "<from bitmap>";
    this.imageData = data;
  }

  private native void scheduleLoad(String url) /*-{
    var loader = new Loader();
    var self = this;
    var context = new flash.system.LoaderContext();
    context.checkPolicyFile = true;
    loader.contentLoaderInfo.addEventListener(flash.events.Event.COMPLETE, function(event) {
      self.@playn.flash.FlashImage::imageData = event.target.content.bitmapData;
      self.@playn.flash.FlashImage::runCallbacks(Z)(true);
    });
    loader.contentLoaderInfo.addEventListener(flash.events.IOErrorEvent.IO_ERROR, function() {
        self.@playn.flash.FlashImage::imageData = new flash.display.BitmapData(1, 1, true, 0xFF000000);
        self.@playn.flash.FlashImage::runCallbacks(Z)(false);
    });
    loader.load(new URLRequest(url), context);
  }-*/;

  @Override
  public float width() {
    return imageData == null ? 0 : imageData.getWidth();
  }

  @Override
  public float height() {
    return imageData == null ? 0 : imageData.getHeight();
  }

  @Override
  public boolean isReady() {
    return imageData != null;
  }

  @Override
  public void addCallback(Callback<? super Image> callback) {
    if (isReady())
      callback.onSuccess(this);
    else
      callbacks = Callbacks.createAdd(callbacks, callback);
  }

  @Override
  public Region subImage(float x, float y, float width, float height) {
    return new FlashImageRegion(this, x, y, width, height);
  }

  @Override
  public Pattern toPattern() {
    return new FlashPattern(this);
  }

  @Override
  public void getRgb(int startX, int startY, int width, int height, int[] rgbArray, int offset,
                     int scanSize) {
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        rgbArray[offset + x] = imageData.getPixel32(startX + x, startY + y);
      }
      offset += scanSize;
    }
  }

  @Override
  public Image transform(BitmapTransformer xform) {
    return new FlashImage(((FlashBitmapTransformer) xform).transform(imageData));
  }

  @Override
  public int ensureTexture(boolean repeatX, boolean repeatY) {
    return 0; // not supported
  }

  @Override
  public void clearTexture() {
    // noop
  }

  BitmapData bitmapData() {
    return imageData;
  }

  private void runCallbacks(boolean success) {
    if (success)
      callbacks = Callbacks.dispatchSuccessClear(callbacks, this);
    else
      callbacks = Callbacks.dispatchFailureClear(
        callbacks, new Exception("Error loading image: " + url));
  }
}
