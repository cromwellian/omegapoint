package com.omegapoint.core.components;

import com.artemis.Component;
import com.omegapoint.core.tween.TextColorChanger;

/**
 *
 */
public class SimpleTweenComponent extends Component {
    private float startVal;
    private float endVal;
    private int duration;
    private TextColorChanger propertyChanger;
    private boolean repeat;
    private boolean mirror;
    private int progress = 0;

    public float getEndVal() {
        return endVal;
    }

    public void setEndVal(float endVal) {
        this.endVal = endVal;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public TextColorChanger getPropertyChanger() {
        return propertyChanger;
    }

    public void setPropertyChanger(TextColorChanger propertyChanger) {
        this.propertyChanger = propertyChanger;
    }

    public float getStartVal() {

        return startVal;
    }

    public void setStartVal(float startVal) {
        this.startVal = startVal;
    }

    public SimpleTweenComponent(float startVal, float endVal, int duration, TextColorChanger propertyChanger,
                                boolean repeat,
                                boolean mirror) {
        this.startVal = startVal;
        this.endVal = endVal;
        this.duration = duration;
        this.propertyChanger = propertyChanger;
        this.repeat = repeat;
        this.mirror = mirror;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void update(int delta) {
        progress += delta;
        if (progress > duration) {
            progress = 0;
        }
        setDirty(true);

    }

    public float getValue() {
        float t = (float) progress / (float) duration;
        if (!mirror || t <= 0.5) {
          if (mirror) { t *= 2; }
          return Math.min(startVal + t * (endVal - startVal), endVal);
        } else {
          t = 0.5f - t;
          t *= 2;
          return Math.max(startVal + t * (endVal - startVal), startVal);
        }
    }
}
