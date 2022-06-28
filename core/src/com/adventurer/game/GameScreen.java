package com.adventurer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import helper.BodyHelper;
import helper.MapHelper;
import helper.ObjectsContactListener;
import levels.GameLevel;
import levels.Level1;

import static helper.Constants.PIXELS_PER_METER;

public class GameScreen implements Screen {

    private final AdventurerGame game;
    private final World world;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final OrthographicCamera camera;
    private final ObjectsContactListener contactListener;

    private float stateTime;

    private GameLevel level;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TiledMap map;

    public GameScreen(AdventurerGame game) {
        this.camera = new OrthographicCamera();
        this.world = new World(new Vector2(0, -50f), false);
        this.contactListener = new ObjectsContactListener();
        this.world.setContactListener(contactListener);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.game = game;
    }

    @Override
    public void show() {
        this.level = new Level1(this);
        this.orthogonalTiledMapRenderer = level.getOrthogonalTiledMapRenderer();
        this.map = level.getMap();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void update() {
        stateTime += Gdx.graphics.getDeltaTime();

        world.step(Gdx.graphics.getDeltaTime(), 8, 3);
        cameraUpdate();

        game.batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
    }

    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = Math.max(level.getAdventurer().getBody().getPosition().x * PIXELS_PER_METER, Gdx.graphics.getWidth() / 2.0f);
        position.x = Math.min(map.getProperties().get("width", Integer.class) * PIXELS_PER_METER - Gdx.graphics.getWidth() / 2.0f, position.x);
        position.y = Math.max(level.getAdventurer().getBody().getPosition().y * PIXELS_PER_METER, Gdx.graphics.getHeight() / 2.0f);
        camera.position.set(position);
        camera.update();
    }

    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.batch.begin();
        level.render(game.batch, stateTime);
        orthogonalTiledMapRenderer.render();
        game.batch.end();


        //box2DDebugRenderer.render(world, camera.combined.scl(PIXELS_PER_METER));
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

    public World getWorld() { return world; }

    public OrthographicCamera getCamera() { return camera; }

    public ContactListener getContactListener() { return contactListener; }
}
