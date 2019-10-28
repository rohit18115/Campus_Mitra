package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.TimetableRecylerViewAdaptor;
import com.team13.campusmitra.csvhandler.CSVHelper;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.TimeTableElement;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseTimeTableHelper;

import org.apache.commons.beanutils.IntrospectionContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    CheckBox checkBox;
    Button addButton;
    TimeTableElement bufferElement;
    SearchView searchView;
    Button loadTTbtn;
    ProgressBar progressBar1,progressBar2;
    TimetableRecylerViewAdaptor adaptor;
    CardView cardView;
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
    private void loadAllDataToFirebase(List<TimeTableElement> timeTable){
        for(TimeTableElement element:timeTable){

            FirebaseTimeTableHelper helper = new FirebaseTimeTableHelper();

            helper.addTimeTable(AddTimeTableActivity.this,element);
        }

    }
    private void initComponents(){
        startTimeTv  =findViewById(R.id.tt_add_startTime);
        endTimeTv = findViewById(R.id.tt_add_end_time);
        daySpinner = findViewById(R.id.tt_add_day);
        courseSpinner = findViewById(R.id.add_tt_course_spinner);
        roomSpinner = findViewById(R.id.tt_add_room);
        addButton = findViewById(R.id.add_tt_btn);
        loadTTbtn = findViewById(R.id.add_tt_loadTT);
        cardView = findViewById(R.id.tt_add_cardview);
        searchView = findViewById(R.id.tt_add_sv);
        checkBox = findViewById(R.id.tt_add_checkBox);
        progressBar1 = findViewById(R.id.tt_add_progress1);
        progressBar2 = findViewById(R.id.tt_add_progress2);
        progressBar1.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

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
                    if(!(stTime.contains(":")&&enTime.contains(":"))){
                        Toast.makeText(getApplicationContext(),"Please Select Time",Toast.LENGTH_LONG).show();
                        return;
                    }
                    FirebaseTimeTableHelper helper = new FirebaseTimeTableHelper();
                    enTime = getTimeConverted(enTime);
                    stTime = getTimeConverted(stTime);
                    bufferElement = new TimeTableElement(courseID,stTime,enTime,roomID,day);
                    helper.addTimeTable(AddTimeTableActivity.this,bufferElement);

            }
        });
        loadTTbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/*");
                startActivityForResult(intent,101);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterResult(s);
                return false;
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                    startTimeTv.setEnabled(!checkBox.isChecked());
                    endTimeTv.setEnabled(!checkBox.isChecked());
                    roomSpinner.setEnabled(!checkBox.isChecked());

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                cardView.setVisibility(View.VISIBLE);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.GONE);
            }
        });

        loadDays();
        loadData();

    }
    private String getTimeConverted(String time){
        String [] times = time.split(":");
        int hour = Integer.parseInt(times[0]);
        hour = hour*100;
        int min = Integer.parseInt(times[1]);
        min = hour+min;
        return ""+min;
    }
    private void filterResult(String text){
        ArrayList<TimeTableElement> fiteredTT = new ArrayList<>();
        for(TimeTableElement element: timeTable){
            if(getCourseName(element.getCourseID(),courses).toLowerCase().contains(text.toLowerCase())
                    || getCourseCode(element.getCourseID(),courses).toLowerCase().contains(text.toLowerCase())
                    || getRoomNumber(element.getRoomID(),rooms).toLowerCase().contains(text.toLowerCase())){
                fiteredTT.add(element);
            }
        }
        Object[] objects = fiteredTT.toArray();
        if(objects.length>0)
        adaptor.filter(Arrays.copyOf(objects,objects.length,TimeTableElement[].class));
    }
    private String getRoomBuilding(String key,ArrayList<Room> r){
        for(Room room:r){
            if(room.getRoomID().equals(key)){
                return room.getRoomBuilding();
            }
        }
        return "";
    }
    private String getRoomNumber(String key,ArrayList<Room> r){
        for(Room room:r){
            if(room.getRoomID().equals(key)){
                return room.getRoomNumber();
            }
        }
        return "";
    }
    private String getCourseCode(String key,ArrayList<Course> c){
        //String result;
        for(Course course:c){
            if(course.getCourseID().equals(key)){
                return course.getCourseCode();
            }
        }
        return "";
    }
    private String getCourseName(String key,ArrayList<Course> c){
        //String result;
        for(Course course:c){
            if(course.getCourseID().equals(key)){
                return course.getCourseName();
            }
        }
        return "";
    }
    private void loadDays(){
        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AddTimeTableActivity.this, android.R.layout.simple_spinner_item, days);
        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(areasAdapter);


    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            if (requestCode==101){
                Uri uri = data.getData();
                CSVHelper csvHelper = new CSVHelper(uri.toString());
                List<TimeTableElement> tt = csvHelper.readTimeTable(courses,rooms);
                loadAllDataToFirebase(tt);

            }
        }
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
                progressBar1.setVisibility(View.GONE);
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
                progressBar2.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void loadRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tt_recyclerView);
        Object[] objects = timeTable.toArray();
        adaptor = new TimetableRecylerViewAdaptor(Arrays.copyOf(objects,objects.length,TimeTableElement[].class),rooms,courses,this,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadAdaptorToRecyclerView(recyclerView,adaptor);
        //recyclerView.setAdapter(adaptor);
    }
    private void loadAdaptorToRecyclerView(RecyclerView recyclerView,TimetableRecylerViewAdaptor adaptor){
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = null;
        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_anim_fall_down);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
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
