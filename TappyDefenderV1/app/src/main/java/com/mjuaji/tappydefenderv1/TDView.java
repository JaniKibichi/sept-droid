package com.mjuaji.tappydefenderv1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class TDView extends SurfaceView implements Runnable {
    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;
    private int screenX;
    private int screenY;
    private Context context;
    private boolean gameEnded;

    //make some random space dust
    public ArrayList<SpaceDust> dustList = new ArrayList<SpaceDust>();

    volatile boolean playing;
    Thread gameThread = null;
    //Game objects
    private PlayerShip player;
    private EnemyShip enemy1;
    private EnemyShip enemy2;
    private EnemyShip enemy3;

    //For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    public TDView(Context context, int x, int y) {
        super(context);
        this.context = context;

        //Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        screenX =x;
        screenY=y;

        startGame();
    }
    @Override
    public void run(){
        while(playing){
            update();
            draw();
            control();
        }
    }
    private void startGame(){
        //initialize game objects
        player = new PlayerShip(context, screenX, screenY);
        enemy1 = new EnemyShip(context, screenX, screenY);
        enemy2 = new EnemyShip(context, screenX, screenY);
        enemy3 = new EnemyShip(context, screenX, screenY);

        int numSpecs = 40;
        for(int i = 0; i<numSpecs; i++){
            //where will the dust spawn?
            SpaceDust spec = new SpaceDust(screenX, screenY);
            dustList.add(spec);

        }
        //Reset time and distance
        distanceRemaining = 10000; //10km
        timeTaken =0;

        //Get start time
        timeStarted = System.currentTimeMillis();
        //when starting
        gameEnded=false;
    }
    private void update(){
        //collision detection on new positions if images in excess of 100 px increase -100 value accordingly
        boolean hitDetected = false;

        if(Rect.intersects(player.getHitBox(), enemy1.getHitBox())){
                hitDetected = true;
                enemy1.setX(-100);
         }
        if(Rect.intersects(player.getHitBox(), enemy2.getHitBox())){
                hitDetected = true;
                enemy2.setX(-100);
        }
        if(Rect.intersects(player.getHitBox(), enemy3.getHitBox())){
                hitDetected = true;
                enemy3.setX(-100);
        }

        //check if hit detected
        if(hitDetected){
            player.reduceShieldStrength();
            if(player.getShieldStrength()<0){
                //game over -> do something
                gameEnded = true;
            }
        }

        //update the player
        player.update();

        //update the enemy
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());

        //update the SpaceDust objects
        for(SpaceDust sd : dustList){
            sd.update(player.getSpeed());
        }

        if(!gameEnded){
            //subtract distance to home planet based on current speed
            distanceRemaining -= player.getSpeed();

            //how long has the player been flying
            timeTaken = System.currentTimeMillis() - timeStarted;
        }

        //completed the game
        if(distanceRemaining < 0){
            //check for new fastest time
            if(timeTaken < fastestTime){
                fastestTime = timeTaken;
            }
            //avoid ugly negative numbers in HUD
            distanceRemaining = 0;
            //now end the game
            gameEnded = true;
        }
    }
    private void draw(){
        if(ourHolder.getSurface().isValid()){

            //first we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();
            //Rub out the last frame
            canvas.drawColor(Color.argb(255,0,0,0));

            //draw our dust
            paint.setColor(Color.argb(255,255,255,255));
            //draw dust from arraylist
            for (SpaceDust sd : dustList){
                canvas.drawPoint(sd.getX(), sd.getY(), paint);
            }

            //Draw the player
            canvas.drawBitmap(player.getBitmap(),player.getX(), player.getY(), paint);
            //draw our enemies to screen
            canvas.drawBitmap(enemy1.getBitmap(), enemy1.getX(), enemy1.getY(), paint);
            canvas.drawBitmap(enemy2.getBitmap(), enemy2.getX(), enemy2.getY(), paint);
            canvas.drawBitmap(enemy3.getBitmap(), enemy3.getX(), enemy3.getY(), paint);

            //draw the HUD
            if(!gameEnded) {
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25);

                canvas.drawText("Fastest:" + fastestTime + "s", 10, 20, paint);
                canvas.drawText("Time:" + timeTaken + "s", screenX / 2, 20, paint);
                canvas.drawText("Distance:" + distanceRemaining / 1000 + "KM", screenX / 3, screenY - 20, paint);
                canvas.drawText("Shield:" + player.getShieldStrength(), 10, screenY - 20, paint);
                canvas.drawText("Speed:" + player.getSpeed() * 60 + "MPS", (screenX / 3) * 2, screenY - 20, paint);
            }else{
                //different HUD when game ends - show pause screen
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", screenX/2, 100, paint);

                paint.setTextSize(25);
                canvas.drawText("Fastest:" + fastestTime + "s", screenX/2, 160, paint);
                canvas.drawText("Time:" + timeTaken + "s", screenX/2, 200, paint);
                canvas.drawText("Distance remaining:" + distanceRemaining/1000 + "KM", screenX/2, 240, paint);

                paint.setTextSize(80);
                canvas.drawText("Tap to replay!", screenX/2, 350, paint);
            }
            //unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }
    private void control(){
        try{
            gameThread.sleep(17);
        }catch(InterruptedException e){
        }
    }
    //surfaceview allows us to handle the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        //There are many different events in MotionEvent, but we look at 2
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
        //has the player lifted their finger up?
        case MotionEvent.ACTION_UP:
        player.stopBoosting();
        break;

        //has the player touched the screen?
        case MotionEvent.ACTION_DOWN:
        player.setBoosting();
        break;
        }
        return true;
    }
    //clean up the thread if the game is interrupted or the player quits
    public void pause(){
        playing=false;
        try{
            gameThread.join();
        }catch(InterruptedException e){
        }
    }
    //make new thread and start it
    public void resume(){
        playing=true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
