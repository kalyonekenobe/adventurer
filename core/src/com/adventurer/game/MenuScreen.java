package com.adventurer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import helper.ButtonsSprites;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MenuScreen implements Screen {

    public class MenuButton {
        private TextureRegion texture;
        private ButtonsSprites.GameButtons name;
        private Vector2 location;

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

        public boolean hasPointInside(int x, int y) {
            return x >= location.x && x <= location.x + texture.getRegionWidth() && y >= location.y && y <= location.y + texture.getRegionHeight();
        }
    }

    private AdventurerGame game;
    private List<MenuButton> buttons = new ArrayList<>();

    public MenuScreen(AdventurerGame game) {
        this.game = game;
        buttons.add(new MenuButton(ButtonsSprites.GameButtons.PLAY));
        buttons.add(new MenuButton(ButtonsSprites.GameButtons.REPLAY));
        setButtonsLocations();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        for (MenuButton button : buttons) {
            game.batch.draw(button.getTexture(), button.getLocation().x, button.getLocation().y);
        }

        checkMouseOverButtons();

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void setButtonsLocations() {
        int gap = 20;
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        int summaryButtonsHeight = gap * (buttons.size() - 1);
        for (MenuButton button : buttons) {
            summaryButtonsHeight += button.getTexture().getRegionHeight();
        }
        int drawnButtonsHeight = 0;
        for (MenuButton button : buttons) {
            int buttonWidth = button.getTexture().getRegionWidth();
            int buttonHeight = button.getTexture().getRegionHeight();
            button.setLocation((screenWidth - buttonWidth) / 2, (screenHeight - (screenHeight - summaryButtonsHeight) / 2) - buttonHeight - drawnButtonsHeight);
            drawnButtonsHeight += buttonHeight + gap;
        }
    }

    private void checkMouseOverButtons() {
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        for (MenuButton button : buttons) {
            if (button.hasPointInside(mouseX, mouseY)) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                if (Gdx.input.isTouched()) {
                    switch (button.getName()) {
                        case PLAY:
                            this.dispose();
                            game.setScreen(new GameScreen(game));
                            break;
                        case REPLAY:
                            this.dispose();
                            Gdx.app.exit();
                            break;
                    }

                }
            }
        }
    }
}
