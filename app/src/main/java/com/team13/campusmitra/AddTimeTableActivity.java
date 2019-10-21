package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.TimetableRecylerViewAdaptor;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.TimeTableElement;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseTimeTableHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class AddTimeTableActivity extends AppCompatActivity {

    ArrayList<Room> rooms;
    ArrayList<Course> courses;
    ArrayList<String> days;
    ArrayList<TimeTableElement> timeTable;
    TextView startTimeTv;
    TextView endTimeTv;
    Spinner daySpinner;
    Spinner courseSpinner;
    Spinner roomSpinner;
    Button addButton;
    TimeTableElement bufferElement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_table);
        initComponents();
    }
    private ArrayList<String> getDays(){
        ArrayList<String> ar = new ArrayList<>();
        ar.add("Sunday");
        ar.add("Monday");
        ar.add("Tuesday");
        ar.add("Wednesday");
        ar.add("Thursday");
        ar.add("Friday");
        ar.add("Saturday");
        return ar;


    }

    private void initComponents(){
        startTimeTv  =findViewById(R.id.tt_add_startTime);
        endTimeTv = findViewById(R.id.tt_add_end_time);
        daySpinner = findViewById(R.id.tt_add_day);
        courseSpinner = findViewById(R.id.add_tt_course_spinner);
        roomSpinner = findViewById(R.id.tt_add_room);
        addButton = findViewById(R.id.add_tt_btn);
        days = getDays();

        startTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime(startTimeTv);

            }
        });
        endTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime(endTimeTv);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String courseID=courses.get(courseSpinner.getSelectedItemPosition()).getCourseID();
                    String roomID = rooms.get(roomSpinner.getSelectedItemPosition()).getRoomID();
                    String day = (String)daySpinner.getSelectedItem();
                    String stTime = startTimeTv.getText().toString();
                    String enTime = endTimeTv.getText().toString();

                    FirebaseTimeTableHelper helper = new FirebaseTimeTableHelper();

                    bufferElement = new TimeTableElement(courseID,stTime,enTime,roomID,day);
                    helper.addTimeTable(AddTimeTableActivity.this,bufferElement);

            }
        });
        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AddTimeTableActivity.this, android.R.layout.simple_spinner_item, days);
        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(areasAdapter);
        loadData();

    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    private void loadData(){
        rooms = new ArrayList<>();
        courses = new ArrayList<>();
        timeTable = new ArrayList<>();
        DatabaseReference roomReference = new FirebaseRoomHelper().getReference();
        final DatabaseReference coursesReference = new FirebaseCoursesHelper().getReference();
        DatabaseReference timeTableReference = new FirebaseTimeTableHelper().getReference();

        roomReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rooms.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Room room = snapshot.getValue(Room.class);
                    rooms.add(room);

                }

                ArrayList<String> ar = new ArrayList<>();
                for(Room room:rooms){
                    ar.add(room.getRoomNumber());
                }
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AddTimeTableActivity.this, android.R.layout.simple_spinner_item, ar);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                roomSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        coursesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courses.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Course course = snapshot.getValue(Course.class);
                    courses.add(course);

                }
                ArrayList<String> ar = new ArrayList<>();
                for(Course course:courses){
                    ar.add(course.getCourseName());
                }
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AddTimeTableActivity.this, android.R.layout.simple_spinner_item, ar);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
               courseSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        timeTableReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                timeTable.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    TimeTableElement element = snapshot.getValue(TimeTableElement.class);
                    timeTable.add(element);

                }
                if(timeTable.size()>0)
                loadRecyclerView();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void loadRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tt_recyclerView);
        Object[] objects = timeTable.toArray();
        TimetableRecylerViewAdaptor adaptor = new TimetableRecylerViewAdaptor(Arrays.copyOf(objects,objects.length,TimeTableElement[].class),rooms,courses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
    }
   // private int mYear, mMonth, mDay, mHour, mMinute;
    private String getTime(final TextView textView){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String minut ="";
                if(selectedMinute<10){
                    minut = "0"+selectedMinute;
                }
                else{
                    minut=""+selectedMinute;
                }
                textView.setText( selectedHour + ":" + minut);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

        return "";
    }

}
