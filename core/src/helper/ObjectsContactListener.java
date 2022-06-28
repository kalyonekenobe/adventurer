package helper;

import com.badlogic.gdx.physics.box2d.*;
import objects.player.Adventurer;

public class ObjectsContactListener implements ContactListener {

    protected Object objectA;
    protected Object objectB;
    public Object lastAdventurerContact;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureB == null) return;
        if (fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;

        objectA = fixtureA.getUserData();
        objectB = fixtureB.getUserData();

        if (objectA instanceof Adventurer)
            lastAdventurerContact = objectB;
        if (objectB instanceof Adventurer)
            lastAdventurerContact = objectA;
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA == null || fixtureB == null) return;
        if (fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;

        objectA = null;
        objectB = null;
        lastAdventurerContact = null;
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
