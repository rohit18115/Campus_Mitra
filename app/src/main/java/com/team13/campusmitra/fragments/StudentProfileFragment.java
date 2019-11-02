package com.team13.campusmitra.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.firebaseassistant.FirebaseStudentHelper;

import java.util.ArrayList;

public class StudentProfileFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton fab;
    Animation rotateForward, rotateBackward;
    AppCompatTextView rollNo;
    AppCompatTextView department;
    AppCompatTextView enrollCourse;
    AppCompatTextView coursesTaken;
    AppCompatTextView interests;
    Button resume;
    String url;
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
        resume = view.findViewById(R.id.fsp_resume);
        pb = view.findViewById(R.id.fbp_pb);
        fab = view.findViewById(R.id.fbp_fab);
    }

    protected void loadData(View view) {
        Log.d("lolo", "on loaddata");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        FirebaseStudentHelper helper = new FirebaseStudentHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (getActivity() == null) {
                        return;
                    }
                    Student student = snapshot.getValue(Student.class);
                    if (student.getUserID().equals(uid)) {
                        Log.d("lololo", "onDataChange: " + student.getRollNumber());
                        rollNo.setText(student.getRollNumber());
                        department.setText(student.getDepartment());
                        enrollCourse.setText(student.getEnrollCourse());
                        interests.setText(student.getAreaOfInterest());
                        ArrayList<String> s = student.getCourses();
                        String cor = "";
                        if (s != null) {
                            for (int i = 0; i < s.size(); i++) {
                                cor = cor + s.get(i) + "\n";
                            }
                        }
                        coursesTaken.setText(cor);
                        url = student.getResumeURL();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void animateFab() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fsp_resume:
                if (url != null && !url.isEmpty()) {
                    if (!url.contains("http://"))
                        url = "http://" + url;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "No Resume Uploaded", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

