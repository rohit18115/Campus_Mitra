package com.team13.campusmitra.firebaseassistant;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.TimeTableElement;

public class FirebaseTimeTableHelper {
    private DatabaseReference firebaseDatabase;

    public FirebaseTimeTableHelper(){
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("timetable");
    }
    public DatabaseReference getReference(){
        return  firebaseDatabase;
    }
    public void addTimeTable(Context context, TimeTableElement element){
        String uid = firebaseDatabase.push().getKey();
        element.setTimeTableID(uid);
        firebaseDatabase.child(uid).setValue(element);
        Toast.makeText(context,"Timetable Record added successfully!!",Toast.LENGTH_SHORT).show();
    }
    public void updateTimeTable(Context context, TimeTableElement element){
        String uid = element.getTimeTableID();
        element.setTimeTableID(uid);
        firebaseDatabase.child(uid).setValue(element);
        Toast.makeText(context,"Timetable Record updated successfully!!",Toast.LENGTH_SHORT).show();
    }


}
