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

import java.text.ParseException;
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

    public static int APPOINTMENT = 1;
    public static int ROOMBOOKING = 0;
    final Calendar c = Calendar.getInstance();
    public TextView date, from_time, to_time;
    public EditText comment;
    public String title;
    public Appointment appointment;
    public Booking booking;

    private int type;
    private String mYear, mMonth, mDay, mFromHour, mFromMinute, mToHour, mToMinute;

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
        if (type == 1) {
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
        View view = inflater.inflate(R.layout.appointment_dialgo, null);
        builder.setView(view);
        builder.setTitle(title);
        builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                String toast_string = "Date:" + date.getText() + "From Time:" + from_time.getText() +
//                        "To Time:" + to_time.getText() + "Comments:" + comment.getText();
//                Toast.makeText(getContext(), toast_string, Toast.LENGTH_LONG).show();
                if ((date.getText() != "" && from_time.getText() != "" && !comment.getText().toString().isEmpty())) {
                    Date sDate = null;
                    try {
                        sDate = (new SimpleDateFormat("dd/MM/yyyy HH:mm")).parse(mDay + "/" + mMonth + "/" + mYear + " " + mFromHour + ":" + mFromMinute);
                        Log.i("BDlog", "sDate:" + sDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (Calendar.getInstance().getTime().after(sDate)) {
                        Log.i("BDlog", "calDate:" + Calendar.getInstance().getTime());
                        Toast.makeText(getContext(), "Invalid Date or Time", Toast.LENGTH_SHORT).show();
                    } else {

                        //::::::::::::::::::Appointment::::::::::::::::::
                        if (type == 1) {

                            appointment.setDate(date.getText().toString());//::::::::: dd/MM/yyyy
                            appointment.setTime(mFromHour + mFromMinute);//::::::::: HHmm
                            appointment.setDescription(comment.getText().toString());
                            appointment.setAppointmentStatus("active");
                            //::::::::::This is where i get good data::::::::::
                            Toast.makeText(getContext(), date.getText() + " " + from_time.getText() + " " + comment.getText().toString(), Toast.LENGTH_SHORT).show();
                            /*
                            TODO send data to firebase here!

                            BookAppointmentManager manager =new BookAppointmentManager(getActivity().getApplicationContext(),appointment);
                            manager.bookAppointmentWithData();
                             */
                        }
                        //::::::::::::::::::Booking::::::::::::::::::
                        else if (to_time.getText() != "") {
                            Date eDate = null;
                            try {
                                eDate = (new SimpleDateFormat("dd/MM/yyyy HH:mm")).parse(mDay + "/" + mMonth + "/" + mYear + " " + mToHour + ":" + mToMinute);
                                Log.i("BDlog", "eDate:" + eDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (sDate.after(eDate)) {
                                Log.i("BDlog", "calDate:" + Calendar.getInstance().getTime());
                                Toast.makeText(getContext(), "Invalid Timeings", Toast.LENGTH_SHORT).show();
                            } else {


                                booking.setDate(date.getText().toString());//::::::::: dd/MM/yyyy
                                booking.setStartTime(mFromHour + mFromMinute);//::::::::: HHmm
                                booking.setEndTime(mToHour + mToMinute);//::::::::: HHmm
                                booking.setDescription(comment.getText().toString());
                                booking.setBookingStatus("active");
                                //::::::::::This is where i get good data::::::::::
                                Toast.makeText(getContext(), date.getText() + " " + from_time.getText() + " " + to_time.getText() + " " + comment.getText().toString(), Toast.LENGTH_SHORT).show();
                                /*
                                TODO send data to firebase here!

                                BookingManager manager = new BookingManager(getActivity().getApplicationContext(), booking);
                                manager.bookiingWithData();
                                 */
                            }
                        } else {
                            Log.i("BDlog:", "Missing To Time Input");
                            Toast.makeText(getContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.i("BDlog:", "Missing Input");
                    Toast.makeText(getContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
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
        if (v == date) {

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mYear = String.valueOf(year);
                            mMonth = ((monthOfYear < 9) ? "0" : "") + (monthOfYear + 1);
                            mDay = ((dayOfMonth < 10) ? "0" : "") + dayOfMonth;
                            date.setText(mDay + "/" + mMonth + "/" + mYear);
//
//                        try {
//                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                            Date strDate = sdf.parse(Integer.valueOf(dayOfMonth) + "/" + Integer.valueOf(monthOfYear + 1) + "/" + Integer.valueOf(year));
//                            Toast.makeText(getContext(), strDate.getTime() + "j", Toast.LENGTH_SHORT).show();
//                            if (System.currentTimeMillis() > strDate.getTime()) {
//
//                                Toast.makeText(getContext(), "Invalid Date", Toast.LENGTH_SHORT).show();
////                                    Toast.makeText(getContext(), strDate.getTime() + "j" + , Toast.LENGTH_SHORT).show();
//                            }
//                            else{
//                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                                Toast.makeText(getContext(), strDate.getTime() + "j", Toast.LENGTH_SHORT).show();
//                            }
//
//                        } catch (Exception e) {
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else if (v == from_time) {

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            mFromHour = ((hour < 10) ? "0" : "") + hour;
                            mFromMinute = ((minute < 10) ? "0" : "") + minute;
                            from_time.setText(mFromHour + ":" + mFromMinute);
                        }
                    }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), false);
            timePickerDialog.show();

//            // Launch Time Picker Dialog
//            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
//                    new TimePickerDialog.OnTimeSetListener() {
//
//                        @Override
//                        public void onTimeSet(TimePicker view, int hourOfDay,
//                                              int minute) {
//
//                            try {
//                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                                Date strDate = sdf.parse(hourOfDay + ":" + minute);
//                                if (System.currentTimeMillis() > strDate.getTime()) {
//                                    Toast.makeText(getContext(), "Invalid Time", Toast.LENGTH_SHORT).show();
//                                }
//                                else{
//                                    from_time.setText(hourOfDay + ":" + minute);
//                                }
//
//                            } catch (Exception e) {
//                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                                e.printStackTrace();
//                            }
//;
//                        }
//                    }, mFromHour, mFromMinute, false);
//            timePickerDialog.show();
        } else if (v == to_time) {

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            mToHour = ((hour < 10) ? "0" : "") + hour;
                            mToMinute = ((minute < 10) ? "0" : "") + minute;
                            to_time.setText(mToHour + ":" + mToMinute);
                        }
                    }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), false);
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