package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btSignIn;
    private TextInputEditText etEmail, etPswd;
    private String TAG = "Sign In Activity";
    private String email, pswd;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().setElevation(0);

        mAuth = FirebaseAuth.getInstance();

        //Initializing views
        TextView tvSignUp = findViewById(R.id.tv_sign_up);
        etEmail = findViewById(R.id.et_email);
        etPswd = findViewById(R.id.et_pswd);
        btSignIn = findViewById(R.id.btn_sign_in);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        //highlighting and creating link to the "sign up" text
        SpannableString spannableString =new SpannableString("No account? Sign Up here");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        }, 12, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignUp.setText(spannableString);
        tvSignUp.setMovementMethod(LinkMovementMethod.getInstance());

        //signIn button onclick listener
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    email = etEmail.getText().toString().trim();
                    pswd = etPswd.getText().toString().trim();
                }catch (NullPointerException e ){
                    Log.d(TAG, "Null pointer exception: ");
                }
                if(email.isEmpty()){
                    etEmail.setError("email cannot be empty");
                }
                else if(!email.contains("@iiitd.ac.in")){
                    etEmail.setError("Enter IIITD email only",null);
                    etEmail.requestFocus();
                }else if(pswd.isEmpty()){
                    etPswd.setError("Password cannot be empty", null);
                    etPswd.requestFocus();
                }else{
                    loginUser(email,pswd );
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }
    private void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            try{
                                throw task.getException();
                            }
                            catch(FirebaseAuthInvalidCredentialsException e) {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "email or password incorrect", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }catch (FirebaseTooManyRequestsException e){
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "too many invalid login Attempts please try later", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }

                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    void updateUI(final FirebaseUser user){
        if(user != null){
            if(user.isEmailVerified()) {
                Intent intent = new Intent(SignIn.this, UserProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "email not verified", Snackbar.LENGTH_LONG)
                        .setAction("Send Verification email", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                verifyEmail(user);
                            }
                        });
                snackbar.show();
            }
        }
    }

    void verifyEmail(final FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignIn.this,
                            "Verification email sent to " + user.getEmail(),
                            Toast.LENGTH_SHORT).show();

                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(SignIn.this,
                            "Failed to send verification email.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
