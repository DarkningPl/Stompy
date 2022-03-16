package com.qb.stompy.scenes;

import com.qb.stompy.dataReaders.LevelReader;
import com.qb.stompy.dataReaders.World1Reader;
import com.qb.stompy.dataReaders.World2Reader;
import com.qb.stompy.living.MapCharacter;
import com.qb.stompy.objects.*;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.Scene;
import com.rubynaxela.kyanite.game.assets.AssetsBundle;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.assets.TextureAtlas;
import com.rubynaxela.kyanite.game.gui.Font;
import com.rubynaxela.kyanite.game.gui.Text;
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
    private final Text worldNumberText = new Text(new Font(getContext().getAssetsBundle().get("font_mc"), 24));
    private final Text worldNameText = new Text(new Font(getContext().getAssetsBundle().get("font_mc"), 24));
    private final MapCharacter character = new MapCharacter();
    private final ArrayList<MapPoint> points = new ArrayList<>();
    private final ArrayList<MapPoint> levelPoints = new ArrayList<>();
    private final ArrayList<MapPath> paths = new ArrayList<>();
    private final ArrayList<GameText> levelNumbers = new ArrayList<>();
    private boolean cd = false;

    public LoadedWorldScene(int world) {
        worldNumber = world;
        AssetsBundle assets = GameContext.getInstance().getAssetsBundle();
        float size_x, size_y;
        switch (world) {
            case 0 -> {
                size_x = assets.<DataAsset>get("world_0").convertTo(World1Reader.class).getSize().x;
                size_y = assets.<DataAsset>get("world_0").convertTo(World1Reader.class).getSize().y;
            }
            case 1 -> {
                size_x = assets.<DataAsset>get("world_1").convertTo(World2Reader.class).getSize().x;
                size_y = assets.<DataAsset>get("world_1").convertTo(World2Reader.class).getSize().y;
            }
            default -> {
                size_x = 0;
                size_y = 0;
            }
        }
        mapSize = new Vector2f(size_x, size_y);
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
                throw new IllegalArgumentException("Unable to make this path!");
            }
        }
    }

    public void placeCharacterAtEnd() {
        character.setPositionOnMap(getLevelPoints().get(getLevelPoints().size() - 1).getPositionOnMap());
    }
    public void placeCharacterAtLevel(int level) {
        character.setPositionOnMap(getLevelPoints().get(level + 1).getPositionOnMap());
    }

    public void loadWorld0() {
        World1Reader world = GameContext.getInstance().getAssetsBundle().<DataAsset>get("world_0").convertTo(World1Reader.class);

        worldNumberText.setText("World " + 1);
        worldNameText.setText(world.getWorldName());

        //Background
        MapBackground back = new MapBackground(Vec2.f(world.getBackgroundTextureSize().x, world.getBackgroundTextureSize().y));
        GameContext.getInstance().getAssetsBundle().<Texture>get("texture_" + world.getTextureName()).apply(back);

        //Character
        character.setPositionOnMap(world.getStartPoint().x, world.getStartPoint().y);

        //Start point
        MapPoint start = new MapPoint(2);
        start.setPositionOnMap(world.getStartPoint().x, world.getStartPoint().y);
        levelPoints.add(start);

        //Levels
        for (int i = 0; i < world.getLevels().size(); i++) {
            World1Reader.W1Level lvl = world.getLevels().get(i);
            MapPoint crossroad = new MapPoint(1);
            MapPoint level = new MapPoint((float)Math.sqrt(2));
            GameText levelNumber = new GameText((getWorldNumber() + 1) + " - " + (i + 1), new Font(getContext().getAssetsBundle().get("font_mc"), 24));
            levelNumber.setAlignment(Text.Alignment.CENTER);
            crossroad.setPositionOnMap(lvl.getPosition().x, lvl.getPosition().y);
            level.setPositionOnMap(lvl.getPosition().x, lvl.getPosition().y - 60);
            levelNumber.setPositionOnMap(lvl.getPosition().x, lvl.getPosition().y - 80);
            points.add(crossroad);
            levelPoints.add(level);
            levelNumbers.add(levelNumber);
            if (i == 0) makePath(start.getPositionOnMap(), crossroad.getPositionOnMap(), i);
            else makePath(Vec2.f(world.getLevels().get(i - 1).getPosition().x, world.getLevels().get(i - 1).getPosition().y), crossroad.getPositionOnMap(), i);
            makePath(crossroad.getPositionOnMap(), level.getPositionOnMap(), i);
        }

        //End point
        MapPoint end = new MapPoint(2);
        end.setPositionOnMap(world.getEndPoint().x, world.getEndPoint().y);
        levelPoints.add(end);
        if (world.getLevels().size() > 0) makePath(Vec2.f(world.getLevels().get(world.getLevels().size() - 1).getPosition().x, world.getLevels().get(world.getLevels().size() - 1).getPosition().y), end.getPositionOnMap(), world.getLevels().size());
        else makePath(start.getPositionOnMap(), end.getPositionOnMap(), 0);

        add(back);
    }

    public void loadWorld1() {

    }

    public void loadWorld2() {

    }

     public void loadWorld3() {

     }

    @Override
    protected void init() {
        switch (worldNumber) {
            case 0 -> loadWorld0();
            case 1 -> loadWorld1();
            case 2 -> loadWorld2();
            case 3 -> loadWorld3();
        }
        for (MapObject path : paths) {
            add(path);
        }
        for (MapPoint point : points) {
            add(point);
        }
        for (MapPoint point : levelPoints) {
            add(point);
        }
        for (GameText number : levelNumbers) {
            add(number);
        }
        worldNumberText.setAlignment(Text.Alignment.CENTER);
        worldNumberText.setPosition(GameContext.getInstance().getWindow().getSize().x / 2f, 40);
        worldNameText.setAlignment(Text.Alignment.CENTER);
        worldNameText.setPosition(GameContext.getInstance().getWindow().getSize().x / 2f, 80);
        add(worldNameText);
        add(worldNumberText);
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
            if (obj instanceof final GameText text) {
                text.setPosition(Vec2.subtract(text.getPositionOnMap(), mapOffset));
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
