package com.mjuaji.platformgame;

public class MachineGunUpgrade extends GameObject{
    MachineGunUpgrade(float worldStartX, float worldStartY, char type){
        final float HEIGHT = .5f;
        final float WIDTH = .5f;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        //choose a bitmap
        setBitmapName("clip");
        //where does the tile start, X and Y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    @Override
    public void update(long fps, float gravity) {

    }
}
