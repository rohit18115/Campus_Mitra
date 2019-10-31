package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.text.Spanned;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.Projects_Adapter;
import com.team13.campusmitra.dataholder.Project;
import com.team13.campusmitra.dataholder.ResearchLab;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class R_Lab extends AppCompatActivity {
    TextView HyperLink;
    Spanned Text;
    Projects_Adapter adaptor;
    Button add_proj;
    Room room;
    ResearchLab researchLab;
    Intent myIntent;
    ArrayList<Project> projects;
    //ArrayList<Project> projects_present;
    RecyclerView project_list;
    private int new_project = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_lab);
        myIntent = getIntent();
        room = (Room) myIntent.getSerializableExtra("ROOM");
        researchLab = (ResearchLab) myIntent.getSerializableExtra("RL");

        HyperLink = (TextView)findViewById(R.id.r_lab_et3);
        add_proj = findViewById(R.id.add_proj_btn);
        Text = Html.fromHtml("Website: " +
                "<a href=' http://iab-rubric.org/'> http://iab-rubric.org/</a>");
        HyperLink.setMovementMethod(LinkMovementMethod.getInstance());
        HyperLink.setText(Text);
        projects = new ArrayList<>();
        project_list = (RecyclerView) findViewById(R.id.proj_list);
        loadProjects();
        add_proj.setOnClickListener(new View.OnClickListener() {
            @Override
            //************************Have to update intent*********************
            public void onClick(View view) {
                Intent intent = new Intent(R_Lab.this, Add_Project.class);
                startActivity(intent);
            }
        });
        //String[] project_items = {"Project 1", "Project 2", "Project 3", "Project 4", "Project 5", "Project 6","Project 7","Project 8","Project 9","Project 10"};
        /*RecyclerView project_list;
        project_list = (RecyclerView) findViewById(R.id.proj_list);
        project_list.setLayoutManager(new LinearLayoutManager(this));
        String[] project_items = {"Project 1", "Project 2", "Project 3", "Project 4", "Project 5", "Project 6","Project 7","Project 8","Project 9","Project 10"};
        project_list.setAdapter(new Projects_Adapter(project_items));*/
    }

    private void loadProjects(){
        FirebaseCoursesHelper helper = new FirebaseCoursesHelper();
        DatabaseReference reference = helper.getReference();
        final ArrayList<String> rprojects = researchLab.getProjects();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projects.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Project project = snapshot.getValue(Project.class);
                    if(rprojects.contains(project.getProjectID()))
                        projects.add(project);
                }
                //Projects_Adapter adapter = new Projects_Adapter(projects);
                //project_list.setAdapter(adapter);
                if (projects.size()>0)
                    loadRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void loadRecyclerView(){
        Object[] objects = projects.toArray();
        adaptor = new Projects_Adapter(Arrays.copyOf(objects,objects.length, Project[].class),this);
        project_list.setHasFixedSize(true);
        project_list.setLayoutManager(new LinearLayoutManager(this));
        loadAdaptorToRecyclerView(project_list,adaptor);
        //recyclerView.setAdapter(adaptor);
    }
    private void loadAdaptorToRecyclerView(RecyclerView recyclerView, Projects_Adapter adaptor){
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = null;
        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_anim_fall_down);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
