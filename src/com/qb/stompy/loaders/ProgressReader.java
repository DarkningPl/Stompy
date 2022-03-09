package com.qb.stompy.loaders;

import java.util.List;

public class ProgressReader {
    private List<PRWorld> worlds;
    public List<PRWorld> getWorlds() {return worlds;}

    public static class PRWorld {
        private int bestScore;
        private boolean[] paths;

        public int getBestScore() {return bestScore;}
        public boolean[] getPaths() {return paths;}
        public boolean isPathUnlocked(int pathNumber) {return paths[pathNumber];}
    }
}
