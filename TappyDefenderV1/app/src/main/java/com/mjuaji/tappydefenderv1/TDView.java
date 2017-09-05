package com.mjuaji.tappydefenderv1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TDView extends SurfaceView implements Runnable {
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
        //Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();
        player = new PlayerShip(context, x, y);
        enemy1 = new EnemyShip(context, x, y);
        enemy2 = new EnemyShip(context, x, y);
        enemy3 = new EnemyShip(context, x, y);
    }

    @Override
    public void run(){
        while(playing){
            update();
            draw();
            control();
        }
    }

    private void update(){
        //update the player
        player.update();
        //update the enemy
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());
    }

    private void draw(){
        if(ourHolder.getSurface().isValid()){
            //first we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();
            //Rub out the last frame
            canvas.drawColor(Color.argb(255,0,0,0));

            //Draw the player
            canvas.drawBitmap(player.getBitmap(),player.getX(), player.getY(), paint);
            //draw our enemies to screen
            canvas.drawBitmap(enemy1.getBitmap(), enemy1.getX(), enemy1.getY(), paint);
            canvas.drawBitmap(enemy2.getBitmap(), enemy2.getX(), enemy2.getY(), paint);
            canvas.drawBitmap(enemy3.getBitmap(), enemy3.getX(), enemy3.getY(), paint);

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
