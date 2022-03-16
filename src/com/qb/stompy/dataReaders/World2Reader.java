package com.qb.stompy.dataReaders;

import java.util.List;

public class World2Reader extends WorldReader {
    private List<W2Level> levels;
    public List<W2Level> getLevels() {return levels;}

    public static class W2Level extends WorldReader.WRLevel {
        private List<W2Block> blocks;
        public List<W2Block> getBlocks() {return blocks;}
    }

    public static class W2Block {
        private String textureName;
        private WorldReader.WRVec2f size, position;
        public String getTextureName() {return textureName;}
        public WorldReader.WRVec2f getSize() {return size;}
        public WorldReader.WRVec2f getPosition() {return position;}
    }
}
