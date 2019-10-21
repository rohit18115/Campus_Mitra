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
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.TimeTableElement;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimetableRecylerViewAdaptor extends RecyclerView.Adapter<TimetableRecylerViewAdaptor.ViewHolder> {

    private TimeTableElement[] elements;
    private ArrayList<Room> rooms;
    private ArrayList<Course> courses;
    public TimetableRecylerViewAdaptor(TimeTableElement[] elements) {
        this.elements = elements;
    }

    public TimetableRecylerViewAdaptor(TimeTableElement[] elements, ArrayList<Room> rooms, ArrayList<Course> courses) {
        this.elements = elements;
        this.rooms = rooms;
        this.courses = courses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItem = inflater.inflate(R.layout.recyclerview_timetable,parent,false);
        ViewHolder holder = new ViewHolder(listItem);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TimeTableElement element = elements[position];
        holder.courseId.setText(getCourseCode(element.getCourseID()));
        holder.courseName.setText(getCourseName(element.getCourseID()));
        holder.fromTime.setText(element.getStartTime());
        holder.toTime.setText(element.getEndTime());
        holder.day.setText(element.getDay());
        holder.roomNumber.setText(getRoomNumber(element.getRoomID()));
        addImage(getCourseName(element.getCourseID()),holder.imageView);

    }
    private String getCourseName(String key){
        for(Course c:courses){
            if(c.getCourseID().equals(key)){
                return c.getCourseName();
            }
        }
        return "";
    }
    private String getCourseCode(String key){
        for(Course c:courses){
            if(c.getCourseID().equals(key)){
                return c.getCourseCode();
            }
        }
        return "";
    }

    private String getRoomNumber(String key){
        for(Room c:rooms){
            if(c.getRoomID().equals(key)){
                return c.getRoomNumber();
            }
        }
        return "";
    }
    @Override
    public int getItemCount() {
        return elements.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView courseId;
        public TextView courseName;
        public TextView fromTime;
        public TextView toTime;
        public TextView day;
        public TextView roomNumber;
        public RelativeLayout myLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.tt_element_image);
            this.courseId = itemView.findViewById(R.id.tt_element_courseid);
            this.courseName = itemView.findViewById(R.id.tt_element_courseName);
            this.fromTime = itemView.findViewById(R.id.tt_element_fromTime);
            this.toTime = itemView.findViewById(R.id.tt_element_toTime);
            this.day = itemView.findViewById(R.id.tt_element_Day);
            this.roomNumber = itemView.findViewById(R.id.tt_element_roomNo);
            this.myLayout = itemView.findViewById(R.id.tt_element_rl);
        }
    }
    public void addImage(String str, ImageView civ){
        String input =  str.toUpperCase();
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

