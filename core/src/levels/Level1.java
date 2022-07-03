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
import objects.elements.*;
import objects.player.Adventurer;

import java.awt.*;

import static helper.Constants.PIXELS_PER_METER;

public class Level1 extends GameLevel {

    private final Texture backgroundImage = new Texture("background3.jpg");

    public Level1(GameScreen gameScreen) {
        this.mapObjects = new Array<>();
        this.mapPathName = "Maps/Level1.tmx";
        this.gameScreen = gameScreen;
        this.mapHelper = new MapHelper(this);
        this.orthogonalTiledMapRenderer = this.mapHelper.setupMap(mapPathName);
        this.map = mapHelper.getTiledMap();
        initializeMapObjects();
    }

    private void initializeMapObjects() {
        adventurer = new Adventurer(new Vector2(160, 160), new Dimension(30, 62), gameScreen.getWorld(), this, 5);
        adventurer.setMap(map);
        adventurer.setContactListener((ObjectsContactListener) gameScreen.getContactListener());
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(976, 560), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(1616, 880), new Dimension(32, 32), new Dimension(4, 16), 35));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(2000, 880), new Dimension(32, 32), new Dimension(4, 16), 20));
        mapObjects.add(new Box(new Vector2(2624, 160), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2592, 160), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2624, 192), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2592, 192), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2624, 226), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2592, 226), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Coin(new Vector2(656, 192), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Coin(new Vector2(1696, 704), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Coin(new Vector2(2304, 192), new Dimension(32, 32), getWorld()));
        this.totalCoins = 3;
    }

    @Override
    public void render(SpriteBatch batch, float stateTime) {
        batch.draw(backgroundImage, gameScreen.getCamera().position.x - Gdx.graphics.getWidth() / 2.0f, gameScreen.getCamera().position.y - Gdx.graphics.getHeight() / 2.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        super.render(batch, stateTime);
    }
}

