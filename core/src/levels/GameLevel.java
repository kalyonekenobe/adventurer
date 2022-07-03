package levels;

import com.adventurer.game.AdventurerGame;
import com.adventurer.game.GameResultsScreen;
import com.adventurer.game.GameScreen;
import com.adventurer.game.MenuScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import helper.BodyHelper;
import helper.MapHelper;
import objects.elements.*;
import objects.player.Adventurer;

import java.awt.*;

import static helper.Constants.PIXELS_PER_METER;

public abstract class GameLevel {

    protected String mapPathName;
    protected MapHelper mapHelper;
    protected TiledMap map;
    protected GameScreen gameScreen;
    protected Array<GameMapObject> mapObjects;
    protected OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    protected Adventurer adventurer;
    protected int coinsCollected;
    protected int totalCoins;

    public void render(SpriteBatch batch, float stateTime) {
        boolean adventurerOnGround = false;

        if (adventurer.getState() != Adventurer.AdventurerState.DEAD) {
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
                                adventurer.setBodyInert(false);
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
                        if (adventurer.getBody().getPosition().y - adventurer.getHeight() / 2.0f / PIXELS_PER_METER <= mapObject.getPosition().y + 4 / PIXELS_PER_METER && !(this instanceof Level2) && !(this instanceof Level5)) {
                            adventurer.setState(Adventurer.AdventurerState.STAY);
                        }
                        adventurer.setIsRunning(true);
                        adventurer.getBody().setTransform(new Vector2(mapObject.getPosition().x + mapObject.getDimension().width / 2.0f / PIXELS_PER_METER, adventurer.getY() / PIXELS_PER_METER), adventurer.getBody().getAngle());
                        adventurer.getBody().setLinearVelocity(adventurer.getBody().getLinearVelocity().x, velocityY * speed);
                    }
                }
            }

            if (adventurer.getState() == Adventurer.AdventurerState.CLIMB) {
                if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    if (!adventurer.isOnLadder()) {
                        Ladder.sound.loop(1.0f);
                        adventurer.setOnLadder(true);
                    }
                } else {
                    if (adventurer.isOnLadder()) {
                        Ladder.sound.stop();
                        adventurer.setOnLadder(false);
                    }
                }
            } else {
                if (adventurer.isOnLadder()) {
                    Ladder.sound.stop();
                    adventurer.setOnLadder(false);
                }
            }

            if (adventurer.objectInHands != null) {
                for (GameMapObject mapObject : mapObjects) {
                    if (mapObject instanceof Ground) {
                        if (adventurer.objectInHands instanceof Body) {
                            Polygon adventurerPolygon = new Polygon(BodyHelper.getBodyVertices(adventurer.getBody()));
                            Polygon mapObjectPolygon = new Polygon(BodyHelper.getBodyVertices(mapObject.getBody()));
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

            for (GameMapObject mapObject : mapObjects) {
                if (mapObject.getBody() != null && !(mapObject instanceof Ladder) && adventurer.getState() != Adventurer.AdventurerState.HANG && adventurer.getState() != Adventurer.AdventurerState.CLIMB && adventurer.getState() != Adventurer.AdventurerState.CROUCHING) {
                    Polygon objectPolygon = new Polygon(BodyHelper.getBodyVertices(mapObject.getBody()));
                    Vector2 position = mapObject.getBody().getType() == BodyDef.BodyType.DynamicBody ? mapObject.getBody().getPosition() : mapObject.getPosition();
                    objectPolygon.setOrigin(position.x * PIXELS_PER_METER, position.y * PIXELS_PER_METER);
                    objectPolygon.setRotation(mapObject.getBody().getAngle() / (float)Math.PI * 180);
                    Vector2 pointA = new Vector2(adventurer.getBody().getPosition().x * PIXELS_PER_METER - adventurer.getWidth() / 2.0f, adventurer.getBody().getPosition().y * PIXELS_PER_METER - adventurer.getHeight() / 2.0f - 1);
                    Vector2 pointB = new Vector2(adventurer.getBody().getPosition().x * PIXELS_PER_METER + adventurer.getWidth() / 2.0f, adventurer.getBody().getPosition().y * PIXELS_PER_METER - adventurer.getHeight() / 2.0f - 1);
                    if (Intersector.overlapConvexPolygons(new Polygon(new float[] { pointA.x, pointA.y, pointB.x, pointB.y, pointB.x, pointB.y, pointA.x, pointA.y }), objectPolygon)) {
                        adventurer.setState(Adventurer.AdventurerState.STAY);
                        if (adventurer.getBodyState() != Adventurer.BodyState.LANDED) {
                            adventurer.setBodyInert(false);
                            adventurer.setBodyState(Adventurer.BodyState.LANDED);
                            adventurer.setCanJumpState(true);
                            adventurer.setIsRunning(false);
                            Adventurer.fallSound.play(1.0f);
                        }
                        adventurerOnGround = true;
                    }
                }

                if (mapObject instanceof Coin) {
                    if (mapObject.getBody() != null) {
                        Polygon coinPolygon = new Polygon(BodyHelper.getBodyVertices(mapObject.getBody()));
                        Polygon adventurerPolygon = new Polygon(BodyHelper.getBodyVertices(adventurer.getBody()));
                        if (Intersector.overlapConvexPolygons(coinPolygon, adventurerPolygon)) {
                            mapObjects.removeValue(mapObject, false);
                            Coin.sound.play(1.0f);
                            this.getWorld().destroyBody(mapObject.getBody());
                            coinsCollected++;
                        }
                    }
                }

                if (mapObject instanceof Ground) {
                    if (adventurer.getState() != Adventurer.AdventurerState.CROUCHING) {
                        if (adventurer.objectInHands != null && adventurer.objectInHands instanceof Box && !adventurer.objectInHands.equals(mapObject)) {
                            Box box = (Box) adventurer.objectInHands;
                            Polygon boxPolygon = new Polygon(BodyHelper.getBodyVertices(box.getBody()));
                            Polygon groundPolygon = new Polygon(BodyHelper.getBodyVertices(mapObject.getBody()));
                            if (Intersector.overlapConvexPolygons(boxPolygon, groundPolygon)) {
                                Vector2 position = mapObject.getBody().getType() == BodyDef.BodyType.DynamicBody ? new Vector2(mapObject.getBody().getPosition().x - mapObject.getDimension().width / 2.0f / PIXELS_PER_METER, mapObject.getBody().getPosition().y - mapObject.getDimension().height / 2.0f / PIXELS_PER_METER) : mapObject.getPosition();
                                if (box.getBody().getPosition().x - box.getDimension().width / 2.0f / PIXELS_PER_METER < position.x && adventurer.getBody().getPosition().x < position.x) {
                                    adventurer.getBody().setTransform(new Vector2(Math.min(position.x - box.getDimension().width / PIXELS_PER_METER - adventurer.getWidth() / 2.0f / PIXELS_PER_METER, position.x), adventurer.getBody().getPosition().y), 0);
                                } else {
                                    if (adventurer.getBody().getPosition().x > position.x)
                                        adventurer.getBody().setTransform(new Vector2(Math.max(position.x + mapObject.getDimension().width / PIXELS_PER_METER + box.getDimension().width / PIXELS_PER_METER + adventurer.getWidth() / 2.0f / PIXELS_PER_METER, position.x), adventurer.getBody().getPosition().y), 0);
                                }
                            }
                        }
                    }
                }

                if (mapObject instanceof Finish) {
                    Polygon finishPolygon = new Polygon(BodyHelper.getBodyVertices(mapObject.getBody()));
                    Polygon adventurerPolygon = new Polygon(BodyHelper.getBodyVertices(adventurer.getBody()));
                    if (Intersector.overlapConvexPolygons(adventurerPolygon, finishPolygon)) {
                        Adventurer.runSound.stop();
                        Adventurer.jumpSound.stop();
                        Adventurer.crouchingSound.stop();
                        Adventurer.fallSound.stop();
                        this.gameScreen.game.setScreen(new GameResultsScreen(this.gameScreen.game, this, GameResultsScreen.GameResults.VICTORY));
                        this.gameScreen.dispose();
                    }
                }

                if (mapObject instanceof Bomb) {
                    Polygon adventurerPolygon = new Polygon(BodyHelper.getBodyVertices(adventurer.getBody()));
                    Polygon bombPolygon = new Polygon(BodyHelper.getBodyVertices(mapObject.getBody()));
                    if (Intersector.overlapConvexPolygons(adventurerPolygon, bombPolygon)) {
                        Bomb bomb = (Bomb) mapObject;
                        int bodyDirectionCoefficient = (adventurer.getBody().getPosition().x <= bomb.getPosition().x + bomb.getDimension().width / 2.0f / PIXELS_PER_METER) ? -1 : 1;
                        adventurer.getBody().setLinearVelocity(new Vector2(0, 0));
                        adventurer.getBody().applyLinearImpulse(new Vector2(bodyDirectionCoefficient * adventurer.getBody().getMass() * 5, adventurer.getBody().getMass() * 15), adventurer.getBody().getPosition(), true);
                        adventurer.setBodyInert(true);
                        adventurer.setHealthPoints(adventurer.getHealthPoints() - 1);
                        Adventurer.damageSound.play(1.0f);
                        if (adventurer.getState() == Adventurer.AdventurerState.CROUCHING) {
                            adventurer.setState(Adventurer.AdventurerState.STAY);
                            adventurer.setSize(new Dimension(adventurer.getWidth(), adventurer.getHeight() * 2));
                            adventurer.getBody().setTransform(new Vector2(adventurer.getBody().getPosition().x, adventurer.getBody().getPosition().y + adventurer.getHeight() / 4.0f / PIXELS_PER_METER), 0);
                            BodyHelper.resizeBody(adventurer.getBody(), new Dimension(adventurer.getWidth(), adventurer.getHeight()));
                            adventurer.setSpeed(adventurer.getSpeed() / 0.5f);
                        }
                    }
                }
            }

            if (!adventurerOnGround) {
                if (adventurer.getBodyState() != Adventurer.BodyState.FLYING) {
                    adventurer.setBodyState(Adventurer.BodyState.FLYING);
                    adventurer.setCanJumpState(false);
                    if (adventurer.isRunning())
                        Adventurer.runSound.stop();
                }
            }
        }

        for (GameMapObject object : mapObjects) {
            object.render(batch, stateTime);
        }

        adventurer.update();
        adventurer.render(batch, stateTime);
    }

    public void renderAdventurerHealth(SpriteBatch batch, float stateTime) {
        for (int i = 0; i < adventurer.getHealthPoints(); i++) {
            batch.draw(Adventurer.redHeart, gameScreen.getCamera().position.x - Gdx.graphics.getWidth() / 2.0f + 10 + 56 * i, gameScreen.getCamera().position.y + Gdx.graphics.getHeight() / 2.0f - 53, 46, 43);
        }
        for (int i = 0; i < adventurer.getTotalHealthPoints() - adventurer.getHealthPoints(); i++) {
            batch.draw(Adventurer.grayHeart, gameScreen.getCamera().position.x - Gdx.graphics.getWidth() / 2.0f + 10 + 56 * (getAdventurer().getHealthPoints() + i), gameScreen.getCamera().position.y + Gdx.graphics.getHeight() / 2.0f - 53, 46, 43);
        }
        for (int i = 0; i < coinsCollected; i++) {
            batch.draw(Adventurer.coinTexture, gameScreen.getCamera().position.x - Gdx.graphics.getWidth() / 2.0f + 50 + 56 * getAdventurer().getTotalHealthPoints() + 43 * i, gameScreen.getCamera().position.y + Gdx.graphics.getHeight() / 2.0f - 53, 43, 43);
        }
    }

    public Adventurer getAdventurer() { return adventurer; }

    public OrthogonalTiledMapRenderer getOrthogonalTiledMapRenderer() { return orthogonalTiledMapRenderer; }

    public TiledMap getMap() { return map; }

    public World getWorld() { return gameScreen.getWorld(); }

    public void appendMapObjects(GameMapObject object) { mapObjects.add(object); }

    public Array<GameMapObject> getMapObjects() { return mapObjects; }

    public AdventurerGame getGame() { return gameScreen.game; }

    public GameScreen getScreen() { return gameScreen; }

    public int getCoinsCollected() { return coinsCollected; }

    public int getTotalCoins() { return totalCoins; }
}
