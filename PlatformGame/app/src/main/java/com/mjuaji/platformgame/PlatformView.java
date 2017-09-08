package com.mjuaji.platformgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
        //load the first level
        loadLevel("LevelCave",15,2);
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

                    if(lm.isPlaying()){
                        //run unclipped updates
                        go.update(fps, lm.gravity);
                    }
                } else {
                    //set visible flag to false so that draw() can ignore them
                    go.setVisible(false);
                    //select all relevant objects and test for collisions
                    int hit = lm.player.checkCollisions(go.getHitbox());
                    if(hit>0){
                        //collision, deal with different types
                        switch(go.getType()){
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
                }
            }
        }
        //update viewport/frame to remain center of the viewport
        if(lm.isPlaying()){
            //reset player location
            vp.setWorldCentre(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x, lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);
        }
    }
    private void draw(){
        if(ourHolder.getSurface().isValid()){
            //first we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();

            //rub out last frame with arbitrary color
            paint.setColor(Color.argb(255,0,0,255));
            canvas.drawColor(Color.argb(255,0,0,255));

            //draw all GameObjects
            Rect toScreen2d = new Rect();

            //draw layer at a atime
            for(int layer = -1; layer <= 1; layer++){
                for(GameObject go : lm.gameObjects){
                    //only draw if visible and this layer
                    if(go.isVisible() && go.getWorldLocation().z == layer){
                        toScreen2d.set(vp.worldToScreen(go.getWorldLocation().x,go.getWorldLocation().y,go.getWidth(),go.getHeight()));
                    }
                    //draw the appropriate bitmap
                    canvas.drawBitmap(lm.bitmapsArray[lm.getBitmapIndex(go.getType())],toScreen2d.left, toScreen2d.top, paint);
                }
            }
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

        //set players location as the world centre
        vp.setWorldCentre(lm.gameObjects.get(lm.playerIndex).getWorldLocation().x,lm.gameObjects.get(lm.playerIndex).getWorldLocation().y);
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
