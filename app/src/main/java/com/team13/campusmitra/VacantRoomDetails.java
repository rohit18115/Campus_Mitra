package com.team13.campusmitra;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.AddCourseRecyclerViewAdaptor;
import com.team13.campusmitra.adaptors.VacantRoomRecyclerViewAdaptor;
import com.team13.campusmitra.dataholder.Booking;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.TimeTableElement;
import com.team13.campusmitra.firebaseassistant.FirebaseBookingHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseTimeTableHelper;
import com.team13.campusmitra.managers.VacantRoomManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class VacantRoomDetails extends FragmentActivity {

    TextView date;
    TextView start_time;
    TextView end_time;
    EditText capacity;
    Button send_button;
    ArrayList<Room> rooms;
    ArrayList<TimeTableElement> elements;
    ArrayList<Booking> bookings;
    Calendar calendar;
    String ayear, amonth, adayOfMonth, shour, sminute, ehour, eminute;
    RecyclerView recyclerView;
    VacantRoomRecyclerViewAdaptor adaptor;
    SearchView searchView;
    String dates, stimes, etimes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacant_room_details);

        rooms = new ArrayList<>();
        elements = new ArrayList<>();
        bookings = new ArrayList<>();
        //::::::::::Initialization::::::::::
        date = findViewById(R.id.vacant_room_date);
        start_time = findViewById(R.id.vacant_room_start_time);
        end_time = findViewById(R.id.vacant_room_end_time);
        capacity = findViewById(R.id.vacant_room_capacity);
        send_button = findViewById(R.id.vacant_room_send);
        searchView = findViewById(R.id.vacant_room_search_view);
        recyclerView = findViewById(R.id.vacant_room_rview);
        calendar = Calendar.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    public void setStartTime(View view) {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                shour = ((hour < 10) ? "0" : "") + hour;
                sminute = ((minute < 10) ? "0" : "") + minute;
                start_time.setText(shour + ":" + sminute);
                stimes = shour+""+sminute;
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    public void setEndTime(View view) {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                ehour = ((hour < 10) ? "0" : "") + hour;
                eminute = ((minute < 10) ? "0" : "") + minute;
                end_time.setText(ehour + ":" + eminute);
                etimes = ehour+""+eminute;
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    public void setDate(View view) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                ayear = String.valueOf(year);
                amonth = ((month < 9) ? "0" : "") + (month + 1);
                adayOfMonth = ((dayOfMonth < 10) ? "0" : "") + dayOfMonth;
                date.setText(adayOfMonth + "-" + amonth + "-" + ayear);
                dates = adayOfMonth + "-" + amonth + "-" + ayear;
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void onClick(View view) {


        if (date.getText() != "" && start_time.getText() != "" && end_time.getText() != "" && !capacity.getText().toString().isEmpty()) {
            Log.i("VRD", "inside if");
//            Log.i("VRD", "Cap:" + capacity.getText().toString() + ":");
            Date sDate = null;
            try {
                sDate = (new SimpleDateFormat("dd/MM/yyyy HH:mm")).parse(adayOfMonth + "/" + amonth + "/" + ayear + " " + shour + ":" + sminute);
                Log.i("VRD", "sDate:" + sDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date eDate = null;
            try {
                eDate = (new SimpleDateFormat("dd/MM/yyyy HH:mm")).parse(adayOfMonth + "/" + amonth + "/" + ayear + " " + ehour + ":" + eminute);
                Log.i("VRD", "eDate:" + eDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (Calendar.getInstance().getTime().after(sDate) || sDate.after(eDate)) {
                Log.i("VRD", "calDate:" + Calendar.getInstance().getTime());
                Toast.makeText(this, "Invalid Details", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("VRD", "calDate:" + Calendar.getInstance().getTime());
                Log.i("VRD", "sDate:" + sDate);
                Log.i("VRD", "eDate:" + eDate);
                //::::::::::This is where i get good data::::::::::
                Toast.makeText(this, date.getText() + " " + start_time.getText() + " " + end_time.getText() + " " + capacity.getText(), Toast.LENGTH_SHORT).show();
                /*
                TODO send data to firebase here!

                Manipulate strings here!
                String ayear, amonth, adayOfMonth, shour, sminute, ehour, eminute;
                dates
                stimes
                etimes


                 */
                //loadData();
                loadRecyclerView();


            }
        } else {
            Log.i("VRD", "Wrong Input");
            Toast.makeText(this, "Invalid Details", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData(){
        FirebaseRoomHelper helper=new FirebaseRoomHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rooms.clear();
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Room room = snapshot.getValue(Room.class);
                        rooms.add(room);
                    }
                FirebaseBookingHelper bookingHelper = new FirebaseBookingHelper();
                bookingHelper.getReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        bookings.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Booking booking = snapshot.getValue(Booking.class);
                            bookings.add(booking);
                        }
                        FirebaseTimeTableHelper timeTableHelper = new FirebaseTimeTableHelper();
                        timeTableHelper.getReference().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                elements.clear();
                                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    TimeTableElement element = snapshot.getValue(TimeTableElement.class);
                                    elements.add(element);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void loadRecyclerView(){
        VacantRoomManager manager = new VacantRoomManager(rooms,elements,bookings);
        //manager.setDate();
        manager.setDate(dates);
        manager.setEndTime(Integer.parseInt(etimes));
        manager.setStartTime(Integer.parseInt(stimes));

        ArrayList<Room> r =  manager.getVacantRoomsWithCapacity(Integer.parseInt(capacity.getText().toString().trim()));
        adaptor = new VacantRoomRecyclerViewAdaptor(r,VacantRoomDetails.this,getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadAdaptorToRecyclerView(recyclerView,adaptor);
        //recyclerView.setAdapter(adaptor);
    }
    private void loadAdaptorToRecyclerView(RecyclerView recyclerView,VacantRoomRecyclerViewAdaptor adaptor){
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = null;
        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_anim_fall_down);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


}