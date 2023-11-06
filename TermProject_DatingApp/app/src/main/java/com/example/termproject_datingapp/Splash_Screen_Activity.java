package com.example.termproject_datingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class Splash_Screen_Activity extends AppCompatActivity {

    private TextView app_name_1;
    private TextView app_name_2;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declare the values
        app_name_1 = findViewById(R.id.app_name_1);
        app_name_2 = findViewById(R.id.app_name_2);
        lottieAnimationView = findViewById(R.id.lottie);

        //animation that drops down after 5 seconds
        //and moves to next activity - onBoarding activity
        app_name_1.animate().translationY(1400).setDuration(1000).setStartDelay(4800);
        app_name_2.animate().translationY(1400).setDuration(1000).setStartDelay(4800);
        lottieAnimationView.animate().translationY(1400).setDuration(1000).setStartDelay(4800);

        //creates a delay of 5 seconds and then moves to next activity
        //onBoarding is next activity, which is the information page about the app
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //create onBoarding part, Three pages
                //messaging aspect
                //swiping aspect
                //creating account aspect
                //POINTS TO NEXT ACTIVITY
                //startActivity(new Intent(Splash_Screen_Activity.this, onBoarding.class));
                finish();
            }
        }, 5000);
    }
}