package com.qb.stompy.dataReaders;

import java.util.List;

public class World1Reader extends WorldReader {
    private List<W1Level> levels;
    public List<W1Level> getLevels() {return levels;}

    public static class W1Level extends WorldReader.WRLevel {
        private List<W1Block> blocks;
        private List<W1Candy> candies;
        private List<W1Caramel> caramels;
        private List<W1Chocolate> chocolates;
        private List<W1Cookie> cookies;
        private List<W1CottonCandy> cottonCandies;
        private List<W1Cupcake> cupcakes;
        public List<W1Block> getBlocks() {return blocks;}
        public List<W1Candy> getCandies() {return candies;}
        public List<W1Caramel> getCaramels() {return caramels;}
        public List<W1Chocolate> getChocolates() {return chocolates;}
        public List<W1Cookie> getCookies() {return cookies;}
        public List<W1CottonCandy> getCottonCandies() {return cottonCandies;}
        public List<W1Cupcake> getCupcakes() {return cupcakes;}
    }

    public static class W1Block {
        private String textureName;
        private WorldReader.WRVec2f size, position;
        public String getTextureName() {return textureName;}
        public WorldReader.WRVec2f getSize() {return size;}
        public WorldReader.WRVec2f getPosition() {return position;}
    }

    public static class W1Candy {
        private WorldReader.RGB color;
        private WorldReader.WRVec2f position;
        public WorldReader.RGB getColor() {return color;}
        public WorldReader.WRVec2f getPosition() {return position;}
    }

    public static class W1Caramel {
        private WorldReader.WRVec2f position;
        public WorldReader.WRVec2f getPosition() {return position;}
    }

    public static class W1Chocolate {
        private int hp;
        private WorldReader.WRVec2f position;
        public int getHp() {return hp;}
        public WorldReader.WRVec2f getPosition() {return position;}
    }

    public static class W1Cookie {
        private WorldReader.WRVec2f position;
        public WorldReader.WRVec2f getPosition() {return position;}
    }

    public static class W1CottonCandy {
        private WorldReader.RGB color;
        private WorldReader.WRVec2f position;
        public WorldReader.RGB getColor() {return color;}
        public WorldReader.WRVec2f getPosition() {return position;}
    }

    public static class W1Cupcake {
        private WorldReader.WRVec2f position;
        public WorldReader.WRVec2f getPosition() {return position;}
    }
}
