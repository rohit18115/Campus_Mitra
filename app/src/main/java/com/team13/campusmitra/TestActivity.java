package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team13.campusmitra.actions.LoginAction;
import com.team13.campusmitra.actions.SignupAction;
import com.team13.campusmitra.dataholder.Student;

public class TestActivity extends AppCompatActivity {

    Button testButton;
    DatabaseReference databaseReference;
    DatabaseReference courseRefrence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        testButton = findViewById(R.id.test_btn);
        databaseReference = FirebaseDatabase.getInstance().getReference("courses");
        //courseRefrence = FirebaseDatabase.getInstance().getReference("courses");

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insert("nikks95","nikks@gmail.com","Mt18129",0);
                //doSignup("nikhilgola21@gmail.com","12345678");
                doLogin("nikhilgola21@gmail.com","12345678");
            }
        });
    }

    private void doSignup(String email,String pass){
        SignupAction action = new SignupAction(this,email,pass,this);
        int i = action.doSignup();
    }
    private void doLogin(String email,String pass){
        LoginAction action = new LoginAction(this,email,pass,this);

        action.doLcgin();


    }
    private void insert(String username, String useremail, String rollnum,int utype){
       // String uid = databaseReference.push().getKey();
       // Student student = new Student(uid,username,useremail,"2",rollnum);
        //databaseReference.child(uid).setValue(student);
        Toast.makeText(this,"Added Success",Toast.LENGTH_LONG).show();
    }

}
