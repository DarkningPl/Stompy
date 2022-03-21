package com.qb.stompy.objects;

import com.qb.stompy.dataReaders.ProgressReader;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import org.jsfml.graphics.Color;

public class MapPath extends MapObject {
    private final int worldNumber, levelNumber;
    public MapPath(int world, int level) {
        super();
        worldNumber = world;
        levelNumber = level;
        if (!GameContext.getInstance().getAssetsBundle().<DataAsset>get("worlds").convertTo(ProgressReader.class).getWorlds().get(getWorldNumber()).isPathUnlocked(getLevelNumber())) {
            mainBody.setFillColor(new Color(255, 60, 60));
        }
    }
    public int getWorldNumber() {
        return worldNumber;
    }
    public int getLevelNumber() {
        return levelNumber;
    }
}
