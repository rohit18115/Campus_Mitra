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

public class SelectCoursesRecyclerView extends AppCompatActivity {

    private static final String TAG = "SelectCoursesRecyclerView";

    private List<CourseSelectModel> mModelList;
    FloatingActionButton fab;
    Animation rotateForward, rotateBackward;
    boolean isOpen = false;
    SelectCoursesRecyclerViewAdaptor adapter;
    String text = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_courses_recycler_view);
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
                for (CourseSelectModel model : mModelList) {
                    if (model.isSelected()) {
                        text += model.getCourseCode();
                    }

                    Intent intent1 = new Intent(SelectCoursesRecyclerView.this, StudentProfile.class);
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

    private List<CourseSelectModel> initComponents() {

        Log.d(TAG, "initImage: started");
        String prof = "saku baby";
        String code = "A-403";
        String name = "Machine Learning";
        mModelList = new ArrayList<>();
        mModelList.add(new CourseSelectModel(name, code, prof));
        mModelList.add(new CourseSelectModel("Deep Learning", "DL101", "Saket Anand"));
        mModelList.add(new CourseSelectModel("Artificial intelligence", "MV101", "mayank vatsa"));
        mModelList.add(new CourseSelectModel("qwerty", "hgkghk", "asdfsafd"));
        mModelList.add(new CourseSelectModel(name, code, prof));
        mModelList.add(new CourseSelectModel(name, code, prof));
        mModelList.add(new CourseSelectModel(name, code, prof));
        mModelList.add(new CourseSelectModel(name, code, prof));
        mModelList.add(new CourseSelectModel(name, code, prof));
        mModelList.add(new CourseSelectModel(name, code, prof));
        mModelList.add(new CourseSelectModel(name, code, prof));
        mModelList.add(new CourseSelectModel(name, code, prof));
        mModelList.add(new CourseSelectModel(name, code, prof));
        return mModelList;
    }

    private void initRecycler() {
        Log.d(TAG, "initComponents: started");
        RecyclerView recyclerView = findViewById(R.id.select_courses_recycler_view);
        adapter = new SelectCoursesRecyclerViewAdaptor(initComponents(),this);
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

class CourseSelectModel {

    private String courseName, courseCode, instructorName;
    private boolean isSelected = false;

    public CourseSelectModel(String courseName, String courseCode, String instructorName) {
        this.courseName = courseName;
        this.courseCode =  courseCode;
        this.instructorName = instructorName;
    }

    public String getCourseName() {
        return courseName;
    }
    public String getCourseCode() {
        return courseCode;
    }
    public String getInstructorName() {
        return instructorName;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

}


