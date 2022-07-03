package objects.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.awt.*;

public class Finish extends GameMapObject {

    public Finish(Vector2 position, Dimension dimension) {
        this.position = position;
        this.dimension = dimension;
    }

    @Override
    public void setBody(Body body) {
        this.body = body;
        this.body.getFixtureList().get(0).setSensor(true);
    }

    @Override
    public void render(SpriteBatch batch, float stateTime) { }
}
