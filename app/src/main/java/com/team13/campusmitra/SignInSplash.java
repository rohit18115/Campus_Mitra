package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

public class SignInSplash extends AppCompatActivity {

    final static  String TAG="LOGIN_FAILURE";
    RelativeLayout rellay1, rellay2;
    private FirebaseAuth mAuth;
    //private ProgressBar progressBar;
    ArrayList<EmailHolder> emailHolders ;

    String email,pswd;
    Button signupbtn,forgotPasswordbtn;
    RelativeLayout coordinatorLayout;
    EditText etEmail,etPassword;
    private AppCompatCheckBox checkbox;
    Button loginbtn;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            launchActivity();
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_splash);

        //Mukul added this
//        startActivity(new Intent(this,OpenDialogue.class));

        checkbox = (AppCompatCheckBox) findViewById(R.id.checkbox);
        etPassword = (EditText) findViewById(R.id.et_signin_pswd);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        emailHolders = new ArrayList<>();
        loadFacultyEmail();
        //progressBar = findViewById(R.id.splash_progressbar);
        //progressBar.setVisibility(View.VISIBLE);
        coordinatorLayout = findViewById(R.id.rl1);
        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        initComponents();
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

    }
   private void initComponents(){
        etEmail = findViewById(R.id.et_signin_email);
        etPassword = findViewById(R.id.et_signin_pswd);
        loginbtn = findViewById(R.id.btn_signin);
        getSupportActionBar().hide();
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    email = etEmail.getText().toString().trim();
                    pswd = etPassword.getText().toString().trim();
                } catch (NullPointerException e) {
                    Log.d(TAG, "Null pointer exception: ");
                }
                if (email.isEmpty()) {
                    etEmail.setError("email cannot be empty");
                } else if (!email.contains("@iiitd.ac.in")) {
                    etEmail.setError("Enter IIITD email only", null);
                    etEmail.requestFocus();
                 //   hideKeyBoard();
                } else if (pswd.isEmpty()) {
                    etPassword.setError("Password cannot be empty", null);
                    etPassword.requestFocus();
                } else {
                    if (!CheckInternet(getApplicationContext())) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet Connection", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    //    hideKeyBoard();
                    } else {
                        //hideKeyBoard();
                        loginUser(email, pswd);

                    }

                }

            }

        });
       etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View view, boolean b) {
               if (hasWindowFocus()) {
                   etEmail.setError(null);
               }
           }
       });

        signupbtn = findViewById(R.id.tv_sign_up);
        forgotPasswordbtn = findViewById(R.id.tv_forgot_password);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignInSplash.this, SignUp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });

        forgotPasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String forgotpswdemail = etEmail.getText().toString().trim();
                //hideKeyBoard();
                if (forgotpswdemail.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Enter email to reset password", Snackbar.LENGTH_SHORT);
                    //setTextColor not working
                    snackbar.setActionTextColor(Color.WHITE);
                    snackbar.show();
                    etEmail.setError("Enter email to reset password");
                    etEmail.requestFocus();
                } else if (!forgotpswdemail.contains("@iiitd.ac.in")) {
                    etEmail.setError("Enter IIITD email only ");
                } else {
                    forgotPswd(forgotpswdemail);
                }

            }
        });



    }
    public void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(SignInSplash.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
        //progressBar.setIndeterminate(true);
        //progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //progressBar.setVisibility(View.GONE);
                            updateUI(user);
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                //progressBar.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "email or password incorrect", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } catch (FirebaseTooManyRequestsException e) {
                                //progressBar.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "too many invalid login Attempts please try later", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            //progressBar.setVisibility(View.GONE);
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void launchActivity(){
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null && currentUser.isEmailVerified()){

            final FirebaseUser user= currentUser;
//=============================================================================================================
            //startActivity(new Intent(SplashActivity.this, DashboardAdmin.class));
            FirebaseUserHelper helper = new FirebaseUserHelper();
            helper.getReference().child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User myuser = dataSnapshot.getValue(User.class);
                    if(myuser==null){
                        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                    else if(myuser.getProfileCompleteCount()==0) {
                        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else if(myuser.getProfileCompleteCount()==1){
                        if(myuser.getUserType()==0){
                            Intent intent = new Intent(getApplicationContext(), StudentProfile.class);
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
                                Intent intent = new Intent(getApplicationContext(), FacultyProfile.class);
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
                            Intent intent = new Intent(getApplicationContext(),NewDashboard.class);
                            intent.putExtra("MYKEY",myuser);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(), DashboardProfessor.class);
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
//=============================================================================================================
        }else{

            //progressBar.setVisibility(View.GONE);
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    }


    void verifyEmail(final FirebaseUser user) {
        //progressBar.setIndeterminate(true);
        //progressBar.setVisibility(View.VISIBLE);
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),
                            "Verification email sent to " + user.getEmail(),
                            Toast.LENGTH_SHORT).show();

                } else {
                    //progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(getApplicationContext(),
                            "Failed to send verification email.",
                            Toast.LENGTH_SHORT).show();
                }
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
                            Intent intent = new Intent(SignInSplash.this, UserProfile.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        else if(myuser.getProfileCompleteCount()==2){
                            if(myuser.getUserType()==0){
                                Intent intent = new Intent(SignInSplash.this, StudentProfile.class);
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
                                    Intent intent = new Intent(SignInSplash.this, FacultyProfile.class);
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
                                Intent intent = new Intent(SignInSplash.this,NewDashboard.class);
                                intent.putExtra("MYKEY",myuser);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(SignInSplash.this, DashboardProfessor.class);
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
    public static boolean CheckInternet(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return wifi.isConnected() || mobile.isConnected();
    }


}
