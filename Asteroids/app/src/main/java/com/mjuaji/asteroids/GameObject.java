package com.mjuaji.asteroids;
import android.graphics.PointF;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setRotateM;
import static android.opengl.Matrix.translateM;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.mjuaji.asteroids.GLManager.*;

public class GameObject {
    boolean isActive;
    public enum Type {SHIP, ASTEROID, BORDER, BULLET, STAR}
    private Type type;
    private static int glProgram = -1;
    //how many vertices does it take to make this particular game object?
    private int numElements;
    private int numVertices;
    //to hold the coordinates of the vertices that define our GameObject model
    private float[] modelVertices;
    //which way is the object moving and how fast?
    private float xVelocity = 0f;
    private float yVelocity = 0f;
    private float speed =0;
    private float maxSpeed = 200;
    //where is the object centre in the game game world?
    private PointF worldLocation = new PointF();
    //this will hold our vertext data that is passed into the openGL glProgram, openGL likes FloatBuffer
    private FloatBuffer vertices;
    //for translating each point from the modelk(ship, asteroid etc) to its game world coordinates
    private final float[] modelMatrix = new float[16];
    //some more matrices forOpenGL transformations
    float[] viewportModelMatrix = new float[16];
    float[] rotateViewportModelMatrix = new float[16];
    //where is the GameObject facing?
    private float facingAngle = 90f;
    //how fast is it rotating?
    private float rotationRate = 0f;
    //which direction is it heading?
    private float travellingAngle = 0f;
    //how long and wide is teh GameObject
    private float length;
    private float width;

    public GameObject(){
        //only compile shaders once
        if(glProgram == -1){
            setGLProgram();
            //tell OpenGL to use the glProgram
            glUseProgram(glProgram);
            //now we have a glPorgram we need the locations of our three GLSL variables. We will use these when we call\
            //draw on the object
            uMatrixLocation = glGetUniformLocation(glProgram, U_MATRIX);
            aPositionLocation = glGetAttribLocation(glProgram, A_POSITION);
            uColorLocation = glGetUniformLocation(glProgram, U_COLOR);
        }
        //set the object as active
        isActive = true;
    }

    public boolean isActive(){
        return isActive;
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
    }

    public void setGLProgram(){
        glProgram = GLManager.getGLProgram();
    }

    public Type getType(){
        return type;
    }

    public void setType(Type t){
        this.type = t;
    }
    public void setSize(float w, float l){
        width = w;
        length = l;
    }

    public PointF getWorldLocation(){
        return worldLocation;
    }

    public void setWorldLocation(float x, float y){
        this.worldLocation.x = x;
        this.worldLocation.y = y;
    }

    public void setVertices(float[] objectVertices){
        modelVertices = new float[objectVertices.length];
        modelVertices = objectVertices;

        //store how many vertices and elements there is for future use
        numElements = modelVertices.length;
        numVertices = numElements/ELEMENTS_PER_VERTEX;
        //initialize the vertices ByteBuffer object based on the number of vertices in the shipdesign and the number
        //of bytes in the float type
        vertices = ByteBuffer.allocateDirect(numElements*FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        //add the ship to the ByteBuffer object
        vertices.put(modelVertices);
    }

    public void draw(float[] viewportMatrix){
        //tell opengl to use the glProgram
        glUseProgram(glProgram);
        //set vertices to teh first byte
        vertices.position(0);
        glVertexAttribPointer(aPositionLocation, COMPONENTS_PER_VERTEX, GL_FLOAT, false, STRIDE, vertices);
        glEnableVertexAttribArray(aPositionLocation);
        //translate model coordinated into world coordinated - make an identity matrix to base our future calculations
        //on or we will get very strange results
        setIdentityM(modelMatrix, 0);
        //make a translation matrix
        /* parameters:
        m matrix
        mOffset index into m where the matrix starts
        x translation factor x
        y translation factor y
        z translation factor z
        */
        translateM(modelMatrix, 0, worldLocation.x, worldLocation.y, 0);
        //combine the model with the viewport int a new matrix
        multiplyMM(viewportModelMatrix, 0, viewportMatrix, 0, modelMatrix, 0);

        /*rotate the model - just the ship model
        Parameters
        rm returns the result
        rmOffset index into rm where teh result matrix starts
        a angle to rotate in degrees
        x X axis component
        y Y axis component
        z Z axis component
        */
        setRotateM(modelMatrix, 0, facingAngle, 0, 0, 1.0f);
        //and multiply the rotation matrix into the model-viewport matrix
        multiplyMM(rotateViewportModelMatrix, 0, viewportModelMatrix, 0, modelMatrix, 0);
        //give the matrix to opengl
        glUniformMatrix4fv(uMatrixLocation, 1, false, rotateViewportModelMatrix, 0);
        //assign a color to the fragment shader
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        //draw the point, lines or triangle
        switch(type){
            case SHIP:
                glDrawArrays(GL_TRIANGLES, 0, numVertices);
                break;

            case ASTEROID:
                glDrawArrays(GL_LINES, 0, numVertices);
                break;

            case BORDER:
                glDrawArrays(GL_LINES, 0, numVertices);
                break;

            case STAR:
                glDrawArrays(GL_POINTS, 0, numVertices);
                break;

            case BULLET:
                glDrawArrays(GL_POINTS, 0, numVertices);
                break;
        }
    }
}
