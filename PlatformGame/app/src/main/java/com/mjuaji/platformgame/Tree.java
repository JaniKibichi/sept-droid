package com.mjuaji.platformgame;

import java.util.Random;

public class Tree extends GameObject {
    Tree (float worldStartX, float worldStartY, char type){
        final float HEIGHT = 4;
        final float WIDTH = 2;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("tree");
        setActive(false);
        //Randomly set the tree either just in front or just behind the player -1 or 1
        Random rand = new Random();
        if(rand.nextInt(2)==0){
            setWorldLocation(worldStartX, worldStartY, -1);
        }else{
            setWorldLocation(worldStartX, worldStartY, 1);
        }
        //no tree
    }
    @Override
    public void update(long fps, float gravity) {
    }
}
