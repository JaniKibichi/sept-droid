package com.mjuaji.platformgame;

import android.graphics.Rect;

public class Viewport {
    private Vector2Point5D currentViewportWorldCentre;
    private Rect convertedRect;
    private int pixelsPerMetreX;
    private int pixelsPerMetreY;
    private int screenXResolution;
    private int screenYResolution;
    private int screenCentreX;
    private int screenCentreY;
    private int metresToShowX;
    private int metresToShowY;
    private int numClipped;

    Viewport(int x, int y){
        screenXResolution = x;
        screenYResolution = y;

        screenCentreX = screenXResolution;
        screenCentreY = screenYResolution;

        pixelsPerMetreX = screenXResolution;
        pixelsPerMetreY = screenYResolution;

        metresToShowX = 34;
        metresToShowY = 20;

        convertedRect = new Rect();
        currentViewportWorldCentre = new Vector2Point5D();
    }
}
