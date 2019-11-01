package com.team13.campusmitra;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VacantRoomDetails extends AppCompatActivity {

    TextView date;
    TextView start_time;
    TextView end_time;
    EditText capacity;
    Button send_button;
    Calendar calendar;
    String ayear, amonth, adayOfMonth, shour, sminute, ehour, eminute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacant_room_details);

        //::::::::::Initialization::::::::::
        date = findViewById(R.id.vacant_room_date);
        start_time = findViewById(R.id.vacant_room_start_time);
        end_time = findViewById(R.id.vacant_room_end_time);
        capacity = findViewById(R.id.vacant_room_capacity);
        send_button = findViewById(R.id.vacant_room_send);
        calendar = Calendar.getInstance();
    }

    public void setStartTime(View view) {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                shour = ((hour < 10) ? "0" : "") + hour;
                sminute = ((minute < 10) ? "0" : "") + minute;
                start_time.setText(shour + ":" + sminute);
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
                date.setText(adayOfMonth + "/" + amonth + "/" + ayear);
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

                 */
            }
        } else {
            Log.i("VRD", "Wrong Input");
            Toast.makeText(this, "Invalid Details", Toast.LENGTH_SHORT).show();
        }
    }


}
