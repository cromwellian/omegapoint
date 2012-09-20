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
package playn.android;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

import playn.core.Image;
import playn.core.Pattern;
import playn.core.gl.GLContext;
import playn.core.gl.ImageGL;
import playn.core.gl.Scale;
import playn.core.util.Callback;

class AndroidImage extends ImageGL implements AndroidGLContext.Refreshable, AndroidCanvas.Drawable {

  protected Bitmap bitmap; // only mutated in AndroidAsyncImage

  AndroidImage(GLContext ctx, Bitmap bitmap, Scale scale) {
    super(ctx, scale);
    this.bitmap = bitmap;
    ((AndroidGLContext) ctx).addRefreshable(this);
  }

  @Override
  public void addCallback(Callback<? super Image> callback) {
    // we're always ready immediately
    callback.onSuccess(this);
  }

  @Override
  public void onSurfaceCreated() {
  }

  @Override
  public void onSurfaceLost() {
    clearTexture();
  }

  public void destroy() {
    ((AndroidGLContext) ctx).removeRefreshable(this);
    clearTexture();
  }

  @Override
  public float height() {
    return scale.invScaled(bitmap.getHeight());
  }

  @Override
  public float width() {
    return scale.invScaled(bitmap.getWidth());
  }

  @Override
  public boolean isReady() {
    return true;
  }

  @Override
  public Region subImage(float x, float y, float width, float height) {
    return new AndroidImageRegion(this, x, y, width, height);
  }

  @Override
  public Pattern toPattern() {
    return new AndroidPattern(this);
  }

  @Override
  public void getRgb(int startX, int startY, int width, int height, int[] rgbArray, int offset,
                     int scanSize) {
    bitmap.getPixels(rgbArray, offset, scanSize, startX, startY, width, height);
  }

  @Override
  public Image transform(BitmapTransformer xform) {
    return new AndroidImage(ctx, ((AndroidBitmapTransformer) xform).transform(bitmap), scale);
  }

  @Override
  public Bitmap bitmap() {
    return bitmap;
  }

  @Override
  public void prepDraw(Rect rect, RectF rectf, float dx, float dy, float dw, float dh,
                       float sx, float sy, float sw, float sh) {
    // adjust our source rect to account for the scale factor
    sx *= scale.factor;
    sy *= scale.factor;
    sw *= scale.factor;
    sh *= scale.factor;

    rect.set((int) sx, (int) sy, (int) (sx + sw), (int) (sy + sh));
    rectf.set(dx, dy, dx + dw, dy + dh);
  }

  @Override
  protected void updateTexture(int tex) {
    ((AndroidGLContext) ctx).updateTexture(tex, bitmap);
  }
}
