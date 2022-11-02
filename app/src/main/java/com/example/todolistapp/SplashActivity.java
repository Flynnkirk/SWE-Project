package com.example.todolistapp;
import android.os.Handler;
import android.content.Intent;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;



public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Hiding the top bar of the screen to make the application full screen
        //To do this, call the parent class (AppCompatActivity) method of getSupportActionBar()
        getSupportActionBar().hide();
        //Intent messaging object is used to connect to another app component (this is for the main activity which we want to start)
        final Intent i = new Intent(SplashActivity.this, MainActivity.class);
        //We want to show the SplashActivity for a few seconds before switching to the to do list screen main activity
        //Below, the handler handles messages and runnable objects which are sent to a message loop. Looper associates the loop with a thread.
        //postDelayed() method adds a Runnable object to the message queue to be run after a few seconds.

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
          }, 1000);
    }
}