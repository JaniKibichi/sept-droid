package com.mjuaji.platformgame;

import android.content.Context;

public class Player extends GameObject {
    final float MAX_X_VELOCITY = 10;
    boolean isPressingRight = false;
    boolean isPressingLeft = false;
    public boolean isFalling;
    public boolean isJumping;
    private long jumpTime;
    private long maxJumpTime = 700;

    public Player(Context context, float worldStartX, float worldStartY, int pixelPerMetre) {
        //standing still to start with
        setxVelocity(0);
        setyVelocity(0);
        setFacing(LEFT);
        isFalling = false;
        //now for the other players attributes
        setMoves(true);
        setActive(true);
        setVisible(true);

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
    public void update(long fps, float gravity){

        if(isPressingRight){
            this.setxVelocity(MAX_X_VELOCITY);
        }else if (isPressingLeft){
            this.setxVelocity(-MAX_X_VELOCITY);
        }else{
            this.setxVelocity(0);
        }

        //which way the player is facing
        if(this.getxVelocity()>0){
        //facing right
            setFacing(RIGHT);
        }else if (this.getxVelocity() < 0){
        //facing left
            setFacing(LEFT);
        }

        //jumping and gravity
        if(isJumping){
            long timeJumping = System.currentTimeMillis() - jumpTime;
            if(timeJumping < maxJumpTime){
                if(timeJumping<maxJumpTime/2){
                    //on the way up
                    this.setyVelocity(-gravity);
                }else if (timeJumping > maxJumpTime/2){
                    //on the way down
                    this.setyVelocity(gravity);
                }
            } else {
                isJumping = false;
            }
        }else {
            this.setyVelocity(gravity);
            //making it easier, comment isFalling
            isFalling = true;
        }
        //update x and y coordinates if they have changed
        this.move(fps);
    }
}
