package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button sign_up_button = findViewById(R.id.signup_button);
        Button sign_in_button = findViewById(R.id.signin_button);

        sign_in_button.setOnClickListener(this);
        sign_up_button.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_button:
                Intent intent1 = new Intent(this, SignIn.class);
                startActivity(intent1);
                break;

            case R.id.signup_button:

                break;
        }
    }
}
