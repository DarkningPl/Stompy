package com.qb.stompy.loaders;

import com.qb.stompy.living.*;
import com.qb.stompy.living.Character;
import com.qb.stompy.objects.GameObject;
import com.qb.stompy.objects.MapBackground;
import com.qb.stompy.objects.SolidBlock;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.Scene;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.Window;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.system.Vector2f;

public class LoadedLevelScene extends Scene {
    private final int levelNumber, worldNumber;
    private final Vector2f mapSize;
    private Vector2f mapOffset = Vec2.f(0, 0);
    private final LevelReader.LRLevel levelData;
    private final Character character = new Character();

    public LoadedLevelScene(int world, int level) {
        levelNumber = level;
        worldNumber = world;
        levelData = GameContext.getInstance().getAssetsBundle().<DataAsset>get("levels").convertTo(LevelReader.class).getWorlds().get(world).getLevels().get(level);
        mapSize = Vec2.f(levelData.getLength(), 600);
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

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getWorldNumber() {
        return worldNumber;
    }

    @Override
    protected void init() {
        //Background
        //setBackgroundTexture(GameContext.getInstance().getAssetsBundle().get("texture_" + levelData.textureName));
        MapBackground back = new MapBackground(Vec2.f(levelData.getBackgroundTextureSize().x, levelData.getBackgroundTextureSize().y));
        GameContext.getInstance().getAssetsBundle().<Texture>get("texture_" + levelData.getTextureName()).apply(back);
        add(back);


        //Character
        character.setPositionOnMap(levelData.getCharacterPosition().x, levelData.getCharacterPosition().y);
        add(character);

        //Solid Blocks
        for (int i = 0; i < levelData.getBlocks().size(); i++) {
            LevelReader.LRBlock blockData = levelData.getBlocks().get(i);
            SolidBlock block = new SolidBlock(Vec2.f(blockData.getSize().x, blockData.getSize().y));
            block.setPositionOnMap(blockData.getPosition().x, blockData.getPosition().y);
            GameContext.getInstance().getAssetsBundle().<Texture>get("texture_" + blockData.getTextureName()).apply(block);
            add(block);
        }

        //Candies
        for (int i = 0; i < levelData.getCandies().size(); i++) {
            LevelReader.LRCandy candyData = levelData.getCandies().get(i);
            Candy candy = new Candy(new Color(candyData.getColor().r, candyData.getColor().g, candyData.getColor().b));
            candy.setPositionOnMap(candyData.getPosition().x, candyData.getPosition().y);
            add(candy);
        }

        //Chocolates
        for (int i = 0; i < levelData.getChocolates().size(); i++) {
            LevelReader.LRChocolate chocolateData = levelData.getChocolates().get(i);
            Chocolate chocolate = new Chocolate(chocolateData.getHp());
            chocolate.setPositionOnMap(chocolateData.getPosition().x, chocolateData.getPosition().y);
            add(chocolate);
        }

        //Cupcakes
        for (int i = 0; i < levelData.getCupcakes().size(); i++) {
            LevelReader.LRCupcake cupcakeData = levelData.getCupcakes().get(i);
            Cupcake cupcake = new Cupcake();
            cupcake.setPositionOnMap(cupcakeData.getPosition().x, cupcakeData.getPosition().y);
            add(cupcake);
        }

        //Pizzas
        for (int i = 0; i < levelData.getPizzas().size(); i++) {
            LevelReader.LRPizza pizzaData = levelData.getPizzas().get(i);
            Pizza pizza = new Pizza();
            pizza.setPositionOnMap(pizzaData.getPosition().x, pizzaData.getPosition().y);
            add(pizza);
        }
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
                back.setPosition(Vec2.multiply(Vec2.subtract(back.getPositionOnMap(), Vec2.f(backgroundOffsetX, backgroundOffsetY)),getMapOffset()));
            }
        }
    }
}
