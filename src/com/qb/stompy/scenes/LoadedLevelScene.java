package com.qb.stompy.scenes;

import com.qb.stompy.dataReaders.LevelReader;
import com.qb.stompy.dataReaders.World1Reader;
import com.qb.stompy.dataReaders.World2Reader;
import com.qb.stompy.dataReaders.WorldReader;
import com.qb.stompy.living.*;
import com.qb.stompy.living.Character;
import com.qb.stompy.objects.GameObject;
import com.qb.stompy.objects.MapBackground;
import com.qb.stompy.objects.SolidBlock;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.Scene;
import com.rubynaxela.kyanite.game.assets.AssetsBundle;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.Window;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.system.Vector2f;

public class LoadedLevelScene extends Scene {
    private final int levelNumber, worldNumber;
    private boolean paused = false;
    private final Vector2f mapSize;
    private Vector2f mapOffset = Vec2.f(0, 0);
    private final Character character = new Character();

    public LoadedLevelScene(int world, int level) {
        levelNumber = level;
        worldNumber = world;
        AssetsBundle assets = GameContext.getInstance().getAssetsBundle();
        float size_x, size_y;
        switch (world) {
            case 0 -> {
                size_x = assets.<DataAsset>get("world_0").convertTo(World1Reader.class).getLevels().get(level).getSize().x;
                size_y = assets.<DataAsset>get("world_0").convertTo(World1Reader.class).getLevels().get(level).getSize().y;
            }
            case 1 -> {
                size_x = assets.<DataAsset>get("world_1").convertTo(World2Reader.class).getLevels().get(level).getSize().x;
                size_y = assets.<DataAsset>get("world_1").convertTo(World2Reader.class).getLevels().get(level).getSize().y;
            }
            default -> {
                size_x = 0;
                size_y = 0;
            }
        }
        mapSize = Vec2.f(size_x, size_y);
    }

    public Vector2f getMapSize() {
        return mapSize;
    }

    public Vector2f getMapOffset() {
        return mapOffset;
    }

    public Character getPlayerCharacter() {
        return character;
    }

    public int getLevelNumber() { return levelNumber; }

    public int getWorldNumber() { return worldNumber; }

    public boolean isPaused() { return paused; }

    public void pause() { paused = true; }

    public void unpause() { paused = false; }

    private void loadLevelFromWorld0() {
        World1Reader.W1Level level = GameContext.getInstance().getAssetsBundle().<DataAsset>get("world_0").convertTo(World1Reader.class).getLevels().get(levelNumber);

        //Background
        MapBackground background = new MapBackground(Vec2.f(level.getBackgroundTextureSize().x, level.getBackgroundTextureSize().y));
        GameContext.getInstance().getAssetsBundle().<Texture>get("texture_" + level.getTextureName()).apply(background);
        add(background);

        //Character
        character.setPositionOnMap(level.getCharacterPosition().x, level.getCharacterPosition().y);
        add(character);

        //Solid Blocks
        for (int i = 0; i < level.getBlocks().size(); i++) {
            World1Reader.W1Block blockData = level.getBlocks().get(i);
            SolidBlock block = new SolidBlock(Vec2.f(blockData.getSize().x, blockData.getSize().y));
            block.setPositionOnMap(blockData.getPosition().x, blockData.getPosition().y);
            GameContext.getInstance().getAssetsBundle().<Texture>get("texture_" + blockData.getTextureName()).apply(block);
            add(block);
        }

        //Candies
        for (int i = 0; i < level.getCandies().size(); i++) {
            World1Reader.W1Candy candyData = level.getCandies().get(i);
            Candy candy = new Candy(new Color(candyData.getColor().r, candyData.getColor().g, candyData.getColor().b));
            candy.setPositionOnMap(candyData.getPosition().x, candyData.getPosition().y);
            add(candy);
        }

        //Chocolates
        for (int i = 0; i < level.getChocolates().size(); i++) {
            World1Reader.W1Chocolate chocolateData = level.getChocolates().get(i);
            Chocolate chocolate = new Chocolate(chocolateData.getHp());
            chocolate.setPositionOnMap(chocolateData.getPosition().x, chocolateData.getPosition().y);
            add(chocolate);
        }

        //Cupcakes
        for (int i = 0; i < level.getCupcakes().size(); i++) {
            World1Reader.W1Cupcake cupcakeData = level.getCupcakes().get(i);
            Cupcake cupcake = new Cupcake();
            cupcake.setPositionOnMap(cupcakeData.getPosition().x, cupcakeData.getPosition().y);
            add(cupcake);
        }

        //Pizzas
        for (int i = 0; i < level.getPizzas().size(); i++) {
            World1Reader.W1Pizza pizzaData = level.getPizzas().get(i);
            Pizza pizza = new Pizza();
            pizza.setPositionOnMap(pizzaData.getPosition().x, pizzaData.getPosition().y);
            add(pizza);
        }
    }

    private void loadLevelFromWorld1() {

    }

    private void loadLevelFromWorld2() {

    }

    private void loadLevelFromWorld3() {

    }

    @Override
    protected void init() {

        switch (worldNumber) {
            case 0 -> loadLevelFromWorld0();
            case 1 -> loadLevelFromWorld1();
            case 2 -> loadLevelFromWorld2();
            case 3 -> loadLevelFromWorld3();
        }

        bringToTop(character);
    }

    @Override
    protected void loop() {
        float offsetX = 0, offsetY = 0;
        Window window = GameContext.getInstance().getWindow();
        if (!paused) {
            if (getContext().getWindow().getSize().x < getMapSize().x) {
                if (character.getPositionOnMap().x > getContext().getWindow().getSize().x / 2.0f) {
                    if (character.getPositionOnMap().x < getMapSize().x - getContext().getWindow().getSize().x / 2.0f)
                        offsetX = character.getPositionOnMap().x - getContext().getWindow().getSize().x / 2.0f;
                    else
                        offsetX = getMapSize().x - getContext().getWindow().getSize().x;
                }
            }
            if (getContext().getWindow().getSize().y < getMapSize().y) {
                if (character.getPositionOnMap().y > getContext().getWindow().getSize().y / 2.0f) {
                    if (character.getPositionOnMap().y < getMapSize().y - getContext().getWindow().getSize().y / 2.0f)
                        offsetY = character.getPositionOnMap().y - getContext().getWindow().getSize().y / 2.0f;
                    else
                        offsetY = getMapSize().y - getContext().getWindow().getSize().x;
                }
            }
            mapOffset = Vec2.f(offsetX, offsetY);

            for (final Drawable obj : getContext().getWindow().getScene()) {
                if (obj instanceof final GameObject gameObject) {
                    gameObject.setPosition(Vec2.subtract(gameObject.getPositionOnMap(), mapOffset));
                    if (gameObject.gGB().top > getMapSize().y || gameObject.gGB().left > getMapSize().x || gameObject.gGB().left + gameObject.gGB().width < 0 - mapOffset.x) {
                        scheduleToRemove(gameObject);
                    }
                    if (gameObject instanceof final LivingGameObject livingGameObject) {
                        if (livingGameObject.isDead()) {
                            scheduleToRemove(livingGameObject);
                        }
                    }
                }
                if (obj instanceof final MapBackground back) {
                    float backgroundOffsetX, backgroundOffsetY;
                    if (getMapSize().x > window.getSize().x) {
                        backgroundOffsetX = (back.getSize().x - window.getSize().x) / (getMapSize().x - window.getSize().x);
                    } else backgroundOffsetX = (back.getSize().x - window.getSize().x) / getMapSize().x;
                    if (getMapSize().y > window.getSize().y) {
                        backgroundOffsetY = (back.getSize().y - window.getSize().y) / (getMapSize().y - window.getSize().y);
                    } else backgroundOffsetY = (back.getSize().y - window.getSize().y) / getMapSize().y;
                    back.setPosition(Vec2.multiply(Vec2.subtract(back.getPositionOnMap(), Vec2.f(backgroundOffsetX, backgroundOffsetY)), getMapOffset()));
                }
            }
        }
    }
}
