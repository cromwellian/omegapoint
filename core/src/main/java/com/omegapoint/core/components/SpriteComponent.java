package com.omegapoint.core.components;

import com.artemis.Component;

/**
 *
 */
public class SpriteComponent extends Component {
    private String img;
    private int sw;
    private int sh;
    private int cols;
    private int rows;
    private int startFrame;
    private int msPerFrame;
    private int curFrame;
    private boolean cyclic;

    private int timeToNextFrame = 0;

    public int getTimeToNextFrame() {
        return timeToNextFrame;
    }

    public void setTimeToNextFrame(int timeToNextFrame) {
        this.timeToNextFrame = timeToNextFrame;
    }

    public SpriteComponent(String img) {
        this(img, 0, 0, 0, 0, 0, -1, false);
    }

    public void setCurFrame(int curFrame) {
        this.curFrame = curFrame;
    }

    public int getSw() {

        return sw;
    }

    public int getSh() {
        return sh;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int getStartFrame() {
        return startFrame;
    }

    public int getMsPerFrame() {
        return msPerFrame;
    }

    public int getCurFrame() {
        return curFrame;
    }

    public boolean isCyclic() {
        return cyclic;
    }

    public SpriteComponent(String img, int sw, int sh, int cols, int rows, int startFrame, int msPerFrame, boolean cyclic) {
        this.img = img;
        this.sw = sw;
        this.sh = sh;
        this.cols = cols;
        this.rows = rows;
        this.startFrame = startFrame;
        this.msPerFrame = msPerFrame;
        this.curFrame = startFrame;
        this.cyclic = cyclic;
    }

    public String getImg() {
        return img;
    }
}
