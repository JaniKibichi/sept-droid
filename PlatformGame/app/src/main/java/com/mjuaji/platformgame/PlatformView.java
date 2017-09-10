package com.mjuaji.platformgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class PlatformView extends SurfaceView implements Runnable {
    private boolean debugging = true;
    private volatile boolean running;
    private Thread gameThread = null;

    //For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    Context context;
    long startFrameTime;
    long timeThisFrame;
    long fps;

    //new engine classes
    private LevelManager lm;
    private Viewport vp;
    InputController ic;
    SoundManager sm;
    private PlayerState ps;
    public PlatformView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;
        //initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();
        //initialize viewport
        vp = new Viewport(screenWidth, screenHeight);
        sm = new SoundManager();
        sm.loadSound(context);
        ps = new PlayerState();
        //load the first level
        loadLevel("LevelCave",1,16);
    }
    @Override
    public void run() {
        while(running){
            startFrameTime = System.currentTimeMillis();
            update();
            draw();

            //Calculate the fps this frame, use result to time animations/movement.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if(timeThisFrame >= 1){
                fps = 1000 / timeThisFrame;
            }
        }
    }
    private void update(){
        for(GameObject go : lm.gameObjects){
            if(go.isActive()){
                //clip anything off-screen
                if(!vp.clipObjects(go.getWorldLocation().x,go.getWorldLocation().y,go.getWidth(),go.getHeight())){
                    //set visible flag to true
                    go.setVisible(true);

                    //select all relevant objects and test for collisions
                    int hit = lm.player.checkCollisions(go.getHitbox());

                    if(hit>0){
                        //collision, deal with different types
                        switch(go.getType()){
                            //handle collisions for each pickup
                            case 'c':
                                sm.playSound("coin_pickup");
                                go.setActive(false);
                                go.setVisible(false);
                                ps.gotCredit();
                                //restore state removed ny collision detection
                                if(hit !=2){
                                    lm.player.restorePreviousVelocity();
                                }
                                break;


                            case 'u':
                                sm.playSound("gun_upgrade");
                                go.setActive(false);
                                go.setVisible(false);
                                lm.player.bfg.upgradeRateOfFire();
                                ps.increaseFireRate();
                                //restore state removed by collision detection
                                if(hit !=2){
                                    lm.player.restorePreviousVelocity();
                                }
                                break;

                            case 'e':
                                sm.playSound("extra_life");
                                go.setActive(false);
                                go.setVisible(false);
                                ps.addLife();
                                //restore state removed by collision detection
                                if(hit !=2){
                                    lm.player.restorePreviousVelocity();
                                }
                                break;

                            case 'd':
                                PointF location;
                                //hit by drone
                                sm.playSound("player_burn");
                                ps.loseLife();
                                location = new PointF(ps.loadLocation().x, ps.loadLocation().y);
                                lm.player.setWorldLocationX(location.x);
                                lm.player.setWorldLocationY(location.y);
                                lm.player.setxVelocity(0);
                                break;

                            case 'g':
                                //hit by guard
                                sm.playSound("player_burn");
                                ps.loseLife();
                                location = new PointF(ps.loadLocation().x, ps.loadLocation().y);
                                lm.player.setWorldLocationX(location.x);
                                lm.player.setWorldLocationY(location.y);
                                lm.player.setxVelocity(0);
                                break;

                            case 'f':
                                //touched fire
                                sm.playSound("player_burn");
                                ps.loseLife();
                                location = new PointF(ps.loadLocation().x, ps.loadLocation().y);
                                lm.player.setWorldLocationX(location.x);
                                lm.player.setWorldLocationX(location.y);
                                lm.player.setxVelocity(0);
                                break;

                            case 't':
                                //touched teleport door
                                Teleport teleport = (Teleport) go;
                                Location t = teleport.getTarget();
                                loadLevel(t.level, t.x, t.y);
                                sm.playSound("teleport");
                                break;

                            //a regular tile
                            default:
                                if(hit ==1){
                                    lm.player.setxVelocity(0);
                                    lm.player.setPressingRight(false);
                                }

                                if(hit==2){
                                    lm.player.isFalling = false;
                                }
                                break;
                        }
                    }

                    //check bullet collisions
                    for(int i = 0; i<lm.player.bfg.getNumBullets(); i++){
                        //make a hitbox out of the current bullet
                        RectHitbox r = new RectHitbox();

                        r.setLeft(lm.player.bfg.getBulletX(i));
                        r.setTop(lm.player.bfg.getBulletY(i));
                        r.setRight(lm.player.bfg.getBulletX(i)+.1f);
                        r.setBottom(lm.player.bfg.getBulletY(i)+.1f);

                        if(go.getHitbox().intersects(r)){
                            //Collision detected, maje bullet disappear until it is respawned
                            lm.player.bfg.hideBullet(i);
                            //respond depending on object hit
                            if(go.getType() !='g' && go.getType() != 'd'){
                                sm.playSound("ricochet");
                            }else if (go.getType() == 'g'){
                                //knock the guard back
                                go.setWorldLocationX(go.getWorldLocation().x+2*(lm.player.bfg.getDirection(i)));
                                sm.playSound("hit_guard");
                            }else if (go.getType() =='d'){
                                //destroy the droid
                                sm.playSound("explode");
                                //permanently clip this drone
                                go.setWorldLocation(-100, -100, 0);
                            }
                        }
                    }

                    if(lm.isPlaying()){
                        //run unclipped updates
                        go.update(fps, lm.gravity);
                        if(go.getType() =='d'){
                            //let any near by drones know where the player is
                            Drone d = (Drone) go;
                            d.setWaypoint(lm.player.getWorldLocation());
                        }
                    }
                } else {
                    //set visible flag to false so that draw() can ignore them
                    go.setVisible(false);
                }
            }
        }
        //update viewport/frame to remain center of the viewport
        if(lm.isPlaying()){
            //reset player location
            vp.setWorldCentre(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);
//Has player fallen out of the map?
            if(lm.player.getWorldLocation().x < 0 || lm.player.getWorldLocation().x > lm.mapWidth || lm.player.getWorldLocation().y > lm.mapHeight){
                sm.playSound("player_burn");
                ps.loseLife();
                PointF location = new PointF(ps.loadLocation().x, ps.loadLocation().y);
                lm.player.setWorldLocationX(location.x);
                lm.player.setWorldLocationY(location.y);
                lm.player.setxVelocity(0);
            }

//check if the game is over
            if(ps.getLives() ==0){
                ps = new PlayerState();
                loadLevel("LevelCave", 1, 16);
            }
        }
    }
    private void draw(){
        if(ourHolder.getSurface().isValid()){
            //first we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();

            //rub out last frame with arbitrary color
            paint.setColor(Color.argb(255,0,0,255));
            canvas.drawColor(Color.argb(255,0,0,255));

            //draw parallax background -1 to -3
            drawBackground(0,-3);

            //draw all GameObjects
            Rect toScreen2d = new Rect();

            //draw layer at a atime
            for(int layer = -1; layer <= 1; layer++){
                for(GameObject go : lm.gameObjects){
                    //only draw if visible and this layer
                    if(go.isVisible() && go.getWorldLocation().z == layer){
                        toScreen2d.set(vp.worldToScreen(go.getWorldLocation().x,go.getWorldLocation().y,go.getWidth(),go.getHeight()));

                        if(go.isAnimated()){
                            //get the next frame of the bitmap, rotate if necessary
                            if(go.getFacing()==1){
                                Matrix flipper = new Matrix();
                                flipper.preScale(-1,1);
                                Rect r = go.getRectToDraw(System.currentTimeMillis());
                                Bitmap b = Bitmap.createBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())],r.left,r.top,r.width(),r.height(),flipper, true);
                                canvas.drawBitmap(b, toScreen2d.left,toScreen2d.top,paint);
                            }else {
                                //draw the regular way
                                canvas.drawBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())],go.getRectToDraw(System.currentTimeMillis()),toScreen2d, paint);
                            }
                        }else{
                            //just draw whole bitmap
                            canvas.drawBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())],toScreen2d.left,toScreen2d.top,paint);
                        }
                    }
                    //draw the appropriate bitmap
                    canvas.drawBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())],toScreen2d.left, toScreen2d.top, paint);
                }
            }
            //draw the bullets
            paint.setColor(Color.argb(255,255,255,255));
            for(int i = 0; i<lm.player.bfg.getNumBullets(); i++){
                //pass in the x and y coords, .25 & .05 for bullet width and height
                toScreen2d.set(vp.worldToScreen(lm.player.bfg.getBulletX(i), lm.player.bfg.getBulletY(i), .25f, .05f));
                canvas.drawRect(toScreen2d, paint);
            }

            //draw parallax background from 1 to 3
            drawBackground(4,0);

            //draw Heads Up Display, the code needs bitmaps: extra life, upgrade & coin, one of each in the level
            int topSpace = vp.getPixelsPerMetreY()/4;

            int iconSize = vp.getPixelsPerMetreX();
            int padding = vp.getPixelsPerMetreX()/5;
            int centring = vp.getPixelsPerMetreY()/6;
            paint.setTextSize(vp.getPixelsPerMetreY()/2);
            paint.setTextAlign(Paint.Align.CENTER);

            paint.setColor(Color.argb(100,0,0,0));
            canvas.drawRect(0,0,iconSize*7.0f, topSpace*2+iconSize, paint);
            paint.setColor(Color.argb(255,255,255,0));

            canvas.drawBitmap(lm.getBitmap('e'), 0, topSpace, paint);
            canvas.drawText(""+ps.getLives(), (iconSize*1)+padding, (iconSize) - centring, paint);

            canvas.drawBitmap(lm.getBitmap('c'), (iconSize*2.5f)+ padding, topSpace,paint);
            canvas.drawText(""+ ps.getCredits(), (iconSize*3.5f)+ padding *2, (iconSize) - centring,paint);

            canvas.drawBitmap(lm.getBitmap('u'), (iconSize*5.0f)+ padding, topSpace, paint);
            canvas.drawText(""+ ps.getFireRate(), (iconSize*6.0f)+ padding *2, (iconSize) - centring,paint);

            //text for debugging
            if(debugging) {
                paint.setTextSize(16);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255,255,255,255));

                canvas.drawText("fps:" + fps, 10, 60, paint);
                canvas.drawText("num objects:" + lm.gameObjects.size(), 10, 80, paint);
                canvas.drawText("num clipped:" + vp.getNumClipped(), 10, 100, paint);
                canvas.drawText("playerX:" + lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, 10, 120, paint);
                canvas.drawText("playerY:" + lm.gameObjects.get(lm.playerIndex).getWorldLocation().y, 10, 140, paint);

                canvas.drawText("Gravity:" + lm.gravity, 10, 160, paint);
                canvas.drawText("X velocity:" + lm.gameObjects.get(lm.playerIndex).getxVelocity(), 10, 180, paint);
                canvas.drawText("Y velocity:" + lm.gameObjects.get(lm.playerIndex).getyVelocity(), 10, 200, paint);

                //for reset the number of clipped objects each frame
                vp.resetNumClipped();
            }

            //draw buttons
            paint.setColor(Color.argb(80,255,255,255));
            ArrayList<Rect> buttonsToDraw;
            buttonsToDraw = ic.getButtons();

            for(Rect rect : buttonsToDraw){
                RectF rf = new RectF(rect.left, rect.top, rect.right, rect.bottom);
                canvas.drawRoundRect(rf, 15f, 15f, paint);
            }
            //draw paused text
            if(!this.lm.isPlaying()){
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255,255,255,255));
                paint.setTextSize(120);
                canvas.drawText("Paused", vp.getScreenWidth()/2, vp.getScreenHeight()/2, paint);
            }
            //unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }
    //clean up thread if game is interrupted
    public void pause(){
        running = false;
        try{
            gameThread.join();
        }catch(InterruptedException e){
            Log.e("error","failed to pause thread");
        }
    }
    //make new thread and start it
    public void resume(){
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void loadLevel(String level, float px, float py){
        lm = null;

        //create a new LevelManager pass in a Context, screen details, level name and player location
        lm = new LevelManager(context, vp.getPixelsPerMetreX(),vp.getScreenWidth(),ic, level, px, py);
        ic = new InputController(vp.getScreenWidth(), vp.getScreenHeight());

        //save location for respawning
        PointF location = new PointF(px, py);
        ps.saveLocation(location);

        //on level load, reload Player, MachineGun, Bullet from PlayerState
        lm.player.bfg.setFireRate(ps.getFireRate());

        //set players location as the world centre
        vp.setWorldCentre(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x,lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);
    }
    //draw the background
    private void drawBackground(int start, int stop){
        Rect fromRect1 = new Rect();
        Rect toRect1 = new Rect();
        Rect fromRect2 = new Rect();
        Rect toRect2 = new Rect();
        for(Background bg : lm.backgrounds){
            if(bg.z < start && bg.z > stop){
                //is this layer in the viewport? clip anything off-screen
                if(!vp.clipObjects(-1, bg.y, 1000, bg.height)){

                    float floatstartY = ((vp.getyCentre() - ((vp.getViewportWorldCentreY() - bg.y)*vp.getPixelsPerMetreY())));
                    int startY = (int) floatstartY;

                    float floatendY = ((vp.getyCentre()-((vp.getViewportWorldCentreY() - bg.endY)*vp.getPixelsPerMetreY())));
                    int endY = (int) floatendY;

                    //define portion of bitmaps to capture and the coordinates to draw them at
                    fromRect1 = new Rect(0,0, bg.width - bg.xClip, bg.height);
                    toRect1 = new Rect(bg.width - bg.xClip, 0, bg.width, bg.height);

                    fromRect2 = new Rect(bg.width - bg.xClip,0, bg.width, bg.height);
                    toRect2 = new Rect(0, startY,bg.xClip,endY);
                }
                //draw backgrounds
                if(!bg.reversedFirst){
                    canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint);
                    canvas.drawBitmap(bg.bitmapReversed, fromRect2, toRect2, paint);
                }else{
                    canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint);
                    canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
                }
                //calculate next value for backgrounds clipping position by modifying xClip, switching background thats drawn 1st
                bg.xClip -= lm.player.getxVelocity()/(20/bg.speed);
                if(bg.xClip>=bg.width){
                    bg.xClip = 0;
                    bg.reversedFirst = !bg.reversedFirst;
                }else if(bg.xClip<=0){
                    bg.xClip = bg.width;
                    bg.reversedFirst = !bg.reversedFirst;
                }
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                lm.switchPlayingStatus();
                break;
        }
        if(lm != null){
            ic.handleInput(motionEvent, lm, sm, vp);
        }
        return true;
    }
}
