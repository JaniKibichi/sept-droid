package com.mjuaji.tappydefenderv1;

import java.util.Random;

public class SpaceDust{
    private int x, y;
    private int speed;

    //detect dust leaving the screen
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    //constructor
    public SpaceDust(int screenX, int screenY){
        maxX = screenX;
        minX = 0;
        maxY = screenY;
        minY = 0;

        //set a speed between 0 and 9
        Random generator = new Random();
        speed = generator.nextInt(10);

        //set the starting coordinates
        x= generator.nextInt(maxX);
        y= generator.nextInt(maxY);
    }

    public void update(int playerSpeed){
        //speed up when the player does
        x -= playerSpeed;
        x -= speed;

        //respawn space dust
        if(x < 0){
            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
            speed = generator.nextInt(15);
        }
    }

    //getters and setters
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}