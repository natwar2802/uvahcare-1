package com.example.socialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class splashScreen extends AppCompatActivity {
    private static int TIME_OUT = 3000;
    LottieAnimationView lottieAnimationView;
    int ch=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        lottieAnimationView=findViewById(R.id.splash);



        lottieAnimationView.animate().translationY(1400).setDuration(3000).setStartDelay(1000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);
    }




}