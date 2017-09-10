package com.mjuaji.asteroids;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class AsteroidsView {
    GameManager gm;

    AsteroidsView(Context context, int screenX, int screenY){
        super(context);
        gm = new GameManager(screenX, screenY);
        //which version of openGL are we using?
        setEGLContextClientVersion(2);
        //attach our renderer to the GLSSurfaceView;
        setRenderer(new AsteroidsRenderer(gm));
    }
}
