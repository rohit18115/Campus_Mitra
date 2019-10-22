package com.team13.campusmitra.adaptors;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Course;

import java.util.ArrayList;
import java.util.Random;

public class AddCourseRecyclerViewAdaptor extends  RecyclerView.Adapter<AddCourseRecyclerViewAdaptor.ViewHolder>{

    private ArrayList<Course> courses;
    private Course[] courseArray;

    public AddCourseRecyclerViewAdaptor(Course[] courseArray) {
        this.courseArray = courseArray;
    }

    public AddCourseRecyclerViewAdaptor() {
    }

    public AddCourseRecyclerViewAdaptor(ArrayList<Course> courses) {
        this.courses = courses;
        //this.courseArray =
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItem = inflater.inflate(R.layout.recyclerview_add_course,parent,false);
        ViewHolder holder = new ViewHolder(listItem);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Course course = courseArray[position];
        holder.courseNameCodeTv.setText(course.getCourseCode()+" ,"+course.getCourseName());
        holder.offeringTv.setText(course.getOffering());
        holder.preqTv.setText(course.getCoursePrequisite());
        addImage(course.getCourseName(), holder.imageView);
        holder.myLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseArray.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView courseNameCodeTv;
        public TextView preqTv;
        public TextView offeringTv;
        public RelativeLayout myLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.co_element_image);
            courseNameCodeTv = itemView.findViewById(R.id.co_element_courseName);
            preqTv = itemView.findViewById(R.id.co_element_preq);
            offeringTv = itemView.findViewById(R.id.co_element_offering);
            myLayout = itemView.findViewById(R.id.co_element_rl);

        }
    }
    public void addImage(String str, ImageView civ) {
        String input = str.toUpperCase();
        char first = input.charAt(0);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        TextDrawable drawable1 = TextDrawable.builder()
                .buildRoundRect(Character.toString(first), color, 15); // radius in px
        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound(Character.toString(first), color);
        civ.setImageDrawable(drawable2);

    }
}
