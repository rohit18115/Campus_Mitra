package com.team13.campusmitra.firebaseassistant;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseProjectHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseProjectHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("project");
    }
    
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
}
