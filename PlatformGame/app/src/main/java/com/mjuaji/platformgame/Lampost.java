package com.mjuaji.platformgame;

import java.util.Random;

public class Lampost extends GameObject {
    Lampost(float worldStartX, float worldStartY, char type){
        final float HEIGHT = 3;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("lampost");
        setActive(false);
        //Randomly set the tree either just in front or just behind the player -1 or 1
        Random rand = new Random();
        if(rand.nextInt(2)==0){
            setWorldLocation(worldStartX, worldStartY, -1);
        }else{
            setWorldLocation(worldStartX, worldStartY, 1);
        }
        //no boulder
    }

    @Override
    public void update(long fps, float gravity) {

    }
}
