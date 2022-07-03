package com.adventurer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import helper.ButtonsSprites;
import helper.MenuButton;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MenuScreen implements Screen {

    public static final Texture logo = new Texture("logo_dark.jpg");
    public static final Texture menuBoard = new Texture("menu_board.png");
    public static final Texture gameLogo = new Texture("game_logo.png");

    public static final Sound menuSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/menu.mp3"));

    public final AdventurerGame game;
    private final List<MenuButton> buttons;
    private final SpriteBatch batch;

    public MenuScreen(AdventurerGame game) {
        this.game = game;
        batch = new SpriteBatch();
        buttons = new ArrayList<>();
    }

    @Override
    public void show() {
        buttons.add(new MenuButton(ButtonsSprites.GameButtons.PLAY, new Dimension((int)(95 * 1.5f), (int)(37 * 1.5f))));
        buttons.add(new MenuButton(ButtonsSprites.GameButtons.LEVELS, new Dimension((int)(95 * 1.5f), (int)(37 * 1.5f))));
        buttons.add(new MenuButton(ButtonsSprites.GameButtons.QUIT, new Dimension((int)(95 * 1.5f), (int)(37 * 1.5f))));
        setButtonsLocations();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(logo, 0, 0);
        int windowWidth = Gdx.graphics.getWidth();
        int windowHeight = Gdx.graphics.getHeight();
        batch.draw(gameLogo, (windowWidth - 500) / 2.0f, 300 + (windowHeight - 100) / 2.0f, 500, 100);
        batch.draw(menuBoard, (windowWidth - menuBoard.getWidth()) / 2.0f, (windowHeight - menuBoard.getHeight()) / 2.0f);
        for (MenuButton button : buttons) {
            batch.draw(button.getTexture(), button.getLocation().x, button.getLocation().y, button.getSize().width, button.getSize().height);
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

    private void setButtonsLocations() {
        int gap = 20;
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        int summaryButtonsHeight = gap * (buttons.size() - 1);
        for (MenuButton button : buttons) {
            summaryButtonsHeight += button.getSize().height;
        }
        int drawnButtonsHeight = 0;
        for (MenuButton button : buttons) {
            int buttonWidth = button.getSize().width;
            int buttonHeight = button.getSize().height;
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
                if (Gdx.input.justTouched()) {
                    switch (button.getName()) {
                        case PLAY:
                            this.dispose();
                            game.setScreen(new GameScreen(game, game.levelId));
                            break;
                        case LEVELS:
                            this.dispose();
                            game.setScreen(new SelectLevelScreen(game));
                            break;
                        case QUIT:
                            this.dispose();
                            Gdx.app.exit();
                            break;
                    }

                }
            }
        }
    }
}
