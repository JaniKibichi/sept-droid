package com.mjuaji.platformgame;

import android.graphics.Rect;

import java.util.ArrayList;

public class InputController {
    Rect left;
    Rect right;
    Rect jump;
    Rect shoot;
    Rect pause;

    public InputController(int screenWidth, int screenHeight){
        //configure the player buttons
        int buttonWidth = screenWidth/8;
        int buttonHeight = screenHeight/7;
        int buttonPadding = screenWidth/80;

        left = new Rect(buttonPadding, screenHeight - buttonHeight - buttonPadding, buttonWidth, screenHeight - buttonPadding);
        right = new Rect(buttonWidth + buttonPadding, screenHeight - buttonHeight - buttonPadding, buttonWidth+buttonPadding+buttonWidth, screenHeight - buttonPadding);
        jump = new Rect(screenWidth - buttonWidth - buttonPadding, screenHeight - buttonHeight - buttonPadding - buttonHeight - buttonPadding, screenWidth - buttonPadding, screenHeight - buttonPadding - buttonHeight - buttonPadding);
        shoot = new Rect(screenWidth - buttonWidth - buttonPadding, screenHeight - buttonHeight - buttonPadding,  screenWidth - buttonPadding, screenHeight - buttonPadding);
        pause = new Rect(screenWidth - buttonPadding - buttonWidth, buttonPadding, screenWidth - buttonPadding, buttonPadding + buttonHeight);
    }

    public ArrayList getButtons(){
        //create an array of buttons for the draw method
        ArrayList<Rect> currentButtonList = new ArrayList<>();
        currentButtonList.add(left);
        currentButtonList.add(right);
        currentButtonList.add(jump);
        currentButtonList.add(shoot);
        currentButtonList.add(pause);
        return currentButtonList;
    }
}
