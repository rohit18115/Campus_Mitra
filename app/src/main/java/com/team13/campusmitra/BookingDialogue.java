package com.team13.campusmitra;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.team13.campusmitra.dataholder.Appointment;
import com.team13.campusmitra.dataholder.Booking;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.managers.BookAppointmentManager;
import com.team13.campusmitra.managers.BookingManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/*
    ::::Book rooms or appointments with professor
    ::::type = 1 --> appointment with professor
    ::::type = 2 --> book rooms
 */

public class BookingDialogue extends DialogFragment {

    public TextView date, from_time, to_time;
    public EditText comment;
    public String title;
    public Appointment appointment;
    public Booking booking;
    public static int APPOINTMENT = 1;
    public static int ROOMBOOKING = 0;
    private int type;
    final Calendar c = Calendar.getInstance();
    private int mYear, mMonth, mDay, mFromHour, mFromMinute, mToHour, mToMinute;

    public BookingDialogue(int type) {
        this.type = type;
    }

    public Appointment setAppointmentDetails(User userID1, User userID2) {
        appointment = new Appointment();
        appointment.setUserID1(userID1.getUserId());
        appointment.setUserID2(userID2.getUserId());
        title = "Appointment Booking";
        return appointment;
    }

    public Appointment setAppointmentDetails(String userID1, String userID2) {
        appointment = new Appointment();
        appointment.setUserID1(userID1);
        appointment.setUserID2(userID2);
        title = "Appointment Booking";
        return appointment;
    }

    public Booking setBookingDetails(String userID, String roomID) {
        booking = new Booking();
        booking.setUserID(userID);
        booking.setRoomID(roomID);
        title = "Room Booking";
        return booking;
    }

    public Booking setBookingDetails(User user, Room room) {
        booking = new Booking();
        booking.setUserID(user.getUserId());
        booking.setRoomID(room.getRoomID());
        title = "Room Booking";
        return booking;
    }

    //initialize widgets
    private void initializer(View view) {
        date = view.findViewById(R.id.appointment_date_picker);
        from_time = view.findViewById(R.id.appointment_from_time);
        to_time = view.findViewById(R.id.appointment_to_time);
        //hide end time if app with prof
        if(type == 1) {
            to_time.setVisibility(View.GONE);
        }
        comment = view.findViewById(R.id.appointment_comments);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.appointment_dialgo,null);
        builder.setView(view);
        builder.setTitle(title);
        builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String toast_string = "Room Id:" + "Date:" + date.getText() + "From Time:" + from_time.getText() +
                        "To Time:" + to_time.getText() + "Comments:" + comment.getText();
                Toast.makeText(getContext(), toast_string, Toast.LENGTH_LONG).show();
                //::::::::::::::::::Appointment::::::::::::::::::
                if(type == 1) {
                    appointment.setDate(date.getText().toString());
                    appointment.setTime(from_time.getText().toString());
                    appointment.setDescription(comment.getText().toString());
                    appointment.setAppointmentStatus("active");
                    BookAppointmentManager manager =new BookAppointmentManager(getActivity().getApplicationContext(),appointment);
                    manager.bookAppointmentWithData();
                }
                //::::::::::::::::::Booking::::::::::::::::::
                else {
                    booking.setDate(date.getText().toString());
                    booking.setStartTime(from_time.getText().toString());
                    booking.setEndTime(to_time.getText().toString());
                    booking.setDescription(comment.getText().toString());
                    booking.setBookingStatus("active");
                    BookingManager manager = new BookingManager(getActivity().getApplicationContext(), booking);
                    manager.bookiingWithData();
                }
            }
        });
        builder.setTitle(title);
        initializer(view);
        addListenerToDialogWidgets();
        return builder.create();
    }

    //generate date and time pickers
    public void pickersOnClick(View v) {
        if(v == date) {

            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date strDate = sdf.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                if (new Date().after(strDate)) {
                                    Toast.makeText(getContext(), "Invalid Date", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }

                            } catch (Exception e) {
                                Toast.makeText(getContext(), "An exception occured!", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        else if(v == from_time) {
            mFromHour= c.get(Calendar.HOUR_OF_DAY);
            mFromMinute= c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date strDate = sdf.parse(hourOfDay + ":" + minute);
                                if (System.currentTimeMillis() > strDate.getTime()) {
                                    Toast.makeText(getContext(), "Invalid Time", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    from_time.setText(hourOfDay + ":" + minute);
                                }

                            } catch (Exception e) {
                                Toast.makeText(getContext(), "An exception occured!", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
;
                        }
                    }, mFromHour, mFromMinute, false);
            timePickerDialog.show();
        }
        else if(v == to_time) {
            mToHour= c.get(Calendar.HOUR_OF_DAY);
            mToMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date strDate = sdf.parse(hourOfDay + ":" + minute);
                                if (System.currentTimeMillis() > strDate.getTime()) {
                                    Toast.makeText(getContext(), "Invalid Time", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    to_time.setText(hourOfDay + ":" + minute);
                                }

                            } catch (Exception e) {
                                Toast.makeText(getContext(), "An exception occured!", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }, mToHour, mToMinute, false);
            timePickerDialog.show();
        }
    }

    public void addListenerToDialogWidgets() {

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickersOnClick(view);
            }
        });

        from_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickersOnClick(view);
            }
        });

        to_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickersOnClick(view);
            }
        });
    }

}