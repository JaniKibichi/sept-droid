package com.mjuaji.platformgame;

import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;

public class PlatformActivity extends AppCompatActivity {
    //handle the view
    private PlatformView platformView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //get a display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        //load the resolution into a Point object
        Point resolution = new Point();
        display.getSize(resolution);

        //set view for our game, pass screen resolution
        platformView = new PlatformView(this, resolution.x, resolution.y);

        //set the view of the activity
        setContentView(platformView);

    }
    //pause the thread when the activity is paused
    @Override
    protected void onPause(){
        super.onPause();
        platformView.pause();
    }
    //if the activity is resumed, resume our thread
    @Override
    protected void onResume(){
        super.onResume();
        platformView.resume();
    }


}
