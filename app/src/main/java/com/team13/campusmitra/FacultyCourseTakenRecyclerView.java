package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class FacultyCourseTakenRecyclerView extends AppCompatActivity {

    private static final String TAG = "FacultyCourseTakenRecyclerView";

    private List<FacultyCourseTakenModel> mModelList;
    FloatingActionButton fab;
    Animation rotateForward, rotateBackward;
    boolean isOpen = false;
    FacultyCourseTakenRecyclerViewAdaptor adapter;
    String text = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_course_taken_recycler_view);
        Log.d(TAG, "onCreate: started");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                for (FacultyCourseTakenModel model : mModelList) {
                    if (model.isSelected()) {
                        text += model.getCourseCode();
                    }

                    Intent intent1 = new Intent(FacultyCourseTakenRecyclerView.this, FacultyProfile.class);
                    intent1.putExtra("selected_course_code",text);
                    startActivity(intent1);

                }
                animateFab();
            }
        });

        initComponents();
        initRecycler();
    }

    private void animateFab(){

        fab.startAnimation(rotateForward);
        fab.startAnimation(rotateBackward);

    }

    private List<FacultyCourseTakenModel> initComponents() {

        Log.d(TAG, "initImage: started");
        String code = "A-403";
        String name = "Machine Learning";
        mModelList = new ArrayList<>();
        mModelList.add(new FacultyCourseTakenModel(name, code));
        mModelList.add(new FacultyCourseTakenModel("Deep Learning", "DL101"));
        mModelList.add(new FacultyCourseTakenModel("Artificial intelligence", "MV101"));
        mModelList.add(new FacultyCourseTakenModel("qwerty", "hgkghk"));
        mModelList.add(new FacultyCourseTakenModel(name, code));
        mModelList.add(new FacultyCourseTakenModel("Deep Learning", "DL101"));
        mModelList.add(new FacultyCourseTakenModel("Artificial intelligence", "MV101"));
        mModelList.add(new FacultyCourseTakenModel("qwerty", "hgkghk"));
        return mModelList;
    }

    private void initRecycler() {
        Log.d(TAG, "initComponents: started");
        RecyclerView recyclerView = findViewById(R.id.faculty_course_taken_recycler_view);
        adapter = new FacultyCourseTakenRecyclerViewAdaptor(initComponents(),this);
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

    private String courseName, courseCode;
    private boolean isSelected = false;

    public FacultyCourseTakenModel(String courseName, String courseCode) {
        this.courseName = courseName;
        this.courseCode =  courseCode;
    }

    public String getCourseName() {
        return courseName;
    }
    public String getCourseCode() {
        return courseCode;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

}


