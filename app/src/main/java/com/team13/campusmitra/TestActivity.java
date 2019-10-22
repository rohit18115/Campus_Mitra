package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.actions.LoginAction;
import com.team13.campusmitra.actions.SignupAction;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    Button testButton;

    DatabaseReference databaseReference;
    DatabaseReference courseRefrence;
    ArrayList<Room> rooms;
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
                //doLogin("nikhilgola21@gmail.com","12345678");
                //addRoom();
                //launchOCR();
                addTT();
               // addCourse();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        rooms = new ArrayList<>();
        FirebaseRoomHelper helper = new FirebaseRoomHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rooms.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Room room = snapshot.getValue(Room.class);
                    rooms.add(room);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void doSignup(String email, String pass){
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
    private void addRoom(){
        System.out.println(rooms);
        Room room = new Room();
        room.setCapacity(30);
        room.setRoomNumber("A-519");
        room.setRoomBuilding("Rnd Block");
        room.setRoomType(0);
        FirebaseRoomHelper helper = new FirebaseRoomHelper();
        helper.addRoom(this,room);
        System.out.println(rooms);
    }
    private void addCourse(){

         Course c = new Course("CSE-536", "Machine Learning(PG)", "Probability and Stats", "Monsoon") ;
        FirebaseCoursesHelper helper = new FirebaseCoursesHelper();
        helper.addCourse(this,c);
    }
    private void launchOCR(){
        Intent intent = new Intent(this,OCRActivity.class);
        startActivity(intent);
        finish();
    }
    private void addTT(){
        Intent intent = new Intent(this,AddCourseActivity.class);
        startActivity(intent);
        finish();
    }



}
