package com.omegapoint.core.components;

import com.omegapoint.core.data.Jsonable;
import playn.core.AssetWatcher;
import playn.core.Json;
import playn.core.PlayN;

/**
 *
 */
public class SpriteComponent extends BaseComponent {
    private String img;
    private int sw;
    private int sh;
    private int cols;
    private int rows;
    private int startFrame;
    private int endFrame;
    private int msPerFrame;
    private int curFrame;
    private boolean cyclic;

    private int timeToNextFrame = 0;
    public static final String NAME = "spriteComponent";

    public int getTimeToNextFrame() {
        return timeToNextFrame;
    }

    public void setTimeToNextFrame(int timeToNextFrame) {
        this.timeToNextFrame = timeToNextFrame;
    }

    public SpriteComponent(String img) {
        this(img, 0, 0, 0, 0, 0, 0, -1, false);
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

    public SpriteComponent(String img, int sw, int sh, int cols, int rows, int startFrame, int endFrame, int msPerFrame, boolean cyclic) {
        this.img = img;
        this.sw = sw;
        this.sh = sh;
        this.cols = cols;
        this.rows = rows;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.msPerFrame = msPerFrame;
        this.curFrame = startFrame;
        this.cyclic = cyclic;
    }

    public String getImg() {
        return img;
    }

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new SpriteComponent(this.getImg(), this.getSw(), this.getSh(), this.getCols(), this.getRows(),
                this.getStartFrame(), this.getEndFrame(), this.getMsPerFrame(),
                this.isCyclic());
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    @Override
    public void addAssetsToWatcher(AssetWatcher watcher) {
        watcher.add(PlayN.assets().getImage(getImg()));
    }

    public int getEndFrame() {
        return endFrame;
    }

    public static class Codec implements Jsonable<SpriteComponent> {

        @Override
        public SpriteComponent fromJson(Json.Object object) {
            return new SpriteComponent(object.getString("assetPath"),
                    object.getInt("spriteWidth"),
                    object.getInt("spriteHeight"),
                    object.getInt("sheetColumns"),
                    object.getInt("sheetRows"),
                    object.getInt("startFrame"),
                    object.getInt("endFrame"), object.getInt("msPerFrame"),
                    object.getBoolean("cyclic"));
        }

        @Override
        public Json.Object toJson(SpriteComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("assetPath", object.getImg());
            obj.put("spriteWidth", object.getSw());
            obj.put("spriteHeight", object.getSh());
            obj.put("sheetColumns", object.getCols());
            obj.put("sheetRows", object.getRows());
            obj.put("startFrame", object.getStartFrame());
            obj.put("endFrame", object.getEndFrame());
            obj.put("msPerFrame", object.getMsPerFrame());
            obj.put("cyclic", object.isCyclic());
            return obj;
        }
    }
}
