package com.mjuaji.platformgame;

import java.util.ArrayList;

public class LevelCave extends LevelData {
        public LevelCave(){
            tiles = new ArrayList<String>();

            this.tiles.add("p.....................................................");
            this.tiles.add("......................................d...............");
            this.tiles.add(".................................g....................");
            this.tiles.add("......................................................");
            this.tiles.add("....1111111111111111111111111111111111111111111111....");
            this.tiles.add(".......................1..........u...........d.......");
            this.tiles.add("....................c...........u1....................");
            this.tiles.add("....................1.........u1......................");
            this.tiles.add("....d..........c............u1...............d........");
            this.tiles.add("...............1..........u1..........................");
            this.tiles.add("...d........c.........................................");
            this.tiles.add("...........................e..1....e.....e............");
            this.tiles.add("....1111111111111111111111111111111111111111111111....");

            backgroundDataList = new ArrayList<BackgroundData>();
            //note that speeds less than 2 cause problems
            this.backgroundDataList.add(new BackgroundData("skyline", true, -1, 3, 18, 10, 15));
            this.backgroundDataList.add(new BackgroundData("grass", true, 1, 20, 24, 24, 4));
        }
}
