package com.team13.campusmitra;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.Projects_Adapter;
import com.team13.campusmitra.dataholder.Project;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;

import java.util.ArrayList;
import java.util.List;


public class Project_list extends Fragment {

    //private static final String TAG = "Research Labs RecyclerView";
    // TODO: Rename and change types of parameters
    ArrayList<Project> projects;
    RecyclerView project_list;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, "onCreateView: started");
        projects = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);
        project_list = (RecyclerView) view.findViewById(R.id.proj_list);
        project_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadProjects();
        //String[] project_items = {"Project 1", "Project 2", "Project 3", "Project 4", "Project 5", "Project 6","Project 7","Project 8","Project 9","Project 10"};
        return view;
    }

    private void loadProjects(){
        FirebaseCoursesHelper helper = new FirebaseCoursesHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projects.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Project course = snapshot.getValue(Project.class);
                    projects.add(course);
                }
                Projects_Adapter adapter = new Projects_Adapter(projects);
                project_list.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
    });
    }
}
