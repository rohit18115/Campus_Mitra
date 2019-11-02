package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseStudentHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentExternalDisplay extends AppCompatActivity implements View.OnClickListener {

    CircleImageView image;
    AppCompatTextView name;
    AppCompatTextView dept;
    AppCompatTextView email;
    AppCompatTextView courses;
    AppCompatTextView interests;
    String url;
    ProgressBar pb;
    Button resume;

    private String userId;

    protected void initComponent() {
        image = findViewById(R.id.ase_profile_image);
        name = findViewById(R.id.ase_profile_name);
        dept = findViewById(R.id.ase_department);
        email = findViewById(R.id.ase_email_id);
        courses = findViewById(R.id.ase_enrolled_courses);
        interests = findViewById(R.id.ase_interests);
        pb = findViewById(R.id.ase_pb);
        resume = findViewById(R.id.ase_resume);
    }

    protected void loadData() {
        FirebaseUserHelper helper = new FirebaseUserHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(user.getUserId().equals(userId)) {
                        Log.d("lololo", "onDataChange: " + user.getUserLastName());
                        String gender = user.getGender();
                        if(gender.equals("Male")) {
                            Glide.with(StudentExternalDisplay.this)
                                    .asBitmap()
                                    .load(user.getImageUrl())
                                    .placeholder(R.drawable.blankboy)
                                    .into(image);
                        }
                        else{
                            Glide.with(StudentExternalDisplay.this)
                                    .asBitmap()
                                    .load(user.getImageUrl())
                                    .placeholder(R.drawable.blankgirl)
                                    .into(image);
                        }
                        name.setText(user.getUserFirstName() + " " + user.getUserLastName());
                        email.setText(user.getUserEmail());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        FirebaseStudentHelper helper2 = new FirebaseStudentHelper();
        DatabaseReference reference2 = helper2.getReference();
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Student student = snapshot.getValue(Student.class);
                    if(student.getUserID().equals(userId)) {
                        Log.d("lololo", "onDataChange: " + student.getUserID());
                        String dep = "", cor = "", dom = "";
                        dep = student.getDepartment();
                        dom = student.getAreaOfInterest();
                        ArrayList<String> s = student.getCourses();
                        if(s!=null) {
                        cor = "";
                        for(int i =0;i<s.size();i++) {
                            cor = cor + s.get(i) + "\n";
                        }
                        if(!cor.isEmpty()) {
                            courses.setText(cor);
                        }}
                        if(!dep.isEmpty()) {
                            dept.setText(dep);
                        }
                        if(!dom.isEmpty()) {
                            interests.setText(dom);
                        }
                        url = student.getResumeURL();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_external_display);
        if (savedInstanceState == null) {
            Bundle id = getIntent().getExtras();
            if(id == null) {
                Toast toast = Toast.makeText(this, "Error: No UserID found", Toast.LENGTH_SHORT);
                toast.show();
            } else {
               userId = id.getString("UserID");
            }
        }
        initComponent();
        loadData();
        resume.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ase_resume:
                if(url!=null && !url.isEmpty()) {
                    if(!url.contains("http://"))
                        url = "http://"+url;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    Toast.makeText(this, "No Resume Uploaded", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
