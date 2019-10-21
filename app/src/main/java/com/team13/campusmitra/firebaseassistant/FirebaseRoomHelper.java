package com.team13.campusmitra.firebaseassistant;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team13.campusmitra.dataholder.Room;

public class FirebaseRoomHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseRoomHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("room");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
    public void addRoom(Context context, Room room){
        String uid = firebaseDatabase.push().getKey();
        room.setRoomID(uid);
        firebaseDatabase.child(uid).setValue(room);
        Toast.makeText(context,"Room added successfully!!",Toast.LENGTH_SHORT).show();
    }
}
