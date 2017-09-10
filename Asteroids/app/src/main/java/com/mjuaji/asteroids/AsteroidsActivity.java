package com.mjuaji.asteroids;

import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class AsteroidsActivity extends AppCompatActivity {
    private GLSurfaceView asteroidsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get a display object to access the screen details
        Display display = getWindowManager().getDefaultDisplay();
        //load the resolution into a Point object
        Point resolution = new Point();
        display.getSize(resolution);
        asteroidsView = new AsteroidsView(this, resolution.x, resolution.y);
        setContentView(asteroidsView);
    }
    @Override
    protected void onPause(){
        super.onPause();
        asteroidsView.onPause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        asteroidsView.onResume();
    }
}
