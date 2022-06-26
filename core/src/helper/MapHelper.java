package helper;

import com.adventurer.game.GameScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import levels.GameLevel;
import objects.elements.*;
import objects.player.Adventurer;

import java.awt.*;

import static helper.Constants.PIXELS_PER_METER;

public class MapHelper {

    private TiledMap tiledMap;
    private GameLevel level;

    public MapHelper(GameLevel level) {
        this.level = level;
    }

    public OrthogonalTiledMapRenderer setupMap(String pathname) {
        tiledMap = new TmxMapLoader().load("Maps/MapTest.tmx");
        parseMapObjects(tiledMap.getLayers().get("objects").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    public TiledMap getTiledMap() { return tiledMap; }

    private void parseMapObjects(MapObjects mapObjects) {

        for (MapObject mapObject : mapObjects) {
            if (mapObject instanceof PolygonMapObject) {
                createStaticBody((PolygonMapObject) mapObject);
            }
        }
    }

    private void createStaticBody(PolygonMapObject polygonMapObject) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        float minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        for (int i = 0; i < vertices.length; i += 2) {
            minX = Math.min(minX, vertices[i]);
            maxX = Math.max(maxX, vertices[i]);
        }
        for (int i = 1; i < vertices.length; i += 2) {
            minY = Math.min(minY, vertices[i]);
            maxY = Math.max(maxY, vertices[i]);
        }
        int width = (int)Math.abs(minX - maxX);
        int height = (int)Math.abs(minY - maxY);
        GameMapObject object;
        switch (polygonMapObject.getName()) {
            case "ground":
                object = new Ground(new Vector2(polygonMapObject.getPolygon().getX() / PIXELS_PER_METER, polygonMapObject.getPolygon().getY() / PIXELS_PER_METER - height / PIXELS_PER_METER), new Dimension(width, height));
                break;
            case "bomb":
                object = new Bomb(new Vector2(polygonMapObject.getPolygon().getX() / PIXELS_PER_METER, polygonMapObject.getPolygon().getY() / PIXELS_PER_METER - height / PIXELS_PER_METER), new Dimension(width, height));
                break;
            case "ladder":
                object = new Ladder(new Vector2(polygonMapObject.getPolygon().getX() / PIXELS_PER_METER, polygonMapObject.getPolygon().getY() / PIXELS_PER_METER - height / PIXELS_PER_METER), new Dimension(width, height));
                break;
            default:
                object = new Ground(new Vector2(polygonMapObject.getPolygon().getX() / PIXELS_PER_METER, polygonMapObject.getPolygon().getY() / PIXELS_PER_METER - height / PIXELS_PER_METER), new Dimension(width, height));
                break;
        }
        Body body = level.getWorld().createBody(bodyDef);
        Shape shape = createPolygonShape(polygonMapObject);
        level.appendMapObjects(object);
        body.setUserData(object);
        body.createFixture(shape, 10000f).setUserData(object);
        object.setBody(body);
        shape.dispose();
    }

    private Shape createPolygonShape(PolygonMapObject polygonMapObject) {
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; i++) {
            Vector2 current = new Vector2(vertices[i * 2] / PIXELS_PER_METER, vertices[i * 2 + 1] / PIXELS_PER_METER);
            worldVertices[i] = current;
        }

        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);
        return shape;
    }
}
