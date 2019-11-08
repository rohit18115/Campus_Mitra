package com.team13.campusmitra.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.R;
import com.team13.campusmitra.SelectCoursesRecyclerView;
import com.team13.campusmitra.Utils.LetterImageView;
import com.team13.campusmitra.dataholder.Faculty;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.firebaseassistant.FirebaseFacultyHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseStudentHelper;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class StudentProfileFragment extends Fragment implements View.OnClickListener {

    private static final int PICK_CONTACT_REQUEST = 1 ;
    FloatingActionButton fab;
    Animation rotateForward, rotateBackward;
    AppCompatTextView rollNo;
    AppCompatTextView department;
    AppCompatTextView enrollCourse;
    AppCompatTextView coursesTaken;
    AppCompatTextView interests;
    Button resume;
    String url;
    private int count = 0;
    private ListView listViewCourse,listViewDept;
    TextInputEditText dbRoll,dbInterests;
    ProgressBar pb;
    private AlertDialog dialogCourses,dialogDept;
    private ArrayList<String> selected;

    protected void initComponent(View view) {
        rollNo = view.findViewById(R.id.fsp_roll);
        department = view.findViewById(R.id.fsp_dept);
        enrollCourse = view.findViewById(R.id.fsp_course);
        coursesTaken = view.findViewById(R.id.fsp_course_taken);
        interests = view.findViewById(R.id.fsp_interests);
        resume = view.findViewById(R.id.fsp_resume);
        resume.setOnClickListener(this);
        pb = view.findViewById(R.id.fsp_pb);
        fab = view.findViewById(R.id.fsp_fab);
        dbRoll = new TextInputEditText(view.getContext());
        dbInterests = new TextInputEditText(view.getContext());
    }

    protected void loadData(View view) {
        Log.d("lolo", "on loaddata");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        FirebaseStudentHelper helper = new FirebaseStudentHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (getActivity() == null) {
                        return;
                    }
                    Student student = snapshot.getValue(Student.class);
                    if (student.getUserID().equals(uid)) {
                        Log.d("lololo", "onDataChange: " + student.getRollNumber());
                        rollNo.setText(student.getRollNumber());
                        department.setText(student.getDepartment());
                        enrollCourse.setText(student.getEnrollCourse());
                        interests.setText(student.getAreaOfInterest());
                        ArrayList<String> s = student.getCourses();
                        String cor = "";
                        if (s != null) {
                            for (int i = 0; i < s.size(); i++) {
                                cor = cor + s.get(i) + "\n";
                            }
                        }
                        coursesTaken.setText(cor);
                        url = student.getResumeURL();
                        pb.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void animateFab() {
        fab.startAnimation(rotateForward);
        fab.startAnimation(rotateBackward);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);
        initComponent(view);
        pb.setVisibility(View.VISIBLE);
        loadData(view);
        setupListview(view);
        setListenerFab();

        return view;
    }

    private void setListenerFab() {
        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        rollNo.setOnClickListener(this);
        department.setOnClickListener(this);
        enrollCourse.setOnClickListener(this);
        coursesTaken.setOnClickListener(this);
        interests.setOnClickListener(this);
        rollNo.setEnabled(false);
        department.setEnabled(false);
        enrollCourse.setEnabled(false);
        coursesTaken.setEnabled(false);
        interests.setEnabled(false);
        AlertDialog.Builder builderCourses = new AlertDialog.Builder(getActivity());
        builderCourses.setCancelable(true);
        builderCourses.setView(listViewCourse);
        dialogCourses = builderCourses.create();
        AlertDialog.Builder builderDept = new AlertDialog.Builder(getActivity());
        builderDept.setCancelable(true);
        builderDept.setView(listViewDept);
        dialogDept = builderDept.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fsp_fab:
                animateFab();
                count++;
                if(count%2==1) {
                    fab.setImageResource(R.drawable.ic_check);
                    Snackbar.make(v, "Click on each component to edit.", Snackbar.LENGTH_LONG).setTextColor(Color.WHITE)
                            .setAction("Action", null).show();
                    rollNo.setEnabled(true);
                    department.setEnabled(true);
                    enrollCourse.setEnabled(true);
                    coursesTaken.setEnabled(true);
                    interests.setEnabled(true);
                } else {
                    fab.setImageResource(R.drawable.ic_mode_edit);
                    rollNo.setEnabled(false);
                    department.setEnabled(false);
                    enrollCourse.setEnabled(false);
                    coursesTaken.setEnabled(false);
                    interests.setEnabled(false);
                }
                break;
            case R.id.fsp_interests:
                rollNo.setEnabled(true);
                department.setEnabled(true);
                enrollCourse.setEnabled(true);
                coursesTaken.setEnabled(true);
                interests.setEnabled(true);
                android.app.AlertDialog.Builder builderInterests = new android.app.AlertDialog.Builder(v.getContext());
                builderInterests.setTitle("Interests");
                builderInterests.setView(dbInterests);
                builderInterests.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String txt = dbInterests.getEditableText().toString();
                        if(txt!=null && !txt.isEmpty()) {
                            interests.setText(txt);
                            uploadData();
                        }
                    }
                });

                builderInterests.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                if(dbInterests.getParent() != null) {
                    ((ViewGroup)dbInterests.getParent()).removeView(dbInterests); // <- fix
                }
                builderInterests.show();
                break;
            case R.id.fsp_roll:
                rollNo.setEnabled(true);
                department.setEnabled(true);
                enrollCourse.setEnabled(true);
                coursesTaken.setEnabled(true);
                interests.setEnabled(true);
                android.app.AlertDialog.Builder builderRoll = new android.app.AlertDialog.Builder(v.getContext());
                builderRoll.setTitle("Roll Number");
                builderRoll.setView(dbRoll);
                builderRoll.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String txt = dbRoll.getEditableText().toString();
                        if(txt!=null && !txt.isEmpty()) {
                            if(isInvalid(txt))
                            rollNo.setText(txt);
                            uploadData();
                        }
                    }

                    private boolean isInvalid(String roll) {
                        if(roll.startsWith("MT") && roll.length()==7)
                            return false;
                        else if(roll.startsWith("Phd") && roll.length() == 8)
                            return false;
                        else if(roll.startsWith("20") && roll.length() == 7)
                            return false;
                        return true;
                    }
                });

                builderRoll.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                if(dbRoll.getParent() != null) {
                    ((ViewGroup)dbRoll.getParent()).removeView(dbRoll); // <- fix
                }
                builderRoll.show();
                break;
            case R.id.fsp_course:
                rollNo.setEnabled(true);
                department.setEnabled(true);
                enrollCourse.setEnabled(true);
                coursesTaken.setEnabled(true);
                interests.setEnabled(true);
                dialogCourses.show();
                break;
            case R.id.fsp_dept:
                rollNo.setEnabled(true);
                department.setEnabled(true);
                enrollCourse.setEnabled(true);
                coursesTaken.setEnabled(true);
                interests.setEnabled(true);
                dialogDept.show();
                break;
            case R.id.fsp_course_taken:
                rollNo.setEnabled(true);
                department.setEnabled(true);
                enrollCourse.setEnabled(true);
                coursesTaken.setEnabled(true);
                interests.setEnabled(true);
                Intent intent1 = new Intent(v.getContext(), SelectCoursesRecyclerView.class);
                startActivityForResult(intent1, PICK_CONTACT_REQUEST);
                break;
            case R.id.fsp_resume:
                if (url != null && !url.isEmpty()) {
                    if (!url.contains("http://"))
                        url = "http://" + url;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), "No Resume Uploaded", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void uploadData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        FirebaseStudentHelper helper = new FirebaseStudentHelper();
        DatabaseReference reference = helper.getReference().child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getActivity() == null) {
                    return;
                }
                Student student = dataSnapshot.getValue(Student.class);
                if(student.getUserID().equals(uid)) {
                    String enrol = "", deptt = "", interes = "",rolln="";

                    try {
                        enrol = enrollCourse.getText().toString();
                        deptt = department.getText().toString();
                        rolln = rollNo.getText().toString();
                        interes = interests.getText().toString();
                    } catch (NullPointerException e) {
                        Log.d("lolo", "Null pointer exception: ");
                    }
                    if(!deptt.isEmpty())
                        student.setDepartment(deptt);
                    if(!enrol.isEmpty())
                        student.setEnrollCourse(enrol);
                    if(!rolln.isEmpty())
                        student.setRollNumber(rolln);
                    if(!interes.isEmpty())
                        student.setAreaOfInterest(interes);
                    if(selected!=null && selected.size()!=0) {
                        student.setCourses(selected);
                    }
                    new FirebaseStudentHelper().addStudent(getActivity(), student);
                    pb.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setupListview(View view) {

        listViewCourse  = new ListView(view.getContext());
        listViewDept  = new ListView(view.getContext());

        String[] option = getResources().getStringArray(R.array.Dept);
        final WeekAdapter adapterDept = new WeekAdapter(view.getContext(), R.layout.activity_office_hours_day_single_item, option);
        listViewDept.setAdapter(adapterDept);

        String[] option2 = getResources().getStringArray(R.array.EnrollCourse);
        final WeekAdapter adapterCourse = new WeekAdapter(view.getContext(), R.layout.activity_office_hours_day_single_item, option2);
        listViewCourse.setAdapter(adapterCourse);

        listViewDept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    department.setText(adapterDept.getItem(position).toString());
                                                    uploadData();
                                                    dialogDept.dismiss();
                                                }});
        listViewCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    enrollCourse.setText(adapterCourse.getItem(position).toString());
                                                    uploadData();
                                                    dialogCourses.dismiss();
                                                }
                                            });
    }

    public class WeekAdapter extends ArrayAdapter {

        private int resource;
        private LayoutInflater layoutInflater;
        private String[] week;
        private String retDay = "";

        public WeekAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.week = objects;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) { WeekAdapter.ViewHolder holder;
            if(convertView == null){
                holder = new WeekAdapter.ViewHolder();
                convertView = layoutInflater.inflate(resource, null);
                holder.ivLogo = convertView.findViewById(R.id.OHDSILetter);
                holder.tvWeek = convertView.findViewById(R.id.OHDSItv);
                convertView.setTag(holder);
            }else{
                holder = (WeekAdapter.ViewHolder)convertView.getTag();
            }

            holder.ivLogo.setOval(true);
            holder.ivLogo.setLetter(week[position].charAt(0));
            holder.tvWeek.setText(week[position]);

            return convertView;
        }


        class ViewHolder{
            private LetterImageView ivLogo;
            private TextView tvWeek;
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle args;
                if(data!=null) {
                    args = data.getBundleExtra("selected_course_Name");
                    selected = (ArrayList<String>)args.getSerializable("ARRAYLIST");
                }
                if(selected.size()!=0) {
                    String t = " ";
                    for(int i =0;i<selected.size();i++) {
                        Log.d("lolo", "onActivityResult: add to t");
                        t = t + selected.get(i) + "\n";
                    }
                    coursesTaken.setText(t.trim());
                    uploadData();
                }
            }
        }
    }

}

