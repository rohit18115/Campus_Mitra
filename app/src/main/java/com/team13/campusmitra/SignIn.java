package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.opengl.Visibility;
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
import android.widget.ProgressBar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.dataholder.EmailHolder;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseApprovFacultyHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.util.ArrayList;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btSignIn;
    private TextInputEditText etEmail, etPswd;
    private String TAG = "Sign In Activity";
    private String email, pswd;
    private ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;

    ArrayList<EmailHolder> emailHolders ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().setElevation(0);

        mAuth = FirebaseAuth.getInstance();

        //Initializing views
        TextView tvSignUp = findViewById(R.id.tv_sign_up);
        TextView tvForgotPswd = findViewById(R.id.tv_forgot_password);
        etEmail = findViewById(R.id.et_email);
        etPswd = findViewById(R.id.et_pswd);
        btSignIn = findViewById(R.id.btn_sign_in);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        progressBar = findViewById(R.id.progressbar_signin);
        emailHolders =new ArrayList<>();
        //highlighting and creating link to the "sign up" text
        SpannableString spannableString = new SpannableString("No account? Sign Up here");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                startActivity(intent);
            }
        }, 12, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignUp.setText(spannableString);
        tvSignUp.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString spannableStringForgotPswd = new SpannableString("Forgot Password");
        spannableStringForgotPswd.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                String forgotpswdemail = etEmail.getText().toString().trim();
                hideKeyBoard();
                if (forgotpswdemail.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Enter email to reset password", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    etEmail.setError("Enter email to reset password");
                } else if (!forgotpswdemail.contains("@iiitd.ac.in")) {
                    etEmail.setError("Enter IIITD email only ");
                } else {
                    forgotPswd(forgotpswdemail);
                }
            }
        }, 0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvForgotPswd.setText(spannableStringForgotPswd);
        tvForgotPswd.setMovementMethod(LinkMovementMethod.getInstance());

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (hasWindowFocus()) {
                    etEmail.setError(null);
                }
            }
        });
        //signIn button onclick listener
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    email = etEmail.getText().toString().trim();
                    pswd = etPswd.getText().toString().trim();
                } catch (NullPointerException e) {
                    Log.d(TAG, "Null pointer exception: ");
                }
                if (email.isEmpty()) {
                    etEmail.setError("email cannot be empty");
                } else if (!email.contains("@iiitd.ac.in")) {
                    etEmail.setError("Enter IIITD email only", null);
                    etEmail.requestFocus();
                    hideKeyBoard();
                } else if (pswd.isEmpty()) {
                    etPswd.setError("Password cannot be empty", null);
                    etPswd.requestFocus();
                } else {
                    if (!CheckInternet(getApplicationContext())) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet Connection", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        hideKeyBoard();
                    } else {
                        loginUser(email, pswd);
                       // hideKeyBoard();
                    }

                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        loadFacultyEmail();
    }
    private void loadFacultyEmail(){
        FirebaseApprovFacultyHelper facultyHelper = new FirebaseApprovFacultyHelper();
        facultyHelper.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emailHolders.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    EmailHolder h = snapshot.getValue(EmailHolder.class);
                    emailHolders.add(h);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loginUser(String email, String password) {
        if(email.equals("admin@iiitd.ac.in")&&password.equals("admin@123")){
            Intent intent = new Intent(getApplicationContext(),DashboardAdmin.class);
            startActivity(intent);
            finish();
            return;
        }
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressBar.setVisibility(View.GONE);
                            updateUI(user);
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                progressBar.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "email or password incorrect", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } catch (FirebaseTooManyRequestsException e) {
                                progressBar.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "too many invalid login Attempts please try later", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            progressBar.setVisibility(View.GONE);
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    void updateUI(final FirebaseUser user) {
        if (user != null) {
            if (user.isEmailVerified()) {

                FirebaseUserHelper helper = new FirebaseUserHelper();
                helper.getReference().child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User myuser = dataSnapshot.getValue(User.class);
                        if(myuser==null){
                            Intent intent = new Intent(SignIn.this, UserProfile.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        else if(myuser.getProfileCompleteCount()==1){
                            if(myuser.getUserType()==0){
                                Intent intent = new Intent(SignIn.this, StudentProfile.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                            else{
                                String em = user.getEmail();
                                boolean flag = false;
                                for(EmailHolder h :emailHolders){
                                    if(em.equals(h.getEmail())){
                                        flag=true;
                                        break;
                                    }
                                }
                                if(flag) {
                                    Intent intent = new Intent(SignIn.this, FacultyProfile.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"You are not Approved as faculty contact admin",Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                        else{
                            if(myuser.getUserType()==0){
                                Intent intent = new Intent(SignIn.this,NewDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(SignIn.this, DashboardProfessor.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            Toast.makeText(getApplicationContext(),"Nothing to login",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
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
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignIn.this,
                            "Verification email sent to " + user.getEmail(),
                            Toast.LENGTH_SHORT).show();

                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(SignIn.this,
                            "Failed to send verification email.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean CheckInternet(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return wifi.isConnected() || mobile.isConnected();
    }

    public void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    void forgotPswd(final String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Reset password link send to" + email, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Log.d(TAG, "onFailure: " + task.getException());
                }
            }
        });
    }
}
