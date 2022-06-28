package objects.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;

public class Ground extends GameMapObject {

    public Ground(Vector2 position, Dimension dimension) {
        this.position = position;
        this.dimension = dimension;
    }

    public Dimension getDimension() { return dimension; }

    public Vector2 getPosition() { return position; }

    @Override
    public void render(SpriteBatch batch) { }
}
