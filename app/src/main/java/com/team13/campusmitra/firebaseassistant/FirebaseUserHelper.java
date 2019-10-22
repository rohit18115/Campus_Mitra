package com.team13.campusmitra.firebaseassistant;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.User;

public class FirebaseUserHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseUserHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("users");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
    public void addUser(Context context, User user){
        String uid = user.getUserId();

        firebaseDatabase.child(uid).setValue(user);
        Toast.makeText(context,"User added successfully!!",Toast.LENGTH_SHORT).show();
    }
    public void updateUser(Context context, User user){
        String uid = user.getUserId();//.getCourseID();
        //course.setCourseID(uid);
        firebaseDatabase.child(uid).setValue(uid);
        Toast.makeText(context,"User updated successfully!!",Toast.LENGTH_SHORT).show();
    }
}
