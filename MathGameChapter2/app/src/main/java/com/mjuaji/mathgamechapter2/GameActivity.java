package com.mjuaji.mathgamechapter2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends Activity implements View.OnClickListener {
    int correctAnswer;
    Button buttonObjectChoice1;
    Button buttonObjectChoice2;
    Button buttonObjectChoice3;
    TextView textObjectPartA;
    TextView textObjectPartB;
    TextView textObjectScore;
    TextView textObjectLevel;
    int currentScore = 0;
    int currentLevel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //link our objects to the relevant UI elements
        textObjectPartA = (TextView)findViewById(R.id.textPartA);
        textObjectPartB = (TextView)findViewById(R.id.textPartB);
        textObjectScore = (TextView)findViewById(R.id.textScore);
        textObjectScore = (TextView)findViewById(R.id.textLevel);

        buttonObjectChoice1 = (Button)findViewById(R.id.buttonChoice1);
        buttonObjectChoice2 = (Button)findViewById(R.id.buttonChoice2);
        buttonObjectChoice3 = (Button)findViewById(R.id.buttonChoice3);

        //give buttons ability to listen
        buttonObjectChoice1.setOnClickListener(this);
        buttonObjectChoice2.setOnClickListener(this);
        buttonObjectChoice3.setOnClickListener(this);

        //set the question
        setQuestion();
    }
    @Override
    public void onClick(View v) {
        //declare a new int to be used in all the cases
        int answerGiven = 0;
        //differentiate between the three different clicks
        switch(v.getId()){

            case R.id.buttonChoice1:
                //button 1 stuff
                answerGiven = Integer.parseInt(""+buttonObjectChoice1.getText());
            break;

            case R.id.buttonChoice2:
                //button 2 stuff
                answerGiven = Integer.parseInt(""+buttonObjectChoice2.getText());
            break;

            case R.id.buttonChoice3:
                //button 3 stuff
                answerGiven = Integer.parseInt(""+buttonObjectChoice3.getText());
            break;
        }
        //update score and level with the answer given
        updateScoreAndLevel(answerGiven);
    }
    void setQuestion(){
        //generate parts of the question
        int numberRange = currentLevel * 3;
        Random randInt = new Random();
        int partA = randInt.nextInt(numberRange);
        //dont want a zero value
        partA++;
        int partB = randInt.nextInt(numberRange);
        //dont want a zero value
        partB++;

        correctAnswer = partA * partB;
        int wrongAnswer1 = correctAnswer-2;
        int wrongAnswer2 = correctAnswer+2;
        textObjectPartA.setText(""+partA);
        textObjectPartB.setText(""+partB);

        //set the multichoice buttons using a switch statement
        int buttonLayout = randInt.nextInt(3);

        switch(buttonLayout){
            case 0:
                buttonObjectChoice1.setText(""+correctAnswer);
                buttonObjectChoice2.setText(""+wrongAnswer1);
                buttonObjectChoice3.setText(""+wrongAnswer2);
                break;

            case 1:
                buttonObjectChoice1.setText(""+wrongAnswer1);
                buttonObjectChoice2.setText(""+correctAnswer);
                buttonObjectChoice3.setText(""+wrongAnswer2);
                break;

            case 2:
                buttonObjectChoice1.setText(""+wrongAnswer2);
                buttonObjectChoice2.setText(""+wrongAnswer1);
                buttonObjectChoice3.setText(""+correctAnswer);
                break;
        }

    }
    void updateScoreAndLevel(int answerGiven){
        if(isCorrect(answerGiven)){
            for(int i=1; i<= currentLevel; i++){
                currentScore = currentScore + i;
            }
            currentLevel++;
        }else{
            currentScore = 0;
            currentLevel = 1;
        }
        //update the two views
        textObjectScore.setText("Score "+currentScore);
        textObjectLevel.setText("Level " + currentLevel);
        //after evaluation ask a new question
        setQuestion();
    }
    boolean isCorrect(int answerGiven){
        boolean correctTrueOrFalse;
        if(answerGiven == correctAnswer){
            Toast.makeText(getApplicationContext(),"Well done!",Toast.LENGTH_LONG).show();
            correctTrueOrFalse=true;
        }else {
            Toast.makeText(getApplicationContext(),"Sorry",Toast.LENGTH_LONG).show();
            correctTrueOrFalse=false;
        }
        return correctTrueOrFalse;
    }

}
