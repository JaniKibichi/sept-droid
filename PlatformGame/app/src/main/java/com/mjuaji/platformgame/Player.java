package com.mjuaji.platformgame;

import android.content.Context;

public class Player extends GameObject {
    RectHitbox rectHitboxFeet;
    RectHitbox rectHitboxHead;
    RectHitbox rectHitboxLeft;
    RectHitbox rectHitboxRight;

    final float MAX_X_VELOCITY = 10;
    boolean isPressingRight = false;
    boolean isPressingLeft = false;
    public boolean isFalling;
    public boolean isJumping;
    private long jumpTime;
    private long maxJumpTime = 700;

    public MachineGun bfg;

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
        final int ANIMATION_FPS = 16;
        final int ANIMATION_FRAME_COUNT= 5;
        //set this object up to be animates
        setAnimFps(ANIMATION_FPS);
        setAnimFrameCount(ANIMATION_FRAME_COUNT);
        setAnimated(context, pixelPerMetre, true);
        //x and y locations from constructor
        setWorldLocation(worldStartX, worldStartY, 0);
        //initialize hitboxes
        rectHitboxRight = new RectHitbox();
        rectHitboxLeft = new RectHitbox();
        rectHitboxHead = new RectHitbox();
        rectHitboxFeet = new RectHitbox();
        //initialize gun
        bfg = new MachineGun();
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
        //update for gun
        bfg.update(fps, gravity);
        //update x and y coordinates if they have changed
        this.move(fps);
        //update hitboxes, get current world location, save as local variables
        Vector2Point5D location = getWorldLocation();
        float lx = location.x;
        float ly = location.y;

        //update the player feet hitBox
        rectHitboxFeet.top = ly + getHeight()*.95f;
        rectHitboxFeet.left = lx + getWidth()*.2f;
        rectHitboxFeet.bottom = ly + getHeight()*.98f;
        rectHitboxFeet.right = lx + getWidth()*.8f;

        //update the player head hitBox
        rectHitboxHead.top = ly;
        rectHitboxHead.left = lx + getWidth()*.4f;
        rectHitboxHead.bottom = ly + getHeight()*.2f;
        rectHitboxHead.right = lx + getWidth()*.6f;

        //update the player left hitBox
        rectHitboxLeft.top = ly + getHeight()*.2f;
        rectHitboxLeft.left = lx + getWidth()*.2f;
        rectHitboxLeft.bottom = ly + getHeight()*.8f;
        rectHitboxLeft.right = lx + getWidth()*.3f;

        //update the player right hitBox
        rectHitboxRight.top = ly + getHeight()*.2f;
        rectHitboxRight.left = lx + getWidth()*.8f;
        rectHitboxRight.bottom = ly + getHeight()*.8f;
        rectHitboxRight.right = lx + getWidth()*.7f;
    }
    public int checkCollisions(RectHitbox rectHitbox){
        //no collision
        int collided = 0;

        //the left
        if(this.rectHitboxLeft.intersects(rectHitbox)){
            //left has collided, move player just to right of current hitbox
            this.setWorldLocationX(rectHitbox.right - getWidth()*.2f);
            collided = 1;
        }

        //the right
        if(this.rectHitboxLeft.intersects(rectHitbox)){
            //right has collided, move player just to left of current hitbox
            this.setWorldLocationX(rectHitbox.left - getWidth()*.8f);
            collided = 1;
        }

        //the feet
        if(this.rectHitboxFeet.intersects(rectHitbox)){
            //feet have collided, move player just above current hitbox
            this.setWorldLocationY(rectHitbox.top - getHeight());
            collided = 2;
        }

        //the head
        if(this.rectHitboxHead.intersects(rectHitbox)){
            //head have collided, move head just below current hitbox
            this.setWorldLocationY(rectHitbox.bottom);
            collided = 3;
        }
        return collided;
    }
    public void setPressingRight(boolean isPressingRight){
        this.isPressingRight = isPressingRight;
    }
    public void setPressingLeft(boolean isPressingLeft){
        this.isPressingLeft = isPressingLeft;
    }
    public void startJump(SoundManager sm){
        if(!isFalling){
            if(!isJumping){
                isJumping = true;
                jumpTime = System.currentTimeMillis();
                sm.playSound("jump");
            }
        }
    }
    public boolean pullTrigger(){
        //Try and fire a shot
        return bfg.shoot(this.getWorldLocation().x, this.getWorldLocation().y, getFacing(), getHeight());
    }
    public void restorePreviousVelocity(){
        if(!isJumping && !isFalling){
            if(getFacing() ==LEFT){
                isPressingLeft = true;
                setxVelocity(-MAX_X_VELOCITY);
            }else {
                isPressingRight=true;
                setxVelocity(MAX_X_VELOCITY);
            }
        }
    }

}
