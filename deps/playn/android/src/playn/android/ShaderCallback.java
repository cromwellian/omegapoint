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

import playn.core.util.Callback;

class ShaderCallback implements Callback<String> {
  private String shader;

  @Override
  public void onSuccess(String shader) {
    this.shader = shader;
  }

  @Override
  public void onFailure(Throwable err) {
    throw new RuntimeException("Exception loading shader strings.", err);
  }

  public String shader() {
    return shader;
  }
}
