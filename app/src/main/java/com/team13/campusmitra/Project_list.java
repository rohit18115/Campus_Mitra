package com.team13.campusmitra;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Project_list extends Fragment {

    //private static final String TAG = "Research Labs RecyclerView";
    // TODO: Rename and change types of parameters



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, "onCreateView: started");
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);
        RecyclerView project_list;
        project_list = (RecyclerView) view.findViewById(R.id.proj_list);
        project_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        //String[] project_items = {"Project 1", "Project 2", "Project 3", "Project 4", "Project 5", "Project 6","Project 7","Project 8","Project 9","Project 10"};
        Projects_Adapter adapter = new Projects_Adapter(ProjectModel.getObjectList());
        project_list.setAdapter(adapter);
        return view;
    }


}
