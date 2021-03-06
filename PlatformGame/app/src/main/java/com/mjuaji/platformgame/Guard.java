package com.mjuaji.platformgame;

import android.content.Context;

public class Guard extends GameObject {
    //guards must move between 2 waypoints on x axis
    private float waypointX1;
    private float waypointX2;
    private int currentWaypoint;
    final float MAX_X_VELOCITY = 3;

    Guard(Context context, float worldStartX, float worldStartY, char type, int pixelsPerMetre){
        final int ANIMATION_FPS = 8;
        final int ANIMATION_FRAME_COUNT = 5;

        final String BITMAP_NAME = "guard";
        final float HEIGHT = 2f;
        final float WIDTH = 1;

        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        //choose bitMap
        setBitmapName("guard");
        setMoves(true);
        setActive(true);
        setVisible(true);

        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setBitmapName(BITMAP_NAME);
        setAnimated(context, pixelsPerMetre, true);
        //where does the tile start, X and Y locations from constructor params
        setWorldLocation(worldStartX, worldStartY, 0);
        setxVelocity(-MAX_X_VELOCITY);
        currentWaypoint = 1;
    }
    public void setWaypoints(float x1, float x2){
        waypointX1 = x1;
        waypointX2 = x2;
    }
    @Override
    public void update(long fps, float gravity) {
        //heading left
        if(currentWaypoint ==1){
            if(getWorldLocation().x <=waypointX1){
                //arrived at waypoint1
                currentWaypoint = 2;
                setxVelocity(MAX_X_VELOCITY);
                setFacing(RIGHT);
            }
        }

        if(currentWaypoint ==2){
            if(getWorldLocation().x <=waypointX2){
                //arrived at waypoint2
                currentWaypoint = 1;
                setxVelocity(-MAX_X_VELOCITY);
                setFacing(LEFT);
            }
        }

        move(fps);
        //update the guards hitbox
        setRectHitbox();
    }
}
