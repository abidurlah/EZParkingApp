package com.example.ezparkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.ezparkingapp.activities.HomeActivity;
import com.example.ezparkingapp.activities.auth.SignInOrSignUpActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    sendUserHomeActivity();
                } else {
                    sendUserToLoginActivity();
                }
            }
        }, SPLASH_SCREEN_TIME);
    }
    private void sendUserHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void sendUserToLoginActivity() {
        startActivity(new Intent(this, SignInOrSignUpActivity.class));
        finish();
    }
}