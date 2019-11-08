package com.team13.campusmitra;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team13.campusmitra.Utils.LetterImageView;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseStudentHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.util.ArrayList;

public class StudentProfile extends AppCompatActivity implements View.OnClickListener {
    TextView display_courses;
    TextView interests;
    TextView rollNo;
    TextView dept;
    TextView selected_file;
    Button select_courses;
    Button department;
    Button upload, next;
    FirebaseStorage storage;
    FirebaseDatabase database;
    AlertDialog dialog;
    private ListView listView;
    ArrayList<String> selected = new ArrayList<>();
    Uri pdfUri;
    String resumeUrl = "";
    ProgressDialog progressDialog;
    public static SharedPreferences sharedPreferences;

    static final int PICK_CONTACT_REQUEST = 1;

    private void initComponents() {
        display_courses = findViewById(R.id.display_courses_id);
        department = findViewById(R.id.selectDept);
        interests = findViewById(R.id.interests);
        rollNo = findViewById(R.id.rollNumber);
        upload = findViewById(R.id.uploadresume);
        next = findViewById(R.id.SPnext);
        selected_file = findViewById(R.id.display_selected_file);
        dept = findViewById(R.id.display_dept);
    }

    private void getStudentObject() {
        String roll = "",deptText = "",interest = "" ,stream = "";
        try {
            roll = rollNo.getText().toString();
            deptText = dept.getText().toString();
            interest = interests.getText().toString();
        } catch (NullPointerException e) {
            Log.d("lolo", "Null pointer exception: ");
        }

        if(roll.isEmpty()) {
            rollNo.setError("Roll Number Can't be empty", null);
            rollNo.requestFocus();
        }if(isInvalid(roll)) {
            rollNo.setError("Roll Number Invalid", null);
            rollNo.requestFocus();
        } else if(deptText.equals("Select Department")) {
            dept.setError("First Name Can't be empty", null);
            dept.requestFocus();
        } else {
            Student student = new Student();
            student.setAreaOfInterest(interest);
            student.setDepartment(deptText);
            student.setRollNumber(roll);
            student.setCourses(selected);
            student.setResumeURL(resumeUrl);
            if(roll.contains("MT")) {
                stream = "M. Tech.";
            } else if(roll.contains("PhD")) {
                stream = "Phd";
            } else {
                stream = "B. Tech.";
            }
            student.setEnrollCourse(stream);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String uid = auth.getCurrentUser().getUid();
            student.setUserID(uid);
            FirebaseStudentHelper helper = new FirebaseStudentHelper();
            helper.addStudent(this,student);
            incrementCount();
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

    private void incrementCount() {
        final User[] user = new User[1];
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        final FirebaseUserHelper helper = new FirebaseUserHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //user[0] = dataSnapshot.getValue(User.class);
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User u = (User)snapshot.getValue(User.class);
                    if(u.getUserId().equals(uid)) {
                        user[0] = u;
                        Log.d("lololo", "onDataChange: " + user[0].getUserLastName());
                        user[0].setProfileCompleteCount(2);
                        helper.updateUser(getApplicationContext(), user[0]);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        initComponents();
        initToolbar();
        setupUIViews();
        setupListView();
        department.setOnClickListener(this);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentProfile.this);
        builder.setCancelable(true);
        builder.setView(listView);
        dialog = builder.create();

        select_courses = findViewById(R.id.selectcourses);
        select_courses.setOnClickListener(this);
        display_courses = (TextView)findViewById(R.id.display_courses_id);
        upload.setOnClickListener(this);
        next.setOnClickListener(this);
        select_courses.setOnClickListener(this);
//        if (savedInstanceState == null) {
//            Bundle courses = getIntent().getExtras();
//            if(courses == null) {
//                display_courses= null;
//            } else {
//                display_courses.setText(courses.getString("selected_course_Name"));
//            }
//        }

      //
    }
    private void initToolbar(){
        getSupportActionBar().setTitle("Student Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void setupUIViews(){
        listView = new ListView(this);
        sharedPreferences = getSharedPreferences("MY_DAY", MODE_PRIVATE);
    }
    private void setupListView() {
        String[] option = getResources().getStringArray(R.array.Dept);
        final DeptAdapter adapter = new DeptAdapter(this, R.layout.activity_office_hours_day_single_item, option);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //adapter.getItem(position).toString()
                dept.setText(adapter.getItem(position).toString());
                dialog.dismiss();


            }

        });
    }
    public class DeptAdapter extends ArrayAdapter {

        private int resource;
        private LayoutInflater layoutInflater;
        private String[] week = new String[]{};
        private String retDay = "";

        public DeptAdapter(StudentProfile context, int resource, String[] objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.week = objects;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            DeptAdapter.ViewHolder holder;
            if(convertView == null){
                holder = new DeptAdapter.ViewHolder();
                convertView = layoutInflater.inflate(resource, null);
                holder.ivLogo = (LetterImageView)convertView.findViewById(R.id.OHDSILetter);
                holder.tvWeek = (TextView)convertView.findViewById(R.id.OHDSItv);
                convertView.setTag(holder);
            }else{
                holder = (DeptAdapter.ViewHolder)convertView.getTag();
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.selectcourses :
                Intent intent1 = new Intent(StudentProfile.this, SelectCoursesRecyclerView.class);
                startActivityForResult(intent1, PICK_CONTACT_REQUEST);
                break;
            case R.id.uploadresume:
                if(ContextCompat.checkSelfPermission(StudentProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){
                    select_pdf();

                }
                else{
                    ActivityCompat.requestPermissions(StudentProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
                break;
            case R.id.SPnext:
                getStudentObject();
                break;
            case R.id.selectDept:
                dialog.show();
                break;
        }
    }

    private void uploadFile(Uri pdfUri) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File");
        progressDialog.setProgress(0);
        progressDialog.show();


        final String fileName = System.currentTimeMillis()+"";
        final StorageReference storageReference = storage.getReference().child(fileName);

        storageReference.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final Uri downloadUrl = uri;
                        resumeUrl = downloadUrl.toString();
                        Log.d("Url",resumeUrl);

                    }
                });



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentProfile.this,"File not uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgess = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgess);
            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            select_pdf();

        }
        else{
            Toast.makeText(StudentProfile.this,"please provide permission...",Toast.LENGTH_SHORT).show();
        }
    }

    private void select_pdf() {

        Intent intentfile = new Intent();
        intentfile.setType("application/pdf");
        intentfile.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentfile,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                        t = t + selected.get(i) + " ";
                    }
                    display_courses.setText(t);
                }
            }
        }
        else if (requestCode==86 && resultCode==RESULT_OK && data!=null) {
            pdfUri = data.getData();
            if(pdfUri!=null){
                Log.d("lolo", "uploaded  granted");
                uploadFile(pdfUri);
            }
            else{
                Toast.makeText(StudentProfile.this,"Select a file....",Toast.LENGTH_SHORT).show();
            }
            selected_file.setText("A file is selected");
        }
        else{
            Toast.makeText(StudentProfile.this,"Please select a file....",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.logout_action_bar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()) {
            case R.id.ab_logout:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent1 = new Intent(getApplicationContext(),SignInSplash.class);
                startActivity(intent1);
                finish();
                return true;
            case android.R.id.home : {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

