package com.mjuaji.tappydefenderv1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class EnemyShip {
    private Bitmap bitmap;
    private int x, y;
    private int speed =1;

    //detect enemies leaving the screen
    private int maxX;
    private int minX;

    //spawn enemies within screen bounds
    private int maxY;
    private int minY;

    //getters and setters
    public Bitmap getBitmap(){
        return bitmap;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    //constructor
    //spawn the enemy
    public EnemyShip(Context context, int screenX, int screenY){
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        Random generator = new Random();
        speed = generator.nextInt(6)+10;

        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();
    }

    //update
    public void update(int playerSpeed){
        //move to the left
        x -= playerSpeed;
        x -= speed;
        //respawn off screen
        if(x < minX - bitmap.getWidth()){
            Random generator = new Random();
            speed = generator.nextInt(10)+10;

            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }
    }

}
