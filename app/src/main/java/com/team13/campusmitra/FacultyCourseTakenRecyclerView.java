package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.Faculty;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;


import java.util.ArrayList;
import java.util.List;

public class FacultyCourseTakenRecyclerView extends AppCompatActivity {

    private static final String TAG = "FacultyCourseTakenRecyclerView";

    private List<FacultyCourseTakenModel> items;
    FloatingActionButton fab;
    Animation rotateForward, rotateBackward;
    boolean isOpen = false;
    FacultyCourseTakenRecyclerViewAdaptor adapter;
    String text = "";
    ArrayList<String> selected = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_course_taken_recycler_view);
        Log.d(TAG, "onCreate: started");
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                for (FacultyCourseTakenModel model : items) {
                    if (model.isSelected()) {
                        selected.add(model.getCourse().getCourseName());
                    }
                }
                Intent intent1 = new Intent();
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",selected);
                intent1.putExtra("selected_course_Name",args);
                setResult(FacultyProfile.RESULT_OK,intent1);
                finish();
                animateFab();
            }
        });

        initComponents();
    }

    private void animateFab(){

        fab.startAnimation(rotateForward);
        fab.startAnimation(rotateBackward);

    }

    private void initComponents() {

        items = new ArrayList<>();
        FirebaseCoursesHelper helper = new FirebaseCoursesHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Course course = snapshot.getValue(Course.class);
                    FacultyCourseTakenModel fm = new FacultyCourseTakenModel(course);
                    items.add(fm);
                }
                //progressBar.setVisibility(View.GONE);
                if (items.size()>0) {
                    Log.d("lololo", "onDataChange: ");
                    initRecycler();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void initRecycler() {
        Log.d(TAG, "initComponents: started");
        RecyclerView recyclerView = findViewById(R.id.faculty_course_taken_recycler_view);
        adapter = new FacultyCourseTakenRecyclerViewAdaptor(items,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_selectcourse, menu);

        MenuItem searchItem = menu.findItem(R.id.SCapp_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}

class FacultyCourseTakenModel {
    private Course course;
    private boolean isSelected = false;

    public FacultyCourseTakenModel(Course course) {
        this.course = course;
    }


    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}


