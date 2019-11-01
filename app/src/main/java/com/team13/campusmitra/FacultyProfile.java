package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.Utils.LetterImageView;
import com.team13.campusmitra.dataholder.Faculty;
import com.team13.campusmitra.dataholder.OfficeHours;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseFacultyHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseStudentHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.util.ArrayList;
import java.util.List;

public class FacultyProfile extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private static final int PICK_CONTACT_REQUEST = 1 ;
    Button setOfficeHours, courseTaken;
    private ListView listView,listViewDept,listViewRoom,listViewDesignation;
    public static SharedPreferences sharedPreferences;
    public static String SEL_DAY;
    private Toolbar toolbar;
    AlertDialog dialog,dialog1,dialogDept,dialogRoom,dialogDesig;
    TextView Day;
    TextView venue;
    WeekAdapter venueadapter;
    TextView display_courses;
    TextView sTime , eTime;
    Button department, room,designation;
    TextView dept,rm,desig,domain;
    ArrayList<String> selected;
    int num=0;

    protected void initComponents() {
        dept = findViewById(R.id.display_dept);
        rm = findViewById(R.id.display_room);
        designation = findViewById(R.id.FPBdesignation);
        desig = findViewById(R.id.FPTVdesignation);
        domain = findViewById(R.id.FPdomain);
        Day = findViewById(R.id.FPTVday);
        venue = findViewById(R.id.FPTVVenue);
        sTime = findViewById(R.id.FPTVStartTime);
        eTime = findViewById(R.id.FPTVEndTime);
        selected = new ArrayList<>();
    }

    protected void uploadData() {
        String dep = "",room = "", desi = "", dom = "", day = "",oVenue = "", oStart = "", oEnd = "";
        try {
            dep = dept.getText().toString();
            room = rm.getText().toString();
            desi = desig.getText().toString();
            dom = domain.getText().toString();
            day = Day.getText().toString();
            oVenue = venue.getText().toString();
            oStart = sTime.getText().toString();
            oEnd = eTime.getText().toString();
        } catch (NullPointerException e) {
            Log.d("lolo", "Null pointer exception: ");
        }
        if(dep.isEmpty()) {
            dept.setError("Roll Number Can't be empty", null);
            department.requestFocus();
        } else if(desi.isEmpty()) {
            desig.setError("Roll Number Can't be empty", null);
            designation.requestFocus();
        } else {
            Faculty faculty = new Faculty();
            faculty.setAvailability(1);
            faculty.setDepartment(dep);
            faculty.setDesignation(desi);
            OfficeHours of = new OfficeHours();
            if(!day.isEmpty()) {
                of.setDay(day);
                of.setStartTime(oStart);
                of.setEndTime(oEnd);
                of.setVenue(oVenue);
                faculty.setOfficeHours(of);
            }
            if(!room.isEmpty()) {
                faculty.setRoomNo(room);

            }
            if(!dom.isEmpty()) {
                faculty.setDomains(dom);
            }
            if(selected.size()==0) {
                faculty.setCoursesTaken(selected);
            }
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String uid = auth.getCurrentUser().getUid();
            faculty.setUserID(uid);
            FirebaseFacultyHelper helper = new FirebaseFacultyHelper();
            helper.addStudent(this,faculty);
            incrementCount();
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_profile);
        Day = findViewById(R.id.FPTVday);
        initComponents();
        initToolbar();
        setupUIViews();
        setupListView();
        AlertDialog.Builder builder = new AlertDialog.Builder(FacultyProfile.this);
        builder.setCancelable(true);
        builder.setView(listView);
        dialog = builder.create();
        AlertDialog.Builder builderDept = new AlertDialog.Builder(FacultyProfile.this);
        builderDept.setCancelable(true);
        builderDept.setView(listViewDept);
        dialogDept = builderDept.create();
        AlertDialog.Builder builderRoom = new AlertDialog.Builder(FacultyProfile.this);
        builderRoom.setCancelable(true);
        builderRoom.setView(listViewRoom);
        dialogRoom = builderRoom.create();
        AlertDialog.Builder builderDesig = new AlertDialog.Builder(FacultyProfile.this);
        builderDesig.setCancelable(true);
        builderDesig.setView(listViewDesignation);
        dialogDesig = builderDesig.create();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        num=num+1;
        TextView startTime = (TextView)findViewById(R.id.FPTVStartTime);
        TextView endTime = (TextView)findViewById(R.id.FPTVEndTime);

        if(num%2!=0)
            startTime.setText(hourOfDay+":"+minute);
        else
            endTime.setText(hourOfDay+":"+minute);
    }

    private void setupUIViews(){
        listView = new ListView(this);
        listViewDept = new ListView(this);
        listViewRoom = new ListView(this);
        listViewDesignation = new ListView(this);
        sharedPreferences = getSharedPreferences("MY_DAY", MODE_PRIVATE);
        display_courses = findViewById(R.id.FPTVdisplay_courses);
        department = findViewById(R.id.selectDept);
        setOfficeHours = findViewById(R.id.setOfficeHours);
        courseTaken = findViewById(R.id.FPCourseTaken);
        setOfficeHours.setOnClickListener(this);
        courseTaken.setOnClickListener(this);
        department.setOnClickListener(this);
        designation.setOnClickListener(this);
        room=findViewById(R.id.FPselectroom);
        room.setOnClickListener(this);
        rm=findViewById(R.id.display_room);
    }

    private void initToolbar(){
        getSupportActionBar().setTitle("Faculty Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupListView() {
        String[] week = getResources().getStringArray(R.array.Week);
        final WeekAdapter adapter = new WeekAdapter(this, R.layout.activity_office_hours_day_single_item, week);
        listView.setAdapter(adapter);
        String[] option = getResources().getStringArray(R.array.Dept);
        final WeekAdapter adapterDept = new WeekAdapter(this, R.layout.activity_office_hours_day_single_item, option);
        listViewDept.setAdapter(adapterDept);
        String[] optionroom = getResources().getStringArray(R.array.Venue);
        final WeekAdapter adapterRoom = new WeekAdapter(this, R.layout.activity_office_hours_day_single_item, optionroom);
        listViewRoom.setAdapter(adapterRoom);
        String[] optionDesig = getResources().getStringArray(R.array.Designation);
        final WeekAdapter adapterDesig = new WeekAdapter(this, R.layout.activity_office_hours_day_single_item, optionDesig);
        listViewDesignation.setAdapter(adapterDesig);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //adapter.getItem(position).toString()
                Day.setText(adapter.getItem(position).toString());
                dialog.dismiss();
                DialogFragment startTimePicker = new TimePickerFragment();
                startTimePicker.show(getSupportFragmentManager(), "Start Time");
                DialogFragment endTimePicker = new TimePickerFragment();
                endTimePicker.show(getSupportFragmentManager(), "End Time");
                ListView venueList = new ListView(FacultyProfile.this);
                String[] venueData = getResources().getStringArray(R.array.Venue);
                venueadapter = new WeekAdapter(FacultyProfile.this, R.layout.activity_office_hours_day_single_item, venueData);
                venueList.setAdapter(venueadapter);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(FacultyProfile.this);
                builder1.setCancelable(true);
                builder1.setView(venueList);
                dialog1 = builder1.create();
                dialog1.show();
                venue = (TextView)findViewById(R.id.FPTVVenue);
                venueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        venue.setText(venueadapter.getItem(position).toString());
                        dialog1.dismiss();
                    }
                });


                }

        });
        listViewDept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                 //adapter.getItem(position).toString()
                                                 dept.setText(adapterDept.getItem(position).toString());
                                                 dialogDept.dismiss();


                                             }

                                         }

        );
        listViewRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    //adapter.getItem(position).toString()
                                                    rm.setText(adapterRoom.getItem(position).toString());
                                                    dialogRoom.dismiss();


                                                }

                                            }

        );
        listViewDesignation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    //adapter.getItem(position).toString()
                                                    desig.setText(adapterDesig.getItem(position).toString());
                                                    dialogDesig.dismiss();


                                                }

                                            }

        );
    }

    public class WeekAdapter extends ArrayAdapter {

            private int resource;
            private LayoutInflater layoutInflater;
            private String[] week = new String[]{};
            private String retDay = "";

            public WeekAdapter(FacultyProfile context, int resource, String[] objects) {
                super(context, resource, objects);
                this.resource = resource;
                this.week = objects;
                layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                WeekAdapter.ViewHolder holder;
                if(convertView == null){
                    holder = new WeekAdapter.ViewHolder();
                    convertView = layoutInflater.inflate(resource, null);
                    holder.ivLogo = (LetterImageView)convertView.findViewById(R.id.OHDSILetter);
                    holder.tvWeek = (TextView)convertView.findViewById(R.id.OHDSItv);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            switch(item.getItemId()){
                case android.R.id.home : {
                    onBackPressed();
                }
            }
            return super.onOptionsItemSelected(item);
        }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.setOfficeHours :
                dialog.show();
                break;
            case R.id.FPCourseTaken:
                Intent intent1 = new Intent(FacultyProfile.this, FacultyCourseTakenRecyclerView.class);
                startActivityForResult(intent1, PICK_CONTACT_REQUEST);
                break;
            case R.id.selectDept:
                dialogDept.show();
                break;
            case R.id.FPselectroom:
                dialogRoom.show();
                break;
            case R.id.FPBdesignation:
                dialogDesig.show();
                break;
            case R.id.SPnext:
                uploadData();
                break;

        }

    }

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
    }

}
