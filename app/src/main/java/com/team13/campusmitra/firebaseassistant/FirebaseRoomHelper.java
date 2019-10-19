package com.team13.campusmitra.firebaseassistant;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRoomHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseRoomHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("room");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
}
