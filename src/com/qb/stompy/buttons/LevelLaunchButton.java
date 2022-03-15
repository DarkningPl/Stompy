package com.qb.stompy.buttons;

import com.qb.stompy.scenes.LoadedLevelScene;
import com.rubynaxela.kyanite.game.GameContext;
import org.jsfml.system.Vector2i;

public class LevelLaunchButton extends GameButton {

    public final int worldNumber, levelNumber;

    public LevelLaunchButton (String textureName, Vector2i textureSize, int world, int level) {
        super(textureName, textureSize);
        worldNumber = world;
        levelNumber = level;
    }

    @Override
    public void clickAction() {
        GameContext.getInstance().getWindow().setScene(new LoadedLevelScene(worldNumber, levelNumber));
    }
}
