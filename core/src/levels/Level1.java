package levels;

import com.adventurer.game.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import helper.BodyHelper;
import helper.MapHelper;
import helper.ObjectsContactListener;
import objects.elements.Box;
import objects.elements.GameMapObject;
import objects.elements.Ladder;
import objects.elements.Rope;
import objects.player.Adventurer;

import java.awt.*;

import static helper.Constants.PIXELS_PER_METER;

public class Level1 extends GameLevel {

    private final Texture backgroundImage = new Texture("background3.jpg");

    public Level1(GameScreen gameScreen) {
        this.mapObjects = new Array<>();
        this.mapPathName = "Maps/MapTest.tmx";
        this.gameScreen = gameScreen;
        this.mapHelper = new MapHelper(this);
        this.orthogonalTiledMapRenderer = this.mapHelper.setupMap(mapPathName);
        this.map = mapHelper.getTiledMap();
        initializeMapObjects();
    }

    private void initializeMapObjects() {
        adventurer = new Adventurer(new Vector2(1020, 320), new Dimension(30, 62), gameScreen.getWorld(), this);
        adventurer.setMap(map);
        adventurer.setContactListener((ObjectsContactListener) gameScreen.getContactListener());
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(500, 700), new Dimension(32, 32), new Dimension(4, 16), 20));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(800, 700), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(100, 500), new Dimension(32, 32), new Dimension(4, 16), 18));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(1000, 960), new Dimension(32, 32), new Dimension(4, 16), 35));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(1808, 816), new Dimension(32, 32), new Dimension(4, 16), 32));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(2164, 916), new Dimension(32, 32), new Dimension(4, 16), 16));
        mapObjects.add(new Box(new Vector2(2560, 128), new Dimension(64, 64), getWorld()));
        mapObjects.add(new Box(new Vector2(2560, 160), new Dimension(64, 64), getWorld()));
        mapObjects.add(new Box(new Vector2(2560, 192), new Dimension(64, 64), getWorld()));
        mapObjects.add(new Box(new Vector2(2560, 224), new Dimension(64, 64), getWorld()));
        mapObjects.add(new Box(new Vector2(2560, 256), new Dimension(64, 64), getWorld()));
        mapObjects.add(new Box(new Vector2(2560, 288), new Dimension(64, 64), getWorld()));
        mapObjects.add(new Box(new Vector2(2560, 320), new Dimension(64, 64), getWorld()));
        mapObjects.add(new Box(new Vector2(2560, 352), new Dimension(64, 64), getWorld()));
    }

    @Override
    public void render(SpriteBatch batch, float stateTime) {
        batch.draw(backgroundImage, gameScreen.getCamera().position.x - Gdx.graphics.getWidth() / 2.0f, gameScreen.getCamera().position.y - Gdx.graphics.getHeight() / 2.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        super.render(batch, stateTime);
    }
}

