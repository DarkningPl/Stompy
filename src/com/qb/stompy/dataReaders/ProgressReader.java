package com.qb.stompy.dataReaders;

import java.util.List;

@SuppressWarnings("unused")
public class ProgressReader {
    private PRLastLevel lastCompletedLevel;
    private List<PRWorld> worlds;
    public PRLastLevel getLastCompletedLevel() {return lastCompletedLevel;}
    public List<PRWorld> getWorlds() {return worlds;}

    public static class PRWorld {
        private int[] bestScores;
        private boolean[] paths;

        public int[] getBestScores() {return bestScores;}
        public boolean[] getPaths() {return paths;}
        public boolean isPathUnlocked(int pathNumber) {return paths[pathNumber];}
    }

    public static class PRLastLevel {
        public int worldNumber, levelNumber;
    }
}
