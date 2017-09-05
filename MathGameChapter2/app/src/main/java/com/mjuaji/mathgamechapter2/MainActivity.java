package com.mjuaji.mathgamechapter2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //access our relevant UI object
        Button buttonPlay = (Button)findViewById(R.id.buttonPlay);

        //give the button the ability to listen to the user clicking on it
        buttonPlay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        i = new Intent(this, GameActivity.class);
        startActivity(i);
    }
}
