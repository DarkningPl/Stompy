package com.qb.stompy.objects;

import com.qb.stompy.scenes.LoadedLevelScene;
import com.qb.stompy.scenes.LoadedWorldScene;
import com.qb.stompy.scenes.LoadedLevelScene;
import com.qb.stompy.scenes.LoadedWorldScene;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.gui.Font;
import com.rubynaxela.kyanite.game.gui.Text;
import org.jetbrains.annotations.NotNull;
import org.jsfml.system.Vector2f;

public class GameText extends Text {

    private Vector2f positionOnMap;

    public GameText() {
        super();
    }

    public GameText(@NotNull Font font) {
        super(font);
    }

    public GameText(@NotNull String text) {
        super(text);
    }

    public GameText(@NotNull String text, @NotNull Font font) {
        super(text, font);
    }

    public void setPositionOnMap(float posX, float posY) {
        positionOnMap = new Vector2f(posX, posY);
    }

    public Vector2f getPositionOnMap() {
        return positionOnMap;
    }

    public LoadedLevelScene getLevelScene() {
        if (GameContext.getInstance().getWindow().getScene() instanceof final LoadedLevelScene levelScene)
            return levelScene;
        return null;
    }

    public LoadedWorldScene getWorldScene() {
        if (GameContext.getInstance().getWindow().getScene() instanceof final LoadedWorldScene worldScene)
            return worldScene;
        return null;
    }
}
