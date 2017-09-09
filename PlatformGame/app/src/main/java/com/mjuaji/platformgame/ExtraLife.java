package com.mjuaji.platformgame;

public class ExtraLife extends GameObject{
    ExtraLife(float worldStartX, float worldStartY, char type){
        final float HEIGHT = .8f;
        final float WIDTH = .65f;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        //choose a Bitmap
        setBitmapName("life");
        //where does the tile start, x and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    @Override
    public void update(long fps, float gravity) {
    }
}
