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

    //For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    public TDView(Context context) {
        super(context);
        //Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        player = new PlayerShip(context);
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
        //update the palyer
        player.update();
    }

    private void draw(){
        if(ourHolder.getSurface().isValid()){
            //first we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();
            //Rub out the last frame
            canvas.drawColor(Color.argb(255,0,0,0));
            //Draw the player
            canvas.drawBitmap(player.getBitmap(),player.getX(), player.getY(), paint);
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
        //do something
        break;

        //has the player touched the screen?
        case MotionEvent.ACTION_DOWN:
        //do something
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
