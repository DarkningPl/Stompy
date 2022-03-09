package com.qb.stompy.loaders;

import java.util.List;

public class LevelReader {

    private List<LRWorld> worlds;
    public List<LRWorld> getWorlds() {return worlds;}

    public static class LRWorld {
        private String textureName;
        private Vec2f size, startPoint, endPoint, backgroundTextureSize;
        private List<LRLevel> levels;
        public String getTextureName() {return textureName;}
        public Vec2f getSize() {return size;}
        public Vec2f getStartPoint() {return startPoint;}
        public Vec2f getEndPoint() {return endPoint;}
        public Vec2f getBackgroundTextureSize() {return backgroundTextureSize;}
        public List<LRLevel> getLevels() {return levels;}
    }

    public static class LRLevel {
        private Vec2f position, characterPosition, backgroundTextureSize;
        private String textureName, iconTextureName;
        private float length;
        private List<LRBlock> blocks;
        private List<LRCandy> candies;
        private List<LRChocolate> chocolates;
        private List<LRCupcake> cupcakes;
        private List<LRPizza> pizzas;
        public Vec2f getPosition() {return position;}
        public Vec2f getCharacterPosition() {return characterPosition;}
        public Vec2f getBackgroundTextureSize() {return backgroundTextureSize;}
        public String getTextureName() {return textureName;}
        public String getIconTextureName() {return iconTextureName;}
        public float getLength() {return length;}
        public List<LRBlock> getBlocks() {return blocks;}
        public List<LRCandy> getCandies() {return candies;}
        public List<LRChocolate> getChocolates() {return chocolates;}
        public List<LRCupcake> getCupcakes() {return cupcakes;}
        public List<LRPizza> getPizzas() {return pizzas;}
    }

    public static class LRBlock {
        private String textureName;
        private Vec2f size, position;
        public String getTextureName() {return textureName;}
        public Vec2f getSize() {return size;}
        public Vec2f getPosition() {return position;}
    }

    public static class LRCandy {
        private RGB color;
        private Vec2f position;

        public RGB getColor() {return color;}
        public Vec2f getPosition() {return position;}
    }

    public static class LRChocolate {
        private int hp;
        private Vec2f position;
        public int getHp() {return hp;}
        public Vec2f getPosition() {return position;}
    }

    public static class LRCupcake {
        private Vec2f position;
        public Vec2f getPosition() {return position;}
    }

    public static class LRPizza {
        private Vec2f position;
        public Vec2f getPosition() {return position;}
    }

    public static class RGB {
        public int r, g, b;
    }

    public static class Vec2f {
        public float x, y;
    }
}
