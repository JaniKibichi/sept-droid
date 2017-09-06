package com.mjuaji.tappydefenderv1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{
    //the entry point to our game
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //load fastest time
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("HiScores", MODE_PRIVATE);

        //get reference to TextView in our layout
        final TextView textFastestTime = (TextView)findViewById(R.id.textHighScore);
        //load fastestTime available or a default of 1000000
        long fastestTime = prefs.getLong("fastestTime", 1000000);
        //put high score in textView
        textFastestTime.setText("Fastest Time:" + fastestTime);

        //reference to our button
        final Button buttonPlay = (Button)findViewById(R.id.buttonPlay);
        //listen for clicks
        buttonPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
     //on click,must be the play button, implement Intent object
     Intent i = new Intent(this, GameActivity.class);
     //start Game Activity via the Intent
     startActivity(i);
     //shut this activity down
     finish();
    }
}
