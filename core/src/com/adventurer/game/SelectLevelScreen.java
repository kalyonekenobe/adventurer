package com.adventurer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import helper.MenuButton;
import objects.player.Adventurer;

import java.awt.*;
import java.util.ArrayList;

public class SelectLevelScreen implements Screen {

    private static final Texture levelsMenuBoard = new Texture("levels_board.png");
    private static final Texture selectLevel = new Texture("select_level.png");
    private static final Texture numbersTextures = new Texture("numbers-icon-set.png");

    private class LevelButton {

        public final Texture texture = new Texture("level_button.png");
        public Vector2 position;
        public Dimension size;
        public String text;

        public LevelButton(Vector2 position, Dimension size, String text) {
            this.position = position;
            this.text = text;
            this.size = size;
        }

        public boolean hasPointInside(int x, int y) {
            return x >= position.x && x <= position.x + size.width && y >= position.y && y <= position.y + size.height;
        }
    }

    private final SpriteBatch batch;
    private ArrayList<LevelButton> levels;
    private AdventurerGame game;
    private final BitmapFont font;

    public SelectLevelScreen(AdventurerGame game) {
        this.batch = new SpriteBatch();
        this.game = game;
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        this.levels = new ArrayList<>();
    }

    @Override
    public void show() {
        for (int i = 1; i <= 5; i++) {
            this.levels.add(new LevelButton(new Vector2(), new Dimension(), String.valueOf(i)));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(MenuScreen.logo, 0, 0);
        int windowWidth = Gdx.graphics.getWidth();
        int windowHeight = Gdx.graphics.getHeight();
        batch.draw(levelsMenuBoard, (windowWidth - 500) / 2.0f, (windowHeight - 300) / 2.0f, 500, 300);
        batch.draw(selectLevel, (windowWidth - 150) / 2.0f, 140 + (windowHeight - 50) / 2.0f, 150, 50);
        for (LevelButton button : levels) {
            int buttonWidth = 80;
            int buttonHeight = 80;
            int gap = 20;
            int buttonsPerRow = Math.min(3, levels.size() - 3 * (levels.indexOf(button) / 3));
            int value = Integer.parseInt(button.text);
            button.position.x = (windowWidth - buttonsPerRow * buttonWidth - (buttonsPerRow - 1) * gap) / 2.0f + (buttonWidth + gap) * (levels.indexOf(button) % 3);
            button.position.y = windowHeight / 2.0f - (gap + buttonHeight) * (int)(levels.indexOf(button) / 3);
            button.size.width = buttonWidth;
            button.size.height = buttonHeight;
            batch.draw(button.texture, button.position.x, button.position.y, button.size.width, button.size.height);
            TextureRegion numberIcon = new TextureRegion(numbersTextures, (value - 1) % 3 * 200, (int)((value - 1) / 3) * 188, 200, 188);
            batch.draw(numberIcon, button.position.x, button.position.y, buttonWidth, buttonHeight);
        }
        batch.end();
        checkMouseOverButtons();
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

    private void checkMouseOverButtons() {
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        Gdx.graphics.setSystemCursor(com.badlogic.gdx.graphics.Cursor.SystemCursor.Arrow);
        for (LevelButton button : levels) {
            if (button.hasPointInside(mouseX, mouseY)) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                if (Gdx.input.justTouched()) {
                    game.levelId = Integer.parseInt(button.text);
                    game.setScreen(new MenuScreen(game));
                    this.dispose();
                }
            }
        }
    }
}
