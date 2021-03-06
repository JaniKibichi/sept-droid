package com.mjuaji.platformgame;

import java.util.Random;

public class Cart extends GameObject {
    Cart(float worldStartX, float worldStartY, char type){
        setTraversable();
        final float HEIGHT = 2;
        final float WIDTH = 3;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("cart");
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
