package com.qb.stompy.buttons;

import com.qb.stompy.scenes.LoadedLevelScene;
import com.rubynaxela.kyanite.game.HUD;
import com.rubynaxela.kyanite.util.Colors;
import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

public class LevelLaunchButton extends GameButton {

    private final int worldNumber, levelNumber;

    public LevelLaunchButton(Vector2f buttonSize, String message, int world, int level) {
        this(buttonSize, 8, message, Colors.BLACK, world, level);
    }
    public LevelLaunchButton (Vector2f buttonSize, int fontSize, String message, Color textColor, int world, int level) {
        super(buttonSize, fontSize, message, textColor);
        worldNumber = world;
        levelNumber = level;
    }

    @Override
    public void clickAction() {
        window.setHUD(HUD.empty());
        window.setScene(new LoadedLevelScene(worldNumber, levelNumber));
    }
}
