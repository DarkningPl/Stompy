package com.qb.stompy.HUDs;

import com.qb.stompy.buttons.LevelLaunchButton;
import com.qb.stompy.dataReaders.LevelReader;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.Scene;
import com.rubynaxela.kyanite.game.assets.AssetsBundle;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.gui.Font;
import com.rubynaxela.kyanite.game.gui.Text;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.Window;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.window.Keyboard;

public class LevelLaunchHUD extends Scene {

    private final int worldNumber, levelNumber;

    public LevelLaunchHUD (int world, int level) {
        worldNumber = world;
        levelNumber = level;
    }
    @Override
    protected void init() {
        AssetsBundle assets = GameContext.getInstance().getAssetsBundle();
        Window window = GameContext.getInstance().getWindow();
        String backgroundTextureName = assets.<DataAsset>get("levels").convertTo(LevelReader.class).getWorlds().get(worldNumber).getLevels().get(levelNumber).getTextureName();
        Text name = new Text((worldNumber + 1) + " - " + (levelNumber + 1), new Font(getContext().getAssetsBundle().get("font_mc"), 24));
        LevelLaunchButton launchButton = new LevelLaunchButton("button_go", Vec2.i(32, 16), worldNumber, levelNumber);
        RectangleShape frame = new RectangleShape();
        RectangleShape background = new RectangleShape();

        Texture backgroundTexture = assets.get(backgroundTextureName);
        backgroundTexture.apply(background);
        background.setSize(Vec2.f(320, 240));
        background.setOrigin(Vec2.divide(background.getSize(), 2));
        background.setPosition(Vec2.divideFloat(window.getSize(), 2));

        frame.setSize(background.getSize());
        frame.setOrigin(Vec2.divide(background.getSize(), 2));
        frame.setPosition(Vec2.divideFloat(window.getSize(), 2));
        frame.setFillColor(Color.TRANSPARENT);
        frame.setOutlineColor(new Color(128, 128, 128));
        frame.setOutlineThickness(4);

        name.setAlignment(Text.Alignment.CENTER);
        name.setPosition(window.getSize().x / 2f, window.getSize().y / 2f - background.getSize().y / 4);

        launchButton.setScale(2, 2);
        launchButton.setPosition(window.getSize().x / 2f, window.getSize().y / 2f + background.getSize().y / 4);
        launchButton.select();

        add(background);
        add(frame);
        add(name);
        add(launchButton);
    }

    @Override
    protected void loop() {
        if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) {
            //TODO remove this HUD from window
            System.out.println("Begone thot!");
        }
    }
}
