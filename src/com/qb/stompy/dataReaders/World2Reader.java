package com.qb.stompy.dataReaders;

import java.util.List;

public class World2Reader extends WorldReader {
    private List<W2Level> levels;
    public List<W2Level> getLevels() {return levels;}

    public static class W2Level extends WorldReader.WRLevel {

    }
}
