package helper;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;

public class MenuButton {
    private TextureRegion texture;
    private ButtonsSprites.GameButtons name;
    private Vector2 location;
    private Dimension size;

    public MenuButton(ButtonsSprites.GameButtons buttonName) {
        this.name = buttonName;
        this.texture = ButtonsSprites.getButtonSprite(buttonName);
    }

    public MenuButton(ButtonsSprites.GameButtons buttonName, Vector2 location) {
        this.name = buttonName;
        this.location = location;
        this.texture = ButtonsSprites.getButtonSprite(buttonName);
    }

    public MenuButton(ButtonsSprites.GameButtons buttonName, int x, int y) {
        this.name = buttonName;
        this.location = new Vector2(x, y);
        this.texture = ButtonsSprites.getButtonSprite(buttonName);
    }

    public MenuButton(ButtonsSprites.GameButtons buttonName, Dimension size) {
        this.name = buttonName;
        this.texture = ButtonsSprites.getButtonSprite(buttonName);
        this.size = size;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public ButtonsSprites.GameButtons getName() {
        return name;
    }

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(int x, int y) {
        this.location = new Vector2(x, y);
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public Dimension getSize() { return size; }

    public boolean hasPointInside(int x, int y) {
        return x >= location.x && x <= location.x + size.width && y >= location.y && y <= location.y + size.height;
    }
}
