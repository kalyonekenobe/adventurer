package helper;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Array;
import objects.elements.Box;
import objects.elements.GameMapObject;
import objects.elements.Ground;
import objects.elements.Ladder;
import objects.player.Adventurer;

import java.awt.*;

import static helper.Constants.PIXELS_PER_METER;

public class BodyHelper {

    public static Body createBody(Vector2 position, Dimension size, boolean isStatic, boolean fixedRotation, World world, Object userData) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x / PIXELS_PER_METER, position.y / PIXELS_PER_METER);
        bodyDef.fixedRotation = fixedRotation;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.width / 2.0f / PIXELS_PER_METER, size.height / 2.0f / PIXELS_PER_METER);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = (userData instanceof Box) ? 10.0f : 0;
        fixtureDef.density = (userData instanceof Box) ? 2.0f : 1.0f;
        body.setUserData(userData);
        body.createFixture(fixtureDef).setUserData(userData);
        shape.dispose();
        return body;
    }

    public static void resizeBody(Body body, Dimension size) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.width / 2.0f / PIXELS_PER_METER, size.height / 2.0f / PIXELS_PER_METER);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = body.getFixtureList().get(0).getFriction();
        fixtureDef.density = body.getFixtureList().get(0).getDensity();
        for (Fixture fixture : body.getFixtureList()) {
            body.destroyFixture(fixture);
        }
        body.createFixture(fixtureDef).setUserData(body.getUserData());
        shape.dispose();
    }

    public static void buildRopeJoints(World world, Dimension partSize, int numberOfParts, Body base) {
        Array<Body> bodies = new Array<>();
        bodies.add(base);

        for (int i = 1; i <= numberOfParts; i++) {
            bodies.add(createBody(new Vector2(base.getPosition().x * PIXELS_PER_METER, base.getPosition().y * PIXELS_PER_METER - i * partSize.height), partSize, false, false, world, base.getUserData()));

            bodies.get(i).setUserData(base.getUserData());
            bodies.get(i).getFixtureList().get(0).setUserData(bodies.get(i));

            RopeJointDef ropeJointDef = new RopeJointDef();
            ropeJointDef.bodyA = bodies.get(0);
            ropeJointDef.bodyB = bodies.get(i);
            ropeJointDef.collideConnected = true;
            ropeJointDef.maxLength = i * (partSize.height / PIXELS_PER_METER);
            ropeJointDef.localAnchorA.set(0, -partSize.height / PIXELS_PER_METER / 2.0f);
            ropeJointDef.localAnchorB.set(0, partSize.height / PIXELS_PER_METER / 2.0f);
            world.createJoint(ropeJointDef);

            RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
            revoluteJointDef.bodyA = bodies.get(i - 1);
            revoluteJointDef.bodyB = bodies.get(i);
            revoluteJointDef.localAnchorA.set(0, -partSize.height / PIXELS_PER_METER / 2.0f);
            revoluteJointDef.localAnchorB.set(0, partSize.height / PIXELS_PER_METER / 2.0f);

            world.createJoint(revoluteJointDef);
        }
    }

    public static float[] getBodyVertices(Body body) {
        float[] vertices = new float[8];
        Dimension size = new Dimension(0, 0);
        Vector2 position = new Vector2(0, 0);
        if (body.getUserData() instanceof Adventurer) {
            size = new Dimension(((Adventurer) body.getUserData()).getWidth(), ((Adventurer) body.getUserData()).getHeight());
            position = new Vector2(((Adventurer) body.getUserData()).getX() - size.width / 2.0f, ((Adventurer) body.getUserData()).getY() - size.height / 2.0f);
        } else if (body.getUserData() instanceof GameMapObject) {
            size = ((GameMapObject) body.getUserData()).getDimension();
            position = new Vector2(((GameMapObject) body.getUserData()).getPosition().x * PIXELS_PER_METER, ((GameMapObject) body.getUserData()).getPosition().y * PIXELS_PER_METER);
        }
        vertices[0] = position.x;
        vertices[1] = position.y;
        vertices[2] = position.x;
        vertices[3] = position.y + size.height;
        vertices[4] = position.x + size.width;
        vertices[5] = position.y + size.height;
        vertices[6] = position.x + size.width;
        vertices[7] = position.y;
        return vertices;
    }
}
