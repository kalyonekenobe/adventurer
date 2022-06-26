package levels;

import com.adventurer.game.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import helper.BodyHelper;
import helper.MapHelper;
import objects.elements.GameMapObject;
import objects.elements.Ground;
import objects.elements.Ladder;
import objects.elements.Rope;
import objects.player.Adventurer;

import static helper.Constants.PIXELS_PER_METER;

public abstract class GameLevel {

    protected String mapPathName;
    protected MapHelper mapHelper;
    protected TiledMap map;
    protected GameScreen gameScreen;
    protected Array<GameMapObject> mapObjects;
    protected OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    protected Adventurer adventurer;

    public void render(SpriteBatch batch, float stateTime) {
        for (GameMapObject mapObject : mapObjects) {
            if (mapObject instanceof Ladder && adventurer.getState() != Adventurer.AdventurerState.HANG) {
                Polygon adventurerPolygon = new Polygon(BodyHelper.getBodyVertices(adventurer.getBody()));
                Polygon ladderPolygon = new Polygon(BodyHelper.getBodyVertices(mapObject.getBody()));
                int velocityY = 0;
                float speed = 4f;
                if (Intersector.overlapConvexPolygons(adventurerPolygon, ladderPolygon)) {
                    if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                        if (adventurer.getState() != Adventurer.AdventurerState.CLIMB && adventurer.getState() != Adventurer.AdventurerState.CROUCHING) {
                            adventurer.setState(Adventurer.AdventurerState.CLIMB);
                            adventurer.setCanJumpState(false);
                        }
                        velocityY = (Gdx.input.isKeyPressed(Input.Keys.UP)) ? 1 : -1;
                    }
                } else {
                    if (adventurer.getState() == Adventurer.AdventurerState.CLIMB) {
                        adventurer.setState(Adventurer.AdventurerState.STAY);
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                    if (ladderPolygon.contains(adventurer.getBody().getPosition().x * PIXELS_PER_METER, adventurer.getBody().getPosition().y * PIXELS_PER_METER - adventurer.getHeight() / 2.0f - 2)) {
                        if (adventurer.getState() != Adventurer.AdventurerState.CLIMB && adventurer.getState() != Adventurer.AdventurerState.CROUCHING) {
                            adventurer.setState(Adventurer.AdventurerState.CLIMB);
                            adventurer.getBody().setTransform(new Vector2(mapObject.getPosition().x + mapObject.getDimension().width / 2.0f / PIXELS_PER_METER, adventurer.getY() / PIXELS_PER_METER - 2 / PIXELS_PER_METER), adventurer.getBody().getAngle());
                            adventurer.setCanJumpState(false);
                        }
                    }
                }
                if (adventurer.getState() == Adventurer.AdventurerState.CLIMB) {
                    if (adventurer.getBody().getPosition().y - adventurer.getHeight() / 2.0f / PIXELS_PER_METER <= mapObject.getPosition().y + 4 / PIXELS_PER_METER) {
                        adventurer.setState(Adventurer.AdventurerState.STAY);
                    }
                    adventurer.getBody().setTransform(new Vector2(mapObject.getPosition().x + mapObject.getDimension().width / 2.0f / PIXELS_PER_METER, adventurer.getY() / PIXELS_PER_METER), adventurer.getBody().getAngle());
                    adventurer.getBody().setLinearVelocity(adventurer.getBody().getLinearVelocity().x, velocityY * speed);
                }
            }
        }

        if (adventurer.objectInHands != null) {
            for (GameMapObject mapObject : mapObjects) {
                if (mapObject instanceof Ground) {
                    Polygon adventurerPolygon = new Polygon(BodyHelper.getBodyVertices(adventurer.getBody()));
                    Polygon mapObjectPolygon = new Polygon(BodyHelper.getBodyVertices(mapObject.getBody()));
                    if (adventurer.objectInHands instanceof Body) {
                        Body objectInHandsBody = (Body) adventurer.objectInHands;
                        if (Intersector.overlapConvexPolygons(adventurerPolygon, mapObjectPolygon)) {
                            int adventurerDirectionCoefficient = 0;
                            if (adventurer.getBody().getPosition().y - adventurer.getHeight() / 2.0f / PIXELS_PER_METER > mapObject.getPosition().y + mapObject.getDimension().height / PIXELS_PER_METER || adventurer.getBody().getPosition().y + adventurer.getHeight() / 2.0f / PIXELS_PER_METER < mapObject.getPosition().y) {
                                if (adventurer.getBody().getPosition().y > mapObject.getPosition().y) {
                                    objectInHandsBody.setTransform(new Vector2(objectInHandsBody.getPosition().x, Math.max(objectInHandsBody.getPosition().y, mapObject.getPosition().y + mapObject.getDimension().height / PIXELS_PER_METER + adventurer.getHeight() / PIXELS_PER_METER + ((Rope) objectInHandsBody.getUserData()).getPartSize().height / 2.0f / PIXELS_PER_METER)), objectInHandsBody.getAngle());
                                } else {
                                    objectInHandsBody.setTransform(new Vector2(objectInHandsBody.getPosition().x, Math.min(objectInHandsBody.getPosition().y, mapObject.getPosition().y - adventurer.getHeight() / PIXELS_PER_METER + ((Rope) objectInHandsBody.getUserData()).getPartSize().height / 2.0f / PIXELS_PER_METER)), objectInHandsBody.getAngle());
                                }
                            } else {
                                if (adventurer.getBody().getPosition().x > mapObject.getPosition().x + mapObject.getDimension().width / 2.0f / PIXELS_PER_METER) {
                                    adventurerDirectionCoefficient = adventurer.getDirection() == Adventurer.AdventurerDirection.LEFT ? 1 : 0;
                                    objectInHandsBody.setTransform(new Vector2(Math.max(mapObject.getPosition().x + mapObject.getDimension().width / PIXELS_PER_METER + adventurerDirectionCoefficient * adventurer.getWidth() / PIXELS_PER_METER, objectInHandsBody.getPosition().x), objectInHandsBody.getPosition().y), objectInHandsBody.getAngle());
                                } else {
                                    adventurerDirectionCoefficient = adventurer.getDirection() == Adventurer.AdventurerDirection.LEFT ? 0 : 1;
                                    objectInHandsBody.setTransform(new Vector2(Math.min(mapObject.getPosition().x - adventurerDirectionCoefficient * adventurer.getWidth() / PIXELS_PER_METER, objectInHandsBody.getPosition().x), objectInHandsBody.getPosition().y), objectInHandsBody.getAngle());
                                }
                            }
                        }
                    }
                }
            }
        }

        if (adventurer.getState() != Adventurer.AdventurerState.CLIMB && !adventurer.inputProcessor.keysStates.get(Input.Keys.UP) && !adventurer.getCanJumpState()) {
            adventurer.setCanJumpState(true);
        }

        for (GameMapObject object : mapObjects) {
            object.render(batch);
        }
        adventurer.update();
        adventurer.render(batch, stateTime);
    }

    public Adventurer getAdventurer() { return adventurer; }

    public OrthogonalTiledMapRenderer getOrthogonalTiledMapRenderer() { return orthogonalTiledMapRenderer; }

    public TiledMap getMap() { return map; }

    public World getWorld() { return gameScreen.getWorld(); }

    public void appendMapObjects(GameMapObject object) { mapObjects.add(object); }

    public Array<GameMapObject> getMapObjects() { return mapObjects; }
}
