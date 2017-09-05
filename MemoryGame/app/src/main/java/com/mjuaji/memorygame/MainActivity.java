package com.mjuaji.memorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //member variables
    SharedPreferences prefs;
    String dataName = "MyData";
    String intName = "MyInt";
    int defaultInt = 0;

    //both activities can see this
    public static int hiScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //initialize objects, read from our file, set the result to hiScore SharedPreferences objects
        prefs = getSharedPreferences(dataName, MODE_PRIVATE);

    //Either load our high score or if not available our default of 0
        hiScore = prefs.getInt(intName, defaultInt);

    //Make a reference to the Hiscore textview in our layout
        TextView textHiScore =(TextView) findViewById(R.id.textHiScore);

    //Display the hi score
        textHiScore.setText("Hi: "+hiScore);

    //Make a button from the button in our layout
        Button button = (Button) findViewById(R.id.button);

    //Make it listen for clicks
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //->OnClick()
        Intent i;
        i = new Intent(this, GameActivity.class);
        startActivity(i);
    }
}
