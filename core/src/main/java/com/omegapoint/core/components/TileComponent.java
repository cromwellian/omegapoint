package com.omegapoint.core.components;

import com.omegapoint.core.util.Base91;
import playn.core.Json;
import playn.core.PlayN;

/**
 * Holds a reference to a tile sheet and tile arrangement.
 */
public class TileComponent extends BaseComponent {

    private int depth;
    public static final int EMPTY_SPACE = 91;

    public String getAssetPath() {
        return assetPath;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileRows() {
        return tileRows;
    }

    public int getTileCols() {
        return tileCols;
    }

    public TileArrangement getArrangement() {
        return arrangement;
    }

    public int getParallaxSpeed() {
        return parallaxSpeed;
    }

    public TileComponent(String assetPath, int tileWidth, int tileHeight, int tileRows, int tileCols, int depth,
                         int parallaxSpeed, TileArrangement arrangement) {
        this.assetPath = assetPath;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tileRows = tileRows;
        this.tileCols = tileCols;
        this.depth = depth;
        this.parallaxSpeed = parallaxSpeed;
        this.arrangement = arrangement;
    }

    public int getCurrentScreenPosition() {
        return currentScreenPosition;
    }

    public void setCurrentScreenPosition(int currentScreenPosition) {
        this.currentScreenPosition = currentScreenPosition;
    }

    public int getDepth() {
        return depth;
    }

    public static class TileRow {
        private int[] indices;

        public TileRow(int[] indices) {
            this.indices = indices;
        }

        public int[] getIndices() {
            return indices;
        }
    }

    public static class TileArrangement {
        TileRow[] rows;

        public TileRow[] getRows() {
            return rows;
        }

        public TileArrangement(TileRow[] rows) {
            this.rows = rows;
        }

        public static class Codec implements Jsonable<TileArrangement> {

            @Override
            public TileArrangement fromJson(Json.Object object) {
                Json.Array arrayRows = object.getArray("rows");
                TileRow[] rows = new TileRow[arrayRows.length()];
                for (int i = 0; i < arrayRows.length(); i++) {
                   rows[i] = decodeRow(arrayRows.getString(i));
                }
                return new TileArrangement(rows);
            }

            private TileRow decodeRow(String string) {
                return new TileRow(Base91.decodeFromBytes(string.getBytes()));
            }

            @Override
            public Json.Object toJson(TileArrangement object) {
                Json.Array array = PlayN.json().createArray();
                for (TileRow row : object.getRows()) {
                  array.add(encodeRow(row));
                }
                Json.Object obj = PlayN.json().createObject();
                obj.put("rows", array);
                return obj;
            }

            private String encodeRow(TileRow row) {
                return new String(Base91.encodeToBytes(row.getIndices()));
            }
        }
    }

    public static final String NAME = "tileComponent";
    private String assetPath;
    private int tileWidth;
    private int tileHeight;
    private int tileRows;
    private int tileCols;
    private final int parallaxSpeed;
    private TileArrangement arrangement;
    private int currentScreenPosition = 0;

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new TileComponent(assetPath, tileWidth, tileHeight, tileRows, tileCols, depth, parallaxSpeed, arrangement);
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    public static class Codec implements Jsonable<TileComponent> {

        @Override
        public TileComponent fromJson(Json.Object object) {
            return new TileComponent(object.getString("assetPath"),
                    object.getInt("tileWidth"), object.getInt("tileHeight"),
                    object.getInt("tileRows"), object.getInt("tileCols"),
                    object.getInt("depth"),
                    object.getInt("parallaxSpeed"),
                    new TileArrangement.Codec().fromJson(object.getObject("arrangement")));
        }

        @Override
        public Json.Object toJson(TileComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("assetPath", object.getAssetPath());
            obj.put("tileWidth", object.getTileWidth());
            obj.put("tileHeight", object.getTileHeight());
            obj.put("tileRows", object.getTileRows());
            obj.put("tileCols", object.getTileCols());
            obj.put("depth", object.getDepth());
            obj.put("parallaxSpeed", object.getParallaxSpeed());
            obj.put("arrangement", new TileArrangement.Codec().toJson(object.getArrangement()));
            return obj;
        }
    }
}
