package com.mjuaji.tappydefenderv1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class PlayerShip {
    //hitbox for collision detection
    private Rect hitBox;

/*PlayerShip class should:
   Know where it is on screen,  Know what it looks like,  Know how fast it is flying
*/
    private int x, y;
    private Bitmap bitmap;
    private int speed = 0;
    private boolean boosting;

    //control boosting onTouch
    private final int GRAVITY = -12;

    //stop ship leaving the screen
    private int maxY;
    private int minY;

    //Limit the bounds of the ship's speed
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

/* What PlayerShip needs to Do:
  Prepare itself(in a constructor), update itself(update method), share it's state with our view(Getters)
*/

    //constructor
    public PlayerShip(Context context, int screenX, int screenY){
        x=50;
        y=50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        boosting = false;
        maxY =screenY - bitmap.getHeight();
        minY = 0;
        //initialize hitbox
        hitBox = new  Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(){
        //are we boosting?
        if(boosting){
            //speed up
            speed += 2;
        }else{
            //slow down
            speed -=5;
        }

        //constrain top speed
        if(speed > MAX_SPEED){
            speed = MAX_SPEED;
        }

        //never stop completely
        if(speed < MIN_SPEED){
            speed = MIN_SPEED;
        }

        //move the ship up or down
        y -= speed + GRAVITY;

        //dont let ship stray off screen
        if(y<minY){
            y = minY;
        }

        if(y<maxY){
            y = maxY;
        }

        //refresh  hit box location in update
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    //getters
    public Bitmap getBitmap(){
        return bitmap;
    }

    public int getSpeed(){
        return speed;
    }
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Rect getHitBox(){
        return hitBox;
    }

    //to boost or not
    public void setBoosting(){
        boosting = true;
    }

    public void stopBoosting(){
        boosting = false;
    }
}
