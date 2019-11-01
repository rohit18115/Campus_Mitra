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

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseStudentHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfileFragment extends Fragment {

    FloatingActionButton fab;
    Animation rotateForward, rotateBackward;
    AppCompatTextView rollNo;
    AppCompatTextView department;
    AppCompatTextView enrollCourse;
    AppCompatTextView coursesTaken;
    AppCompatTextView interests;
//    TextInputEditText ETFName;
//    TextInputEditText ETUName;
//    TextInputEditText ETUname;
//    TextInputEditText ETOEmail;
    ProgressBar pb;

    protected void initComponent(View view) {
        rollNo = view.findViewById(R.id.fsp_roll);
        department = view.findViewById(R.id.fsp_dept);
        enrollCourse = view.findViewById(R.id.fsp_course);
        coursesTaken = view.findViewById(R.id.fsp_course_taken);
        interests = view.findViewById(R.id.fsp_interests);
        pb = view.findViewById(R.id.fbp_pb);
        fab = view.findViewById(R.id.fbp_fab);
    }

    protected void loadData(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        FirebaseStudentHelper helper = new FirebaseStudentHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Student student = snapshot.getValue(Student.class);
                    if(student.getUserID().equals(uid)) {
                        Log.d("lololo", "onDataChange: " + student.getRollNumber());
                        rollNo.setText(student.getRollNumber());
                        department.setText(student.getDepartment());
                        enrollCourse.setText(student.getEnrollCourse());
                        interests.setText(student.getAreaOfInterest());
                        ArrayList<String> s = student.getCourses();
                        String cor = "";
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);

        initComponent(view);
        loadData(view);

        return view;
    }
}
