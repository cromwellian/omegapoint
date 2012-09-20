/**
 * Copyright 2012 The PlayN Authors
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

import pythagoras.f.MathUtil;

import playn.core.Pattern;
import playn.core.gl.ImageRegionGL;

class AndroidImageRegion extends ImageRegionGL implements AndroidCanvas.Drawable {

  public AndroidImageRegion(AndroidImage parent, float x, float y, float width, float height) {
    super(parent, x, y, width, height);
  }

  @Override
  public void getRgb(int startX, int startY, int width, int height, int[] rgbArray, int offset,
                     int scanSize) {
    bitmap().getPixels(rgbArray, offset, scanSize, startX + (int)x, startY + (int)y, width, height);
  }

  @Override
  public Pattern toPattern() {
    int ix = MathUtil.ifloor(x), iy = MathUtil.ifloor(y);
    int iw = MathUtil.iceil(width), ih = MathUtil.iceil(height);
    return new AndroidPattern(this, Bitmap.createBitmap(bitmap(), ix, iy, iw, ih));
  }

  @Override
  public Bitmap bitmap() {
    return ((AndroidImage) parent).bitmap();
  }

  @Override
  public void prepDraw(Rect rect, RectF rectf, float dx, float dy, float dw, float dh,
                       float sx, float sy, float sw, float sh) {
    ((AndroidImage) parent).prepDraw(rect, rectf, dx, dy, dw, dh, x+sx, y+sy, sw, sh);
  }
}
