package objects.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import helper.BodyHelper;

import java.awt.*;
import java.util.Iterator;

import static helper.Constants.PIXELS_PER_METER;

public class Rope extends GameMapObject {

    public static final Sound sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/rope.mp3"));
    private static final TextureRegion texture = new TextureRegion(new Texture("tiles_packed.png"), 301, 128, 8, 32);
    private static final TextureRegion baseTexture = new TextureRegion(new Texture("tiles_packed.png"), 320, 96, 31, 32);

    private final Dimension baseSize;
    private final Dimension partSize;
    private final Body base;
    private final World world;
    private final int numberOfParts;

    public Rope(World world, Vector2 position, Dimension baseSize, Dimension partSize, int numberOfParts) {
        this.numberOfParts = numberOfParts;
        this.world = world;
        this.baseSize = baseSize;
        this.partSize = partSize;
        this.base = BodyHelper.createBody(position, baseSize, true, true, world, this);
        this.base.getFixtureList().get(0).setSensor(true);
        this.position = position;
        BodyHelper.buildRopeJoints(world, partSize, numberOfParts, base);
    }

    @Override
    public void render(SpriteBatch batch, float stateTime) {
        batch.draw(baseTexture, position.x - baseSize.width / 2.0f, position.y - baseSize.height, baseSize.width, 32);
        for (JointEdge edge : base.getJointList()) {
            Joint joint = edge.joint;
            TextureRegion region = new TextureRegion(texture);
            batch.draw(region, joint.getBodyB().getPosition().x * PIXELS_PER_METER - partSize.width / 2.0f, joint.getBodyB().getPosition().y * PIXELS_PER_METER - partSize.height / 2.0f, partSize.width / 2.0f, partSize.height / 2.0f, partSize.width, partSize.height, 1, 1, joint.getBodyB().getAngle() / (float) Math.PI * 180);
        }
    }

    public Body getPreviousPart(Body body) {
        Joint prevJoint = null;
        for (JointEdge edge : base.getJointList()) {
            Joint joint = edge.joint;
            if (joint.getBodyB().equals(body)) {
                return (prevJoint == null) ? body : prevJoint.getBodyB();
            }
            prevJoint = joint;
        }
        return body;
    }

    public Body getNextPart(Body body) {
        if (base.getJointList().first().joint.getBodyB().equals(body))
            return base.getJointList().get(2) == null ? body : base.getJointList().get(2).joint.getBodyB();
        for (Iterator<JointEdge> it = base.getJointList().iterator(); it.hasNext(); ) {
            Joint joint = it.next().joint;
            if (joint.getBodyB().equals(body)) {
                if (it.hasNext())
                    return it.next().joint.getBodyB();
                else
                    return body;
            }
        }
        return body;
    }

    public void swing(Vector2 impulseVector) {
        for (JointEdge edge : base.getJointList()) {
            Body body = edge.joint.getBodyB();
            body.applyLinearImpulse(impulseVector, body.getPosition(), true);
        }
    }

    public Body getBase() { return base; }

    public Dimension getPartSize() { return partSize; }
}
