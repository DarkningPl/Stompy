package com.qb.stompy.HUDs;

import com.qb.stompy.buttons.CancelButton;
import com.qb.stompy.buttons.LevelLaunchButton;
import com.qb.stompy.dataReaders.World1Reader;
import com.qb.stompy.dataReaders.World2Reader;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.HUD;
import com.rubynaxela.kyanite.game.assets.AssetsBundle;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.gui.Font;
import com.rubynaxela.kyanite.game.gui.Text;
import com.rubynaxela.kyanite.util.Colors;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.Window;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;

public class LevelLaunchHUD extends HUD {

    private final int worldNumber, levelNumber;
    private final LevelLaunchButton launchButton;
    private final CancelButton cancelButton;

    public LevelLaunchHUD (int world, int level) {
        worldNumber = world;
        levelNumber = level;
        launchButton = new LevelLaunchButton(Vec2.f(48, 16), "Go!", worldNumber, levelNumber);
        cancelButton = new CancelButton(Vec2.f(48, 16), "Cancel");
    }

    public void selectOptionAbove() {
        if (cancelButton.isSelected() && !launchButton.isSelected()) {
            cancelButton.unselect();
            launchButton.select();
        }
        else if (!cancelButton.isSelected() && launchButton.isSelected()) {
            cancelButton.select();
            launchButton.unselect();
        }
        else if (!cancelButton.isSelected() && !launchButton.isSelected()) {
            cancelButton.select();
        }
    }

    public void selectOptionBelow() {
        if (cancelButton.isSelected() && !launchButton.isSelected()) {
            cancelButton.unselect();
            launchButton.select();
        }
        else if (!cancelButton.isSelected() && launchButton.isSelected()) {
            cancelButton.select();
            launchButton.unselect();
        }
        else if (!cancelButton.isSelected() && !launchButton.isSelected()) {
            launchButton.select();
        }
    }

    @Override
    protected void init() {
        AssetsBundle assets = GameContext.getInstance().getAssetsBundle();
        Window window = GameContext.getInstance().getWindow();
        String backgroundTextureName = "";
        switch (worldNumber) {
            case 0 -> {
                backgroundTextureName = assets.<DataAsset>get("world_0").convertTo(World1Reader.class).getLevels().get(levelNumber).getTextureName();
            }
            case 1 -> {
                backgroundTextureName = assets.<DataAsset>get("world_1").convertTo(World2Reader.class).getLevels().get(levelNumber).getTextureName();
            }
        }
        Text name = new Text((worldNumber + 1) + " - " + (levelNumber + 1), new Font(getContext().getAssetsBundle().get("font_mc"), 24));
        RectangleShape frame = new RectangleShape();
        RectangleShape background = new RectangleShape();

        Texture backgroundTexture = assets.get("texture_" + backgroundTextureName);
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
        name.setColor(Colors.BLACK);

        launchButton.setScale(2, 2);
        launchButton.setPosition(window.getSize().x / 2f, window.getSize().y / 2f + background.getSize().y / 8);

        cancelButton.setScale(2, 2);
        cancelButton.setPosition(window.getSize().x / 2f, window.getSize().y / 2f + background.getSize().y * 3 / 8);

        add(background);
        add(frame);
        add(name);
        add(launchButton);
        add(cancelButton);
    }
}
