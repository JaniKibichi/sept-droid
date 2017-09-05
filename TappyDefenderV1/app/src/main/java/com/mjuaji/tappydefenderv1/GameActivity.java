package com.mjuaji.tappydefenderv1;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends Activity {
    //object to handle the view
    private TDView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make gameView - view for the Activity
        setContentView(gameView);

        //detect screen resolution on app start, get display object
        Display display = getWindowManager().getDefaultDisplay();

        //load resolution to a point object
        Point size = new Point();
        display.getSize(size);

        //create an instance of TDView and pass this, size.x, size.y
        gameView = new TDView (this, size.x, size.y);
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
