package com.qb.stompy.dataReaders;

public class CharacterReader {
    public LoadedCharacter character;

    public static class LoadedCharacter {
        public int lastWorld;
        public int lastLevel;
        public int livesCount;
        public int totalScore;
    }
}
