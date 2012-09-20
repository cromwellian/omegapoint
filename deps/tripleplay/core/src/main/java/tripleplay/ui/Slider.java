//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package tripleplay.ui;

import pythagoras.f.Dimension;
import pythagoras.f.IPoint;

import react.Signal;
import react.SignalView;
import react.UnitSlot;
import react.Value;

import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import static playn.core.PlayN.graphics;

/**
 * Displays a bar and a thumb that can be slid along the bar, representing a floating point value
 * between some minimum and maximum.
 */
public class Slider extends Widget<Slider>
{
    /** The width of the bar of an unstretched slider. The slider's preferred width will be this
     * width plus the width of the thumb image (which can extend past the left edge of the bar by
     * half its width and the right edge of the bar by half its width). Inherited. */
    public static Style<Float> BAR_WIDTH = Style.newStyle(true, 100f);

    /** The height of the bar. The slider's preferred height will be the larger of this height and
     * the height of the thumb image. Inherited. */
    public static Style<Float> BAR_HEIGHT = Style.newStyle(true, 5f);

    /** The background that renders the bar (defaults to a black rectangle). Inherited. */
    public static Style<Background> BAR_BACKGROUND =
        Style.newStyle(true, Background.solid(0xFF000000));

    /** The image to use for the slider thumb. Inherited. */
    public static Style<Image> THUMB_IMAGE = Style.newStyle(true, createDefaultThumbImage());

    /** The origin of the thumb image (used to center the thumb image over the tray). If left as
     * the default (null), the center of the thumb image will be used as its origin. Inherited. */
    public static Style<IPoint> THUMB_ORIGIN = Style.newStyle(false, (IPoint)null);

    /** The value of the slider. */
    public final Value<Float> value;

    public Slider (float value, float min, float max) {
        enableInteraction();
        this.value = Value.create(value);
        _min = min;
        _max = max;
        _range = _max - _min;
        // set up our thumb layer
        layer.add(_thumb = graphics().createImageLayer());
        _thumb.setDepth(1);
        // update our display if the slider value is changed externally
        this.value.connect(new UnitSlot () { @Override public void onEmit () { updateThumb(); }});
    }

    /**
     * Constrains the possible slider values to the given increment. For example, an increment of 1
     * would mean the possible sliders values are {@link #min()}, {@code min() + 1}, etc, up to
     * {@link #max()}. Note this only affects internal updates from pointer or mouse handling. The
     * underlying {@link #value} may be updated arbitrarily.
     */
    public Slider setIncrement (float increment) {
        _increment = increment;
        return this;
    }

    /** A signal that is emitted when the user has released their finger/pointer after having
     * started adjusting the slider. {@link #value} will contain the correct current value at the
     * time this signal is emitted. */
    public SignalView<Slider> clicked () {
        return _clicked;
    }

    /** Returns our maximum allowed value. */
    public float max () { return _max; }

    /** Returns our minimum allowed value. */
    public float min () { return _min; }

    @Override protected Class<?> getStyleClass () {
        return Slider.class;
    }

    @Override protected void wasRemoved () {
        super.wasRemoved();
        if (_barInst != null) {
            _barInst.destroy();
            _barInst = null;
        }
        // the thumb is just an image layer and will be destroyed when we are
    }

    @Override protected LayoutData createLayoutData (float hintX, float hintY) {
        return new SliderLayoutData();
    }

    @Override protected void onPointerStart (Pointer.Event event, float x, float y) {
        super.onPointerStart(event, x, y);
        handlePointer(x, y);
    }

    @Override protected void onPointerDrag (Pointer.Event event, float x, float y) {
        super.onPointerDrag(event, x, y);
        handlePointer(x, y);
    }

    @Override protected void onPointerEnd (Pointer.Event event, float x, float y) {
        super.onPointerEnd(event, x, y);
        handlePointer(x, y);
        _clicked.emit(this);
    }

    // nothing to do for onPointerCancel, just let the interaction stop

    protected void updateThumb () {
        float thumbPct = (value.get() - _min) / _range;
        _thumb.setTranslation(_thumbLeft + _thumbRange * thumbPct, _thumbY);
    }

    protected void handlePointer (float x, float y) {
        float width = _thumbRange;
        x = Math.min(width,  x - _thumbLeft);
        float pos = Math.max(x, 0) / width * _range;
        if (_increment != null) {
            float i = _increment;
            pos = i * Math.round(pos / i);
        }
        value.update(_min + pos);
    }

    protected static Image createDefaultThumbImage () {
        float size = 24;
        CanvasImage image = graphics().createImage(size, size);
        image.canvas().setFillColor(0xFF000000);
        image.canvas().fillCircle(size/2, size/2, size/2-1);
        return image;
    }

    protected class SliderLayoutData extends LayoutData {
        public final float barWidth = resolveStyle(BAR_WIDTH);
        public final float barHeight = resolveStyle(BAR_HEIGHT);
        public final Background barBG = resolveStyle(BAR_BACKGROUND);
        public final Image thumbImage = resolveStyle(THUMB_IMAGE);
        public final IPoint thumbOrigin = resolveStyle(THUMB_ORIGIN);

        @Override public Dimension computeSize (float hintX, float hintY) {
            return new Dimension(barWidth + thumbImage.width(),
                                 Math.max(barHeight, thumbImage.height()));
        }

        @Override public void layout (float left, float top, float width, float height) {
            // note our thumb metrics
            float thumbWidth = thumbImage.width(), thumbHeight = thumbImage.height();
            _thumbRange = width - thumbWidth;
            _thumbLeft = left + thumbWidth/2;
            _thumbY = top + height/2;

            // configure our thumb layer
            _thumb.setImage(thumbImage);
            if (thumbOrigin == null) {
                _thumb.setOrigin(thumbWidth/2, thumbHeight/2);
            } else {
                _thumb.setOrigin(thumbOrigin.x(), thumbOrigin.y());
            }

            // configure our bar background instance
            if (_barInst != null) _barInst.destroy();
            if (width > 0 && height > 0) {
                _barInst = barBG.instantiate(new Dimension(width-thumbWidth, barHeight));
                _barInst.addTo(layer, _thumbLeft, top + (height - barHeight)/2, 1);
            }

            // finally update the thumb position
            updateThumb();
        }
    }

    protected final Signal<Slider> _clicked = Signal.create();
    protected final float _min, _max, _range;
    protected final ImageLayer _thumb;

    protected Background.Instance _barInst;
    protected float _thumbLeft, _thumbRange, _thumbY;
    protected Float _increment;
}
