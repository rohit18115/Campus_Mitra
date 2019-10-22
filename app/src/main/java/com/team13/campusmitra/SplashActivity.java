package com.team13.campusmitra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        progressBar = findViewById(R.id.progressbar_splash_screen);
        Thread timer = new Thread() {

            public void run() {
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                try {
                    if(currentUser != null && currentUser.isEmailVerified()){
                        sleep(1000);

                        startActivity(new Intent(SplashActivity.this, UserProfile.class));
                    }else{
                        sleep(1000);

                        startActivity(new Intent(SplashActivity.this, FacultyExternalDisplayActivity.class));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        timer.start();

        progressBar.setVisibility(View.GONE);

    }


}

