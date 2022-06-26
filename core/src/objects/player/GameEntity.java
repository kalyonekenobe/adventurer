package objects.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import helper.BodyHelper;

import java.awt.*;

public abstract class GameEntity {

    protected Vector2 position;
    protected Dimension size;
    protected float velocityX;
    protected float velocityY;
    protected float speed;
    protected Body body;
    protected World world;

    public GameEntity(Vector2 position, Dimension size, World world) {
        this.position = position;
        this.size = size;
        this.world = world;
        this.body = BodyHelper.createBody(position, size, false, true, world, this);
    }

    public abstract void update();

    public abstract void render(SpriteBatch batch);

    public Body getBody() { return body; }
}
