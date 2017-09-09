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
    public void setWaypoints(){
        //loop through all game objects looking for guards
        for(GameObject guard: this.gameObjects){
            if(guard.getType()=='g'){
                //set waypoint for this guard, find the tile beneath the guard
                //this relies on the designer putting the guard in sensible lcoation
                int startTileIndex = -1;
                int startGuardIndex = 0;
                float waypointX1 = -1;
                float waypointX2 = -1;

                for(GameObject tile : this.gameObjects){
                    startTileIndex++;
                    if(tile.getWorldLocation().y == guard.getWorldLocation().y+2){
                        //tile is two spaces below current guard., see if has same coordinates
                        if(tile.getWorldLocation().x == guard.getWorldLocation().x){
                            //found the tile that the guard is "standing" on, go left as far as possible before traversable tile is found
                            //either on guards row or tile row - upto max of 5 tiles.

                            for(int i = 0; i<5; i++){//left for loop
                                if(!gameObjects.get(startTileIndex - i).isTraversable()){
                                    //set left waypoint
                                    waypointX1 = gameObjects.get(startTileIndex - (i+1)).getWorldLocation().x;
                                    break; //leave for loop
                                }else {
                                    //set to max 5 tiles as non traversible tile found
                                    waypointX1 = gameObjects.get(startTileIndex - 5).getWorldLocation().x;
                                }
                            }

                            for(int i = 0; i<5; i++){//right for loop
                                if(!gameObjects.get(startTileIndex + i).isTraversable()){
                                    //set right waypoint
                                    waypointX2 = gameObjects.get(startTileIndex + (i-1)).getWorldLocation().x;
                                    break; //leave for loop
                                }else {
                                    //set to max 5 tiles as non traversible tile found
                                    waypointX2 = gameObjects.get(startTileIndex + 5).getWorldLocation().x;
                                }
                            }

                            Guard g = (Guard) guard;
                            g.setWaypoints(waypointX1, waypointX2);

                        }
                    }
                }

            }
        }
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

            case 'c':
                index=3;
                break;

            case 'u':
                index=4;
                break;

            case 'e':
                index=5;
                break;

            case 'd':
                index = 6;
                break;

            case 'g':
                index = 7;
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

            case 'c':
                index=3;
                break;

            case 'u':
                index=4;
                break;

            case 'e':
                index=5;
                break;

            case 'd':
                index = 6;
                break;

            case 'g':
                index = 7;
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

                        case 'c':
                            //add a coin to the gameObjects
                            gameObjects.add(new Coin(j,i,c));
                            break;

                        case 'u':
                            //add a machine gun upgrade to the gameObjects
                            gameObjects.add(new MachineGunUpgrade(j,i,c));
                            break;

                        case 'e':
                            //add an extra life to the gameObjects
                            gameObjects.add(new ExtraLife(j,i,c));
                            break;

                        case 'd':
                            //Add a drone to the gameObjects
                            gameObjects.add(new Drone(j,i,c));
                            break;

                        case 'g':
                            //add a guard to the gameObjects
                            gameObjects.add(new Guard(context, j, i, c, pixelsPerMetre));
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