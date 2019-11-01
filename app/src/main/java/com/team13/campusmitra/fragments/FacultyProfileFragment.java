package com.team13.campusmitra.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Faculty;
import com.team13.campusmitra.dataholder.OfficeHours;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.firebaseassistant.FirebaseFacultyHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseStudentHelper;

import java.util.ArrayList;

public class FacultyProfileFragment extends Fragment {

    FloatingActionButton fab;
    Animation rotateForward, rotateBackward;
    AppCompatTextView designation;
    AppCompatTextView department;
    AppCompatTextView officeHours;
    AppCompatTextView coursesTaken;
    AppCompatTextView domains;
    AppCompatTextView roomNo;
    //    TextInputEditText ETFName;
//    TextInputEditText ETUName;
//    TextInputEditText ETUname;
//    TextInputEditText ETOEmail;
    ProgressBar pb;

    protected void initComponent(View view) {
        designation = view.findViewById(R.id.ffp_desig);
        department = view.findViewById(R.id.ffp_dept);
        roomNo = view.findViewById(R.id.ffp_room);
        officeHours = view.findViewById(R.id.ffp_office_hours);
        coursesTaken = view.findViewById(R.id.ffp_course_taken);
        domains = view.findViewById(R.id.ffp_domains);
        pb = view.findViewById(R.id.fbp_pb);
        fab = view.findViewById(R.id.fbp_fab);
    }

    protected void loadData(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        FirebaseFacultyHelper helper = new FirebaseFacultyHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Faculty fac = snapshot.getValue(Faculty.class);
                    if(fac.getUserID().equals(uid)) {
                        Log.d("lololo", "onDataChange: " + fac.getRoomid());
                        designation.setText(fac.getDesignation());
                        department.setText(fac.getDepartment());
                        roomNo.setText(fac.getRoomNo());
                        OfficeHours of = fac.getOfficeHours();
                        String set = of.getDay() + " from " + of.getStartTime() + " to " + of.getEndTime() + " at " + of.getVenue();
                        officeHours.setText(set);
                        //ArrayList<String> s = fac.getDomains();
//                        String cor = "";
//                        for(int i =0;i<s.size();i++) {
//                            cor = cor + s.get(i) + "\n";
//                        }
                        String cor = fac.getDomains();
                        domains.setText(cor);
                        ArrayList<String> s = fac.getCoursesTaken();
                        cor = "";
                        for(int i =0;i<s.size();i++) {
                            cor = cor + s.get(i) + "\n";
                        }
                        coursesTaken.setText(cor);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void animateFab(){
        fab.startAnimation(rotateForward);
        fab.startAnimation(rotateBackward);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faculty_profile, container, false);

        initComponent(view);
        loadData(view);

        return view;
    }
}
