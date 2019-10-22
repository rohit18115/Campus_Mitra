package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.AddCourseRecyclerViewAdaptor;
import com.team13.campusmitra.adaptors.TimetableRecylerViewAdaptor;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.TimeTableElement;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class AddCourseActivity extends AppCompatActivity {
    EditText codeET,nameET,preqET,offeringET;
    Button addCourseBTN;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<Course> courses;

    AddCourseRecyclerViewAdaptor adaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        initComponents();
        loadCourses();
    }
    private void initComponents(){
        codeET = findViewById(R.id.co_add_et_cc);
        nameET = findViewById(R.id.co_add_et_cn);
        preqET = findViewById(R.id.co_add_et_preq);
        offeringET = findViewById(R.id.co_add_et_off);
        addCourseBTN = findViewById(R.id.co_add_btn);
        progressBar = findViewById(R.id.co_add_progress2);
        courses = new ArrayList<>();
        recyclerView = findViewById(R.id.co_recyclerView);
        addCourseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkEditText(codeET)&&checkEditText(nameET)&&checkEditText(preqET)&&checkEditText(offeringET)){
                    Course c = new Course();
                    c.setCourseCode(codeET.getText().toString().trim());
                    c.setCourseName(nameET.getText().toString().trim());
                    c.setCoursePrequisite(preqET.getText().toString().trim());
                    c.setOffering(offeringET.getText().toString().trim());
                    FirebaseCoursesHelper helper = new FirebaseCoursesHelper();
                    helper.addCourse(getApplicationContext(),c);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter valid Values!!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean checkEditText(EditText et){
        if(et.getText().length()<=1){
            et.requestFocus();
            et.setError("Enter Valid values");
            return false;
        }
        return true;
    }
    private void loadCourses(){
        FirebaseCoursesHelper helper = new FirebaseCoursesHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courses.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Course course = snapshot.getValue(Course.class);
                    courses.add(course);
                }
                progressBar.setVisibility(View.GONE);
                if (courses.size()>0)
                loadRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void loadRecyclerView(){
        Object[] objects = courses.toArray();
        adaptor = new AddCourseRecyclerViewAdaptor(Arrays.copyOf(objects,objects.length, Course[].class));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadAdaptorToRecyclerView(recyclerView,adaptor);
        //recyclerView.setAdapter(adaptor);
    }
    private void loadAdaptorToRecyclerView(RecyclerView recyclerView,AddCourseRecyclerViewAdaptor adaptor){
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = null;
        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_anim_fall_down);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
