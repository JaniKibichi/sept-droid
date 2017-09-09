package com.mjuaji.platformgame;

public class Snow extends GameObject {
    Snow(float worldStartX, float worldStartY, char type){
        setTraversable();
        final float HEIGHT = 1;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("snow");
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }
    @Override
    public void update(long fps, float gravity) {
    }
}
