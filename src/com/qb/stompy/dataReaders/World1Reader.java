package com.qb.stompy.dataReaders;

import java.util.List;

public class World1Reader extends WorldReader {
    private List<W1Level> levels;
    public List<W1Level> getLevels() {return levels;}

    public static class W1Level extends WorldReader.WRLevel {
        private List<W1Block> blocks;
        private List<W1Candy> candies;
        private List<W1Chocolate> chocolates;
        private List<W1Cupcake> cupcakes;
        private List<W1Pizza> pizzas;
        public List<W1Block> getBlocks() {return blocks;}
        public List<W1Candy> getCandies() {return candies;}
        public List<W1Chocolate> getChocolates() {return chocolates;}
        public List<W1Cupcake> getCupcakes() {return cupcakes;}
        public List<W1Pizza> getPizzas() {return pizzas;}
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

    public static class W1Chocolate {
        private int hp;
        private WorldReader.WRVec2f position;
        public int getHp() {return hp;}
        public WorldReader.WRVec2f getPosition() {return position;}
    }

    public static class W1Cupcake {
        private WorldReader.WRVec2f position;
        public WorldReader.WRVec2f getPosition() {return position;}
    }

    public static class W1Pizza {
        private WorldReader.WRVec2f position;
        public WorldReader.WRVec2f getPosition() {return position;}
    }
}
