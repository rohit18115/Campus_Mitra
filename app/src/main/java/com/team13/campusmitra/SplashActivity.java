package com.team13.campusmitra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.dataholder.EmailHolder;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseApprovFacultyHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    ArrayList<EmailHolder> emailHolders ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        emailHolders = new ArrayList<>();
        loadFacultyEmail();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        progressBar = findViewById(R.id.progressbar_splash_screen);
        Thread timer = new Thread() {
            public void run() {
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                try {
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
                                else if(myuser.getProfileCompleteCount()==2){
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
                        sleep(5000);
//=============================================================================================================
                    }else{
                        sleep(1000);

                        startActivity(new Intent(SplashActivity.this, SignIn.class));
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


}

