package com.team13.campusmitra.managers;

import android.content.Context;

import com.team13.campusmitra.dataholder.Appointment;
import com.team13.campusmitra.firebaseassistant.FirebaseAppointmentHelper;

public class BookAppointmentManager {
    private String user1;
    private String user2;
    private String date;
    private String time;
    private String remarks;
    private Context context;
    private  String status;

    public BookAppointmentManager(String user1, String user2, String date, String time, String remarks, Context context, String status) {
        this.user1 = user1;
        this.user2 = user2;
        this.date = date;
        this.time = time;
        this.remarks = remarks;
        this.context = context;
        this.status = status;
    }

    public BookAppointmentManager(Context context) {
        this.context = context;
    }

    public BookAppointmentManager(Context context, String user1, String user2, String date, String time, String remarks) {
        this.user1 = user1;
        this.user2 = user2;
        this.date = date;
        this.time = time;
        this.remarks = remarks;
        this.context = context;
    }
    public void bookAppointment(){
        FirebaseAppointmentHelper helper = new FirebaseAppointmentHelper();
        Appointment appointment = new Appointment();
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setAppointmentStatus(status);
        appointment.setDescription(remarks);
        appointment.setUserID1(user1);
        appointment.setUserID2(user2);

        helper.addAppointment(context,appointment);
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
