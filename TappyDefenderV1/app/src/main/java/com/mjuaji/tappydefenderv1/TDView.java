package com.mjuaji.tappydefenderv1;

import android.content.Context;
import android.view.SurfaceView;

public class TDView extends SurfaceView implements Runnable {
    volatile boolean playing;
    Thread gameThread = null;

    public TDView(Context context) {
        super(context);
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

    }

    private void draw(){

    }

    private void control(){

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
