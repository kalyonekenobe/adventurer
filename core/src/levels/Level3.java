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

public class Level3 extends GameLevel {

    private final Texture backgroundImage = new Texture("background3.jpg");

    public Level3(GameScreen gameScreen) {
        this.mapObjects = new Array<>();
        this.mapPathName = "Maps/Level3.tmx";
        this.gameScreen = gameScreen;
        this.mapHelper = new MapHelper(this);
        this.orthogonalTiledMapRenderer = this.mapHelper.setupMap(mapPathName);
        this.map = mapHelper.getTiledMap();
        initializeMapObjects();
    }

    private void initializeMapObjects() {
        adventurer = new Adventurer(new Vector2(160, 448), new Dimension(30, 62), gameScreen.getWorld(), this, 1);
        adventurer.setMap(map);
        adventurer.setContactListener((ObjectsContactListener) gameScreen.getContactListener());
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(464, 656), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(784, 656), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(1104, 656), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(1424, 656), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(1744, 656), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(2064, 656), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(2384, 656), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(2704, 656), new Dimension(32, 32), new Dimension(4, 16), 15));

        mapObjects.add(new Coin(new Vector2(464, 624), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Coin(new Vector2(1584, 464), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Coin(new Vector2(2704, 624), new Dimension(32, 32), getWorld()));
        this.totalCoins = 3;
    }

    @Override
    public void render(SpriteBatch batch, float stateTime) {
        batch.draw(backgroundImage, gameScreen.getCamera().position.x - Gdx.graphics.getWidth() / 2.0f, gameScreen.getCamera().position.y - Gdx.graphics.getHeight() / 2.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        super.render(batch, stateTime);
    }
}
