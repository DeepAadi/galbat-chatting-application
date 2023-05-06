package com.example.galbaat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashscreen.this,MainActivity.class);
                // this mean we tell that move us from splash activity to main activity

                // know we start the intent
                startActivity(intent);
                // know finish it
                finish();
            }
        },1700);// here we will show after how much time we take to go from splash acitvity to main activity.class
    }
}
// intent is basically used to go from one activity to another activity


