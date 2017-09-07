package com.mjuaji.platformgame;

import android.content.Context;

public class Player extends GameObject {

    Player(Context context, float worldStartX, float worldStartY, int pixelPerMetre) {
        final float HEIGHT = 2;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType('p');
        //choose bitmap, sprite sheet with multiple frames
        setBitmapName("player");
        //x and y locations from constructor
        setWorldLocation(worldStartX, worldStartY, 0);
    }
    public void update(long fps, float gravity){}
}
