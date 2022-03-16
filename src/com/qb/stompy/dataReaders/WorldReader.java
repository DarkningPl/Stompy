package com.qb.stompy.dataReaders;

public class WorldReader {
    protected String worldName, textureName;
    protected WRVec2f size, startPoint, endPoint, backgroundTextureSize;
    public String getWorldName() {return worldName;}
    public String getTextureName() {return textureName;}
    public WRVec2f getSize() {return size;}
    public WRVec2f getStartPoint() {return startPoint;}
    public WRVec2f getEndPoint() {return endPoint;}
    public WRVec2f getBackgroundTextureSize() {return backgroundTextureSize;}

    public static class WRLevel {
        protected WRVec2f size, position, characterPosition, backgroundTextureSize;
        protected String textureName;
        protected float bestScore, time;
        public WRVec2f getSize() {return size;}
        public WRVec2f getPosition() {return position;}
        public WRVec2f getCharacterPosition() {return characterPosition;}
        public WRVec2f getBackgroundTextureSize() {return backgroundTextureSize;}
        public String getTextureName() {return textureName;}
        public float getBestScore() {return bestScore;}
        public float getTime() {return time;}
    }

    public static class RGB {
        public int r, g, b;
    }

    public static class WRVec2f {
        public int x, y;
    }
}
