package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team13.campusmitra.dataholder.Course;

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


import java.io.Serializable;
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
    ArrayList<String> selected = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_courses_recycler_view);
        Log.d(TAG, "onCreate: started");

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
                        selected.add(model.getCourse().getCourseName());
                    }
                }
                Intent intent1 = new Intent();
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",selected);
                intent1.putExtra("selected_course_Name",args);
                setResult(StudentProfile.RESULT_OK,intent1);
                finish();
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
        Course c = new Course();
        c.setCourseCode(code);
        c.setFacultyEmail(prof);
        c.setCourseName(name);
        mModelList.add(new CourseSelectModel(c));
        mModelList.add(new CourseSelectModel(c));
        mModelList.add(new CourseSelectModel(c));
        mModelList.add(new CourseSelectModel(c));

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

    private Course course;
    private boolean isSelected = false;

    CourseSelectModel(Course course) {
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


