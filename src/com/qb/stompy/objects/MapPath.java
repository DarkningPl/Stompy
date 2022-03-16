package com.qb.stompy.objects;

import com.qb.stompy.dataReaders.ProgressReader;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import org.jsfml.graphics.Color;

public class MapPath extends MapObject {
    private final int worldNumber, levelNumber;
    private boolean msg = false;
    public MapPath(int world, int level) {
        worldNumber = world;
        levelNumber = level;
        if (!GameContext.getInstance().getAssetsBundle().<DataAsset>get("worlds").convertTo(ProgressReader.class).getWorlds().get(getWorldNumber()).isPathUnlocked(getLevelNumber())) {
            setFillColor(new Color(255, 60, 60));
        }
    }
    public int getWorldNumber() {
        return worldNumber;
    }
    public int getLevelNumber() {
        return levelNumber;
    }
    public void printMsg(){
        if (!msg) {
            System.out.println("World number: " + getWorldNumber() + ", level number: " + getLevelNumber() + ", unlocked: " + GameContext.getInstance().getAssetsBundle().<DataAsset>get("maps").convertTo(ProgressReader.class).getWorlds().get(getWorldNumber()).isPathUnlocked(getLevelNumber()));
            msg=true;
        }
    }
    public void enableMsg(){
        msg=false;
    }
}
