package com.example.socialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashScreen extends AppCompatActivity {
    private static int TIME_OUT = 2000;
    public static int chsplash=0;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        lottieAnimationView=findViewById(R.id.splash);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //MainActivity call=new MainActivity();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        MainActivity.callMain();





        lottieAnimationView.animate().translationY(-800).setDuration(1000).setStartDelay(0000);

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(splashScreen.this,
                        MainActivity.class);
                splashScreen.this.startActivity(mainIntent);
                splashScreen.this.finish();
                handler.removeCallbacks(this);
            }
        }, TIME_OUT);

    }





}