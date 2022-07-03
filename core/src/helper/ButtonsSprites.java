package helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class ButtonsSprites {

    private static final Texture texture = new Texture("game_buttons.png");

    public enum GameButtons {
        PLAY,
        REPLAY,
        LEVELS,
        MENU,
        QUIT
    }

    public static class ButtonsBounds {
        public int x;
        public int y;
        public int width;
        public int height;

        public ButtonsBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    public static Map<GameButtons, ButtonsBounds> buttonsBoundsMap = new HashMap<GameButtons, ButtonsBounds>() {{
        put(GameButtons.PLAY, new ButtonsBounds(0, 0, 95, 37));
        put(GameButtons.QUIT, new ButtonsBounds(809, 0, 95, 37));
        put(GameButtons.REPLAY, new ButtonsBounds(231, 0, 95, 37));
        put(GameButtons.MENU, new ButtonsBounds(809, 0, 95, 37));
        put(GameButtons.LEVELS, new ButtonsBounds(346, 0, 95, 37));
    }};

    public static TextureRegion getButtonSprite(GameButtons button) {
        return getButtonSprite(buttonsBoundsMap.get(button));
    }

    public static TextureRegion getButtonSprite(ButtonsBounds bounds) {
        return getButtonSprite(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public static TextureRegion getButtonSprite(int x, int y, int width, int height) {
        return new TextureRegion(texture, x, y, width, height);
    }
}
