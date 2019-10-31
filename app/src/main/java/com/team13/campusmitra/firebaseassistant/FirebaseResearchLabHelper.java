package com.team13.campusmitra.firebaseassistant;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.ResearchLab;

public class FirebaseResearchLabHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseResearchLabHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("researchlab");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }

    public void addResearchLab(Context context, ResearchLab rl){
        String uid = firebaseDatabase.push().getKey();
        rl.setRoomID(uid);
        firebaseDatabase.child(uid).setValue(rl);
        Toast.makeText(context,"ResearchLab added successfully!!",Toast.LENGTH_SHORT).show();
    }
    public void updateReseachLab(Context context, ResearchLab rl){
        String uid = rl.getRoomID();
        firebaseDatabase.child(uid).setValue(rl);
        Toast.makeText(context,"Course updated successfully!!",Toast.LENGTH_SHORT).show();
    }
}
