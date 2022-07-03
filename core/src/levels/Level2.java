package levels;

import com.adventurer.game.GameScreen;
import com.badlogic.gdx.Game;
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

public class Level2 extends GameLevel {

    private final Texture backgroundImage = new Texture("background3.jpg");

    public Level2(GameScreen gameScreen) {
        this.mapObjects = new Array<>();
        this.mapPathName = "Maps/Level2.tmx";
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
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(464, 528), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(1136, 1040), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(2192, 784), new Dimension(32, 32), new Dimension(4, 16), 15));

        mapObjects.add(new Box(new Vector2(1200, 176), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(1200, 208), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(1232, 176), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(1232, 208), new Dimension(32, 32), getWorld()));

        mapObjects.add(new Box(new Vector2(1520, 928), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(1520, 978), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(1520, 1016), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(1552, 928), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(1552, 978), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(1552, 1016), new Dimension(32, 32), getWorld()));

        mapObjects.add(new Coin(new Vector2(16, 752), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Coin(new Vector2(1536, 1072), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Coin(new Vector2(1808, 464), new Dimension(32, 32), getWorld()));
        this.totalCoins = 3;
    }

    @Override
    public void render(SpriteBatch batch, float stateTime) {
        batch.draw(backgroundImage, gameScreen.getCamera().position.x - Gdx.graphics.getWidth() / 2.0f, gameScreen.getCamera().position.y - Gdx.graphics.getHeight() / 2.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        super.render(batch, stateTime);
    }
}
