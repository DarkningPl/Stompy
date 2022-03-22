package com.qb.stompy.scenes;

import com.qb.stompy.buttons.ExitPromptButton;
import com.qb.stompy.buttons.WorldLaunchButton;
import com.qb.stompy.dataReaders.ProgressReader;
import com.rubynaxela.kyanite.game.Scene;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import com.rubynaxela.kyanite.game.gui.Font;
import com.rubynaxela.kyanite.game.gui.Text;
import com.rubynaxela.kyanite.util.Colors;
import com.rubynaxela.kyanite.util.Vec2;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;

public class MainMenuScene extends Scene {
    private boolean upPressed, downPressed, upReleased, downReleased;
    private final WorldLaunchButton launchButton;
    private final ExitPromptButton exitButton;
    public MainMenuScene() {
        ProgressReader.PRLastLevel lastLevel = getContext().getAssetsBundle().<DataAsset>get("worlds").convertTo(ProgressReader.class).getLastCompletedLevel();
        launchButton = new WorldLaunchButton(Vec2.f(96, 16), "Start game", lastLevel.worldNumber, lastLevel.levelNumber);
        exitButton = new ExitPromptButton(Vec2.f(64, 16), "Exit game");
    }

    @Override
    protected void init() {
        Vector2i windowSize = getContext().getWindow().getSize();
        setBackgroundColor(Colors.AQUAMARINE);

        Text gameTitle = new Text("Stompy game", new Font(getContext().getAssetsBundle().get("font_mc"), 48));
        gameTitle.setAlignment(Text.Alignment.CENTER);
        gameTitle.setPosition(windowSize.x / 2f, windowSize.y / 5f);

        Text gameSubtitle = new Text("Beta", new Font(getContext().getAssetsBundle().get("font_mc"), 24));
        gameSubtitle.setAlignment(Text.Alignment.CENTER);
        gameSubtitle.setPosition(windowSize.x / 2f, windowSize.y / 4f);

        launchButton.setPosition(Vec2.f(windowSize.x / 2f, windowSize.y * 3 / 5f));
        launchButton.setScale(4, 4);

        exitButton.setScale(4, 4);
        exitButton.setPosition(windowSize.x / 2f, windowSize.y * 4 / 5f);

        add(gameTitle, gameSubtitle, launchButton, exitButton);
    }

    @Override
    protected void loop() {
        upPressed = Keyboard.isKeyPressed(Keyboard.Key.UP);
        downPressed = Keyboard.isKeyPressed(Keyboard.Key.DOWN);

        if (upPressed && upReleased) {
            if (!launchButton.isSelected() && !exitButton.isSelected()) {
                exitButton.select();
            }
            else if (launchButton.isSelected() && !exitButton.isSelected()) {
                launchButton.unselect();
                exitButton.select();
            }
            else if (!launchButton.isSelected() && exitButton.isSelected()) {
                launchButton.select();
                exitButton.unselect();
            }
        }
        if (downPressed && downReleased) {
            if (!launchButton.isSelected() && !exitButton.isSelected()) {
                launchButton.select();
            }
            else if (launchButton.isSelected() && !exitButton.isSelected()) {
                launchButton.unselect();
                exitButton.select();
            }
            else if (!launchButton.isSelected() && exitButton.isSelected()) {
                launchButton.select();
                exitButton.unselect();
            }
        }

        upReleased = !upPressed;
        downReleased = !downPressed;
    }
}
