package com.team13.campusmitra.adaptors;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.database.DatabaseReference;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.TimeTableElement;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseTimeTableHelper;

import java.util.ArrayList;
import java.util.Random;

public class AddCourseRecyclerViewAdaptor extends  RecyclerView.Adapter<AddCourseRecyclerViewAdaptor.ViewHolder>{

    private ArrayList<Course> courses;
    private Course[] courseArray;
    private AppCompatActivity activity;
    private ArrayList<TimeTableElement> timeTable;
    public AddCourseRecyclerViewAdaptor(Course[] courseArray,AppCompatActivity activity) {
        this.courseArray = courseArray;
        this.activity = activity;
    }
    String s=null;
    public AddCourseRecyclerViewAdaptor(Course[] courseArray,AppCompatActivity activity,String sss){
        this.s= sss;
        this.courseArray = courseArray;
        this.activity = activity;

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
        holder.offeringTv.setText("Offering: "+course.getOffering());
        holder.preqTv.setText("Pre-requisite: "+course.getCoursePrequisite());
        addImage(course.getCourseName(), holder.imageView);
        holder.myLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(s==null)
                showEditDialog(course);
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
    private void showEditDialog(final Course course){
        final AlertDialog dialog;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_edit_course, null);
        final Spinner actionSpinner = view.findViewById(R.id.dialog_edit_course_spinner);
        final EditText et = view.findViewById(R.id.dialog_edit_course_et);
        Button updateBtn = view.findViewById(R.id.dialog_edit_course_update_btn);
        Button deleteBtn = view.findViewById(R.id.dialog_edit_course_delete_btn);
        loadDataInSpinner(actionSpinner,getActions());
        alertDialog.setView(view);
        alertDialog.setTitle("Edit Course Record");
        dialog= alertDialog.create();
        dialog.show();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = actionSpinner.getSelectedItemPosition();
                if(checkEditText(et)){
                    String data = et.getText().toString().trim();
                switch (i){
                    case 1:
                        course.setCourseCode(data);
                        break;
                    case 2:
                        course.setCourseName(data);
                        break;
                    case 3:
                        course.setCoursePrequisite(data);
                        break;
                    case 4:
                        course.setOffering(data);
                        break;
                    default:
                        et.setError("Some Error occured");
                        et.requestFocus();
                        actionSpinner.requestFocus();

                }
                    FirebaseCoursesHelper helper = new FirebaseCoursesHelper();
                helper.updateCourse(activity,course);
                dialog.cancel();
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(course);
                dialog.cancel();
            }
        });


    }
    private ArrayList<String> getActions(){
        ArrayList<String> arr = new ArrayList<>();
        arr.add("None");
        arr.add("Edit Course code");
        arr.add("Edit Course name");
        arr.add("Edit Course Prerequisite");
        arr.add("Edit Course Offering");
        return arr;
    }
    private void loadDataInSpinner(Spinner daySpinner, ArrayList<String> data) {

        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, data);
        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(areasAdapter);


    }
    private boolean checkEditText(EditText editText){
        if (editText.getText().toString().trim().length()>0){
            return true;
        }
        else{
            editText.setError("Enter valid value");
            editText.requestFocus();
            return false;
        }
    }
    public void showDialog(final Course element) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle("Are you sure want to delete ?");

        dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseCoursesHelper helper = new FirebaseCoursesHelper();
                DatabaseReference ref = helper.getReference().child(element.getCourseID());
                ref.removeValue();



            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });

        dialogBuilder.show();
    }
    public void filter(Course[] e) {
        this.courseArray = e;
        this.notifyDataSetChanged();

    }
}
