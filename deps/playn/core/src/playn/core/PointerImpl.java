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
package playn.core;

import pythagoras.f.Point;

/**
 * Handles the common logic for all platform {@link Pointer} implementations.
 */
public abstract class PointerImpl implements Pointer {

  private boolean enabled = true;
  private Dispatcher dispatcher = Dispatcher.SINGLE;
  private Listener listener;
  private AbstractLayer activeLayer;

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public Listener listener () {
    return listener;
  }

  @Override
  public void setListener(Listener listener) {
    this.listener = listener;
  }

  @Override
  public void cancelLayerDrags() {
    if (activeLayer != null) {
      Event.Impl event = new Event.Impl(new Events.Flags.Impl(), PlayN.currentTime(), 0, 0, false);
      dispatcher.dispatch(activeLayer, Listener.class, event, CANCEL);
      activeLayer = null;
    }
  }

  public void setPropagateEvents(boolean propagate) {
    dispatcher = Dispatcher.Util.select(propagate);
  }

  protected boolean onPointerStart(Event.Impl event, boolean preventDefault) {
    if (!enabled)
      return preventDefault;

    event.flags().setPreventDefault(preventDefault);
    if (listener != null) {
      listener.onPointerStart(event);
    }

    GroupLayer root = PlayN.graphics().rootLayer();
    if (root.interactive()) {
      Point p = new Point(event.x(), event.y());
      root.transform().inverseTransform(p, p);
      p.x += root.originX();
      p.y += root.originY();
      activeLayer = (AbstractLayer)root.hitTest(p);
      if (activeLayer != null) {
        dispatcher.dispatch(activeLayer, Listener.class, event, START);
      }
    }
    return event.flags().getPreventDefault();
  }

  protected boolean onPointerDrag(Event.Impl event, boolean preventDefault) {
    if (!enabled)
      return preventDefault;

    event.flags().setPreventDefault(preventDefault);
    if (listener != null) {
      listener.onPointerDrag(event);
    }

    if (activeLayer != null) {
      dispatcher.dispatch(activeLayer, Listener.class, event, DRAG);
    }
    return event.flags().getPreventDefault();
  }

  protected boolean onPointerEnd(Event.Impl event, boolean preventDefault) {
    if (!enabled)
      return preventDefault;

    event.flags().setPreventDefault(preventDefault);
    if (listener != null) {
      listener.onPointerEnd(event);
    }

    if (activeLayer != null) {
      dispatcher.dispatch(activeLayer, Listener.class, event, END);
      activeLayer = null;
    }
    return event.flags().getPreventDefault();
  }

  protected boolean onPointerCancel(Event.Impl event, boolean preventDefault) {
    if (!enabled)
      return preventDefault;

    event.flags().setPreventDefault(preventDefault);
    if (listener != null) {
      listener.onPointerCancel(event);
    }

    if (activeLayer != null) {
      dispatcher.dispatch(activeLayer, Listener.class, event, CANCEL);
      activeLayer = null;
    }
    return event.flags().getPreventDefault();
  }

  protected AbstractLayer.Interaction<Listener, Event.Impl> START =
      new AbstractLayer.Interaction<Listener, Event.Impl>() {
    public void interact(Listener l, Event.Impl ev) {
      l.onPointerStart(ev);
    }
  };

  protected AbstractLayer.Interaction<Listener, Event.Impl> DRAG =
      new AbstractLayer.Interaction<Listener, Event.Impl>() {
    public void interact(Listener l, Event.Impl ev) {
      l.onPointerDrag(ev);
    }
  };

  protected AbstractLayer.Interaction<Listener, Event.Impl> END =
      new AbstractLayer.Interaction<Listener, Event.Impl>() {
    public void interact(Listener l, Event.Impl ev) {
      l.onPointerEnd(ev);
    }
  };

  protected AbstractLayer.Interaction<Listener, Event.Impl> CANCEL =
      new AbstractLayer.Interaction<Listener, Event.Impl>() {
    public void interact(Listener l, Event.Impl ev) {
      l.onPointerCancel(ev);
    }
  };
}
