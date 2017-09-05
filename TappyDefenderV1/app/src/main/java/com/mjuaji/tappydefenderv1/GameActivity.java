package com.mjuaji.tappydefenderv1;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {
    //object to handle the view
    private TDView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //create instance of TDView
        gameView = new TDView(this);

        //make gameView - view for the Activity
        setContentView(gameView);
    }

    //if the Activity is paused, pause our thread
    @Override
    protected void onPause(){
        super.onPause();
        gameView.pause();
    }

    //if activity is resumed, resume our thread
    @Override
    protected void onResume(){
        super.onResume();
        gameView.resume();
    }
}
