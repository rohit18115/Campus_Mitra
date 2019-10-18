package com.team13.campusmitra.actions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.team13.campusmitra.R;

public class LoginAction{
    private Context context;
    private AppCompatActivity activity;
    private String email;
    private String password;
    public static  int loginFlag=-1;
    final private FirebaseAuth firebaseAuth;
    public LoginAction(Context context, String email, String password,AppCompatActivity activity) {
        this.context = context;
        this.email = email;
        this.password = password;
        this.activity = activity;
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void emailDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater= activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_email_verification,null);
        alertDialog.setView(view);
        alertDialog.setTitle("Email Verification Required");
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
    public void doLcgin(){

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if(!user.isEmailVerified()){
                        // The login Error code is for the Email Not Verified
                        loginFlag = -2;
                        user.sendEmailVerification();
                        Toast.makeText(context,"Email Verification link has been sent again",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        emailDialog();

                    }
                    else{
                            loginFlag = 0;
                        Toast.makeText(context,"Yes",Toast.LENGTH_SHORT).show();

                    }
                }
                else{

                }
            }
        });

    }
}
