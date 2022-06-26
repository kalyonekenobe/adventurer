package helper;

import com.badlogic.gdx.physics.box2d.*;

public class ObjectsContactListener implements ContactListener {

    private Object objectA;
    private Object objectB;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureB == null) return;
        if (fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;

        objectA = fixtureA.getUserData();
        objectB = fixtureB.getUserData();
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureB == null) return;
        if (fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;

        objectA = null;
        objectB = null;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean isContactDetected() {
        return objectA != null && objectB != null;
    }

    public Object getObjectA() { return objectA; }

    public Object getObjectB() { return objectB; }
}
