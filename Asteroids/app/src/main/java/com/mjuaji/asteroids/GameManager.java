package com.mjuaji.asteroids;

public class GameManager {
    int mapWidth = 600;
    int mapHeight = 600;
    private boolean playing  = false;

    //first spaceship object
    SpaceShip ship;

    int screenHeight;
    int screenWidth;

    //meters of our virtual world to show at any time
    int metresToShowX = 390;
    int metresToShowY = 220;

    public GameManager(int x, int y){
        screenHeight = y;
        screenWidth = x;
    }

    public void switchPlayingStatus(){
        playing = !playing;
    }

    public boolean isPlaying(){
        return playing;
    }
}
