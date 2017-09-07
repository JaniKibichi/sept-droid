package com.mjuaji.platformgame;

public class Grass extends GameObject {
    Grass(float worldStartX, float worldStartY, char type){
        final float HEIGHT = 1;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        //choose a Bitmap
        setBitmapName("turf");
        //where the tile start, x&y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
    }
    public void update(long fps, float gravity){}
}