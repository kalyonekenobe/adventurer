package com.adventurer.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import helper.ButtonsSprites;
import helper.MenuButton;
import levels.GameLevel;
import objects.elements.Coin;
import objects.elements.GameMapObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameResultsScreen implements Screen {

    public enum GameResults {
        VICTORY,
        DEFEAT
    }

    private static final Texture victoryImage = new Texture("victory.png");
    private static final Texture defeatImage = new Texture("defeat.png");
    private static final Texture coinImage = new Texture("coin.png");
    private static final Texture notCollectedCoinImage = new Texture("not_collected_coin.png");

    public static final Sound victorySound = Gdx.audio.newSound(Gdx.files.internal("Sounds/victory.wav"));
    public static final Sound defeatSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/defeat.wav"));

    public final AdventurerGame game;
    private final List<MenuButton> buttons;
    private final GameLevel level;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final GameResults gameResult;

    public GameResultsScreen(AdventurerGame game, GameLevel level, GameResults result) {
        this.game = game;
        this.level = level;
        this.buttons = new ArrayList<>();
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.gameResult = result;

        switch (gameResult) {
            case VICTORY:
                victorySound.play(0.5f);
                break;
            case DEFEAT:
                defeatSound.play(0.5f);
                break;
        }
    }


    @Override
    public void show() {
        buttons.add(new MenuButton(ButtonsSprites.GameButtons.REPLAY, new Dimension((int)(95 * 1.5f), (int)(37 * 1.5f))));
        buttons.add(new MenuButton(ButtonsSprites.GameButtons.MENU, new Dimension((int)(95 * 1.5f), (int)(37 * 1.5f))));
        setButtonsLocations();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(MenuScreen.logo, 0, 0);
        int windowWidth = Gdx.graphics.getWidth();
        int windowHeight = Gdx.graphics.getHeight();
        switch (gameResult) {
            case VICTORY:
                batch.draw(victoryImage, (windowWidth - victoryImage.getWidth()) / 2.0f, (windowHeight - victoryImage.getHeight()) / 2.0f);
                break;
            case DEFEAT:
                batch.draw(defeatImage, (windowWidth - defeatImage.getWidth()) / 2.0f, (windowHeight - defeatImage.getHeight()) / 2.0f);
                break;
        }


        for (MenuButton button : buttons) {
            batch.draw(button.getTexture(), button.getLocation().x, button.getLocation().y, button.getSize().width, button.getSize().height);
        }

        if (gameResult == GameResults.VICTORY) {
            for (int i = 0; i < level.getCoinsCollected(); i++) {
                batch.draw(coinImage, (windowWidth - level.getTotalCoins() * 64) / 2.0f + 64 * i, (windowHeight - 64) / 2.0f + 80, 64, 64);
            }

            for (int i = 0; i < level.getTotalCoins() - level.getCoinsCollected(); i++) {
                batch.draw(notCollectedCoinImage, (windowWidth - level.getTotalCoins() * 64) / 2.0f + 64 * level.getCoinsCollected() + 64 * i, (windowHeight - 64) / 2.0f + 80, 64, 64);
            }
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
            button.setLocation((screenWidth - buttonWidth) / 2, (gameResult == GameResults.VICTORY ? -50 : 0) + (screenHeight - (screenHeight - summaryButtonsHeight) / 2) - buttonHeight - drawnButtonsHeight);
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
                        case REPLAY:
                            this.dispose();
                            game.setScreen(new GameScreen(game, Integer.parseInt(String.valueOf(level.getClass().getName().charAt(level.getClass().getName().length() - 1)))));
                            break;
                        case MENU:
                            this.dispose();
                            MenuScreen.menuSound.loop(1.0f);
                            game.setScreen(new MenuScreen(game));
                            break;
                    }

                }
            }
        }
    }
}
