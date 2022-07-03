package objects.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.awt.*;

public class Bomb extends GameMapObject {

    public Bomb(Vector2 position, Dimension size) {
        this.position = position;
        this.dimension = size;
    }

    @Override
    public void render(SpriteBatch batch, float stateTime) { }
}
