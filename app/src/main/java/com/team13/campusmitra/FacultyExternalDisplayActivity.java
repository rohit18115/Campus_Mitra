package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.dataholder.Faculty;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseFacultyHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseStudentHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacultyExternalDisplayActivity extends AppCompatActivity implements View.OnClickListener{

    CircleImageView image;
    AppCompatTextView name;
    AppCompatTextView dept;
    AppCompatTextView email;
    AppCompatTextView courses;
    AppCompatTextView domain;
    Button book;
    //ProgressBar pb;

    private String userId;

    protected void initComponent() {
        image = findViewById(R.id.afe_profile_image);
        name = findViewById(R.id.afe_profile_name);
        dept = findViewById(R.id.afe_department);
        email = findViewById(R.id.afe_email_id);
        courses = findViewById(R.id.afe_courses);
        domain = findViewById(R.id.afe_domains);
        book = findViewById(R.id.afe_appointment);
        //pb = findViewById(R.id.afe_pb);
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
                        Glide.with(FacultyExternalDisplayActivity.this)
                                .asBitmap()
                                .load(user.getImageUrl())
                                .placeholder(R.drawable.ic_loading)
                                .into(image);
                        name.setText(user.getUserFirstName() + " " + user.getUserLastName());
                        email.setText(user.getUserEmail());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        FirebaseFacultyHelper helper2 = new FirebaseFacultyHelper();
        DatabaseReference reference2 = helper2.getReference();
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Faculty faculty = snapshot.getValue(Faculty.class);
                    if(faculty.getUserID().equals(userId)) {
                        Log.d("lololo", "onDataChange: " + faculty.getUserID());
                        String dep = "", cor = "", dom = "";
                        dep = faculty.getDepartment();
                        dom = faculty.getDomains();
                        ArrayList<String> s = faculty.getCoursesTaken();
                        if(s!=null) {
                        cor = "";
                        for(int i =0;i<s.size();i++) {
                            cor = cor + s.get(i) + "\n";
                        } }
                        if(!dep.isEmpty()) {
                            dept.setText(dep);
                        }
                        if(!dom.isEmpty()) {
                            domain.setText(dom);
                        }
                        if(!cor.isEmpty()) {
                            courses.setText(cor);
                        }
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
        setContentView(R.layout.activity_faculty_external_display);
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
        book.setOnClickListener(this);
    }

    public void BookAppointment(View view) {
        BookingDialogue bookingDialogue = new BookingDialogue(BookingDialogue.APPOINTMENT);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user1ID = auth.getCurrentUser().getUid();
        String user2ID=userId; /**/
        bookingDialogue.setAppointmentDetails(user1ID,user2ID);
        bookingDialogue.show(getSupportFragmentManager(),"Dialog Box for Appointment Booking");
         /**/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.afe_appointment:
               BookAppointment(view);
            break;
        }

    }
}
