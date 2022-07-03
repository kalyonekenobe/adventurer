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

public class Level5 extends GameLevel {

    private final Texture backgroundImage = new Texture("background3.jpg");

    public Level5(GameScreen gameScreen) {
        this.mapObjects = new Array<>();
        this.mapPathName = "Maps/Level5.tmx";
        this.gameScreen = gameScreen;
        this.mapHelper = new MapHelper(this);
        this.orthogonalTiledMapRenderer = this.mapHelper.setupMap(mapPathName);
        this.map = mapHelper.getTiledMap();
        initializeMapObjects();
    }

    private void initializeMapObjects() {
        adventurer = new Adventurer(new Vector2(160, 160), new Dimension(30, 62), gameScreen.getWorld(), this, 3);
        adventurer.setMap(map);
        adventurer.setContactListener((ObjectsContactListener) gameScreen.getContactListener());
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(208, 1104), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(1264, 848), new Dimension(32, 32), new Dimension(4, 16), 15));
        mapObjects.add(new Rope(gameScreen.getWorld(), new Vector2(1616, 848), new Dimension(32, 32), new Dimension(4, 16), 15));

        mapObjects.add(new Box(new Vector2(240, 224), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(240, 256), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(240, 288), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(272, 224), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(272, 256), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(272, 288), new Dimension(32, 32), getWorld()));

        mapObjects.add(new Box(new Vector2(2096, 592), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2096, 624), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2096, 656), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2128, 592), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2128, 624), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2128, 656), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2160, 592), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2160, 624), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2160, 656), new Dimension(32, 32), getWorld()));

        mapObjects.add(new Box(new Vector2(2000, 176), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2032, 176), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2288, 176), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2320, 176), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2928, 176), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(2960, 176), new Dimension(32, 32), getWorld()));

        mapObjects.add(new Box(new Vector2(3154, 432), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(3154, 464), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Box(new Vector2(3154, 496), new Dimension(32, 32), getWorld()));

        mapObjects.add(new Coin(new Vector2(16, 864), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Coin(new Vector2(2240, 1056), new Dimension(32, 32), getWorld()));
        mapObjects.add(new Coin(new Vector2(3152, 192), new Dimension(32, 32), getWorld()));
        this.totalCoins = 3;
    }

    @Override
    public void render(SpriteBatch batch, float stateTime) {
        batch.draw(backgroundImage, gameScreen.getCamera().position.x - Gdx.graphics.getWidth() / 2.0f, gameScreen.getCamera().position.y - Gdx.graphics.getHeight() / 2.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        super.render(batch, stateTime);
    }
}
