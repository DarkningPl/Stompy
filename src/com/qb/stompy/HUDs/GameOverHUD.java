package com.qb.stompy.HUDs;

import com.qb.stompy.buttons.CancelButton;
import com.qb.stompy.buttons.LevelLaunchButton;
import com.qb.stompy.buttons.WorldLaunchButton;
import com.rubynaxela.kyanite.game.HUD;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.gui.Font;
import com.rubynaxela.kyanite.game.gui.Text;
import com.rubynaxela.kyanite.util.Colors;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.Window;
import org.jsfml.graphics.RectangleShape;

public class GameOverHUD extends HUD {
    private final LevelLaunchButton tryAgain;
    private final WorldLaunchButton returnToWorld;

    public GameOverHUD(int world, int level) {
        tryAgain = new LevelLaunchButton(Vec2.f(80, 16), "Try again", world, level);
        returnToWorld = new WorldLaunchButton(Vec2.f(80, 16), "Return to world", world, level);
    }

    public void selectOptionAbove() {
        if (returnToWorld.isSelected() && !tryAgain.isSelected()) {
            returnToWorld.unselect();
            tryAgain.select();
        }
        else if (!returnToWorld.isSelected() && tryAgain.isSelected()) {
            returnToWorld.select();
            tryAgain.unselect();
        }
        else if (!returnToWorld.isSelected() && !tryAgain.isSelected()) {
            returnToWorld.select();
        }
    }

    public void selectOptionBelow() {
        if (returnToWorld.isSelected() && !tryAgain.isSelected()) {
            returnToWorld.unselect();
            tryAgain.select();
        }
        else if (!returnToWorld.isSelected() && tryAgain.isSelected()) {
            returnToWorld.select();
            tryAgain.unselect();
        }
        else if (!returnToWorld.isSelected() && !tryAgain.isSelected()) {
            tryAgain.select();
        }
    }

    @Override
    protected void init() {
        //TODO do background
        Window window = getContext().getWindow();
        Texture blurr = getContext().getAssetsBundle().get("texture_blurr");
        RectangleShape background = blurr.createRectangleShape();
        background.setSize(Vec2.f(window.getSize()));

        Text title = new Text("Game over!", new Font(getContext().getAssetsBundle().get("font_mc"), 24));
        title.setAlignment(Text.Alignment.CENTER);
        title.setPosition(window.getSize().x / 2f, window.getSize().y / 2f - background.getSize().y / 8);
        title.setColor(Colors.BLACK);

        tryAgain.setScale(2, 2);
        tryAgain.setPosition(window.getSize().x / 2f, window.getSize().y / 2f - background.getSize().y / 16);

        returnToWorld.setScale(2, 2);
        returnToWorld.setPosition(window.getSize().x / 2f, window.getSize().y / 2f + background.getSize().y / 16);

        add(background, title, tryAgain, returnToWorld);
    }
}
