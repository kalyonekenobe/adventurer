package objects.player;

import com.adventurer.game.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import helper.BodyHelper;
import helper.CustomInputProcessor;
import helper.ObjectsContactListener;
import levels.GameLevel;
import levels.Level1;
import objects.elements.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static helper.Constants.PIXELS_PER_METER;

public class Adventurer extends GameEntity {

    public enum AdventurerState {
        STAY,
        RUN,
        JUMP,
        CLIMB,
        HANG,
        CROUCHING,
        DEAD
    }

    public enum AdventurerDirection {
        LEFT,
        UP,
        RIGHT,
        BOTTOM
    }

    public enum BodyState {
        LANDED,
        FLYING
    }

    private static class AdventurerAnimation {
        public final TextureRegion[] textureRegions;
        public final float duration;
        public final boolean repeat;

        public AdventurerAnimation(float duration, boolean repeat, TextureRegion[] textureRegions) {
            this.duration = duration;
            this.textureRegions = textureRegions;
            this.repeat = repeat;
        }
    }

    private static final Texture texture = new Texture("adventurer-assets.png");
    private Map<AdventurerState, AdventurerAnimation> adventurerAnimations;
    private AdventurerState adventurerState;
    private Animation<TextureRegion> animation;
    private BodyState bodyState;
    private AdventurerDirection adventurerDirection;
    private TiledMap map;
    private GameLevel level;

    private float mapWidth;
    private boolean bodyInert = false;
    private boolean canJump = true;

    public CustomInputProcessor inputProcessor;
    public ObjectsContactListener contactListener;
    public Object objectInHands;
    public Object lastContact;

    public Adventurer(Vector2 position, Dimension size, World world, GameLevel level) {
        super(position, size, world);
        this.speed = 10f;
        this.level = level;
        this.bodyState = BodyState.LANDED;
        this.adventurerDirection = AdventurerDirection.RIGHT;
        this.adventurerState = AdventurerState.STAY;
        this.inputProcessor = (CustomInputProcessor) Gdx.input.getInputProcessor();
        setAdventurerAnimations();
    }

    @Override
    public void update() {
        lastContact = contactListener.lastAdventurerContact;

        if (contactListener.isContactDetected()) {
            Object objectA = contactListener.getObjectA();
            Object objectB = contactListener.getObjectB();
            if (objectA.equals(this) && objectB instanceof Bomb || objectA instanceof Bomb && objectB.equals(this)) {
                //setState(AdventurerState.DEAD);
                //disableAdventurer();
            }
        }
        if (adventurerState != AdventurerState.DEAD) {
            position = new Vector2(body.getPosition().x * PIXELS_PER_METER, body.getPosition().y * PIXELS_PER_METER);

            if (objectInHands != null) {
                if (objectInHands instanceof Body) {
                    if (((Body) objectInHands).getUserData() instanceof Rope) {
                        Body ropeItem = (Body) objectInHands;
                        int directionCoefficient = adventurerDirection == AdventurerDirection.LEFT ? 1 : adventurerDirection == AdventurerDirection.RIGHT ? -1 : 0;
                        body.setTransform(new Vector2(ropeItem.getPosition().x - directionCoefficient * size.width / 2.0f / PIXELS_PER_METER, ropeItem.getPosition().y - ((Rope) ropeItem.getUserData()).getPartSize().height / 2.0f / PIXELS_PER_METER - size.height / 2.0f / PIXELS_PER_METER), 0);
                        ropeItem.setTransform(new Vector2(body.getPosition().x + directionCoefficient * size.width / 2.0f / PIXELS_PER_METER, body.getPosition().y + ((Rope) ropeItem.getUserData()).getPartSize().height / 2.0f / PIXELS_PER_METER + size.height / 2.0f / PIXELS_PER_METER), ropeItem.getAngle());
                    }
                }
                if (objectInHands instanceof Box) {
                    Box box = (Box) objectInHands;
                    int adventureDirectionCoefficient = adventurerDirection == AdventurerDirection.LEFT ? -1 : (adventurerDirection == AdventurerDirection.RIGHT ? 1 : 0);
                    box.getBody().setTransform(new Vector2(body.getPosition().x + adventureDirectionCoefficient * (size.width / 2.0f / PIXELS_PER_METER + box.getDimension().width / 2.0f / PIXELS_PER_METER + 2 / PIXELS_PER_METER), body.getPosition().y + box.getDimension().height / 2.0f / PIXELS_PER_METER), 0);
                    objectInHands = box;
                }
            }

            if (mapWidth == 0)
                this.mapWidth = map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class);

            interactWithKeyboard();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(getCurrentAnimationFrame(0), getX() - 50, getY() - getHeight() / 2.0f, 100, 74);
    }

    public void render(SpriteBatch batch, float stateTime) {
        batch.draw(getCurrentAnimationFrame(stateTime), getX() - 50, getY() - getHeight() / 2.0f, 50, 37, 100, 74, 1, 1, body.getTransform().getRotation() / (float)Math.PI * 180);
    }

    private void disableAdventurer() {
        this.body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
        if (bodyState == BodyState.LANDED) {
            this.body.setActive(false);
        }
    }

    private void setAdventurerAnimations() {
        adventurerAnimations = new HashMap<AdventurerState, AdventurerAnimation>() {{
            put(AdventurerState.STAY, new AdventurerAnimation(0.1f, true, new TextureRegion[] {
                    new TextureRegion(texture, 0, 0, 50, 37),
                    new TextureRegion(texture, 50, 0, 50, 37),
                    new TextureRegion(texture, 100, 0, 50, 37),
                    new TextureRegion(texture, 150, 0, 50, 37)
            }));
            put(AdventurerState.RUN, new AdventurerAnimation(0.1f, true, new TextureRegion[] {
                    new TextureRegion(texture, 50, 37, 50, 37),
                    new TextureRegion(texture, 100, 37, 50, 37),
                    new TextureRegion(texture, 150, 37, 50, 37),
                    new TextureRegion(texture, 200, 37, 50, 37),
                    new TextureRegion(texture, 250, 37, 50, 37),
                    new TextureRegion(texture, 300, 37, 50, 37),
            }));
            put(AdventurerState.JUMP, new AdventurerAnimation(0.1f, true, new TextureRegion[] {
                    new TextureRegion(texture, 0, 74, 50, 37),
                    new TextureRegion(texture, 50, 74, 50, 37),
                    new TextureRegion(texture, 100, 74, 50, 37),
                    new TextureRegion(texture, 150, 74, 50, 37),
                    new TextureRegion(texture, 200, 74, 50, 37),
                    new TextureRegion(texture, 250, 74, 50, 37),
                    new TextureRegion(texture, 300, 74, 50, 37),
                    new TextureRegion(texture, 0, 111, 50, 37),
            }));
            put(AdventurerState.CROUCHING, new AdventurerAnimation(0.1f, true, new TextureRegion[] {
                    new TextureRegion(texture, 200, 0, 50, 37),
                    new TextureRegion(texture, 250, 0, 50, 37),
                    new TextureRegion(texture, 300, 0, 50, 37),
                    new TextureRegion(texture, 0, 37, 50, 37)
            }));
            put(AdventurerState.HANG, new AdventurerAnimation(0.1f, true, new TextureRegion[] {
                    new TextureRegion(texture, 50, 148, 50, 37),
                    new TextureRegion(texture, 100, 148, 50, 37),
                    new TextureRegion(texture, 150, 148, 50, 37),
                    new TextureRegion(texture, 200, 148, 50, 37)
            }));
            put(AdventurerState.DEAD, new AdventurerAnimation(0.1f, true, new TextureRegion[] {
                    new TextureRegion(texture, 150, 111, 50, 37),
                    new TextureRegion(texture, 200, 111, 50, 37),
                    new TextureRegion(texture, 250, 111, 50, 37),
                    new TextureRegion(texture, 300, 111, 50, 37),
            }));
            put(AdventurerState.CLIMB, new AdventurerAnimation(0.1f, true, new TextureRegion[] {
                    new TextureRegion(texture, 200, 407, 50, 37),
                    new TextureRegion(texture, 250, 407, 50, 37),
                    new TextureRegion(texture, 300, 407, 50, 37),
                    new TextureRegion(texture, 0, 444, 50, 37)
            }));
        }};
        for (TextureRegion textureRegion : adventurerAnimations.get(AdventurerState.HANG).textureRegions) {
            textureRegion.flip(true, false);
        }
        setAnimation(adventurerState);
    }

    private void setAdventurerAnimations(Map<AdventurerState, AdventurerAnimation> animationFrames) {
        adventurerAnimations = animationFrames;
        setAnimation(adventurerState);
    }

    public void setState(AdventurerState adventurerState) {
        this.adventurerState = adventurerState;
        if (adventurerState == AdventurerState.CLIMB || adventurerState == AdventurerState.HANG) {
            body.setGravityScale(0);
            body.getFixtureList().get(0).setSensor(true);
        } else {
            body.setGravityScale(1);
            body.getFixtureList().get(0).setSensor(false);
        }
        this.setAnimation(adventurerState);
    }

    public AdventurerState getState() { return adventurerState; }

    public void setBodyInert(boolean bodyInert) { this.bodyInert = bodyInert; }

    public void setBodyState(BodyState bodyState) { this.bodyState = bodyState; }

    public BodyState getBodyState() { return bodyState; }

    public void setDirection(AdventurerDirection adventurerDirection) {
        if ((adventurerDirection == AdventurerDirection.LEFT || adventurerDirection == AdventurerDirection.RIGHT) && adventurerDirection != this.adventurerDirection) {
            for (Map.Entry<AdventurerState, AdventurerAnimation> entry : adventurerAnimations.entrySet()) {
                for (TextureRegion frame : adventurerAnimations.get(entry.getKey()).textureRegions) {
                    frame.flip(true, false);
                }
            }
        }
        this.adventurerDirection = adventurerDirection;
    }

    public AdventurerDirection getDirection() { return adventurerDirection; }

    public void setAnimation(AdventurerState adventurerState) {
        animation = new Animation<TextureRegion>(adventurerAnimations.get(adventurerState).duration, adventurerAnimations.get(adventurerState).textureRegions);
    }

    public TextureRegion getCurrentAnimationFrame(float stateTime) {
        if (!animation.isAnimationFinished(stateTime))
            return animation.getKeyFrame(stateTime);
        return animation.getKeyFrame(stateTime, adventurerAnimations.get(adventurerState).repeat);
    }

    public void setPosition(Vector2 position) { body.setTransform(position, body.getAngle()); }

    public boolean getCanJumpState() { return canJump; }

    public void setCanJumpState(boolean state) { this.canJump = state; }

    public float getX() { return body.getPosition().x * PIXELS_PER_METER; }

    public float getY() { return body.getPosition().y * PIXELS_PER_METER; }

    public int getWidth() { return size.width; }

    public int getHeight() { return size.height; }

    public void setSize(Dimension size) { this.size = size; }

    public void setMap(TiledMap map) { this.map = map; }

    public void setContactListener(ObjectsContactListener contactListener) { this.contactListener = contactListener; }

    public void interactWithKeyboard() {
        velocityX = 0;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && adventurerState != AdventurerState.JUMP && adventurerState != AdventurerState.CROUCHING && adventurerState != AdventurerState.CLIMB && bodyState == BodyState.LANDED && canJump) {
            if (adventurerState != AdventurerState.HANG) {
                setState(AdventurerState.JUMP);
                float force = body.getMass() * 25;
                body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (adventurerState == AdventurerState.HANG) {
                if (objectInHands != null) {
                    if (objectInHands instanceof Body) {
                        if (((Body) objectInHands).getUserData() instanceof Rope) {
                            Body ropeItem = (Body) objectInHands;
                            objectInHands = ((Rope) (ropeItem.getUserData())).getPreviousPart(ropeItem);
                        }
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (adventurerState == AdventurerState.HANG) {
                if (objectInHands != null) {
                    if (objectInHands instanceof Body) {
                        if (((Body) objectInHands).getUserData() instanceof Rope) {
                            Body ropeItem = (Body) objectInHands;
                            objectInHands = ((Rope) (ropeItem.getUserData())).getNextPart(ropeItem);
                        }
                    }
                }
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (bodyState == BodyState.LANDED && adventurerState != AdventurerState.CROUCHING && adventurerState != AdventurerState.HANG && adventurerState != AdventurerState.CLIMB) {
                setState(AdventurerState.CROUCHING);
                size.height /= 2;
                body.setTransform(new Vector2(body.getPosition().x, body.getPosition().y - size.height / 2.0f / PIXELS_PER_METER), 0);
                BodyHelper.resizeBody(body, new Dimension(size.width, size.height));
                speed *= 0.5;
            }
        }

        if (adventurerState == AdventurerState.CROUCHING) {
            if (!inputProcessor.keysStates.get(Input.Keys.DOWN)) {
                boolean underGround = false;
                for (GameMapObject mapObject : level.getMapObjects()) {
                    if (mapObject instanceof Ground) {
                        Ground ground = (Ground) mapObject;
                        if (Intersector.overlapConvexPolygons(new Polygon(BodyHelper.getBodyVertices(body)), new Polygon(BodyHelper.getBodyVertices(ground.getBody())))) {
                            underGround = body.getPosition().y - size.height / 2.0f / PIXELS_PER_METER + size.height * 2 / PIXELS_PER_METER > ground.getPosition().y && body.getPosition().y + size.height / 2.0f / PIXELS_PER_METER <= ground.getPosition().y;
                            if (underGround)
                                break;
                        }
                    }
                }
                if (!underGround) {
                    setState(AdventurerState.STAY);
                    size.height *= 2;
                    body.setTransform(new Vector2(body.getPosition().x, body.getPosition().y + size.height / 4.0f / PIXELS_PER_METER), 0);
                    BodyHelper.resizeBody(body, new Dimension(size.width, size.height));
                    speed /= 0.5;
                    for (GameMapObject mapObject : level.getMapObjects()) {
                        if (mapObject instanceof Ground) {
                            Ground ground = (Ground) mapObject;
                            if (Intersector.overlapConvexPolygons(new Polygon(BodyHelper.getBodyVertices(body)), new Polygon(BodyHelper.getBodyVertices(ground.getBody())))) {
                                underGround = body.getPosition().y + size.height / 2.0f / PIXELS_PER_METER > ground.getPosition().y && body.getPosition().y <= ground.getPosition().y;
                                if (underGround) {
                                    setState(AdventurerState.CROUCHING);
                                    size.height /= 2;
                                    body.setTransform(new Vector2(body.getPosition().x, body.getPosition().y - size.height / 2.0f / PIXELS_PER_METER), 0);
                                    BodyHelper.resizeBody(body, new Dimension(size.width, size.height));
                                    speed *= 0.5;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (inputProcessor.keysStates.get(Input.Keys.SHIFT_LEFT)) {
            if (adventurerState != AdventurerState.HANG && contactListener.isContactDetected()) {
                if (objectInHands == null) {
                    if (lastContact instanceof Body) {
                        if (((Body) lastContact).getUserData() instanceof Rope) {
                            Body ropeItem = (Body) lastContact;
                            objectInHands = ropeItem;
                            this.body.setTransform(new Vector2(ropeItem.getPosition().x, ropeItem.getPosition().y), ropeItem.getAngle());
                            setState(AdventurerState.HANG);
                            setDirection((adventurerDirection == AdventurerDirection.LEFT) ? AdventurerDirection.RIGHT : AdventurerDirection.LEFT);
                        }
                    } else if (lastContact instanceof Box) {
                        Box box = (Box) lastContact;
                        box.getBody().setActive(false);
                        int adventureDirectionCoefficient = adventurerDirection == AdventurerDirection.LEFT ? -1 : (adventurerDirection == AdventurerDirection.RIGHT ? 1 : 0);
                        box.getBody().setTransform(new Vector2(body.getPosition().x + adventureDirectionCoefficient * (size.width / 2.0f / PIXELS_PER_METER + box.getDimension().width / 2.0f / PIXELS_PER_METER), body.getPosition().y + box.getDimension().height / 2.0f / PIXELS_PER_METER), 0);
                        objectInHands = box;
                    }
                }
            }
        }

        if (!inputProcessor.keysStates.get(Input.Keys.SHIFT_LEFT) && inputProcessor.lastKeycode == Input.Keys.SHIFT_LEFT) {
            if (objectInHands != null && objectInHands instanceof Body) {
                body.setLinearVelocity(((Body) objectInHands).getLinearVelocity().x, ((Body) objectInHands).getLinearVelocity().y);
                bodyInert = true;
            }
            if (objectInHands != null && objectInHands instanceof Box) {
                ((Box) objectInHands).getBody().setLinearVelocity(0, 0);
                ((Box) objectInHands).getBody().setActive(true);
            }
            objectInHands = null;
            if (adventurerState == AdventurerState.HANG) {
                setState(AdventurerState.JUMP);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (adventurerState != AdventurerState.JUMP && adventurerState != AdventurerState.CROUCHING && adventurerState != AdventurerState.HANG) {
                setState(AdventurerState.RUN);
            }
            if (objectInHands != null && objectInHands instanceof Body && Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                Rope rope = (Rope)((Body) objectInHands).getUserData();
                rope.swing(new Vector2(-body.getMass() / 10.0f, 0));
            }

            setDirection(AdventurerDirection.LEFT);
            velocityX = -1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (adventurerState != AdventurerState.JUMP && adventurerState != AdventurerState.CROUCHING && adventurerState != AdventurerState.HANG) {
                setState(AdventurerState.RUN);
            }
            if (objectInHands != null && objectInHands instanceof Body && Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                Rope rope = (Rope)((Body) objectInHands).getUserData();
                rope.swing(new Vector2(body.getMass() / 10.0f, 0));
            }

            setDirection(AdventurerDirection.RIGHT);
            velocityX = 1;
        }

        body.setLinearVelocity(!bodyInert ? velocityX * speed : body.getLinearVelocity().x, body.getLinearVelocity().y);
        body.setTransform(new Vector2(Math.max(size.width / PIXELS_PER_METER / 2.0f, Math.min(body.getPosition().x, mapWidth / PIXELS_PER_METER - size.width / PIXELS_PER_METER / 2.0f)), body.getPosition().y), body.getAngle());
    }
}
