package com.adventurer.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import helper.CustomInputProcessor;

public class AdventurerGame extends Game {

    public SpriteBatch batch;
    public int levelId = 5;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MenuScreen(this));
        Gdx.input.setInputProcessor(new CustomInputProcessor());
        MenuScreen.menuSound.loop(0.5f);
    }

    @Override
    public void render() {
        super.render();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
}
