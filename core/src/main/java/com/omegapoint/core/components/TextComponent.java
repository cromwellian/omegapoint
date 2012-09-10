package com.omegapoint.core.components;

import playn.core.Font;
import playn.core.Json;
import playn.core.PlayN;
import playn.core.TextFormat;

/**
 *
 */
public class TextComponent extends BaseComponent {

    private String text;
    private Font font;
    private TextFormat.Alignment align;
    private int color;
    public static final String NAME = "textComponent";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextComponent that = (TextComponent) o;

        if (color != that.color) return false;
        if (align != that.align) return false;
//        if (effect != null ? !effect.equals(that.effect) : that.effect != null) return false;
        if (!font.equals(that.font)) return false;
        if (!text.equals(that.text)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + font.hashCode();
        result = 31 * result + align.hashCode();
        result = 31 * result + color;
//        result = 31 * result + (effect != null ? effect.hashCode() : 0);
        return result;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public TextFormat.Alignment getAlign() {
        return align;
    }

    public void setAlign(TextFormat.Alignment align) {
        this.align = align;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

//    public TextFormat.Effect getEffect() {
//        return effect;
//    }

//    public void setEffect(TextFormat.Effect effect) {
//        this.effect = effect;
//    }

    public TextComponent(String text, Font font, TextFormat.Alignment align, int color) {
        this.text = text;
        this.font = font;
        this.align = align;
        this.color = color;
//        this.effect = effect;
    }

    @Override
    public String getComponentName() {
        return NAME;
    }

    @Override
    public BaseComponent duplicate() {
        return new TextComponent(this.getText(), this.getFont(), this.getAlign(), this.getColor());
    }

    @Override
    public Json.Object toJson() {
        return new Codec().toJson(this);
    }

    public static class Codec implements Jsonable<TextComponent> {

        @Override
        public TextComponent fromJson(Json.Object object) {
          return new TextComponent(object.getString("text"),
                  decodeFont(object.getObject("font")),
                  TextFormat.Alignment.valueOf(object.getString("align").toUpperCase()),
                  (int) Long.parseLong(object.getString("color").substring(2), 16));
        }

        private Font decodeFont(Json.Object font) {
           return PlayN.graphics().createFont(font.getString("name"),
                   Font.Style.valueOf(font.getString("style").toUpperCase()), font.getInt("size"));
        }

        @Override
        public Json.Object toJson(TextComponent object) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("text", object.getText());
            obj.put("font", encodeFont(object.getFont()));
            obj.put("align", object.getAlign().name().toLowerCase());
            obj.put("color", "0x"+Integer.toHexString(object.getColor()));
            return obj;
        }

        private Object encodeFont(Font font) {
            Json.Object obj = PlayN.json().createObject();
            obj.put("name", font.name());
            obj.put("style", font.style().name().toLowerCase());
            obj.put("size", (int) font.size());
            return obj;
        }
    }
}
