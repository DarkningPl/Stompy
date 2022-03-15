package com.qb.stompy.HUDs;

import com.qb.stompy.scenes.LoadedLevelScene;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.Scene;
import com.rubynaxela.kyanite.game.gui.Text;

public class LevelHUD extends Scene {

    private final Text timeVal = new Text();
    private float timePassed = 0;
    private final float levelTime;
    private final LoadedLevelScene level;

    public LevelHUD() {
        levelTime = 400;
        level = GameContext.getInstance().getWindow().getScene();
    }

    @Override
    protected void init() {
        //TODO get the time and score settled in LoadedLevel
        Text time = new Text("TIME:");
        time.setAlignment(Text.Alignment.CENTER_RIGHT);
        time.setCharacterSize(40);
        time.setPosition(760, 40);

        timeVal.setAlignment(Text.Alignment.CENTER_RIGHT);
        timeVal.setCharacterSize(40);
        timeVal.setPosition(760, 90);

        Text levelNumber = new Text(level.getWorldNumber() + " - " + level.getLevelNumber());
        levelNumber.setAlignment(Text.Alignment.BOTTOM_CENTER);
        levelNumber.setPosition(GameContext.getInstance().getWindow().getSize().x / 2f, 80);
        levelNumber.setCharacterSize(40);

        add(time, timeVal, levelNumber);
    }

    @Override
    protected void loop() {
        timePassed += getDeltaTime().asSeconds();
        timeVal.setText("" + (levelTime - (int)timePassed));
    }
}
