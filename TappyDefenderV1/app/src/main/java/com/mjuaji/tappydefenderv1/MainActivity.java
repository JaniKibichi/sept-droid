package com.mjuaji.tappydefenderv1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
