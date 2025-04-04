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
package playn.ios;

import java.io.FileNotFoundException;

import cli.System.IO.File;
import cli.System.IO.FileAccess;
import cli.System.IO.FileMode;
import cli.System.IO.FileShare;
import cli.System.IO.FileStream;
import cli.System.IO.Path;
import cli.System.IO.Stream;
import cli.System.IO.StreamReader;

import cli.MonoTouch.Foundation.NSData;
import cli.MonoTouch.Foundation.NSError;
import cli.MonoTouch.Foundation.NSMutableData;
import cli.MonoTouch.Foundation.NSUrl;
import cli.MonoTouch.Foundation.NSUrlConnection;
import cli.MonoTouch.Foundation.NSUrlConnectionDelegate;
import cli.MonoTouch.Foundation.NSUrlRequest;
import cli.MonoTouch.UIKit.UIImage;

import playn.core.AbstractAssets;
import playn.core.Asserts;
import playn.core.Image;
import playn.core.Sound;
import playn.core.gl.Scale;
import playn.core.util.Callback;

public class IOSAssets extends AbstractAssets {

  private String pathPrefix = "";
  private final IOSPlatform platform;

  public IOSAssets(IOSPlatform platform) {
    this.platform = platform;
  }

  /**
   * Configures the prefix prepended to asset paths before fetching them from the app directory.
   * Note that you specify path components as an array, <em>not</em> a single string that contains
   * multiple components with embedded path separators.
   */
  public void setPathPrefix(String... components) {
    Asserts.checkArgument(components.length > 0);
    for (String component : components) {
      Asserts.checkArgument(!component.contains("/") && !component.contains("\\"),
                            "Path components must not contain path separators: " + component);
    }
    pathPrefix = Path.Combine(components);
  }

  @Override
  public Image getImage(String path) {
    String fullPath = Path.Combine(pathPrefix, path);
    Throwable error = null;
    for (Scale.ScaledResource rsrc : platform.graphics().ctx().scale.getScaledResources(fullPath)) {
      if (!File.Exists(rsrc.path)) continue;
      platform.log().debug("Loading image: " + rsrc.path);
      try {
        Stream stream = new FileStream(rsrc.path, FileMode.wrap(FileMode.Open),
                                       FileAccess.wrap(FileAccess.Read),
                                       FileShare.wrap(FileShare.Read));
        NSData data = NSData.FromStream(stream);
        return new IOSImage(platform.graphics().ctx, UIImage.LoadFromData(data), rsrc.scale);
      } catch (Throwable t) {
        platform.log().warn("Failed to load image: " + rsrc.path, t);
        error = t; // note this error if this is the lowest resolution image, but fall back to
                   // lower resolution images if not; in the Java backend we'd fail here, but this
                   // is a production backend, so we want to try to make things work
      }
    }
    return createErrorImage(error);
  }

  @Override
  public Image getRemoteImage(String url, float width, float height) {
    final IOSAsyncImage image = new IOSAsyncImage(platform.graphics().ctx, Scale.ONE, width, height);
    new NSUrlConnection(new NSUrlRequest(new NSUrl(url)), new NSUrlConnectionDelegate() {
      private NSMutableData data = new NSMutableData();
      @Override
      public void ReceivedData(NSUrlConnection conn, NSData data) {
        this.data.AppendData(data);
      }
      @Override
      public void FailedWithError (NSUrlConnection conn, NSError error) {
        onFailure(new Exception(error.get_LocalizedDescription()));
      }
      @Override
      public void FinishedLoading (NSUrlConnection conn) {
        try {
          final UIImage uiImage = UIImage.LoadFromData(this.data);
          platform.invokeLater(new Runnable() {
            public void run () {
              image.setImage(uiImage);
            }
          });
        } catch (Throwable cause) {
          onFailure(cause);
        }
      }
      protected void onFailure (final Throwable cause) {
        platform.invokeLater(new Runnable() {
          public void run () {
            image.setError(cause);
          }
        });
      }
    }, true);
    return image;
  }

  @Override
  public Sound getSound(final String path) {
    // first try the .caf sound, then fall back to .mp3
    for (String encpath : new String[] { path + ".caf", path + ".mp3" }) {
      String fullPath = Path.Combine(pathPrefix, encpath);
      if (!File.Exists(fullPath)) continue;
      platform.log().debug("Loading sound " + path);
      return platform.audio().createSound(fullPath);
    }

    platform.log().warn("Missing sound: " + path);
    return new Sound.Error(new FileNotFoundException(path));
  }

  @Override
  public void getText(String path, Callback<String> callback) {
    platform.log().debug("Loading text " + path);
    String fullPath = Path.Combine(pathPrefix, path);
    StreamReader reader = null;
    try {
      reader = new StreamReader(fullPath);
      callback.onSuccess(reader.ReadToEnd());
    } catch (Throwable t) {
      callback.onFailure(t);
    } finally {
      if (reader != null) {
        reader.Close();
      }
    }
  }

  @Override
  protected Image createErrorImage(Throwable cause, float width, float height) {
    return new IOSErrorImage(platform.graphics().ctx, cause, width, height);
  }
}
