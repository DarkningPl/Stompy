package com.qb.stompy.HUDs;

import com.qb.stompy.buttons.CancelButton;
import com.qb.stompy.buttons.WorldLaunchButton;
import com.qb.stompy.scenes.LoadedLevelScene;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.HUD;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.gui.Font;
import com.rubynaxela.kyanite.game.gui.Text;
import com.rubynaxela.kyanite.util.Colors;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.Window;
import org.jsfml.graphics.RectangleShape;

public class LevelPauseHUD extends HUD {

    private final CancelButton returnToLevel;
    private final WorldLaunchButton returnToWorld;

    public LevelPauseHUD(int world, int level) {
        returnToLevel = new CancelButton(Vec2.f(80, 16), "Return to level");
        returnToWorld = new WorldLaunchButton(Vec2.f(80, 16), "Return to world", world, level);
    }

    public void selectOptionAbove() {
        if (returnToWorld.isSelected() && !returnToLevel.isSelected()) {
            returnToWorld.unselect();
            returnToLevel.select();
        }
        else if (!returnToWorld.isSelected() && returnToLevel.isSelected()) {
            returnToWorld.select();
            returnToLevel.unselect();
        }
        else if (!returnToWorld.isSelected() && !returnToLevel.isSelected()) {
            returnToWorld.select();
        }
    }

    public void selectOptionBelow() {
        if (returnToWorld.isSelected() && !returnToLevel.isSelected()) {
            returnToWorld.unselect();
            returnToLevel.select();
        }
        else if (!returnToWorld.isSelected() && returnToLevel.isSelected()) {
            returnToWorld.select();
            returnToLevel.unselect();
        }
        else if (!returnToWorld.isSelected() && !returnToLevel.isSelected()) {
            returnToLevel.select();
        }
    }

    @Override
    protected void init() {
        //TODO do background
        Window window = getContext().getWindow();
        Texture blurr = getContext().getAssetsBundle().get("texture_blurr");
        RectangleShape background = blurr.createRectangleShape();
        background.setSize(Vec2.f(window.getSize()));

        Text title = new Text("Game paused", new Font(getContext().getAssetsBundle().get("font_mc"), 24));
        title.setAlignment(Text.Alignment.CENTER);
        title.setPosition(window.getSize().x / 2f, window.getSize().y / 2f - background.getSize().y / 8);
        title.setColor(Colors.BLACK);

        returnToLevel.setScale(2, 2);
        returnToLevel.setPosition(window.getSize().x / 2f, window.getSize().y / 2f - background.getSize().y / 16);

        returnToWorld.setScale(2, 2);
        returnToWorld.setPosition(window.getSize().x / 2f, window.getSize().y / 2f + background.getSize().y / 16);

        add(background, title, returnToLevel, returnToWorld);
    }
}
