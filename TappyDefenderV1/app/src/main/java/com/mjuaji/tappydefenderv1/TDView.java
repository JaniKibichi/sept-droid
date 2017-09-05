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
        //Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();
        player = new PlayerShip(context, x, y);
        enemy1 = new EnemyShip(context, x, y);
        enemy2 = new EnemyShip(context, x, y);
        enemy3 = new EnemyShip(context, x, y);

        //initialize SpaceDust objects using for loop and stash into ArrayList
        int numSpecs = 40;

        for(int i = 0; i<numSpecs; i++){
            //where will the dust spawn?
            SpaceDust spec = new SpaceDust(x,y);
            dustList.add(spec);
        }
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
        //collision detection on new positions if images in excess of 100 px increase -100 value accordingly
        if(Rect.intersects(player.getHitBox(), enemy1.getHitBox())){
                enemy1.setX(-100);
         }
        if(Rect.intersects(player.getHitBox(), enemy2.getHitBox())){
            enemy2.setX(-100);
        }
        if(Rect.intersects(player.getHitBox(), enemy3.getHitBox())){
            enemy3.setX(-100);
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
