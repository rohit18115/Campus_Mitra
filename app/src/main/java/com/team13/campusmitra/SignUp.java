package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity{

    TextInputEditText etEmail, etpswd, etcnfmpswd;
    Button btnSignUp;
    String TAG = "SIgn Up Activity";
    String email, pswd, cnfmpswd;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = findViewById(R.id.et_email);
        etpswd = findViewById(R.id.et_pswd);
        etcnfmpswd = findViewById(R.id.et_cnfm_pswd);
        btnSignUp = findViewById(R.id.btn_sign_up);

        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    email = etEmail.getText().toString().trim();
                    pswd = etpswd.getText().toString().trim();
                    cnfmpswd = etcnfmpswd.getText().toString().trim();
                }catch (NullPointerException e){
                    Log.d(TAG, " Null pointer exception ");
                }

                if(email.isEmpty()){
                    etEmail.setError("email cannot be empty", null);
                }else if(!email.contains("@iiitd.ac.in")){
                    etEmail.setError("enter your iiitd email", null);
                }
                else if(pswd.isEmpty()){
                    etpswd.setError("please enter valid password", null);
                }else if(!pswd.equals(cnfmpswd)){
                    etcnfmpswd.setError("password do not match", null);
                }else{
                    createAccount(email, pswd);
                }
            }
        });

    }

    void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            verifyEmail(user);
                            mAuth.signOut();
                            startActivity(new Intent(SignUp.this, SignIn.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    void verifyEmail(final FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this,
                            "Verification email sent to " + user.getEmail(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(SignUp.this,
                            "Failed to send verification email.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
