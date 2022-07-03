package objects.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import helper.BodyHelper;

import java.awt.*;

import static helper.Constants.PIXELS_PER_METER;

public class Box extends GameMapObject {

    private static final TextureRegion texture = new TextureRegion(new Texture("tiles_packed.png"), 192, 32, 32, 32);

    private final World world;

    public Box(Vector2 position, Dimension dimension, World world) {
        this.position = position;
        this.dimension = dimension;
        this.world = world;
        this.body = BodyHelper.createBody(position, dimension, false, false, world, this);
    }

    @Override
    public void render(SpriteBatch batch, float stateTime) {
        batch.draw(texture, body.getPosition().x * PIXELS_PER_METER - dimension.width / 2.0f, body.getPosition().y * PIXELS_PER_METER - dimension.height / 2.0f, dimension.width / 2.0f, dimension.height / 2.0f, dimension.width, dimension.height, 1, 1, body.getAngle() / (float)Math.PI * 180);
    }
}
