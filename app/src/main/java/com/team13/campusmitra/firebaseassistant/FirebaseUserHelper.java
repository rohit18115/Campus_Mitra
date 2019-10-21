package com.team13.campusmitra.firebaseassistant;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUserHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseUserHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("users");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
}
