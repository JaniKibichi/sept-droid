package com.mjuaji.platformgame;

import android.graphics.PointF;

public class Drone extends GameObject {
    long lastWaypointSetTime;
    PointF currentWaypoint;

    final float MAX_X_VELOCITY = 3;
    final float MAX_Y_VELOCITY = 3;

    Drone(float worldStartX, float worldStartY, char type){
        final float HEIGHT = 1;
        final float WIDTH = 1;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        //chooose Bitmap
        setBitmapName("drone");
        setType(type);
        setMoves(true);
        setActive(true);
        setVisible(true);
        currentWaypoint = new PointF();
        //where does the drone start, x and y locations from constructor params
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
        setFacing(RIGHT);
    }
    @Override
    public void update(long fps, float gravity) {
        if(currentWaypoint.x > getWorldLocation().x){
            setxVelocity(MAX_X_VELOCITY);
        }else if (currentWaypoint.x > getWorldLocation().x){
            setxVelocity(-MAX_X_VELOCITY);
        }else {
            setxVelocity(0);
        }

        if(currentWaypoint.y > getWorldLocation().y){
            setxVelocity(MAX_Y_VELOCITY);
        }else if (currentWaypoint.y > getWorldLocation().y){
            setyVelocity(-MAX_Y_VELOCITY);
        }else {
            setyVelocity(0);
        }

        move(fps);
        //update drone hitbox
        setRectHitbox();
    }
    public void setWaypoint(Vector2Point5D playerLocation){
        //has 2 seconds passed
        if(System.currentTimeMillis() > lastWaypointSetTime + 2000){
            lastWaypointSetTime = System.currentTimeMillis();
            currentWaypoint.x = playerLocation.x;
            currentWaypoint.y = playerLocation.y;
        }
    }
}
