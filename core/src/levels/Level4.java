package levels;

import com.adventurer.game.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import helper.MapHelper;
import helper.ObjectsContactListener;
import objects.elements.Box;
import objects.elements.Coin;
import objects.elements.Rope;
import objects.player.Adventurer;

import java.awt.*;

public class Level4 extends GameLevel {

    private final Texture backgroundImage = new Texture("background3.jpg");

    public Level4(GameScreen gameScreen) {
        this.mapObjects = new Array<>();
        this.mapPathName = "Maps/Level4.tmx";
        this.gameScreen = gameScreen;
        this.mapHelper = new MapHelper(this);
        this.orthogonalTiledMapRenderer = this.mapHelper.setupMap(mapPathName);
        this.map = mapHelper.getTiledMap();
        initializeMapObjects();
    }

    private void initializeMapObjects() {
        adventurer = new Adventurer(new Vector2(160, 192), new Dimension(30, 62), gameScreen.getWorld(), this, 2);
        adventurer.setMap(map);
        adventurer.setContactListener((ObjectsContactListener) gameScreen.getContactListener());
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(1552, 1072), new Dimension(32, 32), new Dimension(4, 16), 15));

        mapObjects.add(new Box(new Vector2(816, 224), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(816, 256), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(816, 288), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(848, 224), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(848, 256), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(848, 288), new Dimension(32, 32), getWorld()));

        mapObjects.add(new Box(new Vector2(2096, 224), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2096, 256), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2096, 288), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2128, 224), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2128, 256), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2128, 288), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2160, 224), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2160, 256), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2160, 288), new Dimension(32, 32), getWorld()));

        mapObjects.add(new Coin(new Vector2(976, 224), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Coin(new Vector2(2832, 224), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Coin(new Vector2(3120, 224), new Dimension(32, 32), getWorld()));
        this.totalCoins = 3;
    }

    @Override
    public void render(SpriteBatch batch, float stateTime) {
        batch.draw(backgroundImage, gameScreen.getCamera().position.x - Gdx.graphics.getWidth() / 2.0f, gameScreen.getCamera().position.y - Gdx.graphics.getHeight() / 2.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        super.render(batch, stateTime);
    }
}
