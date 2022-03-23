package com.qb.stompy.scenes;

import com.qb.stompy.HUDs.GameOverHUD;
import com.qb.stompy.HUDs.LevelPauseHUD;
import com.qb.stompy.dataReaders.World1Reader;
import com.qb.stompy.dataReaders.World2Reader;
import com.qb.stompy.dataReaders.WorldReader;
import com.qb.stompy.living.*;
import com.qb.stompy.living.Character;
import com.qb.stompy.living.candyland.*;
import com.qb.stompy.objects.GameObject;
import com.qb.stompy.objects.MapBackground;
import com.qb.stompy.objects.SolidBlock;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.HUD;
import com.rubynaxela.kyanite.game.Scene;
import com.rubynaxela.kyanite.game.assets.AssetsBundle;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.gui.Font;
import com.rubynaxela.kyanite.game.gui.Text;
import com.rubynaxela.kyanite.util.Colors;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.Window;
import com.rubynaxela.kyanite.window.event.KeyListener;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.KeyEvent;

import java.util.ArrayList;

public class LoadedLevelScene extends Scene {
    private int playerHP;
    private final int levelNumber, worldNumber, bestScore, levelTime;
    private float timePassed = 0, timeSinceCharacterDeath = 0;
    private final float timeFromDeathToHUD = 2.0f;
    private boolean paused = false, isPauseHUD = false, isOverHUD = false, isUpPressed = false, isDownPressed = false, isEscPressed = false,
            wasUpReleased = false, wasDownReleased = false, wasEscReleased = false;
    private final Vector2f mapSize;
    private Vector2f mapOffset = Vec2.f(0, 0);
    private final Text timeVal = new Text();
    private final Font mc_font = new Font(getContext().getAssetsBundle().get("font_mc"), 24);
    private final Character character = new Character();
    private final ArrayList<RectangleShape> playerHearts;
    private LevelPauseHUD pauseHUD = null;
    private GameOverHUD overHUD = null;

    public LoadedLevelScene(int world, int level) {
        AssetsBundle assets = GameContext.getInstance().getAssetsBundle();
        levelNumber = level;
        worldNumber = world;
        float size_x, size_y;
        switch (world) {
            case 0 -> {
                World1Reader.W1Level w1Level = assets.<DataAsset>get("world_0").convertTo(World1Reader.class).getLevels().get(level);
                size_x = w1Level.getSize().x;
                size_y = w1Level.getSize().y;
                bestScore = w1Level.getBestScore();
                levelTime = w1Level.getTime();
            }
            case 1 -> {
                World2Reader.W2Level w2Level = assets.<DataAsset>get("world_1").convertTo(World2Reader.class).getLevels().get(level);
                size_x = w2Level.getSize().x;
                size_y = w2Level.getSize().y;
                bestScore = w2Level.getBestScore();
                levelTime = w2Level.getTime();
            }
            default -> {
                size_x = 0;
                size_y = 0;
                bestScore = 0;
                levelTime = 0;
            }
        }
        mapSize = Vec2.f(size_x, size_y);
        playerHP = character.getCurrentHp();
        playerHearts = new ArrayList<>();
        Texture heartTexture = assets.get("texture_heart");
        for (int i = 0; i < character.getMaxHp(); i++) {
            RectangleShape heart = heartTexture.createRectangleShape();
            heart.setSize(Vec2.f(32, 48));
            heart.setPosition(16 + 36 * i, 16);
            heart.setFillColor(Colors.RED);
            playerHearts.add(heart);
        }

        getContext().getWindow().addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.key) {
                    case DOWN -> isDownPressed = true;
                    case UP -> isUpPressed = true;
                    case ESCAPE -> isEscPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.key) {
                    case DOWN -> isDownPressed = false;
                    case UP -> isUpPressed = false;
                    case ESCAPE -> isEscPressed = false;
                }
            }
        });
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

    public void unpause() { paused = false; isPauseHUD = false; }

    private void loadLevelFromWorld0() {
        World1Reader.W1Level level = GameContext.getInstance().getAssetsBundle().<DataAsset>get("world_0").convertTo(World1Reader.class).getLevels().get(levelNumber);

        //Background
        MapBackground background = new MapBackground(Vec2.f(level.getBackgroundTextureSize().x, level.getBackgroundTextureSize().y));
        GameContext.getInstance().getAssetsBundle().<Texture>get("texture_" + level.getTextureName()).apply(background.mainBody);
        add(background);

        //Character
        character.setPositionOnMap(level.getCharacterPosition().x, level.getCharacterPosition().y);
        add(character);

        //Solid Blocks
        for (int i = 0; i < level.getBlocks().size(); i++) {
            WorldReader.WRBlock blockData = level.getBlocks().get(i);
            SolidBlock block = new SolidBlock(Vec2.f(blockData.getSize().x, blockData.getSize().y));
            block.setPositionOnMap(blockData.getPosition().x, blockData.getPosition().y);
//            Texture texture = GameContext.getInstance().getAssetsBundle().<Texture>get("texture_" + blockData.getTextureName());
//            texture.setTileable(true);
//            texture.apply(block.mainBody);
            block.setTextureTile(blockData.getTextureName());
            add(block);
        }

        //Candies
        for (int i = 0; i < level.getCandies().size(); i++) {
            World1Reader.W1Candy candyData = level.getCandies().get(i);
            Candy candy = new Candy(new Color(candyData.getColor().r, candyData.getColor().g, candyData.getColor().b));
            candy.setPositionOnMap(candyData.getPosition().x, candyData.getPosition().y);
            add(candy);
        }

        //Caramels
        for (int i = 0; i < level.getCaramels().size(); i++) {
            World1Reader.W1Caramel caramelData = level.getCaramels().get(i);
            Caramel caramel = new Caramel();
            caramel.setPositionOnMap(caramelData.getPosition().x, caramelData.getPosition().y);
            add(caramel);
        }

        //Chocolates
        for (int i = 0; i < level.getChocolates().size(); i++) {
            World1Reader.W1Chocolate chocolateData = level.getChocolates().get(i);
            Chocolate chocolate = new Chocolate(chocolateData.getHp());
            chocolate.setPositionOnMap(chocolateData.getPosition().x, chocolateData.getPosition().y);
            add(chocolate);
        }

        //Cookies
        for (int i = 0; i < level.getCookies().size(); i++) {
            World1Reader.W1Cookie cookieData = level.getCookies().get(i);
            Cookie cookie = new Cookie();
            cookie.setPositionOnMap(cookieData.getPosition().x, cookieData.getPosition().y);
            add(cookie);
        }

        //Cotton candies
        for (int i = 0; i < level.getCottonCandies().size(); i++) {
            World1Reader.W1CottonCandy cottonCandyData = level.getCottonCandies().get(i);
            CottonCandy cottonCandy = new CottonCandy(new Color(cottonCandyData.getColor().r, cottonCandyData.getColor().g, cottonCandyData.getColor().b));
            cottonCandy.setPositionOnMap(cottonCandyData.getPosition().x, cottonCandyData.getPosition().y);
            add(cottonCandy);
        }

        //Cupcakes
        for (int i = 0; i < level.getCupcakes().size(); i++) {
            World1Reader.W1Cupcake cupcakeData = level.getCupcakes().get(i);
            Cupcake cupcake = new Cupcake();
            cupcake.setPositionOnMap(cupcakeData.getPosition().x, cupcakeData.getPosition().y);
            add(cupcake);
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

        //Level data and such
        Text textLevel = new Text("Level", mc_font);
        textLevel.setCharacterSize(32);
        textLevel.setColor(Colors.SADDLE_BROWN);
        textLevel.setAlignment(Text.Alignment.TOP_CENTER);
        textLevel.setPosition(GameContext.getInstance().getWindow().getSize().x / 2f, 32);

        Text levelNumber = new Text((getWorldNumber() + 1) + " - " + (getLevelNumber() + 1), mc_font);
        levelNumber.setColor(Colors.SADDLE_BROWN);
        levelNumber.setAlignment(Text.Alignment.TOP_CENTER);
        levelNumber.setPosition(GameContext.getInstance().getWindow().getSize().x / 2f, 80);

        Text textTime = new Text("TIME:", mc_font);
        textTime.setAlignment(Text.Alignment.CENTER_RIGHT);
        textTime.setColor(Colors.SADDLE_BROWN);
        textTime.setPosition(788, 40);

        timeVal.setFont(mc_font);
        timeVal.setAlignment(Text.Alignment.CENTER_RIGHT);
        timeVal.setColor(Colors.SADDLE_BROWN);
        timeVal.setPosition(788, 70);

        Text textBestScore = new Text("BEST SCORE:", mc_font);
        textBestScore.setAlignment(Text.Alignment.CENTER_RIGHT);
        textBestScore.setColor(Colors.SADDLE_BROWN);
        textBestScore.setPosition(788, 100);

        Text bestScoreVal = new Text("" + bestScore, mc_font);
        bestScoreVal.setAlignment(Text.Alignment.CENTER_RIGHT);
        bestScoreVal.setColor(Colors.SADDLE_BROWN);
        bestScoreVal.setPosition(788, 130);

        for (RectangleShape h : playerHearts) add(h);
        add(textLevel, levelNumber, textTime, timeVal, textBestScore, bestScoreVal);
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
                        if (gameObject instanceof final LivingGameObject livingGameObject) livingGameObject.damage(livingGameObject.getCurrentHp());
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
                        backgroundOffsetX = (back.mainBody.getSize().x - window.getSize().x) / (getMapSize().x - window.getSize().x);
                    } else backgroundOffsetX = (back.mainBody.getSize().x - window.getSize().x) / getMapSize().x;
                    if (getMapSize().y > window.getSize().y) {
                        backgroundOffsetY = (back.mainBody.getSize().y - window.getSize().y) / (getMapSize().y - window.getSize().y);
                    } else backgroundOffsetY = (back.mainBody.getSize().y - window.getSize().y) / getMapSize().y;
                    back.setPosition(Vec2.multiply(Vec2.subtract(back.getPositionOnMap(), Vec2.f(backgroundOffsetX, backgroundOffsetY)), getMapOffset()));
                }
            }

            //Update hearts based on player health update
            if (playerHP != character.getCurrentHp()) {
                if (playerHP > character.getCurrentHp()) {
                    for (int i = character.getCurrentHp(); i < playerHP; i++) {
                        playerHearts.get(i).setFillColor(Colors.DIM_GRAY);
                    }
                } else {
                    for (int i = playerHP; i < character.getCurrentHp(); i++) {
                        playerHearts.get(i).setFillColor(Colors.RED);
                    }
                }
                playerHP = character.getCurrentHp();
            }

            if (character.isDead()) timeSinceCharacterDeath += getDeltaTime().asSeconds();
            timePassed += getDeltaTime().asSeconds();
            timeVal.setText("" + (levelTime - (int)timePassed));
            if (timeSinceCharacterDeath >= timeFromDeathToHUD) {
                overHUD = new GameOverHUD(worldNumber, levelNumber);
                window.setHUD(overHUD);
                isOverHUD = true;
                pause();
            }
        }
        if (isOverHUD) {
            if (isUpPressed && wasUpReleased) {
                overHUD.selectOptionAbove();
            }
            if (isDownPressed && wasDownReleased) {
                overHUD.selectOptionBelow();
            }
        } else {
            if (isEscPressed && wasEscReleased) {
                if (isPauseHUD) {
                    window.setHUD(HUD.empty());
                    pauseHUD = null;
                    isPauseHUD = false;
                    unpause();
                } else {
                    pauseHUD = new LevelPauseHUD(worldNumber, levelNumber);
                    window.setHUD(pauseHUD);
                    isPauseHUD = true;
                    pause();
                }
            }
            if (isPauseHUD) {
                if (isUpPressed && wasUpReleased) {
                    pauseHUD.selectOptionAbove();
                }
                if (isDownPressed && wasDownReleased) {
                    pauseHUD.selectOptionBelow();
                }
            }
        }
        wasUpReleased = !isUpPressed;
        wasDownReleased = !isDownPressed;
        wasEscReleased = !isEscPressed;
    }
}
