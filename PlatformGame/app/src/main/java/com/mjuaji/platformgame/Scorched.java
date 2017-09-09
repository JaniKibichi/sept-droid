package com.mjuaji.platformgame;

public class Scorched extends GameObject {
    Scorched(float worldStartX, float worldStartY, char type){
        setTraversable();
        final float HEIGHT =1;
        final float WIDTH =1;
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setType(type);
        setBitmapName("scorched");
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }
    @Override
    public void update(long fps, float gravity) {
    }
}
