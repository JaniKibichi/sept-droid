package com.mjuaji.platformgame;

public class Coin extends GameObject {

    Coin(float worldStartX, float worldStartY, char type){
        final float HEIGHT = .5f;
        final float WIDTH = .5f;

        setHeight(HEIGHT);
        setWidth(WIDTH);

        setType(type);

        //choose a bitmap
        setBitmapName("coin");

        //where does the tile start, X, Y locations from constructor params
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    @Override
    public void update(long fps, float gravity) {

    }
}
