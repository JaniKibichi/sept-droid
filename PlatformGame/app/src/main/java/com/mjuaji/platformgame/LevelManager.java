package com.mjuaji.platformgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

public class LevelManager{
    private String level;
    int mapWidth;
    int mapHeight;
    Player player;
    int playerIndex;
    private boolean playing;
    float gravity;
    LevelData levelData;
    ArrayList<GameObject> gameObjects;
    ArrayList<Rect> currentButtons;
    Bitmap[] bitmapsArray;
    public LevelManager(Context context, int pixelsPerMetre, int screenWidth, InputController ic, String level, float px, float py){
        this.level = level;
        switch(level){
            case "LevelCave":
                levelData = new LevelCave();
                break;
            //add extra levels here
        }
        //arrays ->where to hold all our GameObjects
        gameObjects = new ArrayList<>();

        //arrays ->to hold 1 of every Bitmap
        bitmapsArray = new Bitmap[25];

        //load all the GameObjects and Bitmaps
        loadMapData(context, pixelsPerMetre, px, py);

        //ready to play
        //playing = true;
    }
    public boolean isPlaying(){
        return playing;
    }
    //each index corresponds to a bitmap
    public Bitmap getBitmap(char blockType){
        int index;
        switch(blockType){
            case '.':
                index=0;
                break;

            case '1':
                index=1;
                break;

            case 'p':
                index=2;
                break;

            default:
                index=0;
                break;
        }
        return bitmapsArray[index];
    }
    //each GameObject uses this method to get correct index
    public int getBitmapIndex(char blockType){
        int index;
        switch(blockType){
            case '.':
                index =0;
                break;

            case '1':
                index =0;
                break;

            case 'p':
                index =0;
                break;

            default:
                index =0;
                break;
        }
        return index;
    }
    //load all the grass tiles and the player
    private void loadMapData(Context context, int pixelsPerMetre, float px, float py){
        char c;
        //keep track of where we load our game objects
        int currentIndex = -1;
        //viewport needs to know how wide/high is the map.
        mapHeight = levelData.tiles.size();
        mapWidth = levelData.tiles.get(0).length();

        for(int i=0; i<levelData.tiles.size(); i++){
            for(int j = 0; j<levelData.tiles.get(i).length(); j++){
                c= levelData.tiles.get(i).charAt(j);
                //dont load empty spaces
                if(c != '.'){
                    currentIndex++;
                    switch(c){
                        case '1':
                            //add grass to the gameObjects
                            gameObjects.add(new Grass(j,i,c));
                            break;

                        case 'P':
                            //add player to the gameObjects
                            gameObjects.add(new Player(context,px,py,pixelsPerMetre));
                            //get the index of the player
                            playerIndex = currentIndex;
                            player = (Player) gameObjects.get(playerIndex);
                            break;
                    }
                    //if bitmap is not prepared
                    if(bitmapsArray[getBitmapIndex(c)]==null){
                        //prepare in now and put it in the bitmapsArrayList
                        bitmapsArray[getBitmapIndex(c)] = gameObjects.get(currentIndex).prepareBitmap(context, gameObjects.get(currentIndex).getBitmapName(), pixelsPerMetre);
                    }
                }
            }
        }
    }
    public void switchPlayingStatus(){
        playing = !playing;
        if(playing){
            gravity = 6;
        }else{
            gravity = 0;
        }
    }
}