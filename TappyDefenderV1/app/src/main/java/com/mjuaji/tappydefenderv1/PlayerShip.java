package com.mjuaji.tappydefenderv1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PlayerShip {
/*PlayerShip class should:
   Know where it is on screen,  Know what it looks like,  Know how fast it is flying
*/
    private int x, y;
    private Bitmap bitmap;
    private int speed = 0;

/* What PlayerShip needs to Do:
  Prepare itself(in a constructor), update itself(update method), share it's state with our view(Getters)
*/

    //constructor
    public PlayerShip(Context context){
        x=50;
        y=50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
    }

    public void update(){
        x++;
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
}
