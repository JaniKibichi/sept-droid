package com.mjuaji.platformgame;

import android.content.Context;

public class Fire extends GameObject {
    Fire(Context context, float worldStartX, float worldStartY, char type, int pixelsPerMetre){
        final int ANIMATION_FPS = 3;
        final int ANIMATION_FRAME_COUNT = 3;
        final String BITMAP_NAME ="fire";

        final float HEIGHT = 1;
        final float WIDTH = 1;

        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);

        //now for the player's other attributes, our game engine will use these
        setMoves(false);
        setActive(true);
        setVisible(true);

        //choose a bitmap
        setBitmapName(BITMAP_NAME);
        //set this object to be animated
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setBitmapName(BITMAP_NAME);
        setAnimated(context, pixelsPerMetre, true);

        //where does the tile start, x and y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    @Override
    public void update(long fps, float gravity) {
    }
}
