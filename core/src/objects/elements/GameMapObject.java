package objects.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.awt.*;

public abstract class GameMapObject {

    protected Vector2 position;
    protected Dimension dimension;
    protected Body body;

    public abstract void render(SpriteBatch batch);

    public void setBody(Body body) { this.body = body; }

    public Body getBody() { return body; }

    public Vector2 getPosition() { return position; }

    public void setPosition(Vector2 position) { this.position = position; }

    public Dimension getDimension() { return dimension; }
}
