package com.qb.stompy.dataReaders;

import java.util.List;

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
        protected int bestScore, time;
        protected List<WRBlock> blocks;
        public WRVec2f getSize() {return size;}
        public WRVec2f getPosition() {return position;}
        public WRVec2f getCharacterPosition() {return characterPosition;}
        public WRVec2f getBackgroundTextureSize() {return backgroundTextureSize;}
        public String getTextureName() {return textureName;}
        public int getBestScore() {return bestScore;}
        public int getTime() {return time;}
        public List<WRBlock> getBlocks() {return blocks;}
    }

    public static class WRBlock {
        private String textureName;
        private WorldReader.WRVec2f size, position;
        public String getTextureName() {return textureName;}
        public WRVec2f getSize() {return size;}
        public WRVec2f getPosition() {return position;}
    }

    public static class RGB {
        public int r, g, b;
    }

    public static class WRVec2f {
        public int x, y;
    }
}
