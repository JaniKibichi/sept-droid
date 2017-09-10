package com.mjuaji.asteroids;

import android.graphics.PointF;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

public class AsteroidsRenderer implements Renderer{
    //are we debugging
    boolean debugging  = true;
    //for monitoring and controlling the frames per second
    long frameCounter = 0;
    long averageFPS = 0;
    private long fps;
    //for converting each game world coordinate to GL space coordinate for drawing to the screen
    private final float[] viewportMatrix = new float[16];
    //a class to manage our game objects current state
    private GameManager gm;
    //for capturing various PointF detauls without creating new objects in the speed critical areas
    PointF handyPointF;
    PointF handyPointF2;


    public AsteroidsRenderer(GameManager gameManager){
        gm = gameManager;
        handyPointF = new PointF();
        handyPointF2 = new PointF();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //the color that will be used to clear the screen each frame in onDrawFrame()
        glClearColor(0.0f, 0.0f,0.0f,0.0f);
        //get GLManager to compile and link the shades into an object
        GLManager.buildProgram();
        createObjects();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //make full screen
        glViewport(0,0, width, height);
        /*
        Initialize our viewport matrix by passing in the starting range of the game workd that will be ,aoped, by opengl to the screen. We will dynamically amend this as the player moves around. The argumnts to set up teh viewport matrix:
        our array, starting index in array min x, max x, min y, max y, min z max z)
        */
        orthoM(viewportMatrix, 0, 0, gm.metresToShowX, 0, gm.metresToShowY, 0f, 1f);
    }

    private void createObjects(){
        //create our game objects, first the ship in the center of the map
        gm.ship = new SpaceShip(gm.mapWidth/2, gm.mapHeight/2);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        long startFrameTime = System.currentTimeMillis();
        if(gm.isPlaying()){
            update(fps);
        }
        draw();
        //calculate the fps this frame, we can then use the result to time animations and more
        long timeThisFrame = System.currentTimeMillis() - startFrameTime;
        if(timeThisFrame >= 1){
            fps = 1000/timeThisFrame;
        }
        //output the average frames per second to the console
        if(debugging){
            frameCounter++;
            averageFPS = averageFPS + fps;
            frameCounter = 0;
            Log.e("averageFPS: ", "" + averageFPS);
        }

    }

    private void update(long fps){

    }

    private void draw(){
        //where is the ship?
        handyPointF = gm.ship.getWorldLocation();
        //modify the viewport matrix orthigraphic projection based on ship location
        orthoM(viewportMatrix, 0,
                handyPointF.x - gm.metresToShowX/2,
                handyPointF.x + gm.metresToShowX/2,
                handyPointF.y - gm.metresToShowY/2,
                handyPointF.y + gm.metresToShowY/2,
                0f, 1f);
        //clear the screen
        glClear(GL_COLOR_BUFFER_BIT);
        //start drawing

        //draw the ship
        gm.ship.draw(viewportMatrix);
    }
}
