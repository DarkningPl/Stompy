package com.qb.stompy.loaders;

import com.qb.stompy.living.MapCharacter;
import com.qb.stompy.objects.*;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.Scene;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.assets.TextureAtlas;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.Window;
import org.jsfml.graphics.Drawable;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;

import java.util.ArrayList;

public class LoadedWorldScene extends Scene {

    private final int worldNumber;
    private final Vector2f mapSize;
    private Vector2f mapOffset = Vector2f.ZERO;
    private final LevelReader.LRWorld worldData;
    private final MapCharacter character = new MapCharacter();
    private final ArrayList<MapPoint> points = new ArrayList<>();
    private final ArrayList<MapPoint> levelPoints = new ArrayList<>();
    private final ArrayList<MapPath> paths = new ArrayList<>();
    private final ArrayList<MapObject> icons = new ArrayList<>();
    private boolean cd = false;

    public LoadedWorldScene(int world) {
        worldNumber = world;
        worldData = GameContext.getInstance().getAssetsBundle().<DataAsset>get("levels").convertTo(LevelReader.class).getWorlds().get(worldNumber);
        mapSize = new Vector2f(worldData.getSize().x, worldData.getSize().y);
    }

    public int getWorldNumber() {
        return worldNumber;
    }

    public Vector2f getMapSize() {
        return mapSize;
    }

    public Vector2f getMapOffset() {
        return mapOffset;
    }

    public MapCharacter getPlayerCharacter() {
        return character;
    }

    public ArrayList<MapPoint> getLevelPoints() {
        return levelPoints;
    }

    public ArrayList<MapPath> getPaths() {
        return paths;
    }

    private void makePath(Vector2f start, Vector2f end, int pathNumber) {
        TextureAtlas atlas = GameContext.getInstance().getAssetsBundle().get("texture_map_path");
        Texture[] textures = { atlas.get(0, 0, 8, 8), atlas.get(8, 0, 16, 8) };
        if (start.x < end.x) {
            if (start.y < end.y) {
                //down then right
                MapPoint point = new MapPoint(1);
                point.setPositionOnMap(start.x, end.y);
                points.add(point);
                makePath(start, point.getPositionOnMap(), pathNumber);
                makePath(point.getPositionOnMap(), end, pathNumber);
            } else if (start.y > end.y) {
                //right then up
                MapPoint point = new MapPoint(1);
                point.setPositionOnMap(end.x, start.y);
                points.add(point);
                makePath(start, point.getPositionOnMap(), pathNumber);
                makePath(point.getPositionOnMap(), end, pathNumber);
            } else {
                MapPath path = new MapPath(getWorldNumber(), pathNumber);
                textures[0].apply(path);
                path.setSize(Vec2.f(end.x - start.x, textures[0].getSize().y));
                path.setOrigin(0, path.getSize().y / 2);
                path.setPositionOnMap(start);
                paths.add(path);
            }
        } else if (start.x > end.x) {
            if (start.y < end.y) {
                //down then left
                MapPoint point = new MapPoint(1);
                point.setPositionOnMap(start.x, end.y);
                points.add(point);
                makePath(start, point.getPositionOnMap(), pathNumber);
                makePath(point.getPositionOnMap(), end, pathNumber);
            } else if (start.y > end.y) {
                //left then up
                MapPoint point = new MapPoint(1);
                point.setPositionOnMap(end.x, start.y);
                points.add(point);
                makePath(start, point.getPositionOnMap(), pathNumber);
                makePath(point.getPositionOnMap(), end, pathNumber);
            } else {
                MapPath path = new MapPath(getWorldNumber(), pathNumber);
                textures[0].apply(path);
                path.setSize(Vec2.f(start.x - end.x, textures[0].getSize().y));
                path.setOrigin(0, path.getSize().y / 2);
                path.setPositionOnMap(end);
                paths.add(path);
            }
        } else {
            if (start.y > end.y) {
                MapPath path = new MapPath(getWorldNumber(), pathNumber);
                textures[1].apply(path);
                path.setSize(Vec2.f(textures[1].getSize().x, start.y - end.y));
                path.setOrigin(path.getSize().x / 2, 0);
                path.setPositionOnMap(end);
                paths.add(path);
            } else if (start.y < end.y) {
                MapPath path = new MapPath(getWorldNumber(), pathNumber);
                textures[1].apply(path);
                path.setSize(Vec2.f(textures[1].getSize().x, end.y - start.y));
                path.setOrigin(path.getSize().x / 2, 0);
                path.setPositionOnMap(start);
                paths.add(path);
            } else {
                //throw
                //catch "Unable to make this path!"
            }
        }
    }

    public void placeCharacterAtEnd() {
        character.setPositionOnMap(getLevelPoints().get(getLevelPoints().size() - 1).getPositionOnMap());
    }
    public void placeCharacterAtLevel(int level) {
        character.setPositionOnMap(getLevelPoints().get(level + 1).getPositionOnMap());
    }

    @Override
    protected void init() {
        //Background
        setBackgroundTexture(GameContext.getInstance().getAssetsBundle().get("texture_" + worldData.getTextureName()));
        MapBackground back = new MapBackground(Vec2.f(worldData.getBackgroundTextureSize().x, worldData.getBackgroundTextureSize().y));
        GameContext.getInstance().getAssetsBundle().<Texture>get("texture_" + worldData.getTextureName()).apply(back);

        //Character
        character.setPositionOnMap(worldData.getStartPoint().x, worldData.getStartPoint().y);

        //Start point
        MapPoint start = new MapPoint(2);
        start.setPositionOnMap(worldData.getStartPoint().x, worldData.getStartPoint().y);
        levelPoints.add(start);

        //Levels
        for (int i = 0; i < worldData.getLevels().size(); i++) {
            LevelReader.LRLevel lvl = worldData.getLevels().get(i);
            MapPoint crossroad = new MapPoint(1);
            MapPoint level = new MapPoint((float)Math.sqrt(2));
            MapObject icon = new MapObject();
            Texture texture = GameContext.getInstance().getAssetsBundle().get("texture_" + lvl.getIconTextureName());
            texture.apply(icon);
            icon.setOrigin(texture.getSize().x / 2f, texture.getSize().y);
            crossroad.setPositionOnMap(lvl.getPosition().x, lvl.getPosition().y);
            level.setPositionOnMap(lvl.getPosition().x, lvl.getPosition().y - 60);
            icon.setPositionOnMap(lvl.getPosition().x, lvl.getPosition().y - 80);
            points.add(crossroad);
            levelPoints.add(level);
            icons.add(icon);
            if (i == 0) makePath(start.getPositionOnMap(), crossroad.getPositionOnMap(), i);
            else makePath(Vec2.f(worldData.getLevels().get(i - 1).getPosition().x, worldData.getLevels().get(i - 1).getPosition().y), crossroad.getPositionOnMap(), i);
            makePath(crossroad.getPositionOnMap(), level.getPositionOnMap(), i);
        }

        //End point
        MapPoint end = new MapPoint(2);
        end.setPositionOnMap(worldData.getEndPoint().x, worldData.getEndPoint().y);
        levelPoints.add(end);
        if (worldData.getLevels().size() > 0) makePath(Vec2.f(worldData.getLevels().get(worldData.getLevels().size() - 1).getPosition().x, worldData.getLevels().get(worldData.getLevels().size() - 1).getPosition().y), end.getPositionOnMap(), worldData.getLevels().size());
        else makePath(start.getPositionOnMap(), end.getPositionOnMap(), 0);

        add(back);
        for (MapObject path : paths) {
            add(path);
        }
        for (MapPoint point : points) {
            add(point);
        }
        for (MapPoint point : levelPoints) {
            add(point);
        }
        for (MapObject icon : icons) {
            add(icon);
        }
        add(character);
    }

    @Override
    protected void loop() {
        float offsetX = 0, offsetY = 0;
        Window window = GameContext.getInstance().getWindow();

        if (character.getPositionOnMap().x > getContext().getWindow().getSize().x / 2.0f) {
            if (character.getPositionOnMap().x < getMapSize().x - getContext().getWindow().getSize().x / 2.0f)
                offsetX = character.getPositionOnMap().x - getContext().getWindow().getSize().x / 2.0f;
            else
                offsetX = getMapSize().x - getContext().getWindow().getSize().x;
        }
        if (character.getPositionOnMap().y > getContext().getWindow().getSize().y / 2.0f) {
            if (character.getPositionOnMap().y < getMapSize().y - getContext().getWindow().getSize().y / 2.0f)
                offsetY = character.getPositionOnMap().y - getContext().getWindow().getSize().y / 2.0f;
            else
                offsetY = getMapSize().y - getContext().getWindow().getSize().y;
        }
        mapOffset = Vec2.f(offsetX, offsetY);

        for (final Drawable obj : getContext().getWindow().getScene()) {
            if (obj instanceof final GameObject gameObject) {
                gameObject.setPosition(Vec2.subtract(gameObject.getPositionOnMap(), mapOffset));
            }
            if (obj instanceof final MapBackground back) {
                float backgroundOffsetX, backgroundOffsetY;
                if (getMapSize().x > window.getSize().x) {
                    backgroundOffsetX = (back.getSize().x - window.getSize().x) / (getMapSize().x - window.getSize().x);
                } else backgroundOffsetX = (back.getSize().x - window.getSize().x) / getMapSize().x;
                if (getMapSize().y > window.getSize().y) {
                    backgroundOffsetY = (back.getSize().y - window.getSize().y) / (getMapSize().y - window.getSize().y);
                } else backgroundOffsetY = (back.getSize().y - window.getSize().y) / getMapSize().y;
                back.setPosition(Vec2.multiply(Vec2.subtract(back.getPositionOnMap(), Vec2.f(backgroundOffsetX, backgroundOffsetY)),getMapOffset()));
            }
            if (obj instanceof final MapPath mapPath) {
                if (Mouse.isButtonPressed(Mouse.Button.LEFT)) {
                    if (mapPath.getGlobalBounds().contains(Vec2.f(Mouse.getPosition(window).x, Mouse.getPosition(window).y))) {
                        mapPath.printMsg();
                    } else mapPath.enableMsg();
                }
            }
        }

        if (Keyboard.isKeyPressed(Keyboard.Key.P)) {
            if (!cd) {
                cd=true;
                System.out.println("Map offset:");
                System.out.println("X: " + mapOffset.x + ", " + mapOffset.y);
                for (final Drawable obj : getContext().getWindow().getScene()) {
                    if (obj instanceof final MapCharacter g) {
                        System.out.println("Character position:");
                        System.out.println("X: " + g.getPositionOnMap().x + ", Y: " + g.getPositionOnMap().y);
                    }
                    else if (obj instanceof final MapPoint g) {
                        System.out.println("Point bounds:");
                        System.out.println("Left: " + g.gGB().left + ", right: " + (g.gGB().left + g.gGB().width));
                        System.out.println("Top: " + g.gGB().top + ", bottom: " + (g.gGB().top + g.gGB().height));
                    }
                    else if (obj instanceof final MapObject g) {
                        System.out.println("Bounds:");
                        System.out.println("Left: " + g.gGB().left + ", right: " + (g.gGB().left + g.gGB().width));
                        System.out.println("Top: " + g.gGB().top + ", bottom: " + (g.gGB().top + g.gGB().height));
                    }
                }
            }
        } else cd = false;
    }
}
