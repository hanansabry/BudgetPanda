package com.android.budgetpanda;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.budgetpanda.control.ControlActivity;
import com.android.budgetpanda.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-ServiceListActivity. */
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Intent mainIntent = new Intent(SplashActivity.this, ControlActivity.class);
                    startActivity(mainIntent);
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                }
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
