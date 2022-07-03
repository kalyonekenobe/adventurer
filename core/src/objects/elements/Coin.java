package objects.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import helper.BodyHelper;

import java.awt.*;

import static helper.Constants.PIXELS_PER_METER;

public class Coin extends GameMapObject {

    public static final Sound sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/coin.mp3"));
    private static final Texture texture = new Texture("coin3_16x16.png");
    private static final TextureRegion[] textureRegions = new TextureRegion[] {
            new TextureRegion(texture, 0, 0, 16, 16),
            new TextureRegion(texture, 16, 0, 16, 16),
            new TextureRegion(texture, 32, 0, 16, 16),
            new TextureRegion(texture, 48, 0, 16, 16),
            new TextureRegion(texture, 64, 0, 16, 16),
            new TextureRegion(texture, 80, 0, 16, 16),
            new TextureRegion(texture, 96, 0, 16, 16),
            new TextureRegion(texture, 112, 0, 16, 16),
            new TextureRegion(texture, 128, 0, 16, 16),
            new TextureRegion(texture, 144, 0, 16, 16),
            new TextureRegion(texture, 160, 0, 16, 16),
            new TextureRegion(texture, 176, 0, 16, 16),
            new TextureRegion(texture, 192, 0, 16, 16),
            new TextureRegion(texture, 208, 0, 16, 16),
    };

    private final Animation<TextureRegion> animation;
    private final World world;

    public Coin(Vector2 position, Dimension dimension, World world) {
        this.position = new Vector2(position.x / PIXELS_PER_METER - dimension.width / 2.0f / PIXELS_PER_METER, position.y / PIXELS_PER_METER - dimension.height / 2.0f / PIXELS_PER_METER);
        this.dimension = dimension;
        this.animation = new Animation<>(0.05f, textureRegions);
        this.world = world;
        this.body = BodyHelper.createBody(position, dimension, true, true, world, this);
        this.body.getFixtureList().get(0).setSensor(true);
    }

    @Override
    public void render(SpriteBatch batch, float stateTime) {
        batch.draw(animation.getKeyFrame(stateTime, true), body.getPosition().x * PIXELS_PER_METER - dimension.width / 2.0f, body.getPosition().y * PIXELS_PER_METER - dimension.height / 2.0f, dimension.width, dimension.height);
    }

    public World getWorld() { return world; }
}
