package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    TextInputEditText etEmail, etpswd, etcnfmpswd;
    TextView tvSignIn;
    Button btnSignUp;
    String TAG = "SIgn Up Activity";
    String email, pswd, cnfmpswd;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setElevation(0);

        etEmail = findViewById(R.id.et_email);
        etpswd = findViewById(R.id.et_pswd);
        etcnfmpswd = findViewById(R.id.et_cnfm_pswd);
        btnSignUp = findViewById(R.id.btn_sign_up);
        progressBar = findViewById(R.id.progressbar_signup);
        coordinatorLayout = findViewById(R.id.coordinator_layout_sign_up);
        tvSignIn = findViewById(R.id.tv_sign_in);
        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();

        SpannableString spannableString = new SpannableString("Already have an account? Sign In here");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                startActivity(intent);
            }
        }, 25, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignIn.setText(spannableString);
        tvSignIn.setMovementMethod(LinkMovementMethod.getInstance());

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    email = etEmail.getText().toString().trim();
                    pswd = etpswd.getText().toString().trim();
                    cnfmpswd = etcnfmpswd.getText().toString().trim();
                } catch (NullPointerException e) {
                    Log.d(TAG, " Null pointer exception ");
                }

                if (email.isEmpty()) {
                    etEmail.setError("email cannot be empty", null);
                } else if (!email.contains("@iiitd.ac.in")) {
                    etEmail.setError("enter your iiitd email", null);
                } else if (pswd.isEmpty()) {
                    etpswd.setError("please enter valid password", null);
                } else if (!pswd.equals(cnfmpswd)) {
                    etcnfmpswd.setError("password do not match", null);
                } else {
                    createAccount(email, pswd);
                    hideKeyBoard();
                }
            }
        });

    }

    void createAccount(String email, String password) {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            verifyEmail(user);
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Verification email sent to" + user.getEmail(), Snackbar.LENGTH_SHORT);
                            snackbar.addCallback(new Snackbar.Callback() {

                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    //see Snackbar.Callback docs for event details
                                    mAuth.signOut();
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(new Intent(SignUp.this, SignIn.class));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            try {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "User Already Exists", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Password should be at least 6 characters", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            progressBar.setVisibility(View.GONE);
                            /*Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();*/
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

    public void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
