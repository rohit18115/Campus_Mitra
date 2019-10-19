package com.team13.campusmitra.actions;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignupAction {
    private Context context;
    private String email;
    private String password;
    private FirebaseAuth firebaseAuth;
    private AppCompatActivity activity;
    public SignupAction(Context context, String email, String password,AppCompatActivity activity) {
        this.context = context;
        this.email = email;
        this.password = password;
        this.activity = activity;
        firebaseAuth = FirebaseAuth.getInstance();
    }
    static int signupFlag = -1;

    public int doSignup(){
        signupFlag =-1;
        firebaseAuth.createUserWithEmailAndPassword(this.email,this.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context,"Signed Up successfully Email verification has been Sent!!",Toast.LENGTH_LONG).show();
                    final FirebaseUser user= firebaseAuth.getCurrentUser();
                    user.sendEmailVerification();
                    signupFlag =  0;
                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        //This -2 signifies that the user already exists
                        signupFlag =-2;

                    }
                }
            }
        });
        return signupFlag;
    }
}
